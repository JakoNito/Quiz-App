/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI;

/**
 *
 * @author jakoi
 */
import java.util.ArrayList;
import java.util.List;

public class QuizStructure {
    private String quizName;
    private List<Question> questions;

    public QuizStructure(String quizName) {
        this.quizName = quizName;
        questions = new ArrayList<>();
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getQuizName() {
        return quizName;
    }
}