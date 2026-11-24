/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;



import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.util.IdGenerator;
import com.feedback.admin.util.Rules;
import java.time.LocalDate;

/** A notice for the Administrator, for example too few responses or a failed e-mail. */
public final class Alert {
    private final String alertId;
    private final AlertType type;
    private final String message;
    private final LocalDate createdDate;

    public Alert(AlertType type, String message) {
        if (type == null) {
            throw new ValidationException("Alert type is required");
        }
        if (Rules.blank(message)) {
            throw new ValidationException("Alert message is required");
        }
        this.alertId = IdGenerator.next("ALT");
        this.type = type;
        this.message = message.trim();
        this.createdDate = LocalDate.now();
    }

    public String getAlertId() { return alertId; }
    public AlertType getType() { return type; }
    public String getMessage() { return message; }
    public LocalDate getCreatedDate() { return createdDate; }
}