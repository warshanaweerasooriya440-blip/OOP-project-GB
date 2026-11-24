/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;

import com.feedback.admin.domain.Semester;
import com.feedback.admin.repository.SemesterRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Keeps semesters in memory, so the Admin Panel works without a database. */
public class InMemorySemesterRepository implements SemesterRepository {
    private final Map<String, Semester> data = new LinkedHashMap<>();

    @Override
    public void save(Semester semester) {
        data.put(semester.getLabel(), semester);
    }

    @Override
    public Optional<Semester> findByLabel(String label) {
        return Optional.ofNullable(data.get(label));
    }

    @Override
    public List<Semester> findAll() {
        return new ArrayList<>(data.values());
    }
}
