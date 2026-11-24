/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.exception;


/** Thrown when an action is not allowed in the session's current state. */
public class SessionStateException extends RuntimeException {
    public SessionStateException(String message) {
        super(message);
    }
}
