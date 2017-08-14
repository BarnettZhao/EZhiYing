package com.common.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author tianzhenhai
 */
public class StringUtil {
	/**
	 * Native to ascii string. It's same as execut native2ascii.exe.
	 *
	 * @param str native string
	 * @return ascii string
	 */
	public static String native2Ascii(String str) {
		char[] chars = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			sb.append(char2Ascii(chars[i]));
		}
		return sb.toString();
	}

	/**
	 * Native character to ascii string.
	 *
	 * @param c native character
	 * @return ascii string
	 */
	private static String char2Ascii(char c) {
		if (c > 255) {
			StringBuilder sb = new StringBuilder();
			sb.append("\\u");
			int code = (c >> 8);
			String tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			code = (c & 0xFF);
			tmp = Integer.toHexString(code);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			return sb.toString();
		} else {
			return Character.toString(c);
		}
	}

	/**
	 * Ascii to native string. It's same as execut native2ascii.exe -reverse.
	 *
	 * @param str ascii string
	 * @return native string
	 */
	public static String ascii2Native(String str) {
		StringBuilder sb = new StringBuilder();
		int begin = 0;
		int index = str.indexOf("\\u");
		while (index != -1) {
			sb.append(str.substring(begin, index));
			sb.append(ascii2Char(str.substring(index, index + 6)));
			begin = index + 6;
			index = str.indexOf("\\u", begin);
		}
		sb.append(str.substring(begin));
		return sb.toString();
	}

	/**
	 * Ascii to native character.
	 *
	 * @param str ascii string
	 * @return native character
	 */
	private static char ascii2Char(String str) {
		if (str.length() != 6) {
			throw new IllegalArgumentException("Ascii string of a native character must be 6 character.");
		}
		if (!"\\u".equals(str.substring(0, 2))) {
			throw new IllegalArgumentException("Ascii string of a native character must start with \"\\u\".");
		}
		String tmp = str.substring(2, 4);
		int code = Integer.parseInt(tmp, 16) << 8;
		tmp = str.substring(4, 6);
		code += Integer.parseInt(tmp, 16);
		return (char) code;
	}

	/**
	 * 由于服务器的图片服务器，当返回的图片尺寸超过150像素时会自动添加水印，
	 * 而客户端有些情况下是不允许显示添加了水印的照片的（比如列表页的缩略图），
	 * 所以该方法需要将请求的尺寸控制在150像素以内
	 */
	public static String getClampedSizeImageUrlByArg(String imageUrl, int width, int height, boolean cut) {
		float ratio = (float) width / height;
		int max = Math.max(width, height);
		if (max >= 150) {
			if (width >= height) {
				width = 149;
				height = (int) (width / ratio);
			} else {
				height = 149;
				width = (int) (height * ratio);
			}
		}

		return getImageUrlByArg(imageUrl, width, height, cut);
	}

	public static String getImageUrlByArg(String imageUrl, int width, int height, boolean cut) {
		if (imageUrl == null || imageUrl.length() == 0) {
			return "";
		}

		Pattern pattern = Pattern.compile("_\\d*-\\d*.*_");
		Matcher matcher = pattern.matcher(imageUrl);
		if (matcher.find()) {
			String arg = "_" + width + "-" + height + (cut ? "c_" : "_");
			return matcher.replaceFirst(arg);
		}
		return imageUrl;
	}

	public static String getImageUrlByArgAndQuality(String imageUrl, int width, int height, boolean cut, int quality) {
		if (imageUrl == null || imageUrl.length() == 0) {
			return "";
		}

		Pattern pattern = Pattern.compile("_\\d*-\\d*.*_\\d*-");
		Matcher matcher = pattern.matcher(imageUrl);
		if (matcher.find()) {
			String arg = "_" + width + "-" + height + (cut ? "c_" : "_") + quality + "-";
			return matcher.replaceFirst(arg);
		}
		return imageUrl;
	}

	/**
	 * 获得新的图片URL
	 */
	public static String getNewImageUrl(String imageUrl, int width, int height) {
		if (imageUrl == null || imageUrl.length() == 0) {
			return "";
		}

		Pattern pattern = Pattern.compile("_\\d*-\\d*.*_");
		Matcher matcher = pattern.matcher(imageUrl);
		if (matcher.find()) {
			String get = matcher.group();
			get = get.replaceFirst("(?<=_)([0-9]*)", "" + width).replaceFirst("(?<=-)([0-9]*)", "" + height);
			return matcher.replaceFirst(get);
		} else {
			int index = imageUrl.lastIndexOf('.');
			if (index > 0) {
				String postfix = imageUrl.substring(index);
				String str = imageUrl.substring(0, index);
				return str + "_" + width + "-" + height + "_8-15" + postfix;
			}
		}
		return imageUrl;
	}

	/**
	 * 转换 url, 返回宽为 120 像素的缩略图的 url
	 */
	public static String toThumbUrl(String imageUrl) {
		final String url = imageUrl;
		try {
			if (!TextUtils.isEmpty(imageUrl)) {
				String[] datas = url.split("_");
				if (datas != null && datas.length == 3) {
					String numberFormat = datas[1];
					if (!TextUtils.isEmpty(numberFormat)) {
						String[] numbers = numberFormat.split("-");
						if (numbers != null && numbers.length == 2) {
							StringBuilder sb = new StringBuilder();
							sb.append(datas[0]);
							sb.append("_");
							sb.append("120" + "-" + numbers[1]);
							sb.append("_");
							sb.append(datas[2]);
							return sb.toString();
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return url;
	}

	public static String MD5(String data) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(data.getBytes());
			return bytesToHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
		}
		return data;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static int parseInt(String str, int defaultValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static float parseFloat(String str, float defaultValue) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double parseDouble(String str, float defaultValue) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long parseLong(String str, long defaultValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 当str==null，为一个或者多个空格，为“null”字符串（不区分大小写）时返回true
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str.trim()) || "null".equalsIgnoreCase(str.trim());
	}

	/**
	 * 按照 lengthLimit截取text,如果text长度大于lengthLimit（不包含）,则截取前lengthLimit个字符+
	 * endExtra（表示省略的后缀,如果该后缀为null则默认加上"..."）返回，否则原样返回
	 *
	 * @param text
	 * @param endExtra    默认为"..."
	 * @param lengthLimit
	 * @return
	 */
	public static String trimFromStart(String text, String endExtra, int lengthLimit) {
		if (text == null) {
			return text;
		}
		if (text.length() <= lengthLimit) {
			return text;
		} else {
			return text.substring(0, lengthLimit) + (endExtra == null ? "..." : endExtra);
		}
	}

	/**
	 * 按照 lengthLimit截取text,如果text长度大于lengthLimit（不包含）,则按照preExtra（表示省略的后缀,
	 * 如果该前缀为null则默认为"..."）+截取后lengthLimit个字符返回，否则原样返回
	 *
	 * @param text
	 * @param preExtra    默认为"..."
	 * @param lengthLimit
	 * @return
	 */
	public static String trimFromEnd(String text, String preExtra, int lengthLimit) {
		if (text == null) {
			return text;
		}
		if (text.length() <= lengthLimit) {
			return text;
		} else {
			return (preExtra == null ? "..." : preExtra) + text.substring(text.length() - lengthLimit, text.length());
		}
	}

	/**
	 * 获取格式化后的百分比字符串
	 *
	 * @param max     最高值
	 * @param current 当前值
	 * @return 百分比，如：81%
	 */
	public static String getFormattedPercent(long max, long current) {
		double percent = (double) current / max;
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("0.00%");
		return df.format(percent);
	}

}
