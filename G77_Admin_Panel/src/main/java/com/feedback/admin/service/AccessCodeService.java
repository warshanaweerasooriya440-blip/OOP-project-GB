/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;



import com.feedback.admin.config.AdminConfig;
import com.feedback.admin.domain.AccessCodeBatch;
import com.feedback.admin.domain.CodeSummary;
import com.feedback.admin.domain.FeedbackSession;
import com.feedback.admin.domain.SessionStatus;
import com.feedback.admin.exception.SessionStateException;
import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.repository.AccessCodeRepository;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Makes single-use access codes and the session QR code (FR9, Section 8).
 * Anonymity rules: only HASHES are stored, the plain codes are returned once, the hashes are
 * shuffled before saving, and the summary shows counts only.
 */
public class AccessCodeService {
    private final AccessCodeRepository codes;
    private final SessionService sessionService;
    private final CodeGenerator generator;
    private final QrCodeGenerator qr;
    private final AdminConfig config;
    private final SecureRandom random = new SecureRandom();

    public AccessCodeService(AccessCodeRepository codes, SessionService sessionService,
                             CodeGenerator generator, QrCodeGenerator qr, AdminConfig config) {
        this.codes = codes;
        this.sessionService = sessionService;
        this.generator = generator;
        this.qr = qr;
        this.config = config;
    }

    /**
     * Makes a batch of codes. The total for a session may not pass its enrolment count,
     * otherwise a student could submit twice.
     * @return the plain codes, to be shown or downloaded ONCE
     */
    public AccessCodeBatch generateBatch(String sessionId, int count) {
        FeedbackSession session = sessionService.find(sessionId);
        if (session.getStatus() == SessionStatus.CLOSED) {
            throw new SessionStateException("Cannot generate codes for a closed session");
        }
        if (count < 1) {
            throw new ValidationException("Number of codes must be greater than 0");
        }
        if (count > config.getMaxCodesPerBatch()) {
            throw new ValidationException(
                    "At most " + config.getMaxCodesPerBatch() + " codes can be made at once");
        }
        int existing = codes.countTotal(sessionId);
        if (existing + count > session.getEnrolmentCount()) {
            throw new ValidationException("Codes would pass the enrolment count (" + existing
                    + " already made, enrolment " + session.getEnrolmentCount() + ")");
        }
        Set<String> plain = new LinkedHashSet<>();
        while (plain.size() < count) {
            plain.add(generator.newCode());
        }
        List<String> hashes = new ArrayList<>();
        for (String code : plain) {
            hashes.add(generator.hash(code));
        }
        Collections.shuffle(hashes, random);
        codes.saveHashes(sessionId, hashes);
        return new AccessCodeBatch(sessionId, new ArrayList<>(plain));
    }

    /** Makes the remaining codes, up to the enrolment count. */
    public AccessCodeBatch generateBatch(String sessionId) {
        FeedbackSession session = sessionService.find(sessionId);
        int remaining = session.getEnrolmentCount() - codes.countTotal(sessionId);
        if (remaining < 1) {
            throw new ValidationException("All codes for this session were already generated");
        }
        return generateBatch(sessionId, remaining);
    }

    /** @return counts only: made, used and unused. Never who used a code or when. */
    public CodeSummary getSummary(String sessionId) {
        sessionService.find(sessionId);
        return new CodeSummary(codes.countTotal(sessionId), codes.countUsed(sessionId));
    }

    /** @return the link students open. It is the same for everyone and holds no code. */
    public String getSessionLink(String sessionId) {
        sessionService.find(sessionId);
        return config.getBaseUrl() + "/feedback?session=" + sessionId;
    }

    public byte[] getQr(String sessionId) {
        return qr.generate(getSessionLink(sessionId));
    }
}
