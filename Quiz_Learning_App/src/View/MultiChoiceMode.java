/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

/**
 *
 * @author jakoi
 */
import Model.Question;
import View.QuestionDisplayMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import Model.Question;
import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class MultiChoiceMode extends QuestionDisplayMode {
    private List<String> choices;
    private JFrame frame;

    public MultiChoiceMode(List<String> choices, JFrame frame) {
        this.choices = choices;
        this.frame = frame;
    }

    @Override
    public void displayQuestion(Question question) {
        Collections.shuffle(choices);
        StringBuilder message = new StringBuilder(question.getQuestion() + "\n");
        for (int i = 0; i < choices.size(); i++) {
            message.append((char) ('a' + i)).append(". ").append(choices.get(i)).append("\n");
        }
        JOptionPane.showMessageDialog(frame, message.toString());
    }
}
