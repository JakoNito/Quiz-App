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
import QuizApp_CLI.Model.Question;
import QuizApp_CLI.Model.QuizStructure;

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
                    showLoadQuizScreen();
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
    }

    private void addQuestion() {
        String questionText = questionField.getText();
        String answerText = answerField.getText();
        if (questionText.trim().isEmpty() || answerText.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Question and Answer cannot be empty.");
            return;
        }

        questions.add(new Question(questionText, answerText));
        questionListArea.append("Q: " + questionText + " A: " + answerText + "\n");

        questionField.setText("");
        answerField.setText("");
    }

    private void saveQuiz() {
        String quizName = quizNameField.getText();
        if (quizName.trim().isEmpty() || questions.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Quiz name cannot be empty and must have at least one question.");
            return;
        }

        quizManager.createQuiz(quizName, questions);
        questions.clear();
        questionListArea.setText("");
        quizNameField.setText("");
        JOptionPane.showMessageDialog(frame, "Quiz saved successfully!");
    }

    private void showLoadQuizScreen() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JList<String> quizList = new JList<>(quizManager.getQuizNames());
        quizList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        quizList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedQuiz = quizList.getSelectedValue();
                if (selectedQuiz != null) {
                    getQuizDetails(selectedQuiz);
                }
            }
        });

        frame.add(new JScrollPane(quizList), BorderLayout.WEST);

        JTextArea quizDetailsArea = new JTextArea();
        quizDetailsArea.setEditable(false);
        frame.add(new JScrollPane(quizDetailsArea), BorderLayout.CENTER);

        JButton loadButton = new JButton("Load Quiz");
        loadButton.addActionListener(e -> {
            String selectedQuiz = quizList.getSelectedValue();
            if (selectedQuiz != null) {
                quizManager.loadQuiz(selectedQuiz, frame, username);
            }
        });

        frame.add(loadButton, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private void getQuizDetails(String quizName) {
        QuizStructure quizStructure = quizManager.getQuizByName(quizName);
        if (quizStructure != null) {
            StringBuilder details = new StringBuilder();
            details.append("Quiz Name: ").append(quizStructure.getQuizName()).append("\n");
            details.append("Questions:\n");
            for (Question question : quizStructure.getQuestions()) {
                details.append("Q: ").append(question.getQuestion()).append("\n");
            }

            JTextArea quizDetailsArea = new JTextArea(details.toString());
            quizDetailsArea.setEditable(false);
            frame.add(new JScrollPane(quizDetailsArea), BorderLayout.CENTER);
        }
    }

}
