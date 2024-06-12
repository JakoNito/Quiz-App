/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Model.Question;
import View.QuestionDisplayMode;
import javax.swing.*;

/**
 *
 * @author jakoi
 */
public class BlindMode extends QuestionDisplayMode {
    @Override
    public void displayQuestion(Question question) {
        JOptionPane.showMessageDialog(null, question.getQuestion());
    }
}
