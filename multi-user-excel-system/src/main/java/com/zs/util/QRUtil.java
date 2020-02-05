package com.zs.util;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.decoder.Mode;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

/**
 * 二维码工具类
 * @author MagicBook
 *
 */
@Component
public class QRUtil {
	// 编码格式,采用utf-8
	private static final String UNICODE = "utf-8";
	// 图片格式
	private static final String FORMAT = "JPG";
	// 二维码宽度,单位：像素pixels
	private static final int QRCODE_WIDTH = 300;
	// 二维码高度,单位：像素pixels
	private static final int QRCODE_HEIGHT = 300;

	/**
	 * 文本转二维码输出到输出流
	 * @param msg 需转的文本
	 * @param output 输出流
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void qrCodeToStream(String msg, OutputStream output) throws WriterException, IOException {
		BitMatrix bitMatrix = new MultiFormatWriter().encode(msg, BarcodeFormat.QR_CODE, QRCODE_WIDTH, QRCODE_HEIGHT);
		MatrixToImageWriter.writeToStream(bitMatrix, FORMAT, output);
	}
}
