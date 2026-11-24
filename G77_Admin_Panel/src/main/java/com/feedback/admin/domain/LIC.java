/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;


import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.util.Rules;

/**
 * Lecturer in Charge. In the B1 UML this class belongs to G8. Here it also carries the
 * e-mail address and the active flag that the Admin Panel and the e-mail service need.
 */
public final class LIC {
    private final String userId;
    private String name;
    private String email;
    private boolean active = true;

    public LIC(String userId, String name, String email) {
        this.userId = userId;
        setName(name);
        updateEmail(email);
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    public void setName(String name) {
        if (Rules.blank(name) || name.length() > Rules.MAX_NAME) {
            throw new ValidationException(
                    "LIC name is required (max " + Rules.MAX_NAME + " characters)");
        }
        this.name = name.trim();
    }

    public void updateEmail(String email) {
        if (email == null || !Rules.EMAIL.matcher(email.trim()).matches()) {
            throw new ValidationException("LIC e-mail is not valid");
        }
        this.email = email.trim();
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }
}
