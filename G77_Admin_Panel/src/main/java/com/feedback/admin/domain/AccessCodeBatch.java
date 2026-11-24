/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;



import com.feedback.admin.exception.ValidationException;
import java.util.HashSet;
import java.util.List;

/** The access codes made for one session. The plain codes are shown to the Administrator once. */
public final class AccessCodeBatch {
    private final String sessionId;
    private final int totalCodes;
    private final List<String> plainCodes;

    public AccessCodeBatch(String sessionId, List<String> plainCodes) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new ValidationException("Session id is required");
        }
        if (plainCodes == null || plainCodes.isEmpty()) {
            throw new ValidationException("A batch needs at least one code");
        }
        if (new HashSet<>(plainCodes).size() != plainCodes.size()) {
            throw new ValidationException("Access codes in a batch must be unique");
        }
        this.sessionId = sessionId;
        this.plainCodes = List.copyOf(plainCodes);
        this.totalCodes = this.plainCodes.size();
    }

    public String getSessionId() { return sessionId; }
    public int getTotalCodes() { return totalCodes; }
    public List<String> getPlainCodes() { return plainCodes; }
    
        /** Never prints the codes, so they cannot leak into logs. */
    @Override
    public String toString() {
        return "AccessCodeBatch[session=" + sessionId + ", codes=" + totalCodes + "]";
    }
}