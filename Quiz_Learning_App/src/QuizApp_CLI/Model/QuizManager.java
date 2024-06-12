package QuizApp_CLI.Model;

import QuizApp_CLI.db.DataManager;
import QuizApp_CLI.View.QuestionDisplayMode;
import QuizApp_CLI.View.MultiChoiceMode;
import QuizApp_CLI.View.BlindMode;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public QuizStructure getQuiz(String quizName) {
        return quizzes.get(quizName);
    }

    public User getUser(String username) {
        return userManager.getOrCreateUser(username);
    }

    public void loadQuiz(String quizName, JFrame frame, String username) {
        QuizStructure quizStructure = quizzes.get(quizName);
        if (quizStructure == null) {
            JOptionPane.showMessageDialog(frame, "Quiz not found.");
            return;
        }

        User user = userManager.getOrCreateUser(username);

        List<Question> questions = quizStructure.getQuestions();

        for (Question question : questions) {
            // Default to BlindMode if user is not prompted for mode
            QuestionDisplayMode displayMode = new BlindMode();
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

        JOptionPane.showMessageDialog(frame, username + ", you scored " + user.getScore() + "/" + questions.size());

    }

    public String[] getQuizNames() {
        return quizzes.keySet().toArray(new String[0]);
    }

    public void deleteQuiz(String quizName) {
        quizzes.remove(quizName);
        dataManager.deleteQuizFromDatabase(quizName);
        saveData();
    }

    public void loadData() {
        // Load data from database
        quizzes.clear();
        dataManager.loadDataFromFiles(quizzes);
    }

    public void saveData() {
        // Save data to database
        dataManager.saveDataToFiles(quizzes);
    }
}
