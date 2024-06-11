/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.View;

import QuizApp_CLI.Model.Question;
import QuizApp_CLI.View.QuestionDisplayMode;
import javax.swing.*;

/**
 *
 * @author jakoi
 */
public class BlindMode implements QuestionDisplayMode {
    @Override
    public void displayQuestion(JFrame frame, Question question) {
        JOptionPane.showMessageDialog(frame, question.getQuestion());
    }

    @Override
    public String getAnswer(JFrame frame) {
        return JOptionPane.showInputDialog(frame, "Enter your answer:");
    }
}
