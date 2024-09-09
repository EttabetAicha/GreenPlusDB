package com.example.greenplusdb.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;


    private static final String URL = "jdbc:postgresql://localhost:5432/postgress";
    private static final String USER = "GreenPulse";
    private static final String PASSWORD = "";


    private DatabaseConnection() throws SQLException {
        try {

            Class.forName("org.postgresql.Driver");

            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Le driver PostgreSQL JDBC est introuvable.", e);
        }
    }


    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }


    public Connection getConnection() {
        return connection;
    }
}
