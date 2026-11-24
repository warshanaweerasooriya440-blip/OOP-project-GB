/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.repository.memory;


import com.feedback.admin.repository.AccessCodeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Keeps code hashes in memory. A plain HashMap is used on purpose, so the stored order does
 * not follow the order in which codes were created or used.
 */
public class InMemoryAccessCodeRepository implements AccessCodeRepository {
    /** session id -> (hash -> used?) */
    private final Map<String, Map<String, Boolean>> data = new HashMap<>();

    @Override
    public void saveHashes(String sessionId, List<String> hashes) {
        for (String hash : hashes) {
            for (Map<String, Boolean> codes : data.values()) {
                if (codes.containsKey(hash)) {
                    throw new IllegalStateException("Duplicate access code hash");
                }
            }
        }
        Map<String, Boolean> codes = data.computeIfAbsent(sessionId, k -> new HashMap<>());
        for (String hash : hashes) {
            codes.put(hash, Boolean.FALSE);
        }
    }

    @Override
    public int countTotal(String sessionId) {
        return data.getOrDefault(sessionId, new HashMap<>()).size();
    }

    @Override
    public int countUsed(String sessionId) {
        int used = 0;
        for (Boolean flag : data.getOrDefault(sessionId, new HashMap<>()).values()) {
            if (flag) {
                used++;
            }
        }
        return used;
    }

    @Override
    public boolean markUsedByHash(String hash) {
        for (Map<String, Boolean> codes : data.values()) {
            Boolean used = codes.get(hash);
            if (used != null) {
                if (used) {
                    return false;
                }
                codes.put(hash, Boolean.TRUE);
                return true;
            }
        }
        return false;
    }
}
