package org.example.dev2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CustomerInserter {

    public static void addCustomers() {
        // Les données des clients
        Object[][] customers = {
                {1, "John Doe", "john.doe@example.com", "1234567890"},
                {2, "Jane Smith", "jane.smith@example.com", "0987654321"},
                {3, "Alice Brown", "alice.brown@example.com", "1122334455"},
        };

        // Requête SQL pour insérer un client
        String sql = "INSERT INTO customer (id, nom, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection connection = new DBConnexion().getConnection()) {
            if (connection == null) {
                System.err.println("Impossible de se connecter à la base de données.");
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Object[] customer : customers) {
                    statement.setInt(1, (int) customer[0]);
                    statement.setString(2, (String) customer[1]);
                    statement.setString(3, (String) customer[2]);
                    statement.setString(4, (String) customer[3]);
                    statement.addBatch(); // Ajout au batch
                }

                // Exécuter le batch d'insertion
                int[] results = statement.executeBatch();
                System.out.println("Insertion réussie pour " + results.length + " clients.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des clients : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        addCustomers();
    }
}
