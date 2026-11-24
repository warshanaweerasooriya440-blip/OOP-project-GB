/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.stub;



/** STAND-IN for G8's free-text question: the answer length is limited. */
public class Text_Question extends Questions {
    private final int maxLength;

    public Text_Question(String id, String question, boolean compulsory, String sectionType,
                         int maxLength) {
        super(id, question, compulsory, sectionType);
        this.maxLength = maxLength;
    }

    public int getMaxLength() { return maxLength; }

    @Override
    public boolean Validate_Ans(String answer) {
        if (answer == null || answer.isBlank()) {
            return !compulsory;
        }
        return answer.length() <= maxLength;
    }
}
