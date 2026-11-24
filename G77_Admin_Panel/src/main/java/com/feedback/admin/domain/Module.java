/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.domain;


import com.feedback.admin.exception.ValidationException;
import com.feedback.admin.util.Rules;

/** One course module, for example EC1022. A module is deactivated instead of deleted. */
public final class Module {
    private final String code;
    private String name;
    private int level;
    private boolean active = true;

    public Module(String code, String name, int level) {
        if (code == null || !Rules.MODULE_CODE.matcher(code).matches()) {
            throw new ValidationException("Module code must look like EC1022");
        }
        this.code = code;
        setName(name);
        setLevel(level);
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getLevel() { return level; }

    public void setName(String name) {
        if (Rules.blank(name) || name.length() > Rules.MAX_NAME) {
            throw new ValidationException("Module name is required (max " + Rules.MAX_NAME + " characters)");
        }
        this.name = name.trim();
    }

    public void setLevel(int level) {
        if (level < Rules.MIN_LEVEL || level > Rules.MAX_LEVEL) {
            throw new ValidationException("Level must be between " + Rules.MIN_LEVEL + " and " + Rules.MAX_LEVEL);
        }
        this.level = level;
    }

    public void deactivate() { active = false; }
    public boolean isActive() { return active; }
}