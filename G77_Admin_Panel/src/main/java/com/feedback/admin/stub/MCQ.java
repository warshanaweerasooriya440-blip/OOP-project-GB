/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.stub;


import java.util.List;

/** STAND-IN for G8's multiple-choice question: the answer is a letter a, b, c ... */
public class MCQ extends Questions {
    private final List<String> options;

    public MCQ(String id, String question, boolean compulsory, String sectionType,
               List<String> options) {
        super(id, question, compulsory, sectionType);
        this.options = List.copyOf(options);
    }

    public List<String> getOptions() { return options; }

    @Override
    public boolean Validate_Ans(String answer) {
        if (answer == null || answer.trim().length() != 1) {
            return false;
        }
        int index = Character.toLowerCase(answer.trim().charAt(0)) - 'a';
        return index >= 0 && index < options.size();
    }
}
