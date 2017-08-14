package com.common.utils;

import com.common.encoder.BASE64Decoder;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by jacktian on 15/11/2.
 */
public class EnCryptionUtils {

	public static final String PAYPWD_SHA1_KEY = "gGxjpjdiV2BOe+C+Y3ZwKA==";
	private static final String CipherMode = "AES/ECB/PKCS5Padding";

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String SHA1(String decript, String key) {
		try {
			if (StringUtil.isEmpty(key)) {
				key = PAYPWD_SHA1_KEY;
			}
			decript = decript + key;
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String SHA(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String hexString(byte[] bytes) {
		StringBuffer hexValue = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			int val = ((int) bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static byte[] eccrypt(String info, String shaType) throws NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance(shaType);
		byte[] srcBytes = info.getBytes();
		//使用srcBytes更新摘要
		sha.update(srcBytes);
		//完成哈希计算，得到result
		byte[] resultBytes = sha.digest();
		return resultBytes;
	}

	public static byte[] eccryptSHA1(String info) throws NoSuchAlgorithmException {
		return eccrypt(info, "SHA1");
	}

	public static byte[] eccryptSHA256(String info) throws NoSuchAlgorithmException {
		return eccrypt(info, "SHA-256");
	}

	public static byte[] eccryptSHA384(String info) throws NoSuchAlgorithmException {
		return eccrypt(info, "SHA-384");
	}

	public static byte[] eccryptSHA512(String info) throws NoSuchAlgorithmException {
		return eccrypt(info, "SHA-512");
	}

	public static String MD5(String input) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(input.getBytes());
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < md.length; i++) {
				String shaHex = Integer.toHexString(md[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	//AES解密
	public static String AES1(String key, String content) throws IllegalBlockSizeException, BadPaddingException, IOException {
		Cipher cipher = buildCipher(key, Cipher.DECRYPT_MODE);
		byte[] result = cipher.doFinal(hexStr2bytes(content));
		return new String(result, "utf8");
	}

	private static Cipher buildCipher(String key, int mode) {
		try {
			byte[] raw = MessageDigest.getInstance("SHA-256").digest(key.getBytes());
			SecretKeySpec skeySpec = new SecretKeySpec(raw, 0, 16, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(raw, 16, 16);
			cipher.init(mode, skeySpec, iv);
			return cipher;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (NoSuchPaddingException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] hexStr2bytes(String hexStr) {
		int size = hexStr.length() / 2;
		byte[] result = new byte[size];
		for (int i = 0; i < size; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	//AES解密法2

	/**
	 * @param sSrc 待解字符串
	 * @param key  密key1
	 * @param ivs  密key2
	 * @throws Exception
	 */
	public static String AES2(String sSrc, String key, String ivs) throws Exception {
		try {
			byte[] raw = key.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(ivs.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, "utf-8");
			return originalString;
		} catch (Exception ex) {
			return null;
		}
	}
}
