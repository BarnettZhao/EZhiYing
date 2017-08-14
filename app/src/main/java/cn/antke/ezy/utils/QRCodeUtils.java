package cn.antke.ezy.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Liu_Zhichao on 2016/7/5 17:35.
 * 条形码、二维码处理
 */
public final class QRCodeUtils {

	private static final int WHITE = 0xFFFFFFFF;//两种码显示的背景色
	private static final int BLACK = 0xFF000000;//两种码显示的前景色

	public static void setImageView(ImageView imageView, String code, BarcodeFormat format, int width, int height) {
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(code);
		if (encoding != null) {
			hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}

		BitMatrix result = null;
		try {
			result = new MultiFormatWriter().encode(code, format, width, height, hints);
		} catch (IllegalArgumentException iae) {
			return;
		} catch (WriterException | NullPointerException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		width = result.getWidth();
		height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		if (bitmap == null){
			return;
		}
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}

	public static void setQvImageView(ImageView imageView, String code, BarcodeFormat format, int width, int height) {
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(code);
		if (encoding != null) {
			hints = new EnumMap<>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}

		BitMatrix result = null;
		try {
			result = new MultiFormatWriter().encode(code, format, width, height, hints);
		} catch (IllegalArgumentException iae) {
			return;
		} catch (WriterException e) {
			e.printStackTrace();
		}
		if (result == null)
			return;
		width = result.getWidth();
		height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		imageView.setImageBitmap(bitmap);
	}

	/**
	 * 获取合适的编码方式
	 */
	private static String guessAppropriateEncoding(CharSequence contents) {
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF)
				return "UTF-8";
		}
		return null;
	}
}
