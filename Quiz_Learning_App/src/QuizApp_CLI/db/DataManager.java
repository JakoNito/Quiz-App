/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.db;

/**
 *
 * @author jakoi
 */
import QuizApp_CLI.Model.Question;
import QuizApp_CLI.Model.QuizStructure;
import QuizApp_CLI.db.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DataManager {

    private static final String INSERT_QUIZ_SQL = "INSERT INTO Quizzes (quiz_name) VALUES (?)";
    private static final String INSERT_QUESTION_SQL = "INSERT INTO Questions (quiz_id, question_text, correct_answer) VALUES (?, ?, ?)";
    private static final String SELECT_QUIZZES_SQL = "SELECT quiz_id, quiz_name FROM Quizzes";
    private static final String SELECT_QUESTIONS_SQL = "SELECT question_text, correct_answer FROM Questions WHERE quiz_id = ?";

    public void loadDataFromFiles(Map<String, QuizStructure> quizzes) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement quizStmt = conn.prepareStatement(SELECT_QUIZZES_SQL);
             PreparedStatement questionStmt = conn.prepareStatement(SELECT_QUESTIONS_SQL)) {

            ResultSet quizRs = quizStmt.executeQuery();
            while (quizRs.next()) {
                int quizId = quizRs.getInt("quiz_id");
                String quizName = quizRs.getString("quiz_name");

                QuizStructure quizStructure = new QuizStructure(quizName);

                questionStmt.setInt(1, quizId);
                ResultSet questionRs = questionStmt.executeQuery();
                while (questionRs.next()) {
                    String questionText = questionRs.getString("question_text");
                    String correctAnswer = questionRs.getString("correct_answer");
                    quizStructure.addQuestion(new Question(questionText, correctAnswer));
                }

                quizzes.put(quizName, quizStructure);
            }

        } catch (SQLException e) {
            System.out.println("Error loading quizzes: " + e.getMessage());
        }
    }

    public void saveDataToFiles(Map<String, QuizStructure> quizzes) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement quizStmt = conn.prepareStatement(INSERT_QUIZ_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement questionStmt = conn.prepareStatement(INSERT_QUESTION_SQL)) {
             
            if(conn == null || conn.isClosed()) {
                System.out.println("no current connection");
                return;
            }
            
            for (QuizStructure quizStructure : quizzes.values()) {
                quizStmt.setString(1, quizStructure.getQuizName());
                quizStmt.executeUpdate();

                ResultSet generatedKeys = quizStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int quizId = generatedKeys.getInt(1);

                    for (Question question : quizStructure.getQuestions()) {
                        questionStmt.setInt(1, quizId);
                        questionStmt.setString(2, question.getQuestion());
                        questionStmt.setString(3, question.getCorrectAnswer());
                        questionStmt.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving quizzes: " + e.getMessage());
        }
    }
}
