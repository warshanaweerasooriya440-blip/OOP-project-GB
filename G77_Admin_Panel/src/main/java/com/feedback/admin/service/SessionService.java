/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;


import com.feedback.admin.domain.FeedbackSession;
import com.feedback.admin.domain.LIC;
import com.feedback.admin.domain.Module;
import com.feedback.admin.domain.Semester;
import com.feedback.admin.domain.SessionStatus;
import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.SessionStateException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.SessionRepository;
import com.feedback.admin.request.SessionRequest;
import com.feedback.admin.stub.Questions;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Creates feedback sessions, edits them, assigns their questions, opens and closes them
 * (FR2, FR7, FR8, FR9). A session that is no longer SCHEDULED can never be edited.
 */
public class SessionService {
    private final RequestValidator validator;
    private final SessionRepository sessions;
    private final ModuleService moduleService;
    private final LICService licService;
    private final SemesterService semesterService;
    private final QuestionBankService questionService;

    public SessionService(RequestValidator validator, SessionRepository sessions,
                          ModuleService moduleService, LICService licService,
                          SemesterService semesterService, QuestionBankService questionService) {
        this.validator = validator;
        this.sessions = sessions;
        this.moduleService = moduleService;
        this.licService = licService;
        this.semesterService = semesterService;
        this.questionService = questionService;
    }

    public FeedbackSession createSession(SessionRequest request) {
        validator.validate(request);
        Module module = moduleService.find(request.getModuleCode());
        if (!module.isActive()) {
            throw new ValidationException("Module " + module.getCode() + " is deactivated");
        }
        LIC lic = licService.find(request.getLicId());
        if (!lic.isActive()) {
            throw new ValidationException("LIC " + lic.getName() + " is deactivated");
        }
        Semester semester = semesterService.find(request.getYear(), request.getSemesterNumber());
        for (FeedbackSession s : sessions.findAll()) {
            boolean same = s.getModule().getCode().equals(module.getCode())
                    && s.getLicId().equals(lic.getUserId())
                    && s.getSemester().getLabel().equals(semester.getLabel());
            if (same && s.getStatus() != SessionStatus.CLOSED) {
                throw new ValidationException(
                        "A live session already exists for this module, LIC and semester");
            }
        }
        FeedbackSession s = new FeedbackSession(module, semester, lic.getUserId(),
                request.getOpenDate(), request.getCloseDate(),
                request.getEnrolmentCount(), request.getMinResponseThreshold());
        sessions.save(s);
        return s;
    }

    /** Changes dates, enrolment and threshold. Module, LIC and semester cannot change. */
    public FeedbackSession updateSession(String sessionId, SessionRequest request) {
        validator.validate(request);
        FeedbackSession s = find(sessionId);
        String label = new Semester(request.getYear(), request.getSemesterNumber()).getLabel();
        if (!s.getModule().getCode().equals(request.getModuleCode())
                || !s.getLicId().equals(request.getLicId())
                || !s.getSemester().getLabel().equals(label)) {
            throw new ValidationException("Module, LIC and semester of a session cannot be changed");
        }
        s.reschedule(request.getOpenDate(), request.getCloseDate(),
                request.getEnrolmentCount(), request.getMinResponseThreshold());
        sessions.save(s);
        return s;
    }

    /** Assigns the question set. Every question must exist and be active. */
    public void assignQuestions(String sessionId, List<String> qIds) {
        FeedbackSession s = find(sessionId);
        if (qIds == null || qIds.isEmpty()) {
            throw new ValidationException("At least one question is required");
        }
        for (String id : new LinkedHashSet<>(qIds)) {
            Questions q = questionService.find(id);
            if (!q.isActive()) {
                throw new ValidationException("Question " + id + " is deactivated");
            }
        }
        s.assignQuestions(qIds);
        sessions.save(s);
    }

    public void openSession(String sessionId) {
        FeedbackSession s = find(sessionId);
        s.open();
        sessions.save(s);
    }

    public void closeSession(String sessionId) {
        FeedbackSession s = find(sessionId);
        s.close();
        sessions.save(s);
    }

    /**
     * Applies the schedule for one day: sessions whose close date has passed are closed, and
     * SCHEDULED sessions whose open date has arrived are opened (if they have questions).
     * @return how many sessions changed status
     */
    public int applySchedule(LocalDate today) {
        int changed = 0;
        for (FeedbackSession s : sessions.findAll()) {
            if (s.getStatus() == SessionStatus.CLOSED) {
                continue;
            }
            if (today.isAfter(s.getCloseDate())) {
                s.close();
                sessions.save(s);
                changed++;
            } else if (s.getStatus() == SessionStatus.SCHEDULED && !today.isBefore(s.getOpenDate())) {
                try {
                    s.open();
                    sessions.save(s);
                    changed++;
                } catch (SessionStateException noQuestionsYet) {
                    // stays SCHEDULED until questions are assigned
                }
            }
        }
        return changed;
    }

    /** Extra helper (not in the UML): other services look up a session with it. */
    public FeedbackSession find(String sessionId) {
        return sessions.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session " + sessionId + " not found"));
    }

    public List<FeedbackSession> listSessions() {
        return sessions.findAll();
    }
}
