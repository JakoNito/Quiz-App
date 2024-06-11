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
import QuizApp_CLI.Model.Question;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QuizAppGUI {
    private JFrame frame;
    private QuizManager quizManager;
    private String username;
    private JTextField quizNameField;
    private JTextField questionField;
    private JTextField answerField;
    private JTextArea questionListArea;
    private List<Question> questions;

    public QuizAppGUI() {
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        quizManager = new QuizManager();
        questions = new ArrayList<>();
    }

    public void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 1));

        JButton createQuizButton = new JButton("Create a Quiz");
        JButton loadQuizButton = new JButton("Load a Quiz");
        JButton quitButton = new JButton("Quit");

        createQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ensureUsername()) {
                    showCreateQuiz();
                }
            }
        });

        loadQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (ensureUsername()) {
                    showLoadQuiz();
                }
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

    private boolean ensureUsername() {
        if (username == null || username.trim().isEmpty()) {
            username = JOptionPane.showInputDialog(frame, "Enter your username:");
            if (username == null || username.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Username cannot be empty.");
                return false;
            }
        }
        return true;
    }

    private void showCreateQuiz() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel quizNameLabel = new JLabel("Quiz Name:");
        quizNameField = new JTextField();
        JLabel questionLabel = new JLabel("Question:");
        questionField = new JTextField();
        JLabel answerLabel = new JLabel("Correct Answer:");
        answerField = new JTextField();

        inputPanel.add(quizNameLabel);
        inputPanel.add(quizNameField);
        inputPanel.add(questionLabel);
        inputPanel.add(questionField);
        inputPanel.add(answerLabel);
        inputPanel.add(answerField);

        frame.add(inputPanel, BorderLayout.NORTH);

        questionListArea = new JTextArea();
        questionListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(questionListArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addQuestionButton = new JButton("Add More Questions");
        JButton saveQuizButton = new JButton("Save Quiz");
        JButton exitButton = new JButton("Exit");

        addQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        saveQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveQuiz();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

        buttonPanel.add(addQuestionButton);
        buttonPanel.add(saveQuizButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

private void addQuestion() {
    String questionText = questionField.getText().trim();
    String answerText = answerField.getText().trim();
    if (!questionText.isEmpty() && !answerText.isEmpty()) {
        // Append a question mark if it's not already included
        if (!questionText.endsWith("?")) {
            questionText += "?";
        }
        questions.add(new Question(questionText, answerText));
        questionListArea.append("Q: " + questionText + " A: " + answerText + "\n");
        questionField.setText("");
        answerField.setText("");
    } else {
        JOptionPane.showMessageDialog(frame, "Both question and answer fields must be filled.");
    }
}


    private void saveQuiz() {
        String quizName = quizNameField.getText().trim();
        if (quizName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Quiz name cannot be empty.");
            return;
        }
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No questions added to the quiz.");
            return;
        }
        quizManager.createQuiz(quizName, questions);
        JOptionPane.showMessageDialog(frame, "Quiz saved successfully!");
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
            quizManager.loadQuiz(quizName, frame, username);
        }
    }
}
