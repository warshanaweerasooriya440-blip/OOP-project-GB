/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.config;



import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Settings for the Admin Panel. They can be read from a properties file, so nothing is
 * hard-coded (non-functional requirement). Missing keys fall back to the defaults.
 */
public class AdminConfig {
    private final String baseUrl;
    private final int maxCodesPerBatch;
    private final int codeLength;

    public AdminConfig(String baseUrl, int maxCodesPerBatch, int codeLength) {
        this.baseUrl = baseUrl;
        this.maxCodesPerBatch = maxCodesPerBatch;
        this.codeLength = codeLength;
    }

    public static AdminConfig defaults() {
        return new AdminConfig("http://localhost:8080", 1000, 10);
    }

    /** Reads config/admin.properties (or any file). Never put passwords in it and commit it. */
    public static AdminConfig load(Path file) throws IOException {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            p.load(in);
        }
        AdminConfig d = defaults();
        return new AdminConfig(
                read(p, "admin.baseUrl", d.baseUrl),
                Integer.parseInt(read(p, "admin.maxCodesPerBatch", String.valueOf(d.maxCodesPerBatch))),
                Integer.parseInt(read(p, "admin.codeLength", String.valueOf(d.codeLength))));
    }

    /** Reads one setting and removes spaces around it (a trailing space would break the number). */
    private static String read(Properties p, String key, String fallback) {
        return p.getProperty(key, fallback).trim();
    }

    public String getBaseUrl() { return baseUrl; }
    public int getMaxCodesPerBatch() { return maxCodesPerBatch; }
    public int getCodeLength() { return codeLength; }
}