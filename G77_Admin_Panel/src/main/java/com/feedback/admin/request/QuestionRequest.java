/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.request;


import java.util.List;

/**
 * The data the Administrator types in on the question editor.
 * options is used by MULTIPLE_CHOICE, maxLength by TEXT (null means the default of 250).
 */
public final class QuestionRequest {
    private final QuestionType type;
    private final String text;
    private final boolean compulsory;
    private final String sectionType;
    private final List<String> options;
    private final Integer maxLength;

    public QuestionRequest(QuestionType type, String text, boolean compulsory,
                           String sectionType, List<String> options, Integer maxLength) {
        this.type = type;
        this.text = text;
        this.compulsory = compulsory;
        this.sectionType = sectionType;
        this.options = options;
        this.maxLength = maxLength;
    }

    public QuestionType getType() { return type; }
    public String getText() { return text; }
    public boolean isCompulsory() { return compulsory; }
    public String getSectionType() { return sectionType; }
    public List<String> getOptions() { return options; }
    public Integer getMaxLength() { return maxLength; }
}