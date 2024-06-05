/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 *
 * @author jakoi
 */

public class UserManager {
    private Scanner scanner = new Scanner(System.in);
    private Map<String, User> users = new HashMap<>();

    public User getOrCreateUser() {
        System.out.println("Enter your username:");
        String username = getStringInput();
        System.out.println();

        User user = users.get(username);
        if (user == null) {
            user = new User(username);
            users.put(username, user);
        }
        return user;
    }

    private String getStringInput() {
        while (!scanner.hasNextLine()) {
            System.out.println("Invalid input. Please enter a number:");
            scanner.next(); // Consume invalid input
        }
        return scanner.nextLine();
    }
}