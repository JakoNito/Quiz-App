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

public class Question {
    private String question;
    private String correctAnswer;
    private List<String> choices;

    public Question(String question, String correctAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.choices = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public boolean isCorrect(String answer) {
        return correctAnswer.equalsIgnoreCase(answer);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
    
    // Add method to add wrong answers
    public void addWrongAnswer(String wrongAnswer) {
        choices.add(wrongAnswer);
    }

    // Add method to retrieve wrong answers
    public List<String> getChoices() {
        return choices;
    }
}