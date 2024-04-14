/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI;

import java.util.Scanner;
/**
 *
 * @author jakoi
 */
public class QuizApp {
    private Scanner scanner = new Scanner(System.in);
    private QuizManager quizManager = new QuizManager();

    public void run() {
        while (true) {
            System.out.println("Welcome to the Quiz App!");
            System.out.println("1. Create a Quiz");
            System.out.println("2. Load a Quiz");
            System.out.println("3. Quit");

            int choice = getIntInput();
            System.out.println();
            switch (choice) {
                case 1:
                    quizManager.createQuiz();
                    break;
                case 2:
                    quizManager.loadQuiz();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    System.out.println();
            }
        }
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number:");
            scanner.next(); // Consume invalid input
        }
        return scanner.nextInt();
    }
}