/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.Model;

/**
 *
 * @author johann
 */
import QuizApp_CLI.View.QuestionDisplayMode;
import java.util.List;

public class Quiz {
    private String quizName;
    private List<Question> questions;
    private User user;
    private QuestionDisplayMode displayMode;
    private int score;

    public Quiz(String quizName, List<Question> questions, User user, QuestionDisplayMode displayMode) {
        this.quizName = quizName;
        this.questions = questions;
        this.user = user;
        this.displayMode = displayMode;
    }

    public void answerQuestion(Question question, String userAnswer) {
        if (question.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
            score++;
        }
    }

    public int calculateScore() {
        return score;
    }
}

