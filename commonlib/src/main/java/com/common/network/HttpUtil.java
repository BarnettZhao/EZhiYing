package com.common.network;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.common.utils.CheckRootUtil;
import com.common.utils.DeviceUtil;
import com.common.utils.EnCryptionUtils;
import com.common.utils.LogUtil;
import com.common.utils.StringUtil;
import com.common.utils.VersionInfoUtils;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okio.Buffer;

import static com.common.network.HttpUtil.Config.APP_TYPE;
import static com.common.network.HttpUtil.Config.INTERFACE_SIGN_KEY;
import static com.common.network.HttpUtil.Config.INTERFACE_VERSION;

/**
 * Created by jacktian on 15/8/19.
 * 网络请求
 */
public class HttpUtil {

	private static final String USER_AGENT = "BCT Android Client v1.0";
	private static final String MULTIPART_DATA_NAME = "files";
	public static final String TAG = HttpUtil.class.getSimpleName();

	private static OkHttpClient client;

	/**
	 * get方式
	 *
	 * @param url 请求地址
	 * @return 响应结果
	 */
	public static String httpGet(Context context, String url) {
		if (url != null && url.contains("?")) {
			if (LogUtil.isDebug) Log.e(TAG, url);

			Request request = new Request.Builder()
					.addHeader("User-Agent", USER_AGENT)
					.url(url)
					.build();
			Response response;
			try {
				response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			} catch (IOException e) {
				LogUtil.e(TAG, e.toString());
			}
		}
		return "";
	}

	/**
	 * get方式
	 *
	 * @param url 请求地址
	 * @return 响应结果
	 */
	public static String httpGet(String url, String lastModified, Context context) {
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		if (url != null && url.contains("?")) {
			Uri uri = Uri.parse(url);
			String uriScheme = uri.getScheme();
			String uriAuthority = uri.getAuthority();
			String uriPath = uri.getPath();
			for (String key : uri.getQueryParameterNames()) {
				params.put(key, uri.getQueryParameter(key));
			}
			params = securityCheckParams(context, params, uriPath);
			url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH
					+ "?" + String.format("%s=%s", Config.INTERFACE_NAME, params.get(Config.INTERFACE_NAME));

			Request request = new Request.Builder()
					.addHeader("User-Agent", USER_AGENT)
					.addHeader("If-Modified-Since", lastModified)
					.url(url)
					.build();

			Response response;
			try {
				response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					LastModified.saveLastModified(context, url.hashCode() + "", response.header("Last-Modified"));
					return response.body().string();
				}
			} catch (IOException e) {
				LogUtil.e(TAG, e.toString());
			}
		}
		return "";
	}

	/**
	 * post方式
	 *
	 * @param url            请求地址
	 * @param postParameters 请求参数
	 * @return 响应结果
	 */
	public static String httpPost(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			if (postParameters == null) {
				postParameters = new IdentityHashMap<>();
			}
			if (url != null) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();

				for (String key : uri.getQueryParameterNames()) {
					postParameters.put(key, uri.getQueryParameter(key));
				}
				if (uriPath.startsWith("/")) {
					uriPath = uriPath.substring(1);
				}
				//没有值的不加入请求体也不参与签名
				IdentityHashMap<String, String> finalParameters = new IdentityHashMap<>();
				for (Map.Entry<String, String> entry : postParameters.entrySet()) {
					if (!TextUtils.isEmpty(entry.getValue())) {
						finalParameters.put(entry.getKey(), entry.getValue());
					}
				}
				finalParameters = securityCheckParams(context, finalParameters, uriPath);
				url = uriScheme + "://" + uriAuthority + Config.INTERFACE_PATH;
				if (LogUtil.isDebug) Log.e(TAG, url);
				String requestJson = new GsonBuilder().create().toJson(finalParameters);
				RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestJson);
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.post(requestBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();

				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e(TAG, "request error " + e.toString());
		}
		return "";
	}

	/**
	 * post方式传输多个文件
	 *
	 * @param url            请求地址
	 * @param postParameters 请求参数
	 * @param file           多个文件
	 * @return 响应结果
	 */
	public static String httpPost(Context context, String url, IdentityHashMap<String, String> postParameters, String streamName, File file) {
		try {
			if (file.exists() && file.isFile() && url != null) {
				FileNameMap fileNameMap = URLConnection.getFileNameMap();
				String type = fileNameMap.getContentTypeFor(file.getAbsolutePath());
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				postParameters = securityCheckParamsSign(context, postParameters);
//				url = uriScheme + "://" + uriAuthority + "/";
				Set<String> set = postParameters.keySet();
				MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
				for (String key : set) {
					multipartBuilder.addFormDataPart(key, postParameters.get(key));
				}
				if (StringUtil.isEmpty(streamName)) {
					streamName = MULTIPART_DATA_NAME;
				}
				multipartBuilder.addFormDataPart(streamName, file.getName(), RequestBody.create(MediaType.parse(type), file));
				RequestBody requestBody = multipartBuilder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.post(requestBody)
						.build();
				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}


	/**
	 * put方式
	 *
	 * @param url            请求地址
	 * @param postParameters 请求参数
	 * @return 响应结果
	 */
	public static String httpPut(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (postParameters == null) {
				postParameters = new IdentityHashMap<>();
			}
			if (url != null && url.contains("?")) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				for (String key : uri.getQueryParameterNames()) {
					postParameters.put(key, uri.getQueryParameter(key));
				}
				url = uriScheme + "://" + uriAuthority;
				postParameters = securityCheckParams(context, postParameters, uriPath);
				Set<String> set = postParameters.keySet();
				for (String key : set) {
					builder.add(key, Uri.encode(postParameters.get(key), "UTF-8"));
				}
				RequestBody formBody = builder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.put(formBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e("HttpUtil", e.toString());
		}
		return "";

	}

	/**
	 * delete方式
	 *
	 * @param url 请求地址
	 * @return 响应结果
	 */
	public static String httpDelete(Context context, String url, IdentityHashMap<String, String> postParameters) {
		try {
			FormEncodingBuilder builder = new FormEncodingBuilder();
			if (postParameters == null) {
				postParameters = new IdentityHashMap<>();
			}
			if (url != null && url.contains("?")) {
				Uri uri = Uri.parse(url);
				String uriScheme = uri.getScheme();
				String uriAuthority = uri.getAuthority();
				String uriPath = uri.getPath();
				for (String key : uri.getQueryParameterNames()) {
					postParameters.put(key, uri.getQueryParameter(key));
				}
				url = uriScheme + "://" + uriAuthority;
				postParameters = securityCheckParams(context, postParameters, uriPath);
				Set<String> set = postParameters.keySet();
				for (String key : set) {
					builder.add(key, Uri.encode(postParameters.get(key), "UTF-8"));
				}
				RequestBody formBody = builder.build();
				Request request = new Request.Builder()
						.addHeader("User-Agent", USER_AGENT)
						.url(url)
						.method("DELETE", formBody)
						.build();

				Response response = getCustomClient().newCall(request).execute();
				if (response.isSuccessful()) {
					return response.body().string();
				}
			}
		} catch (Exception e) {
			LogUtil.e("HttpUtil", e.toString());
		}
		return "";
	}

	/**
	 * 数据安全检查
	 *
	 * @param params 请求参数
	 */
	private static IdentityHashMap<String, String> securityCheckParams(Context context, IdentityHashMap<String, String> params, String method) {
		if (params == null)
			params = new IdentityHashMap<>();

//		params.put("deviceVersion", Build.VERSION.RELEASE);
//		params.put("buildModel", Build.MODEL);
//		params.put("appVersion", VersionInfoUtils.getVersion(context));
		params.put("service", method);
		params.put("sign_type", "MD5");
		params.put("system_id", "12");
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("version", INTERFACE_VERSION);
		params.put("terminal", APP_TYPE);
		params.put(Config.INTERFACE_NAME, getSidParam(params));
		return params;
	}

	/**
	 * 文件上传时的数据安全检查
	 *
	 * @param params 请求参数
	 */
	private static IdentityHashMap<String, String> securityCheckParamsSign(Context context, IdentityHashMap<String, String> params) {
		if (params == null)
			params = new IdentityHashMap<>();

		params.put("buildModel", Build.MODEL);
		params.put("appType", APP_TYPE);
		params.put("deviceId", getDeviceInfo(context));
		params.put("deviceVersion", Build.VERSION.RELEASE);
		params.put("appVersion", VersionInfoUtils.getVersion(context));
		params.put("sign", getSignParam(params));
		params.put("sysVersion", INTERFACE_VERSION);
		return params;
	}

	private static String getDeviceInfo(Context context) {
		StringBuilder deviceStr = new StringBuilder();
		deviceStr.append(CheckRootUtil.isDeviceRooted());
		deviceStr.append(",");
		deviceStr.append(DeviceUtil.getDeviceIdData(context));
		deviceStr.append(",");
		deviceStr.append(DeviceUtil.getAndroidId(context));
		deviceStr.append(",");
		deviceStr.append(DeviceUtil.getBluetoothMac(context));
		deviceStr.append(",");
		deviceStr.append(DeviceUtil.getBootSerialno(context));
		deviceStr.append(",");
		return deviceStr.toString();
	}

	private static String getSignParam(IdentityHashMap<String, String> params) {
		StringBuilder strParams = new StringBuilder();
		String result = "";
		//先将参数以其参数名的字典序升序进行排序
		if (params != null) {
			List<Map.Entry<String, String>> infoIds = new ArrayList<>(params.entrySet());
			//排序

			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});

			//遍历排序后的参数数组中的每一个key/value对
			for (Map.Entry<String, String> entry : infoIds) {
				if (LogUtil.isDebug) Log.e(TAG, "getSignParam entry : " + entry);
				String value = entry.getValue();
				if (StringUtil.isEmpty(value)) {
					value = "";
				}
				strParams.append(String.format("%s%s", entry.getKey(), Uri.encode(value, "UTF-8")));
			}

			try {
				if (LogUtil.isDebug) Log.e(TAG, "getSignParam value : " + strParams.toString().toLowerCase());
				result = Base64.encodeToString(
						EnCryptionUtils.hexString(EnCryptionUtils.eccryptSHA1(strParams.toString().toLowerCase())).getBytes(),
						Base64.DEFAULT).trim();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		if (LogUtil.isDebug) Log.e(TAG, "getSignParam result : " + result);
		return result;
	}

	private static String getSidParam(IdentityHashMap<String, String> params) {
		StringBuilder str = new StringBuilder(); //待签名字符串
		String result = "";
		//先将参数以其参数名的字典序升序进行排序
		if (params != null) {
			List<Map.Entry<String, String>> infoIds = new ArrayList<>(params.entrySet());

			//排序
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});

			//遍历排序后的参数数组中的每一个key/value对
			for (Map.Entry<String, String> entry : infoIds) {
				if (LogUtil.isDebug) Log.e(TAG, "entry : " + entry);
				String value = entry.getValue();
				if (StringUtil.isEmpty(value)) {
					value = "";
				}
				str.append(value);
			}
			str.append(INTERFACE_SIGN_KEY);
			result = EnCryptionUtils.MD5(str.toString());

			if (LogUtil.isDebug) Log.e(TAG, str.toString());
		}
		return result;
	}

	/**
	 * 初始化网络请求Client对象
	 */
	private static OkHttpClient getCustomClient() {
		if (client == null) {
			synchronized (HttpUtil.class) {
				if (client == null) {
					client = new OkHttpClient();

					if (LogUtil.isDebug) {
						client.networkInterceptors().add(new StethoInterceptor());
					}
					//添加https支持
//					setCertificateForString();//双向
//					client.setCertificatePinner(new CertificatePinner.Builder().add(Config.DOMAIN_NAME, "sha256/" + Config.HTTPS_SHA256).build());//单向
					client.setConnectTimeout(10, TimeUnit.SECONDS);
					client.setWriteTimeout(30, TimeUnit.SECONDS);
					client.setReadTimeout(30, TimeUnit.SECONDS);
					client.setHostnameVerifier(new HostnameVerifier() {
						@Override
						public boolean verify(String hostname, SSLSession session) {
							Log.e(TAG, "hostname : " + hostname + " session : " + session.getPeerHost() + " " + session.getProtocol()
									+ " : " + session.getCipherSuite()
//                                    + " : " + session.getValueNames()[0]
									+ (hostname.equals(session.getPeerHost())));
							try {
								for (Certificate certificate : session.getPeerCertificates()) {
									try {
										Log.e(TAG, "public key : " + certificate.getPublicKey()
												+ " type : " + certificate.getType()
												+ " SubjectAlternativeName : " + ((X509Certificate) certificate).getSubjectAlternativeNames()
												+ " OID : " + ((X509Certificate) certificate).getSigAlgOID());
									} catch (CertificateParsingException e) {
										e.printStackTrace();
									}
								}
							} catch (SSLPeerUnverifiedException e) {
								e.printStackTrace();
							}
							return true;
						}
					});
				}
			}
		}
		return client;
	}

	/**
	 * 使用证书文件
	 */
	public static void setCertificates(Context context, InputStream... certificates) {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			for (InputStream certificate : certificates) {
				String certificateAlias = Integer.toString(index++);
				keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

				try {
					if (certificate != null)
						certificate.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.
					getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(keyStore);

			//初始化keystore
			KeyStore clientKeyStore = KeyStore.getInstance("BKS");
			clientKeyStore.load(context.getAssets().open("zhy_client.bks"), "123456".toCharArray());

			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(clientKeyStore, "123456".toCharArray());

			sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
			getCustomClient().setSslSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setCertificateForString() {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			InputStream certificate = new Buffer()
					.writeUtf8(Config.HTTPS_SHA256)
					.inputStream();
			String certificateAlias = Integer.toString(index++);
			keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

			try {
				if (certificate != null)
					certificate.close();
			} catch (IOException e) {
				Log.e(TAG, "IOException : " + e.toString());
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			client.setSslSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			Log.e(TAG, "e : " + e.toString());
		}
	}

	public void setCertificates(InputStream... certificates) {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			for (InputStream certificate : certificates) {
				String certificateAlias = Integer.toString(index++);
				keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

				try {
					if (certificate != null)
						certificate.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			client.setSslSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class Config {
		//接口客户端编号
		public static final String APP_TYPE = "10";
		//域名
		public static final String DOMAIN_NAME = "ihujia.com";
		// 接口版本
		public static final String INTERFACE_VERSION = "1.0";
		//接口签名key
		public static final String INTERFACE_SIGN_KEY = "550a63dbba6933dfef5d6071083abc160ae7b45aaeff676527482f88ec6d3191";
		//接口参数名称
		public static final String INTERFACE_NAME = "sign";
		//接口初始path
		public static final String INTERFACE_PATH = "/interface/dateService.do";
		//HTTPS证书的sha256加密串
		public static final String HTTPS_SHA256 =
				"MIIGEDCCBPigAwIBAgIQVCPDb8OzSdZ6VeCEkQzzCTANBgkqhkiG9w0BAQsFADBS\n" +
						"MQswCQYDVQQGEwJDTjEaMBgGA1UEChMRV29TaWduIENBIExpbWl0ZWQxJzAlBgNV\n" +
						"BAMTHldvU2lnbiBDbGFzcyAzIE9WIFNlcnZlciBDQSBHMjAeFw0xNjA3MjkwMzE0\n" +
						"MzhaFw0xNzA3MjkwMzE0MzhaMIGGMQswCQYDVQQGEwJDTjESMBAGA1UECAwJ5YyX\n" +
						"5Lqs5biCMRIwEAYDVQQHDAnljJfkuqzluIIxOTA3BgNVBAoMMOS4h+mbhuiejeWQ\n" +
						"iOS/oeaBr+aKgOacr++8iOWMl+S6rO+8ieaciemZkOWFrOWPuDEUMBIGA1UEAwwL\n" +
						"Ki53amlrYS5jb20wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCBffTr\n" +
						"2gCfuYfxPhSpEB8AMaRsCVeusEAcMpBKGjAkuNT+iD57W6uK0WLQL3EjVshivdXL\n" +
						"v3eWoJValhzobqfYGOds4Or93qA/d1Fz2QLA4Dna/IbJmYCJ2mu8prk1THofA5hb\n" +
						"2YBhhfIWdgAEHF0rRwjM5iyknZZmN7pw5INIVb/GQH74qPt/bC3sHlUWIUC10Pig\n" +
						"1SYUoZwZVHrvmitobz6EjJldoqk7FcWm4bBQt/86VmcRuWwofahW/CR0VS61MrMa\n" +
						"ALNlCG+qBMHf6HjWcoC/L7U9FIcdQkpbI7ccGTOKrYeLwk0vmC8DKVvsALoS75P/\n" +
						"O026VWpBQZGxn6A5AgMBAAGjggKrMIICpzAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0l\n" +
						"BBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMBMAkGA1UdEwQCMAAwHQYDVR0OBBYEFLOL\n" +
						"UP85xM5teae6eTgZFEmOkSrSMB8GA1UdIwQYMBaAFPmL7AQ4aj+qBsaUrXOVKrDI\n" +
						"5rj7MHMGCCsGAQUFBwEBBGcwZTAvBggrBgEFBQcwAYYjaHR0cDovL29jc3AxLndv\n" +
						"c2lnbi5jb20vY2E2L3NlcnZlcjMwMgYIKwYBBQUHMAKGJmh0dHA6Ly9haWExLndv\n" +
						"c2lnbi5jb20vY2E2LnNlcnZlcjMuY2VyMDgGA1UdHwQxMC8wLaAroCmGJ2h0dHA6\n" +
						"Ly9jcmxzMS53b3NpZ24uY29tL2NhNi1zZXJ2ZXIzLmNybDAhBgNVHREEGjAYggsq\n" +
						"LndqaWthLmNvbYIJd2ppa2EuY29tME8GA1UdIARIMEYwCAYGZ4EMAQICMDoGCysG\n" +
						"AQQBgptRAQECMCswKQYIKwYBBQUHAgEWHWh0dHA6Ly93d3cud29zaWduLmNvbS9w\n" +
						"b2xpY3kvMIIBBgYKKwYBBAHWeQIEAgSB9wSB9ADyAHcAaPaY+B9kgr46jO65KB1M\n" +
						"/HFRXWeT1ETRCmesu09P+8QAAAFWNL17eAAABAMASDBGAiEAiibDQxKHvy8XVcSo\n" +
						"CjJCyqYbZpb8IFEOQeg6z1xf0icCIQC5wyh9awd26Cdap1WjOaAhCIoTxtRtwHb4\n" +
						"SYydK/ZeqwB3AKS5CZC0GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAABVjS9\n" +
						"fFMAAAQDAEgwRgIhAKmKlLUaPV5m8T6M4A+X0PB6DcNwFnefKIbhd1hH2n/+AiEA\n" +
						"+WE5tdkOyIKHwviiADiI4AVY0MWIrITnR35dp+AbvbAwDQYJKoZIhvcNAQELBQAD\n" +
						"ggEBAAXSqiRWh2RT9JSjG7lc1uzfm2lHbmr2+JkPk5s4cL3Ch+lBNslYqc5DnE9c\n" +
						"tDVU4lUDXyW7C6+s09TMiYhFTdmZtU0Dz0LgR/rex9H7wQje28axJMTlAe6NLK+d\n" +
						"zsXvPsOMPpaw1nKfROVWRKBmc297bUHf4ICWFskgRbIozf24Evf9ICYAUimtizRA\n" +
						"v9WJHvzG13luBfl9x3ndzwabYlSSf/0iGDPCZt+YKLaE0uss+PkxBmVA/cwoe07Q\n" +
						"KJ2dgMfUb33BW77AdmeM8i7CC1G+GJT72mCrMoAUTUnbxnBlFm3q0Yaw7DrSjhVQ\n" +
						"jqjN5mPoe3nYFfGnUGop5oOknvk=";
	}
}
