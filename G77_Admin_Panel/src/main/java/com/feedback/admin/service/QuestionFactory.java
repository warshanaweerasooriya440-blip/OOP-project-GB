/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;



import com.feedback.admin.request.QuestionRequest;
import com.feedback.admin.stub.MCQ;
import com.feedback.admin.stub.Questions;
import com.feedback.admin.stub.Rate_type_question;
import com.feedback.admin.stub.Text_Question;
import java.util.ArrayList;
import java.util.List;

/** Builds the right kind of question from the editor form (Factory pattern). */
public final class QuestionFactory {
    /** Default longest answer for a free-text question. */
    public static final int DEFAULT_TEXT_LENGTH = 250;

    private QuestionFactory() { }

    /** @param r a request that has already passed the validator */
    public static Questions create(String id, QuestionRequest r) {
        String text = r.getText().trim();
        String section = r.getSectionType().trim();
        switch (r.getType()) {
            case RATING:
                return new Rate_type_question(id, text, r.isCompulsory(), section);
            case MULTIPLE_CHOICE:
                List<String> options = new ArrayList<>();
                for (String option : r.getOptions()) {
                    options.add(option.trim());
                }
                return new MCQ(id, text, r.isCompulsory(), section, options);
            default:
                int max = r.getMaxLength() == null ? DEFAULT_TEXT_LENGTH : r.getMaxLength();
                return new Text_Question(id, text, r.isCompulsory(), section, max);
        }
    }
}
