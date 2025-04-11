import java.sql.SQLException;

public class TestCli {
	public static DbConnection dbConnection;
    public static void main(String[] args) {
        try {
        	//openConnection
        	DbConnection dbConnection = DbConnection.getInstance();
        	System.out.println("Connexion à la BD ouverte avec Succès!");
        	
            //Utilisé pour les tests avec la console, mais obselette puisque modifié pour l'application GUI
        	GestionCaserne.afficherSecteurs(); //Afficher les secteurs

            // fermer la connection à la base données, avant de quitter
            dbConnection.closeConnection();
            System.out.println("Connexion à la BD fermée avec Succès");
        } 
        catch (SQLException e) { //une gestion simple des erreurs de la BD
            System.err.println("Erreur de BD: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
