/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;


import com.feedback.admin.domain.Module;
import java.util.List;
import java.util.Optional;

/** Saves and reads modules. G18 writes the database version later. */
public interface ModuleRepository {
    void save(Module module);

    Optional<Module> findByCode(String code);

    List<Module> findAll();
}
