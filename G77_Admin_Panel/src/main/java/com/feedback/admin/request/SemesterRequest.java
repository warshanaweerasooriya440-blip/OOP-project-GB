/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.request;

/** The data the Administrator types in to create a semester. */
public final class SemesterRequest {
    private final int year;
    private final int semesterNumber;

    public SemesterRequest(int year, int semesterNumber) {
        this.year = year;
        this.semesterNumber = semesterNumber;
    }

    public int getYear() { return year; }
    public int getSemesterNumber() { return semesterNumber; }
}
