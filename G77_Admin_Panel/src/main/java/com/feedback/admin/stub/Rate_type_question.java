/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.stub;


/** STAND-IN for G8's rating question: the answer is a whole number from 1 to 5. */
public class Rate_type_question extends Questions {

    public Rate_type_question(String id, String question, boolean compulsory, String sectionType) {
        super(id, question, compulsory, sectionType);
    }

    @Override
    public boolean Validate_Ans(String answer) {
        try {
            int rating = Integer.parseInt(answer.trim());
            return rating >= 1 && rating <= 5;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
