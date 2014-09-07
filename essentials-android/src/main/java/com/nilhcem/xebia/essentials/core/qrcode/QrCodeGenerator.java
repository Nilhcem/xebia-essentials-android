package com.nilhcem.xebia.essentials.core.qrcode;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import timber.log.Timber;

public class QrCodeGenerator {

    public Bitmap generate(String toConvert, int qrCodeSize) {
        // Increase QrCode size (otherwise, generated code looks too small)
        int size = qrCodeSize * 3;
        Bitmap qrcode = null;
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix matrix = writer.encode(toConvert, BarcodeFormat.QR_CODE, size, size);
            qrcode = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            int[] pixels = new int[size * size];
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    pixels[(y * size) + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            qrcode.setPixels(pixels, 0, size, 0, 0, size, size);
        } catch (WriterException e) {
            Timber.e(e, "Error generating QR Code");
        }
        return qrcode;
    }
}
