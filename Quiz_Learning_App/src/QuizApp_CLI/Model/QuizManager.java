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
import QuizApp_CLI.View.QuestionDisplayMode;
import QuizApp_CLI.View.MultiChoiceMode;
import QuizApp_CLI.View.BlindMode;
import javax.swing.*;
import java.util.*;

public class QuizManager {
    private Map<String, QuizStructure> quizzes = new HashMap<>();
    private UserManager userManager = new UserManager();
    private DataManager dataManager = new DataManager();

    public QuizManager() {
        // Load data when the application starts
        loadData();
    }

    public void createQuiz(String quizName, List<Question> questions) {
        QuizStructure quizStructure = new QuizStructure(quizName, questions);
        quizzes.put(quizName, quizStructure);
        saveData(); // Save the quizzes after adding a new quiz
    }

    public void loadQuiz(String quizName, JFrame frame, String username) {
        QuizStructure quizStructure = quizzes.get(quizName);
        if (quizStructure == null) {
            JOptionPane.showMessageDialog(frame, "Quiz not found.");
            return;
        }

        User user = userManager.getOrCreateUser(frame, username);

        List<Question> questions = quizStructure.getQuestions();

        String modeChoice = JOptionPane.showInputDialog(frame, "Select display mode:\n1. Blind Mode\n2. Multi-choice Mode");

        QuestionDisplayMode displayMode;
        switch (modeChoice) {
            case "1":
                displayMode = new BlindMode();
                break;
            case "2":
                displayMode = new MultiChoiceMode(quizStructure.getAllChoices(), frame);
                break;
            default:
                displayMode = new BlindMode();
        }

        for (Question question : questions) {
            displayMode.displayQuestion(question);
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

    public String[] getQuizNames() {
        return quizzes.keySet().toArray(new String[0]);
    }

    public void loadData() {
        // Load data from database
        dataManager.loadDataFromFiles(quizzes);
    }

    public void saveData() {
        // Save data to database
        dataManager.saveDataToFiles(quizzes);
    }
}
