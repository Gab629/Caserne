import java.sql.SQLException;

public class TestCli {
	public static DbConnection dbConnection;
    public static void main(String[] args) {
        try {
        	//openConnection
        	DbConnection dbConnection = DbConnection.getInstance();
        	System.out.println("Connexion à la BD ouverte avec Succès!");
        	

        	GestionCaserne.afficherSecteurs(); //Afficher les secteurs
            
        	// GestionCaserne.modifierEmploye(6,"Roe", "Jean"); //modifier les infos de l'employé avec id=6
            
        	// GestionCaserne.afficherEmployes(); //afficher tous les employés
            
        	// GestionCaserne.afficherUnEmploye(1); //afficher employé avec id =2
            
        	// GestionCaserne.supprimerEmploye(6); //supprimer employé avec id =6
            

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
