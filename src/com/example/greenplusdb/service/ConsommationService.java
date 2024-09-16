package com.example.greenplusdb.service;

import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.repository.ConsommationRepository;

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


    public void deleteConsommation(long consommationId) throws SQLException {
        try {
            consommationRepository.deleteConsommation(consommationId);
            System.out.println("Consommation deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting consommation", e);
        }
    }


    public Consommation getConsommationById(long id) throws SQLException {
        return consommationRepository.getConsommationById(id);
    }

    public List<Consommation> getAllConsommations() throws SQLException {
        return consommationRepository.getAllConsommations();
    }


    public List<Consommation> getConsommationByUserId(long userId) throws SQLException {
        return consommationRepository.getConsommationByUserId(userId);
    }


    public double calculateConsommationMoyenne(LocalDateTime startDate, LocalDateTime endDate, long userId) throws SQLException {
        List<Consommation> consommations = getConsommationByUserId(userId);


        List<Consommation> filteredConsommations = consommations.stream()
                .filter(c -> c.getStartDate() != null &&
                        c.getEndDate() != null &&
                        !c.getEndDate().isBefore(startDate) &&
                        !c.getStartDate().isAfter(endDate))
                .collect(Collectors.toList());


        System.out.println("Filtered consommations count: " + filteredConsommations.size());
        filteredConsommations.forEach(c -> {
            System.out.println("Filtered Consommation ID: " + c.getId());
            System.out.println("Start Date: " + c.getStartDate());
            System.out.println("End Date: " + c.getEndDate());
            System.out.println("Impact: " + c.calculerImpact());
        });


        return filteredConsommations.stream()
                .mapToDouble(Consommation::calculerImpact)
                .average()
                .orElse(0.0);
    }

}
