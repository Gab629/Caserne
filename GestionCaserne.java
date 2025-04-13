import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GestionCaserne {

    // Afficher tous les secteurs
    public static String afficherSecteurs() throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------\n");
        sb.append("     Afficher tous les Secteurs      \n");
        sb.append("------------------------------------------------------------------\n");

        String sqlcmd = "SELECT codePostal, nomSecteur, limitesGéographique FROM Secteur;";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        ResultSet rs = pstmt.executeQuery(); // le resultSet contient plusieurs enregistrements

        sb.append("codePostal | nomSecteur | limitesGéographique\n");
        sb.append("------------------------------------------------------------------\n");
        while (rs.next()) {
            String codePostal = rs.getString("codePostal");
            String nomSecteur = rs.getString("nomSecteur");
            String limitesGéographique = rs.getString("limitesGéographique");
            sb.append(codePostal + "   |    " + nomSecteur + "    |    " + limitesGéographique);
            sb.append("\n");
        }
        // fermer le dataset
        rs.close();
        // fermer le prepared statement
        pstmt.close();
        return sb.toString();
    }

    // Insérer un pompier dans une équipe
    public static void InsererPompier(int idEmploye, String nom, String prenom, String poste, String numTel,
            int idEquipe) throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();
        System.out.println("-----------------------------------------");
        System.out.println("-------Insérer un pompier ---------------");
        System.out.println("-----------------------------------------");

        // cette requête est paramètée, les ? remplacent des valeurs
        String sqlcmd = "INSERT INTO Pompier(idEmploye, nom, prenom, poste, numTel, idEquipe) VALUES (?, ?, ?, ?, ?, ?);";

        // pstmt sera utiliser pour remplacer les ? par des valeurs puis exécuter la
        // requête
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, idEmploye); // 6ème paramètre:
        pstmt.setString(2, nom); // 2ème paramètre:
        pstmt.setString(3, prenom); // 3ème paramètre:
        pstmt.setString(4, poste); // 4ème paramètre:
        pstmt.setString(5, numTel); // 5ème paramètre:
        pstmt.setInt(6, idEquipe); // 6ème paramètre:

        int nbLignesAffectees = pstmt.executeUpdate(); // exécution

        // fermer le prepared statement
        pstmt.close();
        System.out.println("Insertion terminée, " + nbLignesAffectees + " Ligne(s) affectée(s)\n\n");
    }

    // Supprimer un pompier
    public static void supprimerPompier(int idEmploye) throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();
        System.out.println("--------------------------------------------------");
        System.out.println("-------supprimer un pompier par id ---------------");
        System.out.println("--------------------------------------------------");

        String sqlcmd = "DELETE FROM Pompier where idEmploye = ?;";

        // pstmt sera utiliser pour remplacer les ? par des valeurs puis exécuter la
        // requête
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, idEmploye);

        int nbLignesAffectees = pstmt.executeUpdate(); // exécution

        // fermer le prepared statement
        pstmt.close();
        System.out.println("Suppression terminée, " + nbLignesAffectees + " Ligne(s) affectée(s)\n\n");
        ;
    }

    // Afficher les pompiers d'une équipe
    public static String afficherPompierDansEquipe(int equipeID) throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------\n");
        sb.append("     Afficher tous les Pompiers dans une équipe      \n");
        sb.append("------------------------------------------------------------------------\n");

        String sqlcmd = "SELECT nom, prenom, poste, idEquipe FROM Pompier WHERE  idEquipe = ?;";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, equipeID);
        ResultSet rs = pstmt.executeQuery(); // le resultSet contient plusieurs enregistrements

        sb.append("nom     |   prénom    |              poste            |    id de l'équipe\n");
        sb.append("--------------------------------------------------------------------\n");
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String poste = rs.getString("poste");
            String idEquipe = rs.getString("idEquipe");
            sb.append(nom + "   |    " + prenom + "    |    " + poste + "    |    " + idEquipe);
            sb.append("\n");
        }
        // fermer le dataset
        rs.close();
        // fermer le prepared statement
        pstmt.close();
        return sb.toString();
    }

    // Compter incidents dans secteurs
    public static String afficherNbIncidentsDansSecteurs(int secteurID) throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();
        StringBuilder sb = new StringBuilder();

        String sqlcmd = "SELECT dbo.compterIncidentsSecteur(?) AS [Nombre d'incidents];";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, secteurID);
        ResultSet rs = pstmt.executeQuery();

        // rs.next() permet d'accéder à la première ligne de résultat
        if (rs.next()) {
            String nbIncidents = rs.getString("Nombre d'incidents");
            sb.append("Nombre d'incidents dans le secteur: ").append(nbIncidents);
        } else {
            sb.append("Aucun incident trouvé pour ce secteur.");
        }

        // fermer le dataset
        rs.close();
        // fermer le prepared statement
        pstmt.close();
        return sb.toString();
    }

    // Afficher les équipements expirés
    public static String afficherEquipementsExpirés() throws SQLException {
        DbConnection dbConnection = DbConnection.getInstance();
        StringBuilder sb = new StringBuilder();

        String sqlcmd = "SELECT idEquipement, typeEquipement, date_expiration FROM Equipement WHERE date_expiration < GETDATE();";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        ResultSet rs = pstmt.executeQuery();

        sb.append("ID | Type | Date d'expiration\n");
        sb.append("-----------------------------------\n");

        while (rs.next()) {
            sb.append(rs.getInt("idEquipement")).append(" | ")
                    .append(rs.getString("typeEquipement")).append(" | ")
                    .append(rs.getDate("date_expiration")).append("\n");
        }

        rs.close();
        pstmt.close();
        return sb.toString();
    }

    public static String incidentsAvecRenfort() throws SQLException {
        StringBuilder sb = new StringBuilder();
        Connection conn = DbConnection.getInstance().getConnection();

        String query = "SELECT Incident.typeIncident, caserne1.nomCaserne AS CaserneInitiale, " +
                "caserne2.nomCaserne AS CaserneRenfort, RepartiteurUrgence.nom " +
                "FROM Rapporte " +
                "JOIN Incident ON Rapporte.incidentId = Incident.incidentId " +
                "JOIN Caserne caserne1 ON Rapporte.idCaserne = caserne1.idCaserne " +
                "JOIN Caserne caserne2 ON Rapporte.CaserneRenfort = caserne2.idCaserne " +
                "JOIN RepartiteurUrgence ON Rapporte.numEmploye = RepartiteurUrgence.numEmploye " +
                "WHERE Rapporte.CaserneRenfort IS NOT NULL";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                sb.append("Incident: ").append(rs.getString("typeIncident"))
                        .append(" | Caserne initiale: ").append(rs.getString("CaserneInitiale"))
                        .append(" | Renfort: ").append(rs.getString("CaserneRenfort"))
                        .append(" | Répartiteur: ").append(rs.getString("nom")).append("\n");
            }
        }

        return sb.toString();
    }

    public static String equipementsParEquipe(int idEquipe) throws SQLException {
        StringBuilder sb = new StringBuilder();
        Connection conn = DbConnection.getInstance().getConnection();

        String query = "SELECT e.idEquipement, e.typeEquipement, e.statutDisponibilite, e.date_expiration " +
                "FROM Equipement e " +
                "JOIN equipementTeam et ON e.idEquipement = et.idEquipement " +
                "WHERE et.idEquipe = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("idEquipement"))
                        .append(" | Type: ").append(rs.getString("typeEquipement"))
                        .append(" | Statut: ").append(rs.getString("statutDisponibilite"))
                        .append(" | Expire le: ").append(rs.getDate("date_expiration")).append("\n");
            }
        }

        return sb.toString();
    }

    public static String vehiculesParEquipe(int idEquipe) throws SQLException {
        StringBuilder sb = new StringBuilder();
        Connection conn = DbConnection.getInstance().getConnection();

        String query = "SELECT idVehicule, typeVehicule, plaque_immatriculation " +
                "FROM Vehicule WHERE idEquipe = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idEquipe);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("idVehicule"))
                        .append(" | Type: ").append(rs.getString("typeVehicule"))
                        .append(" | Plaque: ").append(rs.getString("plaque_immatriculation")).append("\n");
            }
        }

        return sb.toString();
    }

    public static String afficherIncidentsAvecSecteurEtCaserne(Connection con) throws SQLException {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        String query = "SELECT incidentID, typeIncident, nomSecteur, nomCaserne " +
                "FROM Incident " +
                "JOIN Secteur ON Incident.idSecteur = Secteur.idSecteur " +
                "JOIN Couvert ON Secteur.idSecteur = Couvert.idSecteur " +
                "JOIN Caserne ON Couvert.idCaserne = Caserne.idCaserne " +
                "ORDER BY Incident.incidentID";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int incidentID = rs.getInt("incidentID");
                String typeIncident = rs.getString("typeIncident");
                String nomSecteur = rs.getString("nomSecteur");
                String nomCaserne = rs.getString("nomCaserne");

                sb.append("=============================================\n");
                sb.append("Incident ").append(i++).append("\n");
                sb.append("Description  : ").append(typeIncident).append("\n");
                sb.append("Secteur      : ").append(nomSecteur).append("\n");
                sb.append("Caserne      : ").append(nomCaserne).append("\n");
            }
        }

        if (sb.length() == 0) {
            sb.append("Aucun incident trouvé.");
        }

        return sb.toString();
    }

}
