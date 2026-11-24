/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;

import com.feedback.admin.exception.ValidationException;

/** One academic semester, for example "2026 Semester 1". */
public final class Semester {
    private final int year;
    private final int semesterNumber;
    private final String label;

    public Semester(int year, int semesterNumber) {
        if (year < 2000 || year > 2100) {
            throw new ValidationException("Year must be between 2000 and 2100");
        }
        if (semesterNumber < 1 || semesterNumber > 2) {
            throw new ValidationException("Semester number must be 1 or 2");
        }
        this.year = year;
        this.semesterNumber = semesterNumber;
        this.label = year + " Semester " + semesterNumber;
    }

    public int getYear() { return year; }
    public int getSemesterNumber() { return semesterNumber; }
    public String getLabel() { return label; }
}