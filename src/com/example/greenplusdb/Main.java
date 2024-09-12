package com.example.greenplusdb;

import com.example.greenplusdb.model.*;
import com.example.greenplusdb.model.enums.TypeAliment;
import com.example.greenplusdb.model.enums.TypeConsommation;

import com.example.greenplusdb.model.enums.TypeEnergie;
import com.example.greenplusdb.model.enums.TypeVehicule;
import com.example.greenplusdb.repository.ConsommationRepository;
import com.example.greenplusdb.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        try {

            UserRepository userRepository = new UserRepository();
            ConsommationRepository consommationRepository = new ConsommationRepository();


            User user = new User(5L, "test", "test.22@gmail.com");
            if (userRepository.getUserById(user.getId()) == null) {
                userRepository.addUser(user);
                System.out.println("Added user: " + user);
            } else {
                System.out.println("User already exists: " + user);
            }


            Alimentation alimentation = new Alimentation();
            alimentation.setTypeConsumption(TypeConsommation.ALIMENTATION);
            alimentation.setTypeAliment(TypeAliment.VIANDE);
            alimentation.setPoids(2.0);
            alimentation.setCreatedAt(LocalDateTime.now());
            alimentation.setStartDate(LocalDateTime.now().minusDays(30));
            alimentation.setEndDate(LocalDateTime.now());
            alimentation.setUser(user);

            double impact = alimentation.calculerImpact();
            alimentation.setImpact(impact);


            System.out.println("Attempting to add alimentation consommation: " + alimentation);
            consommationRepository.addConsommation(alimentation);


            Transport transport = new Transport();
            transport.setTypeConsumption(TypeConsommation.TRANSPORT);
            transport.setDistanceParcourue(100.0);
            transport.setTypeDeVehicule(TypeVehicule.TRAIN);
            transport.setCreatedAt(LocalDateTime.now());
            transport.setStartDate(LocalDateTime.now().minusDays(30));
            transport.setEndDate(LocalDateTime.now());
            transport.setUser(user);


            impact = transport.calculerImpact();
            transport.setImpact(impact);

            System.out.println("Attempting to add transport consommation: " + transport);
            consommationRepository.addConsommation(transport);


            Logement logement = new Logement();
            logement.setTypeConsumption(TypeConsommation.LOGEMENT);
            logement.setConsommationEnergie(300.0);
            logement.setTypeEnergie(TypeEnergie.ELECTRICITE);
            logement.setCreatedAt(LocalDateTime.now());
            logement.setStartDate(LocalDateTime.now().minusDays(30));
            logement.setEndDate(LocalDateTime.now());
            logement.setUser(user);


            impact = logement.calculerImpact();
            logement.setImpact(impact);

            System.out.println("Attempting to add logement consommation: " + logement);
            consommationRepository.addConsommation(logement);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database operation failed. Please check your database connection and queries.");
        }
    }
}
