/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;


import com.feedback.admin.stub.Questions;
import java.util.List;
import java.util.Optional;

/** Saves and reads questions (the Questions class belongs to G8). */
public interface QuestionRepository {
    void save(Questions question);

    Optional<Questions> findById(String id);

    List<Questions> findAllActive();

    List<Questions> findAll();
}
