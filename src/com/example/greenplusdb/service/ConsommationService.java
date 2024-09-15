package com.example.greenplusdb.service;

import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.model.User;
import com.example.greenplusdb.repository.ConsommationRepository;
import com.example.greenplusdb.utils.DateUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ConsommationService {
    private final ConsommationRepository consommationRepository;

    public ConsommationService(ConsommationRepository consommationRepository) {
        this.consommationRepository = consommationRepository;
    }


    public void addConsommation(Consommation consommation) throws SQLException {
        try {
            consommationRepository.addConsommation(consommation);

        } catch (SQLException e) {
            throw new RuntimeException("Error adding consommation", e);
        }
    }


//    public void updateConsommation(Consommation consommation) throws SQLException {
//        try {
//            consommationRepository.updateConsommation(consommation);
//            System.out.println("Consommation updated successfully.");
//        } catch (SQLException e) {
//            throw new RuntimeException("Error updating consommation", e);
//        }
//    }


    public void deleteConsommation(long consommationId) throws SQLException {
        try {
            consommationRepository.deleteConsommation(consommationId);
            System.out.println("Consommation deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting consommation", e);
        }
    }
    public Consommation getConsommationById(long id) throws SQLException {
        return new ConsommationRepository().getConsommationById(id);
    }
    public List<Consommation> getAllConsommations() throws SQLException {
        return consommationRepository.getAllConsommations();
    }


    public double calculerConsommationMoyenne(User user, LocalDateTime startDate, LocalDateTime endDate) {

        List<Consommation> consommations = user.getConsommations();
        List<LocalDateTime> dateRange = DateUtils.dateListRange(startDate, endDate);
        List<Consommation> consommationsWithinRange = consommations.stream()
                .filter(consommation -> DateUtils.isDateAvailable(
                        consommation.getStartDate(),
                        consommation.getEndDate(),
                        dateRange))
                .collect(Collectors.toList());

        double totalImpact = consommationsWithinRange.stream()
                .mapToDouble(Consommation::calculerImpact)
                .sum();
        long daysInRange = DateUtils.calculateDaysBetween(startDate, endDate.plusDays(1));

        return daysInRange > 0 ? totalImpact / daysInRange : 0;
    }
}
