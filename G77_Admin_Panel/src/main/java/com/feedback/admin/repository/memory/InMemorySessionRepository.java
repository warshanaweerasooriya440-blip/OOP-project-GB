/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;


import com.feedback.admin.domain.FeedbackSession;
import com.feedback.admin.repository.SessionRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Keeps sessions in memory, so the Admin Panel works without a database. */
public class InMemorySessionRepository implements SessionRepository {
    private final Map<String, FeedbackSession> data = new LinkedHashMap<>();

    @Override
    public void save(FeedbackSession session) {
        data.put(session.getSessionId(), session);
    }

    @Override
    public Optional<FeedbackSession> findById(String id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<FeedbackSession> findAll() {
        return new ArrayList<>(data.values());
    }
}
