package com.example.greenplusdb.repository;

import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ConsommationRepository {

    Connection connection;
    public void addConsommation(Consommation consommation) throws SQLException {

        String sql = "INSERT INTO consommations (user_id, type, impact, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Set parameters
            pstmt.setLong(1, consommation.getUser().getId());
            pstmt.setString(2, consommation.getTypeConsommation().toString());
            pstmt.setDouble(3, consommation.getImpact());
            pstmt.setTimestamp(4, consommation.getCreatedAt());
            pstmt.setTimestamp(5, consommation.getUpdatedAt());

            // Execute and commit
            pstmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            // Rollback transaction if an exception occurs
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new SQLException("Error rolling back transaction", rollbackEx);
            }
            throw new SQLException("Error inserting consommation", e);
        }

    }
    // Update an existing consommation
    public void updateConsommation(Consommation consommation) throws SQLException {
        String sql = "UPDATE consommations SET impact = ?, updated_at = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Start transaction
            connection.setAutoCommit(false);

            // Set parameters
            pstmt.setDouble(1, consommation.getImpact());
            pstmt.setTimestamp(2, consommation.getUpdatedAt());
            pstmt.setInt(3, consommation.getId());

            // Execute and commit
            pstmt.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            // Rollback transaction if an exception occurs
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new SQLException("Error rolling back transaction", rollbackEx);
            }
            throw new SQLException("Error inserting consommation", e);
        }
    }
}
