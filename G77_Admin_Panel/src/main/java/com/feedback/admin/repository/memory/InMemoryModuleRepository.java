/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;


import com.feedback.admin.domain.Module;
import com.feedback.admin.repository.ModuleRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Keeps modules in memory, so the Admin Panel works without a database. */
public class InMemoryModuleRepository implements ModuleRepository {
    private final Map<String, Module> data = new LinkedHashMap<>();

    @Override
    public void save(Module module) {
        data.put(module.getCode(), module);
    }

    @Override
    public Optional<Module> findByCode(String code) {
        return Optional.ofNullable(data.get(code));
    }

    @Override
    public List<Module> findAll() {
        return new ArrayList<>(data.values());
    }
}

