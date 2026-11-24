/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;


import com.feedback.admin.domain.FeedbackSession;
import java.util.List;
import java.util.Optional;

/** Saves and reads feedback sessions (shared with G69). G18 writes the database version later. */
public interface SessionRepository {
    void save(FeedbackSession session);

    Optional<FeedbackSession> findById(String id);

    List<FeedbackSession> findAll();
}
