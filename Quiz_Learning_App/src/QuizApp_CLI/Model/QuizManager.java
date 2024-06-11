package QuizApp_CLI.Model;

import QuizApp_CLI.db.DataManager;
import QuizApp_CLI.View.QuestionDisplayMode;
import QuizApp_CLI.View.MultiChoiceMode;
import QuizApp_CLI.View.BlindMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import QuizApp_CLI.db.DataManager;

import javax.swing.*;
import java.util.*;

public class QuizManager {
    private Map<String, QuizStructure> quizzes = new HashMap<>();
    private UserManager userManager = new UserManager();
    private DataManager dataManager = new DataManager();

    public QuizManager() {
        loadData();
    }

    public void createQuiz(String quizName) {
        QuizStructure quizStructure = new QuizStructure(quizName);
        while (true) {
            String questionText = JOptionPane.showInputDialog("Enter question (or leave blank to finish):");
            if (questionText == null || questionText.isEmpty()) {
                break;
            }

            String answer = JOptionPane.showInputDialog("Enter answer:");
            if (answer == null || answer.isEmpty()) {
                break;
            }

            quizStructure.addQuestion(new Question(questionText, answer));
        }

        quizzes.put(quizName, quizStructure);
        saveData();
    }

    public void loadQuiz(String quizName, JFrame frame) {
        QuizStructure quizStructure = quizzes.get(quizName);
        if (quizStructure == null) {
            JOptionPane.showMessageDialog(frame, "Quiz not found.");
            return;
        }

        User user = userManager.getOrCreateUser(frame);
        List<Question> questions = quizStructure.getQuestions();

        if (questions.size() < 4) {
            JOptionPane.showMessageDialog(frame, "This quiz does not support the multi-choice mode due to insufficient questions.\nDefaulting to Blind Mode.");
            loadBlindMode(questions, user, frame);
            return;
        }

        String modeChoice = JOptionPane.showInputDialog(frame, "Select display mode:\n1. Blind Mode\n2. Multi-choice Mode");

        switch (modeChoice) {
            case "1":
                loadBlindMode(questions, user, frame);
                break;
            case "2":
                loadMultiChoiceMode(questions, user, frame);
                break;
            default:
                JOptionPane.showMessageDialog(frame, "Invalid choice. Defaulting to Blind Mode.");
                loadBlindMode(questions, user, frame);
        }
    }

    private void loadBlindMode(List<Question> questions, User user, JFrame frame) {
        for (Question question : questions) {
            String userAnswer = JOptionPane.showInputDialog(frame, question.getQuestion());
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
        }

        JOptionPane.showMessageDialog(frame, "Your score: " + user.getScore());
    }

    private void loadMultiChoiceMode(List<Question> questions, User user, JFrame frame) {
        List<String> allChoices = new ArrayList<>();
        for (Question q : questions) {
            allChoices.add(q.getCorrectAnswer());
            allChoices.addAll(q.getChoices());
        }
        Collections.shuffle(allChoices);

        for (Question question : questions) {
            List<String> choices = new ArrayList<>(question.getChoices());
            choices.add(question.getCorrectAnswer());
            Collections.shuffle(choices);

            StringBuilder message = new StringBuilder(question.getQuestion() + "\n");
            for (int i = 0; i < choices.size(); i++) {
                message.append((char) ('a' + i)).append(". ").append(choices.get(i)).append("\n");
            }

            String userAnswer = JOptionPane.showInputDialog(frame, message.toString());
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
        }

        JOptionPane.showMessageDialog(frame, "Your score: " + user.getScore());
    }

    public String[] getQuizNames() {
        return quizzes.keySet().toArray(new String[0]);
    }

    public void loadData() {
        dataManager.loadDataFromFiles(quizzes);
    }

    public void saveData() {
        dataManager.saveDataToFiles(quizzes);
    }
}
