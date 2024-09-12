package com.example.greenplusdb.repository;

import com.example.greenplusdb.model.*;
import com.example.greenplusdb.config.DatabaseConnection;
import com.example.greenplusdb.model.enums.TypeConsommation;

import java.sql.*;
import java.time.LocalDateTime;

public class ConsommationRepository {
    private final Connection connection;

    public ConsommationRepository() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    public void addConsommation(Consommation consommation) throws SQLException {
        String insertConsommationSql = "INSERT INTO consommation (user_id, type_consumption, impact, start_date, end_date, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertSpecificTableSql = null;
        Connection connection = null;

        try {
            connection = DatabaseConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            long consommationId;
            try (PreparedStatement pstmtConsommation = connection.prepareStatement(insertConsommationSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmtConsommation.setLong(1, consommation.getUser().getId());
                pstmtConsommation.setObject(2, consommation.getTypeConsumption().name(), java.sql.Types.OTHER);
                pstmtConsommation.setDouble(3, consommation.getImpact());
                pstmtConsommation.setTimestamp(4, Timestamp.valueOf(consommation.getStartDate()));
                pstmtConsommation.setTimestamp(5, Timestamp.valueOf(consommation.getEndDate()));
                pstmtConsommation.setTimestamp(6, Timestamp.valueOf(consommation.getCreatedAt()));
                pstmtConsommation.setTimestamp(7, consommation.getUpdatedAt() != null ? Timestamp.valueOf(consommation.getUpdatedAt()) : null);

                int affectedRows = pstmtConsommation.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating consommation failed, no rows affected.");
                }


                try (ResultSet rs = pstmtConsommation.getGeneratedKeys()) {
                    if (rs.next()) {
                        consommationId = rs.getLong(1);
                    } else {
                        throw new SQLException("Failed to retrieve generated ID for consommation.");
                    }
                }
            }
            if (consommation instanceof Transport) {
                insertSpecificTableSql = "INSERT INTO transport (id, user_id, type_consumption, impact, start_date, end_date, distance_parcourue, type_de_vehicule) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                Transport transport = (Transport) consommation;
                try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql)) {
                    pstmtSpecific.setLong(1, consommationId);
                    pstmtSpecific.setLong(2, transport.getUser().getId());
                    pstmtSpecific.setObject(3, transport.getTypeConsumption().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setDouble(4, transport.getImpact());
                    pstmtSpecific.setTimestamp(5, Timestamp.valueOf(transport.getStartDate()));
                    pstmtSpecific.setTimestamp(6, Timestamp.valueOf(transport.getEndDate()));
                    pstmtSpecific.setDouble(7, transport.getDistanceParcourue());
                    pstmtSpecific.setObject(8, transport.getTypeDeVehicule().name(), java.sql.Types.OTHER);
                    pstmtSpecific.executeUpdate();
                }
            } else if (consommation instanceof Logement) {
                insertSpecificTableSql = "INSERT INTO logement (id, user_id, type_consumption, impact, start_date, end_date, consommation_energie, type_energie) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                Logement logement = (Logement) consommation;
                try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql)) {
                    pstmtSpecific.setLong(1, consommationId);
                    pstmtSpecific.setLong(2, logement.getUser().getId());
                    pstmtSpecific.setObject(3, logement.getTypeConsumption().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setDouble(4, logement.getImpact());
                    pstmtSpecific.setTimestamp(5, Timestamp.valueOf(logement.getStartDate()));
                    pstmtSpecific.setTimestamp(6, Timestamp.valueOf(logement.getEndDate()));
                    pstmtSpecific.setDouble(7, logement.getConsommationEnergie());
                    pstmtSpecific.setObject(8, logement.getTypeEnergie().name(), java.sql.Types.OTHER);
                    pstmtSpecific.executeUpdate();
                }
            } else if (consommation instanceof Alimentation) {
                insertSpecificTableSql = "INSERT INTO alimentation (id, user_id, type_aliment, poids, type_consumption, impact, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                Alimentation alimentation = (Alimentation) consommation;


                alimentation.setImpact(alimentation.calculerImpact());

                try (PreparedStatement pstmtSpecific = connection.prepareStatement(insertSpecificTableSql)) {
                    pstmtSpecific.setLong(1, consommationId);
                    pstmtSpecific.setLong(2, alimentation.getUser().getId());
                    pstmtSpecific.setObject(3, alimentation.getTypeAliment().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setDouble(4, alimentation.getPoids());
                    pstmtSpecific.setObject(5, consommation.getTypeConsumption().name(), java.sql.Types.OTHER);
                    pstmtSpecific.setDouble(6, alimentation.getImpact());
                    pstmtSpecific.setTimestamp(7, Timestamp.valueOf(consommation.getStartDate()));
                    pstmtSpecific.setTimestamp(8, Timestamp.valueOf(consommation.getEndDate()));
                    pstmtSpecific.executeUpdate();
                }
            }

            connection.commit();
            System.out.println("Consommation added successfully");

        } catch (SQLException e) {
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

}
