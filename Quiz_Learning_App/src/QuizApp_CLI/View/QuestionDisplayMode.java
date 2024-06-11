/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QuizApp_CLI.View;

import QuizApp_CLI.Model.Question;
import javax.swing.*;

public interface QuestionDisplayMode {
    void displayQuestion(JFrame frame, Question question);
    String getAnswer(JFrame frame);
}

class MultiChoiceMode implements QuestionDisplayMode {
    // Assuming it has necessary constructors and methods

    @Override
    public void displayQuestion(JFrame frame, Question question) {
        // Display question in multi-choice mode
    }

    @Override
    public String getAnswer(JFrame frame) {
        // Get user answer for multi-choice mode
        return ""; // Placeholder
    }
}

class BlindMode implements QuestionDisplayMode {
    @Override
    public void displayQuestion(JFrame frame, Question question) {
        // Display question in blind mode
    }

    @Override
    public String getAnswer(JFrame frame) {
        // Get user answer for blind mode
        return ""; // Placeholder
    }
}
