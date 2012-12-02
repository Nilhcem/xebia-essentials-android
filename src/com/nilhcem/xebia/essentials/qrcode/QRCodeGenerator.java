package com.nilhcem.xebia.essentials.qrcode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class QRCodeGenerator {
	private static final Logger LOG = LoggerFactory.getLogger(QRCodeGenerator.class);

	public Bitmap generate(String toConvert, int size) {
		Bitmap qrCode = null;

		QRCodeWriter writer = new QRCodeWriter();
		try {
			BitMatrix matrix = writer.encode(toConvert, BarcodeFormat.QR_CODE, size, size);
			qrCode = Bitmap.createBitmap(size, size, Config.ARGB_8888);
			for (int i = 0; i < size; i++) { // width
				for (int j = 0; j < size; j++) { // height
					qrCode.setPixel(i, j, matrix.get(i, j) ? Color.BLACK : Color.WHITE);
				}
			}
		} catch (WriterException e) {
			LOG.error("", e);
		}
		return qrCode;
	}
}
