/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;


import com.feedback.admin.exception.SessionStateException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.util.IdGenerator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** One feedback session for a module in a semester. Moves SCHEDULED, OPEN, CLOSED. */
public final class FeedbackSession {
    private final String sessionId;
    private final Module module;
    private final Semester semester;
    private final String licId;
    private LocalDate openDate;
    private LocalDate closeDate;
    private int enrolmentCount;
    private int minResponseThreshold;
    private final List<String> questionIds = new ArrayList<>();
    private SessionStatus status = SessionStatus.SCHEDULED;

    public FeedbackSession(Module module, Semester semester, String licId,
                           LocalDate openDate, LocalDate closeDate,
                           int enrolmentCount, int minResponseThreshold) {
        if (module == null || semester == null) {
            throw new ValidationException("Module and semester are required");
        }
        if (licId == null || licId.isBlank()) {
            throw new ValidationException("LIC id is required");
        }
        checkSetup(openDate, closeDate, enrolmentCount, minResponseThreshold);
        this.sessionId = IdGenerator.next("SES");
        this.module = module;
        this.semester = semester;
        this.licId = licId;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.enrolmentCount = enrolmentCount;
        this.minResponseThreshold = minResponseThreshold;
    }

    /** The checks shared by the constructor and reschedule(). */
    private static void checkSetup(LocalDate open, LocalDate close, int enrolment, int threshold) {
        if (open == null || close == null || !close.isAfter(open)) {
            throw new ValidationException("Close date must be after open date");
        }
        if (enrolment < 1) {
            throw new ValidationException("Enrolment count must be at least 1");
        }
        if (threshold < 1 || threshold > enrolment) {
            throw new ValidationException(
                    "Minimum response threshold must be between 1 and the enrolment count");
        }
    }

    /** Opens the session. It needs at least one question. */
    public void open() {
        if (status != SessionStatus.SCHEDULED) {
            throw new SessionStateException(
                    "Only a SCHEDULED session can be opened (now " + status + ")");
        }
        if (questionIds.isEmpty()) {
            throw new SessionStateException("Assign questions before opening the session");
        }
        status = SessionStatus.OPEN;
    }

    /** Closes the session (also allowed before it opens, FR9). This cannot be undone. */
    public void close() {
        if (status == SessionStatus.CLOSED) {
            throw new SessionStateException("Session " + sessionId + " is already closed");
        }
        status = SessionStatus.CLOSED;
    }

    /** @return true while the session has not opened yet, so its setup can still change. */
    public boolean isEditable() { return status == SessionStatus.SCHEDULED; }

    /** Changes dates, enrolment and threshold together, only while SCHEDULED. */
    public void reschedule(LocalDate open, LocalDate close, int enrolment, int threshold) {
        requireEditable("The setup can only change");
        checkSetup(open, close, enrolment, threshold);
        this.openDate = open;
        this.closeDate = close;
        this.enrolmentCount = enrolment;
        this.minResponseThreshold = threshold;
    }

    public void assignQuestions(List<String> ids) {
        requireEditable("Questions can only change");
        if (ids == null || ids.isEmpty()) {
            throw new ValidationException("At least one question is required");
        }
        questionIds.clear();
        questionIds.addAll(new LinkedHashSet<>(ids));
    }

    private void requireEditable(String what) {
        if (!isEditable()) {
            throw new SessionStateException(what + " while the session is SCHEDULED (now " + status + ")");
        }
    }

    public String getSessionId() { return sessionId; }
    public Module getModule() { return module; }
    public Semester getSemester() { return semester; }
    public String getLicId() { return licId; }
    public LocalDate getOpenDate() { return openDate; }
    public LocalDate getCloseDate() { return closeDate; }
    public int getEnrolmentCount() { return enrolmentCount; }
    public int getMinResponseThreshold() { return minResponseThreshold; }
    public SessionStatus getStatus() { return status; }
    public List<String> getQuestionIds() { return List.copyOf(questionIds); }
}
