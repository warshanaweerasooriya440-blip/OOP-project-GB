/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;


import com.feedback.admin.domain.LIC;
import com.feedback.admin.repository.LICRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Keeps LICs in memory, so the Admin Panel works without a database. */
public class InMemoryLICRepository implements LICRepository {
    private final Map<String, LIC> data = new LinkedHashMap<>();

    @Override
    public void save(LIC lic) {
        data.put(lic.getUserId(), lic);
    }

    @Override
    public Optional<LIC> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<LIC> findByEmail(String email) {
        for (LIC lic : data.values()) {
            if (lic.getEmail().equalsIgnoreCase(email.trim())) {
                return Optional.of(lic);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<LIC> findAll() {
        return new ArrayList<>(data.values());
    }
}
