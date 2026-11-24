/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Makes random codes with SecureRandom and hashes them with SHA-256. Letters and digits that
 * look alike (0/O, 1/I) are left out, so students can type the codes easily.
 */
public class SecureCodeGenerator implements CodeGenerator {
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private final SecureRandom random = new SecureRandom();
    private final int length;

    /** @param length number of characters in a code (without the dash), at least 6 */
    public SecureCodeGenerator(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Code length must be at least 6");
        }
        this.length = length;
    }

    @Override
    public String newCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i > 0 && i % 5 == 0) {
                sb.append('-');
            }
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    @Override
    public String hash(String code) {
        String normal = code == null ? "" : code.replace("-", "").replace(" ", "");
        normal = normal.toUpperCase(Locale.ROOT);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(normal.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
