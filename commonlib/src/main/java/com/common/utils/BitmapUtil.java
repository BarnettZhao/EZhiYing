package com.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

/**
 * Created by jacktian on 15/11/25.
 */
public class BitmapUtil {

	public static final String IMAGE_CACHE_DIR = "image";

	//计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示
	public static Bitmap getSmallBitmap(String filePath, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 把bitmap转换成String
	 */
	public static String bitmapToString(String filePath, int reqWidth, int reqHeight) {

		Bitmap bm = getSmallBitmap(filePath, reqWidth, reqHeight);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/**
	 * 获取图片角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 *
	 * @degress 为角度值
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	/**
	 * 翻转图片
	 * @param isVertical true为垂直翻转 false为水平翻转
	 */
	public static Bitmap flipBitmap(Bitmap bitmap, boolean isVertical) {
		if (bitmap != null) {
			Matrix m = new Matrix();
			if (isVertical) {
				m.postScale(1, -1);
			} else {
				m.postScale(-1, 1);
			}
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		}
		return bitmap;
	}

	/**
	 * @return 将字节数组转换为Bitmap
	 */
	public static Bitmap byteToBitmap(byte[] imgByte, int scale) {
		InputStream input;
		Bitmap bitmap;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = scale;//原来的1/scale，只能是2的整数次幂
		input = new ByteArrayInputStream(imgByte);
		SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(input, null, options));
		bitmap = (Bitmap) softRef.get();
		try {
			if (input != null) {
				input.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 压缩图片大小：降低质量，宽高不变
	 * @param bitmap=压缩图片,size=压缩后图片大小<size
	 */
	public static Bitmap revitionImageSize(Bitmap bitmap, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > size) {
			baos.reset();
			options -= 10;
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return bitmap;
	}

	/**
	 * 压缩图片大小
	 *
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionImageSize(String path, int width, int height) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap;
		while (true) {
			if ((options.outWidth >> i <= width) && (options.outHeight >> i <= height)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static String revitionLocalImage(Context context, String path) {
		String newPath = null;

		try {
			newPath = saveBitmap(context, 90, revitionImageSize(path, 200, 200));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newPath;
	}

	/**
	 * 保存方法
	 */
	public static String saveBitmap(Context context, int quality, Bitmap bitmap) {
		File file = FileUtil.getDiskCacheFile(context, IMAGE_CACHE_DIR);
		file = new File(file.getPath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
		String path = file.getPath();
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return path;
	}


	private BitmapUtil() {

	}
}
