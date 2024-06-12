/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

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

    public User getOrCreateUser(String username) {
        User user = users.get(username);
        if (user == null) {
            user = new User(username);
            users.put(username, user);
        }
        return user;
    }
}
