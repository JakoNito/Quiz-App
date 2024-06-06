package QuizApp_CLI.Controller;

import QuizApp_CLI.db.DBManager;

public class Main {

    public static void main(String[] args) {
        try {
            // Initialize the database
            DBManager dbManager = new DBManager();
            // Optionally, setup database tables if needed
            dbManager.setupDatabase();

            // Start the quiz application
            QuizApp quizApp = new QuizApp();
            quizApp.run();
        } catch (Exception e) {
            System.out.println("An error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown the database
            DBManager.shutdownDatabase();
        }
    }
}
