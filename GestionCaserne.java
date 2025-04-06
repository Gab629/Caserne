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
            String limitesGeographique = rs.getString("limitesGéographique"); 
            System.out.println(codePostal + "\t | \t" + nomSecteur + "\t | \t" + limitesGeographique);  
        }
        // fermer le dataset
        rs.close();
        // fermer le prepared statement
        pstmt.close();
        System.out.println("\n\n");
    }
}
