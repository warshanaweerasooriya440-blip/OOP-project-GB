/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;


import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.request.LICRequest;
import com.feedback.admin.request.ModuleRequest;
import com.feedback.admin.request.QuestionRequest;
import com.feedback.admin.request.QuestionType;
import com.feedback.admin.request.SemesterRequest;
import com.feedback.admin.request.SessionRequest;
import com.feedback.admin.util.Rules;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Checks a request and reports every mistake at once. */
public class RequestValidator {

    public void validate(Object request) {
        if (request == null) {
            throw new ValidationException("Request is required");
        }
        List<String> errors = new ArrayList<>();
        if (request instanceof ModuleRequest) {
            checkModule((ModuleRequest) request, errors);
        } else if (request instanceof LICRequest) {
            checkLic((LICRequest) request, errors);
        } else if (request instanceof SemesterRequest) {
            checkSemester((SemesterRequest) request, errors);
        } else if (request instanceof SessionRequest) {
            checkSession((SessionRequest) request, errors);
        } else if (request instanceof QuestionRequest) {
            checkQuestion((QuestionRequest) request, errors);
        } else {
            throw new ValidationException("Unsupported request type");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void checkModule(ModuleRequest r, List<String> errors) {
        if (r.getCode() == null || !Rules.MODULE_CODE.matcher(r.getCode()).matches()) {
            errors.add("Module code must look like EC1022");
        }
        if (Rules.blank(r.getName()) || r.getName().length() > Rules.MAX_NAME) {
            errors.add("Module name is required (max " + Rules.MAX_NAME + " characters)");
        }
        if (r.getLevel() < Rules.MIN_LEVEL || r.getLevel() > Rules.MAX_LEVEL) {
            errors.add("Level must be between " + Rules.MIN_LEVEL + " and " + Rules.MAX_LEVEL);
        }
    }

    private void checkLic(LICRequest r, List<String> errors) {
        if (Rules.blank(r.getName()) || r.getName().length() > Rules.MAX_NAME) {
            errors.add("LIC name is required (max " + Rules.MAX_NAME + " characters)");
        }
        if (r.getEmail() == null || !Rules.EMAIL.matcher(r.getEmail().trim()).matches()) {
            errors.add("LIC e-mail is not valid");
        }
    }

    private void checkSemester(SemesterRequest r, List<String> errors) {
        if (r.getYear() < 2000 || r.getYear() > 2100) {
            errors.add("Year must be between 2000 and 2100");
        }
        if (r.getSemesterNumber() < 1 || r.getSemesterNumber() > 2) {
            errors.add("Semester number must be 1 or 2");
        }
    }

    private void checkSession(SessionRequest r, List<String> errors) {
        if (r.getModuleCode() == null || !Rules.MODULE_CODE.matcher(r.getModuleCode()).matches()) {
            errors.add("Module code must look like EC1022");
        }
        if (r.getYear() < 2000 || r.getYear() > 2100) {
            errors.add("Year must be between 2000 and 2100");
        }
        if (r.getSemesterNumber() < 1 || r.getSemesterNumber() > 2) {
            errors.add("Semester number must be 1 or 2");
        }
        if (Rules.blank(r.getLicId())) {
            errors.add("LIC id is required");
        }
        if (r.getOpenDate() == null || r.getCloseDate() == null) {
            errors.add("Open and close dates are required");
        } else if (!r.getCloseDate().isAfter(r.getOpenDate())) {
            errors.add("Close date must be after open date");
        }
        if (r.getEnrolmentCount() < 1) {
            errors.add("Enrolment count must be at least 1");
        }
        if (r.getMinResponseThreshold() < 1 || r.getMinResponseThreshold() > r.getEnrolmentCount()) {
            errors.add("Minimum response threshold must be between 1 and the enrolment count");
        }
    }

    private void checkQuestion(QuestionRequest r, List<String> errors) {
        if (r.getType() == null) {
            errors.add("Question type is required");
        }
        if (Rules.blank(r.getText()) || r.getText().length() > Rules.MAX_TEXT) {
            errors.add("Question text is required (max " + Rules.MAX_TEXT + " characters)");
        }
        if (Rules.blank(r.getSectionType())) {
            errors.add("Section is required");
        }
        if (r.getType() == QuestionType.MULTIPLE_CHOICE) {
            checkOptions(r.getOptions(), errors);
        }
        if (r.getType() == QuestionType.TEXT && r.getMaxLength() != null
                && (r.getMaxLength() < 1 || r.getMaxLength() > 1000)) {
            errors.add("Maximum answer length must be between 1 and 1000");
        }
    }

    private void checkOptions(List<String> options, List<String> errors) {
        if (options == null || options.size() < 2) {
            errors.add("A multiple-choice question needs at least 2 options");
            return;
        }
        if (options.size() > 6) {
            errors.add("A multiple-choice question can have at most 6 options");
        }
        Set<String> seen = new HashSet<>();
        for (String option : options) {
            if (Rules.blank(option)) {
                errors.add("Options must not be blank");
                return;
            }
            if (!seen.add(option.trim().toLowerCase())) {
                errors.add("Options must not repeat");
                return;
            }
        }
    }
}
