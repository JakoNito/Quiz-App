/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Controller;

/**
 *
 * @author johann
 */

import QuizApp_CLI.Model.QuizManager;
import QuizApp_CLI.db.DBManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuizAppGUI {
    private JFrame frame;
    private QuizManager quizManager;

    public QuizAppGUI() {
        frame = new JFrame("Quiz App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(3, 1));

        quizManager = new QuizManager();
    }

    public void showMainMenu() {
        frame.getContentPane().removeAll();

        JButton createQuizButton = new JButton("Create a Quiz");
        JButton loadQuizButton = new JButton("Load a Quiz");
        JButton quitButton = new JButton("Quit");

        createQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateQuiz();
            }
        });

        loadQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoadQuiz();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(createQuizButton);
        frame.add(loadQuizButton);
        frame.add(quitButton);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void showCreateQuiz() {
        String quizName = JOptionPane.showInputDialog(frame, "Enter the quiz name:");
        if (quizName == null || quizName.isEmpty()) {
            return;
        }

        quizManager.createQuiz(quizName);

        JOptionPane.showMessageDialog(frame, "Quiz created successfully!");
        showMainMenu();
    }

    private void showLoadQuiz() {
        String[] quizzes = quizManager.getQuizNames();
        if (quizzes.length == 0) {
            JOptionPane.showMessageDialog(frame, "No quizzes available.");
            return;
        }

        String quizName = (String) JOptionPane.showInputDialog(frame, "Select a quiz:", "Load Quiz",
                JOptionPane.QUESTION_MESSAGE, null, quizzes, quizzes[0]);

        if (quizName != null && !quizName.isEmpty()) {
            quizManager.loadQuiz(quizName, frame);
        }
    }
}
