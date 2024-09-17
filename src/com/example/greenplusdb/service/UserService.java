package com.example.greenplusdb.service;

import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.model.User;
import com.example.greenplusdb.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(User user) throws SQLException {
        try {
            userRepository.addUser(user);
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error adding user", e);
        }
    }

    public User getUserById(Long id) throws SQLException {
        return userRepository.getUserById(id);
    }

    public void updateUser(User user) throws SQLException {
        try {
            userRepository.updateUser(user);
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    public void deleteUser(Long id) throws SQLException {
        try {
            userRepository.deleteUser(id);
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    public List<User> findAllUsers() throws SQLException {
        return userRepository.findAllUsers();
    }



    public List<User> filterUsersByTotalConsumption() throws SQLException {
        double threshold = 3000.0;
        List<User> users = userRepository.findAllUsers();

        return users.stream()
                .filter(user -> {
                    try {
                        double totalConsumption = ConsommationService.calculateTotalConsumptionByUserId(user.getId());
                        return totalConsumption > threshold;
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<User> detectInactiveUsers(List<User> allUsers, LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        System.out.println("Detecting inactive users from " + startDate + " to " + endDate);

        // Fetch all users
        List<User> users = userRepository.findAllUsers();

        List<User> inactiveUsers = users.stream()
                .filter(user -> {
                    System.out.println("Checking user: " + user.getName());

                    try {

                        List<Consommation> consommations = ConsommationService.getConsommationByUserId(user.getId());

                        if (consommations == null || consommations.isEmpty()) {
                            System.out.println("User " + user.getName() + " has no consommations. Marking as inactive.");
                            return true;
                        }

                        boolean hasOverlap = consommations.stream()
                                .anyMatch(consommation -> {
                                    LocalDateTime consommationStart = consommation.getStartDate();
                                    LocalDateTime consommationEnd = consommation.getEndDate();
                                    System.out.println("Checking consommation: Start = " + consommationStart + ", End = " + consommationEnd);

                                    return isDateAvailable(startDate, endDate, consommationStart, consommationEnd);
                                });

                        System.out.println("User " + user.getName() + " has overlap: " + hasOverlap);
                        return !hasOverlap;

                    } catch (SQLException e) {
                        e.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());

        System.out.println("Inactive users: " + inactiveUsers);
        return inactiveUsers;
    }

    public static boolean isDateAvailable(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime consommationStart, LocalDateTime consommationEnd) {
        System.out.println("Checking overlap for:");
        System.out.println("Date Range Start: " + startDate + ", Date Range End: " + endDate);
        System.out.println("Consommation Start: " + consommationStart + ", Consommation End: " + consommationEnd);

        boolean overlap = consommationStart.isBefore(endDate) && consommationEnd.isAfter(startDate);

        System.out.println("Overlap detected: " + overlap);
        return overlap;
    }





    public List<User> sortUsersByConsumption(List<User> users) {
        return users.stream()
                .sorted((user1, user2) -> Double.compare(user2.calculerConsommationTotale(), user1.calculerConsommationTotale()))
                .collect(Collectors.toList());
    }
}
