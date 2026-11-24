/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;


/** Counts of access codes for a session. It never shows who used a code or when. */
public final class CodeSummary {
    private final int total;
    private final int used;

    public CodeSummary(int total, int used) {
        this.total = total;
        this.used = used;
    }

    public int getTotal() { return total; }
    public int getUsed() { return used; }
    public int getUnused() { return total - used; }
}
