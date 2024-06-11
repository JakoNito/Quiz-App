/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Model;

/**
 *
 * @author jakoi
 */
import java.util.ArrayList;
import java.util.List;

public class QuizStructure {
    private String quizName;
    private List<Question> questions;

    public QuizStructure(String quizName, List<Question> questions) {
        this.quizName = quizName;
        this.questions = new ArrayList<>(questions);
    }

    public String getQuizName() {
        return quizName;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<String> getAllChoices() {
        // Method to get all choices from the questions, if needed for multiple choice mode
        List<String> allChoices = new ArrayList<>();
        for (Question question : questions) {
            allChoices.add(question.getCorrectAnswer());
        }
        return allChoices;
    }
}
