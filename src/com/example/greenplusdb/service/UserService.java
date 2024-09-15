package com.example.greenplusdb.service;

import com.example.greenplusdb.model.User;
import com.example.greenplusdb.repository.UserRepository;
import com.example.greenplusdb.utils.DateUtils;

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
                .filter(user -> user.calculerConsommationTotale() > threshold)
                .collect(Collectors.toList());
    }

    public List<User> detectInactiveUsers(List<User> users, LocalDateTime startDate, LocalDateTime endDate) {
        List<LocalDateTime> dateRange = DateUtils.dateListRange(startDate, endDate);
        return users.stream()
                .filter(user -> user.getConsommations().stream()
                        .noneMatch(consommation -> DateUtils.isDateAvailable(
                                consommation.getStartDate(),
                                consommation.getEndDate(),
                                dateRange)))
                .collect(Collectors.toList());
    }

    public List<User> sortUsersByConsumption(List<User> users) {
        return users.stream()
                .sorted((user1, user2) -> Double.compare(user2.calculerConsommationTotale(), user1.calculerConsommationTotale()))
                .collect(Collectors.toList());
    }
}
