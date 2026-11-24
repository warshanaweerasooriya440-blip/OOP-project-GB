/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;


import com.feedback.admin.domain.LIC;
import java.util.List;
import java.util.Optional;

/** Saves and reads LICs. G18 writes the database version later. */
public interface LICRepository {
    void save(LIC lic);

    Optional<LIC> findById(String id);

    Optional<LIC> findByEmail(String email);

    List<LIC> findAll();
}
