package com.example.greenplusdb.repository;

import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {


    public void addUser(User user) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "INSERT INTO users (name, email) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());

        int affectedRows = statement.executeUpdate();
        if (affectedRows > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            }
        }

        statement.close();
    }


    public User getUserById(Long id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);

        ResultSet resultSet = statement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            user = new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );
        }

        resultSet.close();
        statement.close();
        return user;
    }


    public void updateUser(User user) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getName());
        statement.setString(2, user.getEmail());
        statement.setLong(3, user.getId());

        statement.executeUpdate();
        statement.close();
    }


    public void deleteUser(Long id) throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "DELETE FROM users WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, id);

        statement.executeUpdate();
        statement.close();
    }


    public List<User> getAllUsers() throws SQLException {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        String query = "SELECT * FROM users";
        PreparedStatement statement = connection.prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();
        List<User> users = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email")
            );
            users.add(user);
        }

        resultSet.close();
        statement.close();
        return users;
    }


    private void closeResources(PreparedStatement statement, ResultSet resultSet) throws SQLException {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
    }
}
