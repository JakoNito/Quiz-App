/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.*;
/**
 *
 * @author jakoi
 */

public class UserManager {
    private Map<String, User> users = new HashMap<>();

    public User getOrCreateUser(JFrame frame) {
        String username = JOptionPane.showInputDialog(frame, "Enter your username:");
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        return users.computeIfAbsent(username, User::new);
    }
}
