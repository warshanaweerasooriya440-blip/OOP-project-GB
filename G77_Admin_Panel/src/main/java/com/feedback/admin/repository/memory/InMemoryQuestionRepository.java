/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;


import com.feedback.admin.repository.QuestionRepository;
import com.feedback.admin.stub.Questions;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Keeps questions in memory, so the Admin Panel works without a database. */
public class InMemoryQuestionRepository implements QuestionRepository {
    private final Map<String, Questions> data = new LinkedHashMap<>();

    @Override
    public void save(Questions question) {
        data.put(question.getId(), question);
    }

    @Override
    public Optional<Questions> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Questions> findAllActive() {
        List<Questions> result = new ArrayList<>();
        for (Questions q : data.values()) {
            if (q.isActive()) {
                result.add(q);
            }
        }
        return result;
    }

    @Override
    public List<Questions> findAll() {
        return new ArrayList<>(data.values());
    }
}

