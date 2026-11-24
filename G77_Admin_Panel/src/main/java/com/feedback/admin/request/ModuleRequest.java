/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.request;


/** The data the Administrator types in to create a module. */
public final class ModuleRequest {
    private final String code;
    private final String name;
    private final int level;

    public ModuleRequest(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getLevel() { return level; }
}