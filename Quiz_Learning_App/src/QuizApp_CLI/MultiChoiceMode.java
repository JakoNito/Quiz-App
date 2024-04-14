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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class MultiChoiceMode extends QuestionDisplayMode {
    private List<String> choices;
    private Scanner scanner;

    public MultiChoiceMode(List<String> choices, Scanner scanner) {
        this.choices = choices;
        this.scanner = scanner;
    }

    @Override
    public void displayQuestion(Question question) {
        System.out.println(question.getQuestion());
        List<String> allChoices = new ArrayList<>(choices); // Start with common choices
        List<String> questionChoices = question.getChoices(); // Get question-specific choices
        for (String choice : questionChoices) {
            if (!allChoices.contains(choice)) { // Add only if not already present
                allChoices.add(choice);
            }
        }
        if (!allChoices.contains(question.getCorrectAnswer())) { // Ensure correct answer is included
            allChoices.add(question.getCorrectAnswer());
        }
        Collections.shuffle(allChoices);
        for (int i = 0; i < allChoices.size(); i++) {
            System.out.println((char) ('a' + i) + ". " + allChoices.get(i));
        }
    }
}
