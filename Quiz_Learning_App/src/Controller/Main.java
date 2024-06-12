package Controller;

import Database.DBManager;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        DBManager dbManager = null;
        try {
            // Initialize the database
            dbManager = new DBManager();
            dbManager.setupDatabase();

            // Register a shutdown hook to close the database properly on exit
            final DBManager finalDbManager = dbManager;
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    finalDbManager.shutdownDatabase();
                } catch (Exception e) {
                    System.err.println("An error occurred while shutting down the database: " + e.getMessage());
                }
            }));

            // Start quiz app
            SwingUtilities.invokeLater(() -> {
                QuizAppGUI quizAppGUI = new QuizAppGUI();
                quizAppGUI.showMainMenu();
            });
        } catch (Exception e) {
            System.out.println("An error occurred while starting the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}