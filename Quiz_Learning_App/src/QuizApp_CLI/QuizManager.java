package QuizApp_CLI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QuizManager {

    private Map<String, QuizStructure> quizzes = new HashMap<>();
    private UserManager userManager = new UserManager();
    private FileIOManager fileIOManager = new FileIOManager();
    private Scanner scanner = new Scanner(System.in);

    public QuizManager() {
        // Load data when the application starts
        loadData();
    }

    public void createQuiz() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the quiz name:");
        String quizName = scanner.nextLine();
        System.out.println();

        QuizStructure quizStructure = new QuizStructure(quizName);
        while (true) {
            System.out.println("Enter question (or 'x' to finish):");
            String questionText = scanner.nextLine();
            if (questionText.equals("x")) {
                break;
            }

            System.out.println("Enter answer:");
            String answer = scanner.nextLine();
            System.out.println();

            quizStructure.addQuestion(new Question(questionText, answer));
        }

        quizzes.put(quizName, quizStructure);
        saveData(); // Save the quizzes after adding a new quiz
    }

    public void loadQuiz() {
        // Show available quizzes
        System.out.println("Available quizzes to load:");
        for (String quizName : quizzes.keySet()) {
            System.out.println("- " + quizName);
        }
        System.out.println();
        System.out.println("Enter the quiz name:");
        String quizNameInput = scanner.nextLine().trim().toLowerCase(); // Convert input to lowercase
        System.out.println();

        QuizStructure quizStructure = null;
        for (Map.Entry<String, QuizStructure> entry : quizzes.entrySet()) {
            if (entry.getKey().toLowerCase().equals(quizNameInput)) { // Compare case-insensitively
                quizStructure = entry.getValue();
                break;
            }
        }

        User user = userManager.getOrCreateUser();
        List<Question> questions = quizStructure.getQuestions();

        // Check if the quiz has less than 4 questions
        if (questions.size() < 4) {
            System.out.println("This quiz does not support the multi-choice mode due to insufficient questions.");
            System.out.println("Defaulting to Blind Mode.");
            System.out.println();
            QuestionDisplayMode displayMode = new BlindMode();
            for (Question question : questions) {
                displayMode.displayQuestion(question);
                System.out.println("Enter your answer:");
                String userAnswer = scanner.nextLine();
                System.out.println();
                if (userAnswer.equalsIgnoreCase("x")) {
                    System.out.println("Exiting quiz...");
                    return;
                }
                if (question.isCorrect(userAnswer)) {
                    System.out.println("Correct!");
                    System.out.println();
                    user.increaseScore();
                } else {
                    System.out.println("Incorrect!");
                    System.out.println();
                }
            }
            System.out.println("Your score: " + user.getScore());
            return;
        }

        // If the quiz has at least 4 questions, proceed with mode selection
        System.out.println("Select display mode:");
        System.out.println("1. Blind Mode");
        System.out.println("2. Multi-choice Mode");
        int modeChoice = getIntInput();
        System.out.println();

        QuestionDisplayMode displayMode;
        switch (modeChoice) {
            case 1:
                displayMode = new BlindMode();
                break;
            case 2:
                // Create a random list of unique choices for multi-choice mode
                List<String> uniqueChoices = new ArrayList<>();
                for (Question q : questions) {
                    uniqueChoices.add(q.getCorrectAnswer());
                    List<String> wrongAnswers = q.getChoices();
                    for (String choice : wrongAnswers) {
                        if (!uniqueChoices.contains(choice)) {
                            uniqueChoices.add(choice);
                        }
                    }
                }
                Collections.shuffle(uniqueChoices);
                displayMode = new MultiChoiceMode(uniqueChoices.subList(0, 3), scanner); // Take first 3 random choices
                break;
            default:
                System.out.println("Invalid choice, defaulting to Blind Mode.");
                System.out.println();
                displayMode = new BlindMode();
        }

        for (Question question : questions) {
            displayMode.displayQuestion(question);
            System.out.println("Typer your answer:");
            String userAnswer = scanner.nextLine();
            if (userAnswer.equalsIgnoreCase("x")) {
                System.out.println("Exiting quiz...");
                System.out.println("=================================");
                return;
            }
            if (question.isCorrect(userAnswer)) {
                System.out.println("Correct!");
                System.out.println();
                user.increaseScore();
            } else {
                System.out.println("Incorrect!");
                System.out.println();
            }
        }
        System.out.println("Your score: " + user.getScore());

        // Ask if the user wants to continue or quit
        System.out.println("Continue (c) or Quit (x)?");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("x")) {
            System.out.println("Happy studying! Exiting quiz...");
            System.exit(0);
            return;
        }
        System.out.println("=================================");
    }

    public void loadData() {
        // Load data from file
        fileIOManager.loadDataFromFiles(quizzes, "quizzes.txt");
    }

    public void saveData() {
        fileIOManager.saveDataToFiles(quizzes, "quizzes.txt");
    }

    private int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number:");
            scanner.next(); // Consume invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        return input;
    }
}
