/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Model;

/**
 *
 * @author jakoi
 */
public class User {
    private String username;
    private int score;

    public User(String username) {
        this.username = username;
        this.score = 0;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore() {
        score++;
    }
}
