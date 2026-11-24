/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;

import com.feedback.admin.domain.Semester;
import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.SemesterRepository;
import com.feedback.admin.request.SemesterRequest;
import java.util.List;

/** Creates semesters (FR7). A session can only use a semester that was created here. */
public class SemesterService {
    private final SemesterRepository semesters;
    private final RequestValidator validator;

    public SemesterService(SemesterRepository semesters, RequestValidator validator) {
        this.semesters = semesters;
        this.validator = validator;
    }

    public Semester createSemester(SemesterRequest request) {
        validator.validate(request);
        Semester s = new Semester(request.getYear(), request.getSemesterNumber());
        if (semesters.findByLabel(s.getLabel()).isPresent()) {
            throw new ValidationException("Semester " + s.getLabel() + " already exists");
        }
        semesters.save(s);
        return s;
    }

    /** Extra helper (not in the UML): other services use it to look up a semester. */
    public Semester find(int year, int semesterNumber) {
        String label = new Semester(year, semesterNumber).getLabel();
        return semesters.findByLabel(label)
                .orElseThrow(() -> new ResourceNotFoundException("Semester " + label + " not found"));
    }

    public List<Semester> listSemesters() {
        return semesters.findAll();
    }
}
