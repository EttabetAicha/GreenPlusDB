package com.example.greenplusdb;

import com.example.greenplusdb.model.Consommation;
import com.example.greenplusdb.model.User;
import com.example.greenplusdb.model.Transport;
import com.example.greenplusdb.model.Logement;
import com.example.greenplusdb.model.Alimentation;
import com.example.greenplusdb.model.enums.TypeAliment;
import com.example.greenplusdb.model.enums.TypeConsommation;
import com.example.greenplusdb.model.enums.TypeEnergie;
import com.example.greenplusdb.model.enums.TypeVehicule;
import com.example.greenplusdb.service.ConsommationService;
import com.example.greenplusdb.service.UserService;
import com.example.greenplusdb.repository.ConsommationRepository;
import com.example.greenplusdb.repository.UserRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static UserService userService;
    private static ConsommationService consommationService;

    public static void main(String[] args) throws SQLException {
        UserRepository userRepository = new UserRepository();
        ConsommationRepository consommationRepository = new ConsommationRepository();

        userService = new UserService(userRepository);
        consommationService = new ConsommationService(consommationRepository);

        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Manage Users");
            System.out.println("2. Manage Consommations");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageUsers();
                    break;
                case 2:
                    manageConsommations();
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void manageUsers() {
        while (true) {
            System.out.println("\n==== User Management ====");
            System.out.println("1. Add User");
            System.out.println("2. Update User");
            System.out.println("3. Delete User");
            System.out.println("4. List All Users");
            System.out.println("5. Sort Users by Consumption");
            System.out.println("6. Filter Users by Total Consumption");
            System.out.println("7. Detect Inactive Users");
            System.out.println("8. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    listAllUsers();
                    break;
                case 5:
                    sortUsersByConsumption();
                    break;
                case 6:
                    filterUsersByTotalConsumption();
                    break;
                case 7:
                    detectInactiveUsers();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addUser() {
        try {
            System.out.println("\n==== Add User ====");
            System.out.print("Enter User ID: ");
            long userId = scanner.nextLong();
            scanner.nextLine();

            System.out.print("Enter User Name: ");
            String name = scanner.nextLine();

            System.out.print("Enter User Email: ");
            String email = scanner.nextLine();

            User user = new User(userId, name, email);
            userService.addUser(user);
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    private static void updateUser() {
        try {
            System.out.println("\n==== Update User ====");
            System.out.print("Enter User ID to update: ");
            long userId = scanner.nextLong();
            scanner.nextLine();

            User user = userService.getUserById(userId);
            if (user != null) {
                System.out.print("Enter new User Name (or press Enter to keep current): ");
                String name = scanner.nextLine();
                if (!name.isEmpty()) {
                    user.setName(name);
                }

                System.out.print("Enter new User Email (or press Enter to keep current): ");
                String email = scanner.nextLine();
                if (!email.isEmpty()) {
                    user.setEmail(email);
                }

                userService.updateUser(user);
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            System.out.println("\n==== Delete User ====");
            System.out.print("Enter User ID to delete: ");
            long userId = scanner.nextLong();
            scanner.nextLine();

            userService.deleteUser(userId);
            System.out.println("User deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void listAllUsers() {
        try {
            System.out.println("\n==== List All Users ====");
            List<User> users = userService.findAllUsers();
            users.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error listing users: " + e.getMessage());
        }
    }

    private static void sortUsersByConsumption() {
        try {
            System.out.println("\n==== Sort Users by Consumption ====");
            List<User> allUsers = userService.findAllUsers();
            List<User> sortedUsers = userService.sortUsersByConsumption(allUsers);
            sortedUsers.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error sorting users: " + e.getMessage());
        }
    }

    private static void filterUsersByTotalConsumption() {
        try {
            System.out.println("\n==== Filter Users by Total Consumption ====");
            List<User> filteredUsers = userService.filterUsersByTotalConsumption();
            filteredUsers.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error filtering users: " + e.getMessage());
        }
    }

    private static void detectInactiveUsers() {
        try {
            System.out.println("\n==== Detect Inactive Users ====");
            System.out.print("Enter start date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime startDate = parseDate(scanner.nextLine());
            System.out.print("Enter end date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime endDate = parseDate(scanner.nextLine());

            List<User> allUsers = userService.findAllUsers();
            List<User> inactiveUsers = userService.detectInactiveUsers(allUsers, startDate, endDate);
            inactiveUsers.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error detecting inactive users: " + e.getMessage());
        }
    }

    private static void manageConsommations() {
        while (true) {
            System.out.println("\n==== Consommation Management ====");
            System.out.println("1. Add Consommation");
            System.out.println("2. Update Consommation");
            System.out.println("3. Delete Consommation");
            System.out.println("4. List All Consommations");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addConsommation();
                    break;
                case 2:
                   // TODO
                    break;
                case 3:
                    deleteConsommation();
                    break;
                case 4:
                    listAllConsommations();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addConsommation() {
        try {
            System.out.println("\n==== Add Consommation ====");
            System.out.print("Enter user ID: ");
            long userId = scanner.nextLong();
            scanner.nextLine(); // Clear the buffer

            // Check if user exists
            User user = userService.getUserById(userId);
            if (user == null) {
                System.out.println("User not found.");
                return;
            }

            System.out.println("Select consumption type: Transport, Logement, or Alimentation");
            String typeInput = scanner.nextLine().trim().toUpperCase();
            System.out.println("User selected type: " + typeInput);


            TypeConsommation typeConsumption;
            try {
                typeConsumption = TypeConsommation.fromValue(typeInput);
                System.out.println("Mapped typeConsumption: " + typeConsumption);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid consumption type.");
                return;
            }

            Consommation consommation;

            switch (typeConsumption) {
                case ALIMENTATION:
                    consommation = new Alimentation();
                    System.out.print("Enter weight (kg): ");
                    ((Alimentation) consommation).setPoids(scanner.nextDouble());
                    scanner.nextLine();

                    System.out.println("Select food type: Viande or Legume");
                    String foodTypeInput = scanner.nextLine().trim().toLowerCase();
                    switch (foodTypeInput) {
                        case "viande":
                            ((Alimentation) consommation).setTypeAliment(TypeAliment.VIANDE);
                            break;
                        case "legume":
                            ((Alimentation) consommation).setTypeAliment(TypeAliment.LEGUME);
                            break;
                        default:
                            System.out.println("Invalid food type selected.");
                            return;
                    }

                    System.out.print("Enter impact (Euros): ");
                    consommation.setImpact(scanner.nextDouble());
                    scanner.nextLine();
                    break;

                case TRANSPORT:
                    consommation = new Transport();
                    System.out.print("Enter distance (km): ");
                    ((Transport) consommation).setDistanceParcourue(scanner.nextDouble());
                    scanner.nextLine();

                    System.out.println("Select vehicle type: Voiture or Train");
                    String vehicleTypeInput = scanner.nextLine().trim().toLowerCase();
                    switch (vehicleTypeInput) {
                        case "voiture":
                            ((Transport) consommation).setTypeDeVehicule(TypeVehicule.VOITURE);
                            break;
                        case "train":
                            ((Transport) consommation).setTypeDeVehicule(TypeVehicule.TRAIN);
                            break;
                        default:
                            System.out.println("Invalid vehicle type selected.");
                            return;
                    }

                    System.out.print("Enter impact (Euros): ");
                    consommation.setImpact(scanner.nextDouble());
                    scanner.nextLine();
                    break;

                case LOGEMENT:
                    consommation = new Logement();
                    System.out.print("Enter energy consumption (kWh): ");
                    ((Logement) consommation).setConsommationEnergie(scanner.nextDouble());
                    scanner.nextLine();

                    System.out.println("Select energy type: Electricite or Gaz");
                    String energyTypeInput = scanner.nextLine().trim().toLowerCase();
                    switch (energyTypeInput) {
                        case "electricite":
                            ((Logement) consommation).setTypeEnergie(TypeEnergie.ELECTRICITE);
                            break;
                        case "gaz":
                            ((Logement) consommation).setTypeEnergie(TypeEnergie.GAZ);
                            break;
                        default:
                            System.out.println("Invalid energy type selected.");
                            return;
                    }

                    System.out.print("Enter impact (Euros): ");
                    consommation.setImpact(scanner.nextDouble());
                    scanner.nextLine();
                    break;

                default:
                    System.out.println("Invalid consumption type.");
                    return;
            }


            consommation.setTypeConsumption(typeConsumption);


            System.out.print("Enter consumption start date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime startDate = parseDate(scanner.nextLine());
            if (startDate != null) {
                consommation.setStartDate(startDate);
            } else {
                System.out.println("Invalid start date format.");
                return;
            }


            System.out.print("Enter consumption end date (yyyy-MM-dd HH:mm:ss): ");
            LocalDateTime endDate = parseDate(scanner.nextLine());
            if (endDate != null) {
                consommation.setEndDate(endDate);
            } else {
                System.out.println("Invalid end date format.");
                return;
            }

            consommation.setUser(user);


            consommationService.addConsommation(consommation);
            System.out.println("Consommation added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding consommation: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static LocalDateTime parseDate(String dateInput) {
        try {
            return LocalDateTime.parse(dateInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }


    private static void deleteConsommation() {
        try {
            System.out.println("\n==== Delete Consommation ====");
            System.out.print("Enter consommation ID to delete: ");
            long consommationId = scanner.nextLong();
            scanner.nextLine();

            consommationService.deleteConsommation(consommationId);
            System.out.println("Consommation deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting consommation: " + e.getMessage());
        }
    }

    private static void listAllConsommations() {
        try {
            System.out.println("\n==== List All Consommations ====");
            List<Consommation> consommations = consommationService.getAllConsommations();
            consommations.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("Error listing consommations: " + e.getMessage());
        }
    }


}
