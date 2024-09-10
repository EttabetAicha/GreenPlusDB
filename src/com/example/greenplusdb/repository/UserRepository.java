package com.example.greenplusdb.repository;

import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.User;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class UserRepository {
    public void addUser(User user) throws SQLException{

    }

   /* public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Start transaction
            connection.setAutoCommit(false);

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                    }
                }
            }

            connection.commit();
        } catch (SQLException e) {
            throw new SQLException("Error adding user", e);
        }
    }*/


    public User getUserById(Long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email")
                    );
                }
            }
        }
        return null;
    }


    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Start transaction
            connection.setAutoCommit(false);

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());

            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new SQLException("Error updating user", e);
        }
    }

    // Delete a user by ID
    public void deleteUser(Long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Start transaction
            connection.setAutoCommit(false);

            statement.setLong(1, id);

            statement.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            throw new SQLException("Error deleting user", e);
        }
    }


    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email")
                ));
            }
        }

        return users;
    }


    public List<User> getUsersAboveConsumptionThreshold(double threshold) throws SQLException {
        String sql = "SELECT u.*, COALESCE(SUM(c.impact), 0) as total_impact " +
                "FROM users u " +
                "LEFT JOIN consommations c ON u.id = c.user_id " +
                "GROUP BY u.id " +
                "HAVING COALESCE(SUM(c.impact), 0) > ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setDouble(1, threshold);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
        }

        return users;
    }


    public Map<User, Double> getAverageConsumptionPerUser(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT u.*, AVG(c.impact) as average_impact " +
                "FROM users u " +
                "JOIN consommations c ON u.id = c.user_id " +
                "WHERE c.created_at BETWEEN ? AND ? " +
                "GROUP BY u.id";
        Map<User, Double> userAverageConsumption = new HashMap<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    User user = new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    );
                    double averageImpact = rs.getDouble("average_impact");
                    userAverageConsumption.put(user, averageImpact);
                }
            }
        }

        return userAverageConsumption;
    }


    public List<User> getInactiveUsers(Date sinceDate) throws SQLException {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "LEFT JOIN consommations c ON u.id = c.user_id " +
                "WHERE c.user_id IS NULL OR c.created_at < ?";
        List<User> inactiveUsers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setDate(1, new Date(sinceDate.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    inactiveUsers.add(new User(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email")
                    ));
                }
            }
        }

        return inactiveUsers;
    }


    public List<User> getUsersSortedByConsumption() throws SQLException {
        String sql = "SELECT u.*, COALESCE(SUM(c.impact), 0) as total_impact " +
                "FROM users u " +
                "LEFT JOIN consommations c ON u.id = c.user_id " +
                "GROUP BY u.id " +
                "ORDER BY total_impact DESC";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("email")
                ));
            }
        }

        return users;
    }
}
