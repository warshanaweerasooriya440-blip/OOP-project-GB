/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;



import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.QuestionRepository;
import com.feedback.admin.request.QuestionRequest;
import com.feedback.admin.request.QuestionType;
import com.feedback.admin.stub.MCQ;
import com.feedback.admin.stub.Questions;
import com.feedback.admin.stub.Rate_type_question;
import com.feedback.admin.stub.Text_Question;
import com.feedback.admin.util.IdGenerator;
import java.util.List;

/** Manages the question bank: add, edit and deactivate (FR8). Questions are never deleted. */
public class QuestionBankService {
    private final QuestionRepository questions;
    private final RequestValidator validator;

    public QuestionBankService(QuestionRepository questions, RequestValidator validator) {
        this.questions = questions;
        this.validator = validator;
    }

    public Questions addQuestion(QuestionRequest request) {
        validator.validate(request);
        Questions q = QuestionFactory.create(IdGenerator.next("Q"), request);
        questions.save(q);
        return q;
    }

    /** Replaces the content but keeps the id and the active state. The type cannot change. */
    public Questions editQuestion(String id, QuestionRequest request) {
        validator.validate(request);
        Questions old = find(id);
        if (typeOf(old) != request.getType()) {
            throw new ValidationException("The type of an existing question cannot be changed");
        }
        Questions updated = QuestionFactory.create(id, request);
        if (!old.isActive()) {
            updated.deactivate();
        }
        questions.save(updated);
        return updated;
    }

    public void deactivateQuestion(String id) {
        Questions q = find(id);
        q.deactivate();
        questions.save(q);
    }

    /** Extra helper (not in the UML): other services use it to look up a question. */
    public Questions find(String id) {
        return questions.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question " + id + " not found"));
    }

    public List<Questions> listActive() {
        return questions.findAllActive();
    }

    public List<Questions> listAll() {
        return questions.findAll();
    }

    private static QuestionType typeOf(Questions q) {
        if (q instanceof Rate_type_question) {
            return QuestionType.RATING;
        }
        if (q instanceof MCQ) {
            return QuestionType.MULTIPLE_CHOICE;
        }
        if (q instanceof Text_Question) {
            return QuestionType.TEXT;
        }
        throw new IllegalStateException("Unknown question class " + q.getClass().getName());
    }
}
