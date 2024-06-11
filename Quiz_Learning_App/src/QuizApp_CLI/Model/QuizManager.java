package QuizApp_CLI.Model;

import QuizApp_CLI.db.DataManager;
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
                JOptionPane.showMessageDialog(frame, "Invalid choice. Defaulting to Blind Mode.");
                displayMode = new BlindMode();
        }

        Quiz quiz = new Quiz(quizStructure.getQuizName(), questions, user, displayMode);

        for (Question question : questions) {
            displayMode.displayQuestion(frame, question);
            String userAnswer = displayMode.getAnswer(frame);
            quiz.answerQuestion(question, userAnswer);
        }

        int score = quiz.calculateScore();
        JOptionPane.showMessageDialog(frame, "Quiz completed! Your score: " + score + "/" + questions.size());
    }

    public String[] getQuizNames() {
        return quizzes.keySet().toArray(new String[0]);
    }

    public QuizStructure getQuizByName(String quizName) {
        return quizzes.get(quizName);
    }

    private void saveData() {
        dataManager.saveQuizzes(quizzes);
    }

    private void loadData() {
        quizzes = dataManager.loadQuizzes();
    }
}
