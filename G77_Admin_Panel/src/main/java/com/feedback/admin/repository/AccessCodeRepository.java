/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.repository;



import java.util.List;

/**
 * Stores access code HASHES for each session. There is deliberately no method that returns
 * who used a code or when. markUsedByHash is what G8's Annonimity and G69 call when a student
 * enters a code.
 */
public interface AccessCodeRepository {
    void saveHashes(String sessionId, List<String> hashes);

    int countTotal(String sessionId);

    int countUsed(String sessionId);

    /** @return true if the hash existed and was unused (it is now used), otherwise false. */
    boolean markUsedByHash(String hash);
}
