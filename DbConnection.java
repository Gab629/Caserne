import java.sql.*;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    // chaine de connexion pour sql server
    String nomBd= "Gestion_Caserne";
    String instanceServeur= "localhost:1433"; //serveur:port
    private String url = "jdbc:sqlserver://" + instanceServeur + ";" //instance par défaut
            + "databaseName="+nomBd+";"  //nom de la BD
            + "encrypt=true;" // chiffrer la connexion
            + "trustServerCertificate=true;" // remplacer par "no" quand vous installez un certificat valide sur le serveur SQL
            + "integratedSecurity=true;" ;// authentification avec WIndows
            


    private DbConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url, null, null);
        } catch (ClassNotFoundException ex) {
            System.out.println("SQL Server JDBC Driver non trouvé: " + ex.getMessage());
        }
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = this.getConnection(); 
        return connection.prepareStatement(sql);
    }
    

    public Connection getConnection() {
        return connection;
    }


    public Boolean closeConnection() throws SQLException {
        if (this.connection != null) {
            this.connection.close();
        }
        return true;
    }
    
    //cette méthode statique garantit l'existence d'une seule instance de la classe DbConnection dans le programme
    //le patron singleton
    public static DbConnection getInstance() throws SQLException {
        if (instance == null || instance.getConnection().isClosed()) {
            instance = new DbConnection();
        }
        return instance;
    }
}
