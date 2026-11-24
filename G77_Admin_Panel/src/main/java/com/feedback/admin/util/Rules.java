/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.util;

import java.util.regex.Pattern;

/** Validation constants and small checks shared by the domain classes and the validator. */
public final class Rules {
    /** Module code such as EC1022. */
    public static final Pattern MODULE_CODE = Pattern.compile("^[A-Z]{2,4}[0-9]{3,4}$");
    /** Simple e-mail format check. */
    public static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final int MAX_NAME = 100;
    public static final int MAX_TEXT = 300;
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 4;

    private Rules() { }

    /** @return true if the text is null or contains only spaces. */
    public static boolean blank(String s) {
        return s == null || s.isBlank();
    }
}
