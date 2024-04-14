/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI;

/**
 *
 * @author jakoi
 */
import java.io.*;
import java.util.Map;

public class FileIOManager {
    public void loadDataFromFiles(Map<String, QuizStructure> quizzes, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 2) {
                    QuizStructure quizStructure = new QuizStructure(parts[0]);
                    String[] questionsData = parts[1].split(",");
                    for (String qData : questionsData) {
                        String[] qParts = qData.split("\\|");
                        String questionText = qParts[0];
                        String correctAnswer = qParts[1];
                        quizStructure.addQuestion(new Question(questionText, correctAnswer));
                    }
                    quizzes.put(parts[0], quizStructure);
                } else {
                    System.out.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading quizzes: " + e.getMessage());
        }
    }

    public void saveDataToFiles(Map<String, QuizStructure> quizzes, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (QuizStructure quizStructure : quizzes.values()) {
                writer.write(quizStructure.getQuizName() + ":");
                for (Question question : quizStructure.getQuestions()) {
                    writer.write(questionToText(question));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving quizzes: " + e.getMessage());
        }
    }

    private String questionToText(Question question) {
        return question.getQuestion() + "|" + question.getCorrectAnswer() + ",";
    }
}