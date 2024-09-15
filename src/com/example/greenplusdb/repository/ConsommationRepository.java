package com.example.greenplusdb.repository;

import com.example.greenplusdb.model.*;
import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.enums.TypeAliment;
import com.example.greenplusdb.model.enums.TypeConsommation;
import com.example.greenplusdb.model.enums.TypeEnergie;
import com.example.greenplusdb.model.enums.TypeVehicule;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsommationRepository {
    private final Connection connection;

    public ConsommationRepository() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void addConsommation(Consommation consommation) throws SQLException {
        Connection connection = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            if (consommation == null) {
                throw new IllegalArgumentException("Consommation is null");
            }
            if (consommation.getUser() == null) {
                System.err.println("User is null. Consommation details: " + consommation);
                throw new IllegalArgumentException("User is null in consommation");
            }

            long consommationId;


            consommationId = handleSpecificConsommationInsert(connection, consommation);

            connection.commit();
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();

            if (connection != null && !connection.isClosed()) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            throw new RuntimeException("Failed to add consommation", e);
        } finally {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private long handleSpecificConsommationInsert(Connection connection, Consommation consommation) throws SQLException {
        String insertSpecificTableSql;
        long generatedId = -1;

        if (consommation instanceof Transport) {
            insertSpecificTableSql = "INSERT INTO transport (user_id, type_consumption, impact, start_date, end_date, distance_parcourue, type_de_vehicule) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Transport transport = (Transport) consommation;
            try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtSpecific.setLong(1, transport.getUser().getId());
                pstmtSpecific.setObject(2, transport.getTypeConsumption().name(), java.sql.Types.OTHER);
                pstmtSpecific.setDouble(3, transport.getImpact());
                pstmtSpecific.setTimestamp(4, Timestamp.valueOf(transport.getStartDate()));
                pstmtSpecific.setTimestamp(5, Timestamp.valueOf(transport.getEndDate()));
                pstmtSpecific.setDouble(6, transport.getDistanceParcourue());
                pstmtSpecific.setObject(7, transport.getTypeDeVehicule().name(), java.sql.Types.OTHER);
                pstmtSpecific.executeUpdate();


                try (ResultSet rs = pstmtSpecific.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getLong(1);
                    }
                }
            }
        } else if (consommation instanceof Logement) {
            insertSpecificTableSql = "INSERT INTO logement (user_id, type_consumption, impact, start_date, end_date, consommation_energie, type_energie) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Logement logement = (Logement) consommation;
            try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtSpecific.setLong(1, logement.getUser().getId());
                pstmtSpecific.setObject(2, logement.getTypeConsumption().name(), java.sql.Types.OTHER);
                pstmtSpecific.setDouble(3, logement.getImpact());
                pstmtSpecific.setTimestamp(4, Timestamp.valueOf(logement.getStartDate()));
                pstmtSpecific.setTimestamp(5, Timestamp.valueOf(logement.getEndDate()));
                pstmtSpecific.setDouble(6, logement.getConsommationEnergie());
                pstmtSpecific.setObject(7, logement.getTypeEnergie().name(), java.sql.Types.OTHER);
                pstmtSpecific.executeUpdate();


                try (ResultSet rs = pstmtSpecific.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getLong(1);
                    }
                }
            }
        } else if (consommation instanceof Alimentation) {
            insertSpecificTableSql = "INSERT INTO alimentation (user_id, type_aliment, poids, type_consumption, impact, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
            Alimentation alimentation = (Alimentation) consommation;


            alimentation.setImpact(alimentation.calculerImpact());

            try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtSpecific.setLong(1, alimentation.getUser().getId());
                pstmtSpecific.setObject(2, alimentation.getTypeAliment().name(), java.sql.Types.OTHER);
                pstmtSpecific.setDouble(3, alimentation.getPoids());
                pstmtSpecific.setObject(4, consommation.getTypeConsumption().name(), java.sql.Types.OTHER);
                pstmtSpecific.setDouble(5, alimentation.getImpact());
                pstmtSpecific.setTimestamp(6, Timestamp.valueOf(consommation.getStartDate()));
                pstmtSpecific.setTimestamp(7, Timestamp.valueOf(consommation.getEndDate()));
                pstmtSpecific.executeUpdate();


                try (ResultSet rs = pstmtSpecific.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getLong(1);
                    }
                }
            }
        }

        return generatedId;
    }

    public void updateConsommation(Consommation consommation) throws SQLException {
        String updateConsommationSql = "UPDATE consommation SET user_id = ?, type_consumption = ?, impact = ?, start_date = ?, end_date = ?, updated_at = ? WHERE id = ?";
        String updateSpecificTableSql = null;
        Connection connection = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement pstmtConsommation = connection.prepareStatement(updateConsommationSql)) {
                pstmtConsommation.setLong(1, consommation.getUser().getId());
                pstmtConsommation.setObject(2, consommation.getTypeConsumption().name(), java.sql.Types.OTHER);
                pstmtConsommation.setDouble(3, consommation.getImpact());
                pstmtConsommation.setTimestamp(4, Timestamp.valueOf(consommation.getStartDate()));
                pstmtConsommation.setTimestamp(5, Timestamp.valueOf(consommation.getEndDate()));
                pstmtConsommation.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                pstmtConsommation.setLong(7, consommation.getId());

                pstmtConsommation.executeUpdate();
            }

            if (consommation instanceof Transport) {
                updateSpecificTableSql = "UPDATE transport SET distance_parcourue = ?, type_de_vehicule = ? WHERE id = ?";
                Transport transport = (Transport) consommation;
                try (PreparedStatement pstmtSpecific = connection.prepareStatement(updateSpecificTableSql)) {
                    pstmtSpecific.setDouble(1, transport.getDistanceParcourue());
                    pstmtSpecific.setObject(2, transport.getTypeDeVehicule().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setLong(3, consommation.getId());
                    pstmtSpecific.executeUpdate();
                }
            } else if (consommation instanceof Logement) {
                updateSpecificTableSql = "UPDATE logement SET consommation_energie = ?, type_energie = ? WHERE id = ?";
                Logement logement = (Logement) consommation;
                try (PreparedStatement pstmtSpecific = connection.prepareStatement(updateSpecificTableSql)) {
                    pstmtSpecific.setDouble(1, logement.getConsommationEnergie());
                    pstmtSpecific.setObject(2, logement.getTypeEnergie().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setLong(3, consommation.getId());
                    pstmtSpecific.executeUpdate();
                }
            } else if (consommation instanceof Alimentation) {
                updateSpecificTableSql = "UPDATE alimentation SET type_aliment = ?, poids = ? WHERE id = ?";
                Alimentation alimentation = (Alimentation) consommation;
                alimentation.setImpact(alimentation.calculerImpact());
                try (PreparedStatement pstmtSpecific = connection.prepareStatement(updateSpecificTableSql)) {
                    pstmtSpecific.setObject(1, alimentation.getTypeAliment().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setDouble(2, alimentation.getPoids());
                    pstmtSpecific.setLong(3, consommation.getId());
                    pstmtSpecific.executeUpdate();
                }
            }

            connection.commit();
            System.out.println("Consommation updated successfully");

        } catch (SQLException e) {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            throw new RuntimeException("Failed to update consommation", e);
        } finally {
            if (connection != null && !connection.isClosed()) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void deleteConsommation(long consommationId) throws SQLException {
        String deleteConsommationSql = "DELETE FROM consommation WHERE id = ?";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmtDeleteConsommation = connection.prepareStatement(deleteConsommationSql)) {
            pstmtDeleteConsommation.setLong(1, consommationId);
            pstmtDeleteConsommation.executeUpdate();
        }
    }
    public Consommation getConsommationById(long id) throws SQLException {
        String selectConsommationSql = "SELECT * FROM consommation WHERE id = ?";
        Consommation consommation = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectConsommationSql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long userId = rs.getLong("user_id");
                    TypeConsommation typeConsommation = TypeConsommation.valueOf(rs.getString("type_consumption"));
                    double impact = rs.getDouble("impact");
                    LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                    LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                    LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null;

                    // Based on the type, call the respective retrieval method
                    switch (typeConsommation) {
                        case TRANSPORT:
                            consommation = getTransportById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                            break;
                        case LOGEMENT:
                            consommation = getLogementById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                            break;
                        case ALIMENTATION:
                            consommation = getAlimentationById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                            break;
                    }
                }
            }
        }

        return consommation;
    }


    private Transport getTransportById(long id, long userId, double impact, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt, LocalDateTime updatedAt) throws SQLException {
        String selectTransportSql = "SELECT * FROM transport WHERE id = ?";
        Transport transport = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectTransportSql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double distanceParcourue = rs.getDouble("distance_parcourue");
                    TypeVehicule typeVehicule = TypeVehicule.valueOf(rs.getString("type_de_vehicule"));

                    transport = new Transport();
                    transport.setId(id);
                    transport.setUser(new User(userId));
                    transport.setImpact(impact);
                    transport.setStartDate(startDate);
                    transport.setEndDate(endDate);
                    transport.setCreatedAt(createdAt);
                    transport.setUpdatedAt(updatedAt);
                    transport.setDistanceParcourue(distanceParcourue);
                    transport.setTypeDeVehicule(typeVehicule);
                }
            }
        }

        return transport;
    }


    private Logement getLogementById(long id, long userId, double impact, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt, LocalDateTime updatedAt) throws SQLException {
        String selectLogementSql = "SELECT * FROM logement WHERE id = ?";
        Logement logement = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectLogementSql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double consommationEnergie = rs.getDouble("consommation_energie");
                    TypeEnergie typeEnergie = TypeEnergie.valueOf(rs.getString("type_energie"));

                    logement = new Logement();
                    logement.setId(id);
                    logement.setUser(new User(userId));
                    logement.setImpact(impact);
                    logement.setStartDate(startDate);
                    logement.setEndDate(endDate);
                    logement.setCreatedAt(createdAt);
                    logement.setUpdatedAt(updatedAt);
                    logement.setConsommationEnergie(consommationEnergie);
                    logement.setTypeEnergie(typeEnergie);
                }
            }
        }

        return logement;
    }


    private Alimentation getAlimentationById(long id, long userId, double impact, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime createdAt, LocalDateTime updatedAt) throws SQLException {
        String selectAlimentationSql = "SELECT * FROM alimentation WHERE id = ?";
        Alimentation alimentation = null;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectAlimentationSql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TypeAliment typeAliment = TypeAliment.valueOf(rs.getString("type_aliment"));
                    double poids = rs.getDouble("poids");

                    alimentation = new Alimentation();
                    alimentation.setId(id);
                    alimentation.setImpact(impact);
                    alimentation.setStartDate(startDate);
                    alimentation.setEndDate(endDate);
                    alimentation.setCreatedAt(createdAt);
                    alimentation.setUpdatedAt(updatedAt);
                    alimentation.setTypeAliment(typeAliment);
                    alimentation.setPoids(poids);
                }
            }
        }

        return alimentation;
    }
    public List<Consommation> getAllConsommations() throws SQLException {
        String selectAllConsommationsSql = "SELECT * FROM consommation";
        List<Consommation> consommations = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectAllConsommationsSql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                long id = rs.getLong("id");
                long userId = rs.getLong("user_id");
                TypeConsommation typeConsommation = TypeConsommation.valueOf(rs.getString("type_consumption"));
                double impact = rs.getDouble("impact");
                LocalDateTime startDate = rs.getTimestamp("start_date").toLocalDateTime();
                LocalDateTime endDate = rs.getTimestamp("end_date").toLocalDateTime();
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                LocalDateTime updatedAt = rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null;

                Consommation consommation = null;
                switch (typeConsommation) {
                    case TRANSPORT:
                        consommation = getTransportById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                        break;
                    case LOGEMENT:
                        consommation = getLogementById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                        break;
                    case ALIMENTATION:
                        consommation = getAlimentationById(id, userId, impact, startDate, endDate, createdAt, updatedAt);
                        break;
                }

                consommations.add(consommation);
            }
        }

        return consommations;
    }




}
