import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionCaserne {
    
    public static String afficherSecteurs() throws SQLException {
    	DbConnection dbConnection = DbConnection.getInstance();

        StringBuilder sb = new StringBuilder();
    	sb.append("------------------------------------------------------------------\n" );
    	sb.append("     Afficher tous les Secteurs      \n" );
    	sb.append("------------------------------------------------------------------\n" );
    	
    	String sqlcmd = "SELECT codePostal, nomSecteur, limitesGéographique FROM Secteur;";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        ResultSet rs = pstmt.executeQuery(); //le resultSet contient plusieurs enregistrements
        
        sb.append("codePostal | nomSecteur | limitesGéographique\n" );
        sb.append("------------------------------------------------------------------\n" );
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

    //INSERT
    public static void InsererPompier(int idEmploye, String nom,  String prenom, String poste, String numTel, int idEquipe) throws SQLException {
    	DbConnection dbConnection = DbConnection.getInstance();
    	System.out.println("-----------------------------------------" );
    	System.out.println("-------Insérer un pompier ---------------" );
    	System.out.println("-----------------------------------------" );
    	
        //cette requête est paramètée, les ? remplacent des valeurs
    	String sqlcmd = "INSERT INTO Pompier(idEmploye, nom, prenom, poste, numTel, idEquipe) VALUES (?, ?, ?, ?, ?, ?);";
    	
    	//pstmt sera utiliser pour remplacer les ? par des valeurs puis exécuter la requête
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, idEmploye); // 6ème paramètre: 
        pstmt.setString(2, nom); // 2ème paramètre: 
        pstmt.setString(3, prenom); // 3ème paramètre: 
        pstmt.setString(4, poste); // 4ème paramètre: 
        pstmt.setString(5, numTel); // 5ème paramètre: 
        pstmt.setInt(6, idEquipe); // 6ème paramètre: 
        
        int nbLignesAffectees = pstmt.executeUpdate(); //exécution
        
        
        // fermer le prepared statement
        pstmt.close();
        System.out.println("Insertion terminée, " + nbLignesAffectees + " Ligne(s) affectée(s)\n\n");
    }

    //DELETE
    public static void supprimerPompier (int idEmploye) throws SQLException{
    	DbConnection dbConnection = DbConnection.getInstance();
    	System.out.println("--------------------------------------------------" );
    	System.out.println("-------supprimer un pompier par id ---------------" );
    	System.out.println("--------------------------------------------------" );
    	
    	String sqlcmd = "DELETE FROM Pompier where idEmploye = ?;";
    	
    	//pstmt sera utiliser pour remplacer les ? par des valeurs puis exécuter la requête
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, idEmploye); 
         
        int nbLignesAffectees = pstmt.executeUpdate(); //exécution
        
        // fermer le prepared statement
        pstmt.close();
        System.out.println("Suppression terminée, " + nbLignesAffectees + " Ligne(s) affectée(s)\n\n");;
    }

    public static String afficherPompierDansEquipe(int equipeID) throws SQLException {
    	DbConnection dbConnection = DbConnection.getInstance();

        StringBuilder sb = new StringBuilder();
    	sb.append("------------------------------------------------------------------------\n" );
    	sb.append("     Afficher tous les Pompiers dans une équipe      \n" );
    	sb.append("------------------------------------------------------------------------\n" );
    	
    	String sqlcmd = "SELECT nom, prenom, poste, idEquipe FROM Pompier WHERE  idEquipe = ?;";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setInt(1, equipeID); 
        ResultSet rs = pstmt.executeQuery(); //le resultSet contient plusieurs enregistrements
        
        sb.append("nom     |   prénom    |              poste            |    id de l'équipe\n" );
        sb.append("--------------------------------------------------------------------\n" );
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

    
}
