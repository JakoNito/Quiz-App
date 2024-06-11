/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Controller;

import QuizApp_CLI.Model.QuizManager;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizApp {
    private QuizManager quizManager = new QuizManager();
    private String username;


    public void run() {
        JFrame frame = new JFrame("Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);

        promptForUsername(frame);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel titleLabel = new JLabel("Welcome to the Quiz App!");
        titleLabel.setBounds(10, 20, 200, 25);
        panel.add(titleLabel);

        JButton createQuizButton = new JButton("Create a Quiz");
        createQuizButton.setBounds(10, 50, 160, 25);
        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username == null || username.trim().isEmpty()) {
                    promptForUsername(new JFrame());
                } else {
                    String quizName = JOptionPane.showInputDialog("Enter the quiz name:");
                    if (quizName != null && !quizName.trim().isEmpty()) {
                        quizManager.createQuiz(quizName);
                    }
                }
            }
        });
        panel.add(createQuizButton);

        JButton loadQuizButton = new JButton("Load a Quiz");
        loadQuizButton.setBounds(10, 80, 160, 25);
        loadQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username == null || username.trim().isEmpty()) {
                    promptForUsername(new JFrame());
                } else {
                    String quizName = (String) JOptionPane.showInputDialog(null, "Select Quiz",
                            "Quiz Selector", JOptionPane.QUESTION_MESSAGE, null,
                            quizManager.getQuizNames(), quizManager.getQuizNames()[0]);
                    if (quizName != null) {
                        quizManager.loadQuiz(quizName, new JFrame(), username);
                    }
                }
            }
        });
        panel.add(loadQuizButton);

        JButton quitButton = new JButton("Quit");
        quitButton.setBounds(10, 110, 160, 25);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(quitButton);
    }

    private void promptForUsername(JFrame frame) {
        while (true) {
            username = JOptionPane.showInputDialog(frame, "Enter your username:");
            if (username != null && !username.trim().isEmpty()) {
                break;
            }
            JOptionPane.showMessageDialog(frame, "Username cannot be empty.");
        }
    }
}

