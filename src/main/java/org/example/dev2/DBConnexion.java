package org.example.dev2;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnexion {
    private Connection connection;
    protected static final Logger LOGGER = Logger.getLogger(DBConnexion.class.getName());

    // Constructeur pour établir la connexion à la base de données
    public DBConnexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/commande", "root", "");
            LOGGER.info("Connexion réussie à la base de données !");
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Échec de la connexion à la base de données", e);
        }
    }

    // Retourne la connexion actuelle
    public Connection getConnection() {
        return connection;
    }

    // Ferme la connexion à la base de données
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                LOGGER.info("Connexion fermée.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la fermeture de la connexion", e);
        }
    }

    // Méthode pour exécuter une requête de sélection
    public boolean selectFromDb(int customerId) {
        String query = "SELECT * FROM customer WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customerId);  // Paramètre pour customerId
            ResultSet result = stmt.executeQuery();
            return result.next();       // Retourne true si le customer_id existe, sinon false
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'exécution de la requête de sélection", e);
        }
        return false;
    }

    // Méthode pour insérer des données dans la table orders
    public void insertToOrders(int id, java.sql.Date date, double amount, int customerId, String status) {
        String query = "INSERT INTO `order` (id, date, amount, customerId, status) VALUES (?, ?, ?, ?, ?)";  // Utilisation des backticks pour éviter le mot réservé "order"
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);          // id
            stmt.setDate(2, date);       // date
            stmt.setDouble(3, amount);   // amount
            stmt.setInt(4, customerId);  // customerId
            stmt.setString(5, status);   // status
            stmt.executeUpdate();
            LOGGER.info("Insertion réussie de la commande.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'exécution de la requête d'insertion", e);
        }
    }

}
