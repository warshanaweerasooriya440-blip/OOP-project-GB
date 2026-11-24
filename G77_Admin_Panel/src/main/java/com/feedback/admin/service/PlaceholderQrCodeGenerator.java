/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.feedback.admin.service;

import java.nio.charset.StandardCharsets;

/**
 * Stand-in QR generator. It returns the link as bytes, NOT a real image. Replace it with a
 * real QR library (for example ZXing) before the final demo.
 */
public class PlaceholderQrCodeGenerator implements QrCodeGenerator {
    @Override
    public byte[] generate(String link) {
        return ("QR-PLACEHOLDER:" + link).getBytes(StandardCharsets.UTF_8);
    }
}
