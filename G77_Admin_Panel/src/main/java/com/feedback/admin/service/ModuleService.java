/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;



import com.feedback.admin.domain.Module;
import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.ModuleRepository;
import com.feedback.admin.request.ModuleRequest;
import java.util.List;

/** Creates, changes and deactivates modules (FR7). Data goes through a ModuleRepository. */
public class ModuleService {
    private final ModuleRepository modules;
    private final RequestValidator validator;

    public ModuleService(ModuleRepository modules, RequestValidator validator) {
        this.modules = modules;
        this.validator = validator;
    }

    public Module createModule(ModuleRequest request) {
        validator.validate(request);
        if (modules.findByCode(request.getCode()).isPresent()) {
            throw new ValidationException("Module " + request.getCode() + " already exists");
        }
        Module m = new Module(request.getCode(), request.getName(), request.getLevel());
        modules.save(m);
        return m;
    }

    /** Changes the name and level. The code can never change. */
    public Module updateModule(String code, ModuleRequest request) {
        validator.validate(request);
        Module m = find(code);
        if (!code.equals(request.getCode())) {
            throw new ValidationException("Module code cannot be changed");
        }
        m.setName(request.getName());
        m.setLevel(request.getLevel());
        modules.save(m);
        return m;
    }

    public void deactivateModule(String code) {
        Module m = find(code);
        m.deactivate();
        modules.save(m);
    }

    /** Extra helper (not in the UML): other services use it to look up a module. */
    public Module find(String code) {
        return modules.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Module " + code + " not found"));
    }

    public List<Module> listModules() {
        return modules.findAll();
    }
}
