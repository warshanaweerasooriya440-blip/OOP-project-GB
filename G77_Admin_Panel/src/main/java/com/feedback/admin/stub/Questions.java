/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.stub;


/**
 * STAND-IN for G8's abstract Questions class, so this project compiles on its own.
 * Replace it with G8's class when we integrate.
 */
public abstract class Questions {
    protected final String id;
    protected final String question;
    protected final boolean compulsory;
    protected final String sectionType;
    protected boolean active = true;

    protected Questions(String id, String question, boolean compulsory, String sectionType) {
        this.id = id;
        this.question = question;
        this.compulsory = compulsory;
        this.sectionType = sectionType;
    }

    /** @return true if the answer is acceptable for this kind of question. */
    public abstract boolean Validate_Ans(String answer);

    public String getId() { return id; }
    public String getQuestion() { return question; }
    public boolean isCompulsory() { return compulsory; }
    public String getSectionType() { return sectionType; }
    public boolean isActive() { return active; }

    /** Stops new sessions using the question; old results stay valid. */
    public void deactivate() { active = false; }
}