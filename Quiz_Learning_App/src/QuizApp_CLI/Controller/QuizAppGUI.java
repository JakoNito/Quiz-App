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
import QuizApp_CLI.Model.QuizStructure;
import QuizApp_CLI.Model.Question;
import QuizApp_CLI.Model.User;
import QuizApp_CLI.View.BlindMode;
import QuizApp_CLI.View.MultiChoiceMode;
import QuizApp_CLI.View.QuestionDisplayMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
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
    private JTextField searchField;
    private JComboBox<String> quizComboBox;

    public QuizAppGUI() {
        frame = new JFrame("Quiz Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        quizManager = new QuizManager();
        questions = new ArrayList<>();
    }

    public void showMainMenu() {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to the Quiz App!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
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

        buttonPanel.add(createQuizButton);
        buttonPanel.add(loadQuizButton);
        buttonPanel.add(quitButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

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
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel searchLabel = new JLabel("Search for a quiz...", SwingConstants.LEFT);
        searchLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(searchLabel, BorderLayout.NORTH);

        searchField = new JTextField();
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateQuizComboBox();
            }
        });

        quizComboBox = new JComboBox<>(quizManager.getQuizNames());
        quizComboBox.setEditable(false);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.NORTH);
        searchPanel.add(quizComboBox, BorderLayout.SOUTH);

        frame.add(searchPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        JButton loadQuizButton = new JButton("Load Quiz");
        JButton deleteQuizButton = new JButton("Delete Quiz");
        JButton exitButton = new JButton("Exit");

        loadQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedQuiz = (String) quizComboBox.getSelectedItem();
                if (selectedQuiz != null && !selectedQuiz.trim().isEmpty()) {
                    showQuizModeOptions(selectedQuiz);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a quiz to load.");
                }
            }
        });

        deleteQuizButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedQuiz = (String) quizComboBox.getSelectedItem();
                if (selectedQuiz != null && !selectedQuiz.trim().isEmpty()) {
                    quizManager.deleteQuiz(selectedQuiz);
                    quizComboBox.removeItem(selectedQuiz);
                    JOptionPane.showMessageDialog(frame, "Quiz deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a quiz to delete.");
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

        buttonPanel.add(loadQuizButton);
        buttonPanel.add(deleteQuizButton);
        buttonPanel.add(exitButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void updateQuizComboBox() {
        String searchText = searchField.getText().trim().toLowerCase();
        quizComboBox.removeAllItems();
        for (String quizName : quizManager.getQuizNames()) {
            if (quizName.toLowerCase().contains(searchText)) {
                quizComboBox.addItem(quizName);
            }
        }
    }

    private void showQuizModeOptions(String selectedQuiz) {
        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JButton enterAnswerModeButton = new JButton("Enter Answer Mode");
        JButton multipleChoiceModeButton = new JButton("Multiple Choice Mode");

        enterAnswerModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startQuiz(selectedQuiz, 1);  // Start quiz in Blind Mode
            }
        });

        multipleChoiceModeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startQuiz(selectedQuiz, 2);  // Start quiz in Multi-choice Mode
            }
        });

        buttonPanel.add(enterAnswerModeButton);
        buttonPanel.add(multipleChoiceModeButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        JPanel exitPanel = new JPanel();
        JButton exitButton = new JButton("Exit");

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMainMenu();
            }
        });

        exitPanel.add(exitButton);
        frame.add(exitPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    private void startQuiz(String quizName, int mode) {
        QuizStructure quizStructure = quizManager.getQuiz(quizName);
        List<Question> questions = quizStructure.getQuestions();

        if (mode == 1) {
            startBlindMode(questions);
        } else {
            startMultiChoiceMode(quizStructure, questions);
        }
    }

    private void startBlindMode(List<Question> questions) {
        User user = quizManager.getUser(username);
        int currentQuestionIndex = 0;

        displayBlindQuestion(questions, currentQuestionIndex, user);
    }

    private void displayBlindQuestion(List<Question> questions, int index, User user) {
        if (index >= questions.size()) {
            JOptionPane.showMessageDialog(frame, username + ", you scored " + user.getScore() + "/" + questions.size());
            showMainMenu();
            return;
        }

        Question question = questions.get(index);

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel questionLabel = new JLabel(question.getQuestion());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        frame.add(questionLabel, BorderLayout.NORTH);

        JTextField answerField = new JTextField();
        answerField.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(answerField, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String userAnswer = answerField.getText().trim();
                if (userAnswer == null || userAnswer.equalsIgnoreCase("x")) {
                    JOptionPane.showMessageDialog(frame, "Exiting quiz...");
                    return;
                }
                if (question.isCorrect(userAnswer)) {
                    JOptionPane.showMessageDialog(frame, "Correct!");
                    user.increaseScore();
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect! Correct answer: " + question.getCorrectAnswer());
                }
                displayBlindQuestion(questions, index + 1, user);
            }
        });

        frame.add(submitButton, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private void startMultiChoiceMode(QuizStructure quizStructure, List<Question> questions) {
        User user = quizManager.getUser(username);
        int currentQuestionIndex = 0;

        displayMultiChoiceQuestion(quizStructure, questions, currentQuestionIndex, user);
    }

    private void displayMultiChoiceQuestion(QuizStructure quizStructure, List<Question> questions, int index, User user) {
        if (index >= questions.size()) {
            JOptionPane.showMessageDialog(frame, username + ", you scored " + user.getScore() + "/" + questions.size());
            showMainMenu();
            return;
        }

        Question question = questions.get(index);
        List<String> choices = new ArrayList<>(quizStructure.getAllChoices());
        Collections.shuffle(choices);

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        JLabel questionLabel = new JLabel(question.getQuestion());
        questionLabel.setFont(new Font("Arial", Font.BOLD, 24));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        frame.add(questionLabel, BorderLayout.NORTH);

        JPanel choicesPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        for (String choice : choices) {
            JButton choiceButton = new JButton(choice);
            choiceButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (question.isCorrect(choice)) {
                        user.increaseScore();
                    }
                    displayMultiChoiceQuestion(quizStructure, questions, index + 1, user);
                }
            });
            choicesPanel.add(choiceButton);
        }

        frame.add(choicesPanel, BorderLayout.CENTER);

        JButton nextQuestionButton = new JButton("Next Question");
        nextQuestionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayMultiChoiceQuestion(quizStructure, questions, index + 1, user);
            }
        });

        frame.add(nextQuestionButton, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }
}

