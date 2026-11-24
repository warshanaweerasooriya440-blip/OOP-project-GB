/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;


import com.feedback.admin.domain.LIC;
import com.feedback.admin.exception.ResourceNotFoundException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.LICRepository;
import com.feedback.admin.request.LICRequest;
import com.feedback.admin.util.IdGenerator;
import java.util.List;

/** Creates and changes Lecturers in Charge and their e-mail addresses (FR7). */
public class LICService {
    private final LICRepository lics;
    private final RequestValidator validator;

    public LICService(LICRepository lics, RequestValidator validator) {
        this.lics = lics;
        this.validator = validator;
    }

    public LIC createLIC(LICRequest request) {
        validator.validate(request);
        if (lics.findByEmail(request.getEmail()).isPresent()) {
            throw new ValidationException(
                    "E-mail " + request.getEmail().trim() + " is already registered");
        }
        LIC lic = new LIC(IdGenerator.next("LIC"), request.getName(), request.getEmail());
        lics.save(lic);
        return lic;
    }

    /** Changes the name and e-mail. The e-mail must not belong to another LIC. */
    public LIC updateLIC(String id, LICRequest request) {
        validator.validate(request);
        LIC lic = find(id);
        for (LIC other : lics.findAll()) {
            boolean sameEmail = other.getEmail().equalsIgnoreCase(request.getEmail().trim());
            if (sameEmail && !other.getUserId().equals(id)) {
                throw new ValidationException(
                        "E-mail " + request.getEmail().trim() + " is already registered");
            }
        }
        lic.setName(request.getName());
        lic.updateEmail(request.getEmail());
        lics.save(lic);
        return lic;
    }

    public void deactivateLIC(String id) {
        LIC lic = find(id);
        lic.deactivate();
        lics.save(lic);
    }

    /** Extra helper (not in the UML): other services use it to look up an LIC. */
    public LIC find(String id) {
        return lics.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LIC " + id + " not found"));
    }

    public List<LIC> listLICs() {
        return lics.findAll();
    }
}
