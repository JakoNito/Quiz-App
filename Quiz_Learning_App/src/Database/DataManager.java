package Database;

import Model.Question;
import Model.QuizStructure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

public class DataManager {

    private static final String INSERT_QUIZ_SQL = "INSERT INTO Quizzes (quiz_name) VALUES (?)";
    private static final String INSERT_QUESTION_SQL = "INSERT INTO Questions (quiz_id, question_text, correct_answer) VALUES (?, ?, ?)";
    private static final String SELECT_QUIZZES_SQL = "SELECT quiz_id, quiz_name FROM Quizzes";
    private static final String SELECT_QUESTIONS_SQL = "SELECT question_text, correct_answer FROM Questions WHERE quiz_id = ?";
    private static final String SELECT_QUIZ_BY_NAME_SQL = "SELECT quiz_id FROM Quizzes WHERE quiz_name = ?";
    private static final String DELETE_QUIZ_SQL = "DELETE FROM Quizzes WHERE quiz_name = ?";
    private static final String DELETE_QUESTIONS_SQL = "DELETE FROM Questions WHERE quiz_id = (SELECT quiz_id FROM Quizzes WHERE quiz_name = ?)";

    public void loadDataFromFiles(Map<String, QuizStructure> quizzes) {
        try ( Connection conn = DBManager.getConnection();  PreparedStatement quizStmt = conn.prepareStatement(SELECT_QUIZZES_SQL);  PreparedStatement questionStmt = conn.prepareStatement(SELECT_QUESTIONS_SQL)) {

            ResultSet quizRs = quizStmt.executeQuery();
            while (quizRs.next()) {
                int quizId = quizRs.getInt("quiz_id");
                String quizName = quizRs.getString("quiz_name");

                QuizStructure quizStructure = new QuizStructure(quizName, Collections.emptyList());
                // Add an empty list for questions

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
        try ( Connection conn = DBManager.getConnection();  PreparedStatement quizStmt = conn.prepareStatement(INSERT_QUIZ_SQL, PreparedStatement.RETURN_GENERATED_KEYS);  PreparedStatement questionStmt = conn.prepareStatement(INSERT_QUESTION_SQL);  PreparedStatement selectQuizByNameStmt = conn.prepareStatement(SELECT_QUIZ_BY_NAME_SQL)) {

            if (conn == null || conn.isClosed()) {
                System.out.println("No current connection");
                return;
            }

            for (QuizStructure quizStructure : quizzes.values()) {
                String quizName = quizStructure.getQuizName();

                // Check if quiz already exists
                selectQuizByNameStmt.setString(1, quizName);
                ResultSet existingQuizRs = selectQuizByNameStmt.executeQuery();

                int quizId;
                if (existingQuizRs.next()) {
                    quizId = existingQuizRs.getInt("quiz_id");
                } else {
                    // Insert new quiz
                    quizStmt.setString(1, quizName);
                    quizStmt.executeUpdate();

                    ResultSet generatedKeys = quizStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        quizId = generatedKeys.getInt(1);
                    } else {
                        System.out.println("Failed to retrieve generated quiz_id for quiz: " + quizName);
                        continue;
                    }
                }

                // Insert questions
                for (Question question : quizStructure.getQuestions()) {
                    // Check if question already exists for this quiz
                    PreparedStatement selectQuestionStmt = conn.prepareStatement(
                            "SELECT 1 FROM Questions WHERE quiz_id = ? AND question_text = ?"
                    );
                    selectQuestionStmt.setInt(1, quizId);
                    selectQuestionStmt.setString(2, question.getQuestion());
                    ResultSet existingQuestionRs = selectQuestionStmt.executeQuery();

                    if (!existingQuestionRs.next()) {
                        questionStmt.setInt(1, quizId);
                        questionStmt.setString(2, question.getQuestion());
                        questionStmt.setString(3, question.getCorrectAnswer());
                        questionStmt.executeUpdate();
                    }
                    selectQuestionStmt.close();
                }
            }

        } catch (SQLException e) {
            System.out.println("Error saving quizzes: " + e.getMessage());
        }
    }

    public void deleteQuizFromDatabase(String quizName) {
        try ( Connection conn = DBManager.getConnection();  PreparedStatement deleteQuestionsStmt = conn.prepareStatement(DELETE_QUESTIONS_SQL);  PreparedStatement deleteQuizStmt = conn.prepareStatement(DELETE_QUIZ_SQL)) {

            deleteQuestionsStmt.setString(1, quizName);
            deleteQuestionsStmt.executeUpdate();

            deleteQuizStmt.setString(1, quizName);
            deleteQuizStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error deleting quiz: " + e.getMessage());
        }
    }
}
