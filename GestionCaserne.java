import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionCaserne {
    
    public static void afficherSecteurs() throws SQLException {
    	DbConnection dbConnection = DbConnection.getInstance();
    	System.out.println("--------------------------------------------" );
    	System.out.println("-------Afficher tous les Secteurs-----------" );
    	System.out.println("--------------------------------------------" );
    	
    	String sqlcmd = "SELECT codePostal, nomSecteur, limitesGéographique FROM Secteur;";
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        ResultSet rs = pstmt.executeQuery(); //le resultSet contient plusieurs enregistrements
        
        System.out.println("codePostal" + "\t | \t" + "nomSecteur" + "\t | \t" + "limitesGéographique" );
        System.out.println("-------------------------------------" );
        while (rs.next()) {
            String codePostal = rs.getString("codePostal"); 
            String nomSecteur = rs.getString("nomSecteur");
            String limitesGéographique = rs.getString("limitesGéographique"); 
            System.out.println(codePostal + "\t | \t" + nomSecteur + "\t | \t" + limitesGéographique);  
        }
        // fermer le dataset
        rs.close();
        // fermer le prepared statement
        pstmt.close();
        System.out.println("\n\n");
    }

    //INSERT
    public static void InsererPompier(String nom,  String prenom, String poste, String numTel, int idEquipe) throws SQLException {
    	DbConnection dbConnection = DbConnection.getInstance();
    	System.out.println("-----------------------------------------" );
    	System.out.println("-------Insérer un pompier ---------------" );
    	System.out.println("-----------------------------------------" );
    	
        //cette requête est paramètée, les ? remplacent des valeurs
    	String sqlcmd = "INSERT INTO Pompier(nom, prenom, poste, numTel, idEquipe) VALUES (?, ?, ?, ?, ?);";
    	
    	//pstmt sera utiliser pour remplacer les ? par des valeurs puis exécuter la requête
        PreparedStatement pstmt = dbConnection.prepareStatement(sqlcmd);
        pstmt.setString(1, nom); // 2ème paramètre: 
        pstmt.setString(2, prenom); // 3ème paramètre: 
        pstmt.setString(3, poste); // 4ème paramètre: 
        pstmt.setString(4, numTel); // 5ème paramètre: 
        pstmt.setInt(5, idEquipe); // 6ème paramètre: 
        
        int nbLignesAffectees = pstmt.executeUpdate(); //exécution
        
        
        // fermer le prepared statement
        pstmt.close();
        System.out.println("Insertion terminée, " + nbLignesAffectees + " Ligne(s) affectée(s)\n\n");
    }
}
