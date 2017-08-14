package com.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件处理工具类
 */
public class FileUtil {

	/**
	 * 获取应用缓存地址根目录
	 */
	public static File getDiskCacheFile(Context context, String cacheDir) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir().getPath();
		File file = new File(cachePath + File.separator + cacheDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return new File(cachePath + File.separator + cacheDir);
	}

	/**
	 * 压缩图片大小：降低质量，宽高不变
	 * @param file=压缩图片,size=压缩后图片大小<size
	 */
	public static void compressBmpToFile(File file, int size) {
		try {
			FileInputStream fis = new FileInputStream(file);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int options = 100;// 从80开始,
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			while (baos.toByteArray().length / 1024 > size) {
				baos.reset();
				options -= 10;
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			try {
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(baos.toByteArray());
				bitmap.recycle();
				fos.flush();
				fos.close();
				fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 压缩图片大小:改变图片宽高压缩图片大小
	 * @param path=图片路径
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

	public static boolean isExternalStorageRemovable() {
		return Environment.isExternalStorageRemovable();
	}

	public static File getExternalCacheDir(Context context) {
		return context.getExternalCacheDir();
	}

	public static void saveBitmap(Bitmap bm, String cpPath, String picName) {
		try {
			File f = new File(cpPath, picName + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除目录下所有文件
	 */
	public static long deleteDir(File file) {
		long size = 0;
		if (file.exists() && file.isDirectory()) {
			for (File f : file.listFiles()) {
				if (f.isFile()) {
					long space = f.getTotalSpace();
					boolean delete = f.delete();// 删除所有文件
					if (delete) size += space;
				} else if (f.isDirectory()) {
					deleteDir(f); // 递规的方式删除文件夹
				}
			}
			//f.delete();// 删除目录本身
		}
		return size;
	}

	/**
	 * 根据文件路径返回File对象
	 */
	public static List<File> getFileByPath(List<String> paths) {
		List<File> files = new ArrayList<>();
		if (paths != null && paths.size() > 0) {
			for (String path : paths) {
				if (!TextUtils.isEmpty(path)) {
					File file = new File(path);
					if (file.exists()) {
						files.add(file);
					}
				}
			}
		}
		return files;
	}

	/**
	 * 根据文件路径返回InputStream对象
	 */
	public static List<InputStream> getInputStreamByPath(List<String> paths) {
		List<InputStream> files = new ArrayList<>();
		try {
			if (paths != null && paths.size() > 0) {
				for (String path : paths) {
					if (!TextUtils.isEmpty(path)) {
						File file = new File(path);
						if (file.exists()) {
							files.add(new FileInputStream(file));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}

	/**
	 * 将Bitmap保存到文件
	 */
	public static void saveBitmapToFile(Bitmap bm, String filePath) {
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从本地文件返回Bitmap
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri) {
		try {
			if (uri == null) {
				return null;
			}
			// 读取uri所在的图片
			return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
		} catch (Exception e) {
			Log.e("[Android]", e.getMessage());
			Log.e("[Android]", "目录为：" + uri);
			e.printStackTrace();
			return null;
		}
	}
}
