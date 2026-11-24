/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.service;


/** Makes access codes and their hashes. Only the hash is stored, so codes stay anonymous. */
public interface CodeGenerator {
    String newCode();
    String hash(String code);
}