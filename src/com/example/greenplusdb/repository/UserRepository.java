package com.example.greenplusdb.repository;

import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.User;

import java.sql.*;
import java.util.*;

public class UserRepository {

    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (username, email) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

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
        }
    }

    public User getUserById(Long id) throws SQLException {
        String query = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("email")
                    );
                }
            }
        }
        return null;
    }

    public void updateUser(User user) throws SQLException {
        String query = "UPDATE users SET username = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setLong(3, user.getId());

            statement.executeUpdate();
        }
    }

    public void deleteUser(Long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        }
    }


    public List<User> findAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email")
                ));
            }
        }
        return users;
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
                User user = new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("email")
                );

                user.setTotalImpact(rs.getDouble("total_impact"));

                users.add(user);
            }
        }

        return users;
    }
}
