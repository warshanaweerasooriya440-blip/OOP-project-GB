/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.request;


import java.time.LocalDate;

/** The data the Administrator types in to create a feedback session. */
public final class SessionRequest {
    private final String moduleCode;
    private final int year;
    private final int semesterNumber;
    private final String licId;
    private final LocalDate openDate;
    private final LocalDate closeDate;
    private final int enrolmentCount;
    private final int minResponseThreshold;

    public SessionRequest(String moduleCode, int year, int semesterNumber, String licId,
                          LocalDate openDate, LocalDate closeDate,
                          int enrolmentCount, int minResponseThreshold) {
        this.moduleCode = moduleCode;
        this.year = year;
        this.semesterNumber = semesterNumber;
        this.licId = licId;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.enrolmentCount = enrolmentCount;
        this.minResponseThreshold = minResponseThreshold;
    }

    public String getModuleCode() { return moduleCode; }
    public int getYear() { return year; }
    public int getSemesterNumber() { return semesterNumber; }
    public String getLicId() { return licId; }
    public LocalDate getOpenDate() { return openDate; }
    public LocalDate getCloseDate() { return closeDate; }
    public int getEnrolmentCount() { return enrolmentCount; }
    public int getMinResponseThreshold() { return minResponseThreshold; }
}