package com.example.greenplusdb;

import com.example.greenplusdb.model.User;
import com.example.greenplusdb.repository.UserRepository;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            UserRepository userRepository = new UserRepository();


            // Fetch all users
            List<User> allUsers = userRepository.getAllUsers();
            System.out.println("All users: ");
            for (User user : allUsers) {
                System.out.println(user);
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
