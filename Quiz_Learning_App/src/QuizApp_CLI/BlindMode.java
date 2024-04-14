/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI;

/**
 *
 * @author jakoi
 */
public class BlindMode extends QuestionDisplayMode {
    @Override
    public void displayQuestion(Question question) {
        System.out.println(question.getQuestion());
    }
}
