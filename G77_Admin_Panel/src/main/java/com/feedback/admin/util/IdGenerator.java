/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/** Makes readable ids such as SES-0001. */
public final class IdGenerator {
    private static final Map<String, AtomicLong> COUNTERS = new ConcurrentHashMap<>();

    private IdGenerator() { }

    /** @return the next id for the given prefix, for example "SES" gives SES-0001. */
    public static String next(String prefix) {
        long n = COUNTERS.computeIfAbsent(prefix, k -> new AtomicLong()).incrementAndGet();
        return String.format("%s-%04d", prefix, n);
    }
}