package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class DBManager {

    private static final String URL = "jdbc:derby:QuizDB_Ebd;create=true"; // Embedded mode URL
    private static Connection connection;

    static {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            System.out.println("Derby driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load Derby driver: " + e.getMessage());
        }
    }

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

    // Establish connection
    public static void establishConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            System.out.println("Connection established successfully.");
        } catch (SQLException ex) {
            System.out.println("Failed to establish connection: " + ex.getMessage());
        }
    }

    public static void setupDatabase() {
        try ( Connection conn = getConnection();  Statement stmt = conn.createStatement()) {

            if (!doesTableExist(conn, "QUIZZES")) {
                // Create Quizzes table
                stmt.executeUpdate("CREATE TABLE QUIZZES ("
                        + "quiz_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "quiz_name VARCHAR(255) UNIQUE NOT NULL)");
                System.out.println("Quizzes table created successfully.");
            }

            if (!doesTableExist(conn, "QUESTIONS")) {
                // Create Questions table
                stmt.executeUpdate("CREATE TABLE QUESTIONS ("
                        + "question_id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                        + "quiz_id INT, "
                        + "question_text VARCHAR(255), "
                        + "correct_answer VARCHAR(255), "
                        + "FOREIGN KEY (quiz_id) REFERENCES Quizzes (quiz_id), "
                        + "CONSTRAINT unique_question UNIQUE (quiz_id, question_text))");
                System.out.println("Questions table created successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Error setting up the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if a table exists in database
    private static boolean doesTableExist(Connection conn, String tableName) {
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            try ( ResultSet rs = dbMetaData.getTables(null, null, tableName.toUpperCase(), null)) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Failed to check if table exists: " + e.getMessage());
            return false;
        }
    }

    // Shutdown the database
    public static void shutdownDatabase() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            if (!"XJ015".equals(e.getSQLState())) {
                System.out.println("Database shutdown failed: " + e.getMessage());
            } else {
                System.out.println("Database shutdown successfully.");
            }
        }
    }
}
