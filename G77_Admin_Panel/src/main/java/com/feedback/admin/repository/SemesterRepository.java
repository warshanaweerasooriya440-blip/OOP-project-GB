/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;

import com.feedback.admin.domain.Semester;
import java.util.List;
import java.util.Optional;

/** Saves and reads semesters. G18 writes the database version later. */
public interface SemesterRepository {
    void save(Semester semester);

    Optional<Semester> findByLabel(String label);

    List<Semester> findAll();
}
