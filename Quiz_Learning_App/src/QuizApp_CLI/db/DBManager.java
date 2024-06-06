package QuizApp_CLI.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

    private static final String USER_NAME = "P11"; //your DB username
    private static final String PASSWORD = "pdc"; //your DB password
    private static final String URL = "jdbc:derby://localhost:1527/QuizDB; create=true";  //url of the DB host

    private static Connection connection;

    public DBManager() {
        establishConnection();
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                establishConnection();
            }
        } catch (SQLException ex) {
            System.out.println("Failed to check connection status: " + ex.getMessage());
        }
        return connection;
    }

    //Establish connection
    public static void establishConnection() {
        try {
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Connection established successfully.");
        } catch (SQLException ex) {
            System.out.println("Failed to establish connection: " + ex.getMessage());
        }
    }

    public static void setupDatabase() {
        try (Statement stmt = getConnection().createStatement()) {
            // Create Quizzes table
            stmt.executeUpdate("CREATE TABLE QUIZZES ("
                    + "quiz_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                    + "quiz_name VARCHAR(255) UNIQUE NOT NULL)");

            // Create Questions table
            stmt.executeUpdate("CREATE TABLE QUESTIONS ("
                    + "question_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                    + "quiz_id INT, "
                    + "question_text VARCHAR(255), "
                    + "correct_answer VARCHAR(255), "
                    + "FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id))");

        } catch (SQLException e) {
            if (!e.getSQLState().equals("X0Y32")) { // Table already exists
                e.printStackTrace();
            }
        }
    }
}
