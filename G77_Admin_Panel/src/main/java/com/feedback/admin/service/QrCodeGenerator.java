/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.feedback.admin.service;


/** Turns a link into a QR code image (as bytes). */
public interface QrCodeGenerator {
    byte[] generate(String link);
}