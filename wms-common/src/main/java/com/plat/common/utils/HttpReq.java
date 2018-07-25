package com.plat.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

public class HttpReq {

	private static final int TIME_OUT = 3 * 1000;

	/**
	 * POST请求，可以携带参数
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String httpPostReq(String path, Map<String, Object> params) throws IOException {
		StringBuilder build = new StringBuilder();
		OutputStream outputStream = null;
		BufferedReader bufReader = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setConnectTimeout(TIME_OUT);
			if (params != null && !params.isEmpty()) {
				StringBuilder outBuild = new StringBuilder();
				for (Entry<String, Object> entry : params.entrySet()) {
					outBuild.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
				}
				if (outBuild.length() > 0) {
					outBuild.delete(outBuild.length() - 1, outBuild.length());
				}
				outputStream = conn.getOutputStream();
				outputStream.write(outBuild.toString().getBytes("utf-8"));
				outputStream.flush();
			}
			int resultCode = conn.getResponseCode();
			if (resultCode != HttpURLConnection.HTTP_OK) {
				throw new IOException(conn.getResponseMessage());
			}
			bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String sCurrentLine = "";
			while ((sCurrentLine = bufReader.readLine()) != null) {
				build.append(sCurrentLine);
			}

		} finally {
			if (bufReader != null) {
				bufReader.close();
				bufReader = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}
		return build.toString();
	}

	/**
	 * POST请求，可以携带参数, 模拟登录，
	 * 
	 * @param path
	 * @param params
	 * @return JsessionId
	 */
	public static String simulateLogin(String path) throws IOException {
		StringBuilder build = new StringBuilder();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(TIME_OUT);
		int resultCode = conn.getResponseCode();
		if (resultCode != HttpURLConnection.HTTP_OK) {
			throw new IOException(conn.getResponseMessage());
		}
		String setCookie = conn.getHeaderField("Set-Cookie");
		if (!StringUtil.isEmpty(setCookie)) {
			String[] cookies = setCookie.split(";");
			for (String cookie : cookies) {
				if (cookie.indexOf("JSESSIONID=") > -1) {
					String jsessionId = cookie.split("=")[1];
					build.append(jsessionId);
					break;
				}
			}
		}
		return build.toString();
	}

	/**
	 * httpClientPost
	 * 
	 * @param path
	 * @param headerParams
	 * @param reqParams
	 * @return
	 * @throws IOException
	 */
	public static String execReq(String path, Map<String, String> headerParams, Map<String, Object> reqParams)
			throws IOException {
		StringBuilder build = new StringBuilder();
		OutputStream outputStream = null;
		BufferedReader bufReader = null;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setConnectTimeout(TIME_OUT);
			for (Entry<String, String> entry : headerParams.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}
			if (reqParams != null && !reqParams.isEmpty()) {
				StringBuilder outBuild = new StringBuilder();
				for (Entry<String, Object> entry : reqParams.entrySet()) {
					outBuild.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
				}
				if (outBuild.length() > 0) {
					outBuild.delete(outBuild.length() - 1, outBuild.length());
				}
				outputStream = conn.getOutputStream();
				outputStream.write(outBuild.toString().getBytes("utf-8"));
				outputStream.flush();
			}
			int resultCode = conn.getResponseCode();
			if (resultCode != HttpURLConnection.HTTP_OK) {
				throw new IOException(conn.getResponseMessage());
			}
			String setCookie = conn.getHeaderField("Set-Cookie");
			if (!StringUtil.isEmpty(setCookie)) {
				String[] cookies = setCookie.split(";");
				for (String cookie : cookies) {
					if (cookie.indexOf("JSESSIONID=") > -1) {
						String jsessionId = cookie.split("=")[1];
//						build.append(jsessionId);
						break;
					}
				}
			}
			bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String sCurrentLine = "";
			while ((sCurrentLine = bufReader.readLine()) != null) {
				build.append(sCurrentLine);
			}

		} finally {
			if (bufReader != null) {
				bufReader.close();
				bufReader = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}
		return build.toString();
	}

	/**
	 * GET请求，可以携带参数
	 * 
	 * @param path
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public static String httpGetReq(String path, Map<String, Object> params) throws IOException {
		if (StringUtil.isEmpty(path)) {
			throw new IOException("请求路径为空。");
		}
		StringBuilder build = new StringBuilder();
		BufferedReader bufReader = null;
		try {

			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(TIME_OUT);
			if (params != null && !params.isEmpty()) {
				for (Entry<String, Object> entry : params.entrySet()) {
					conn.setRequestProperty(entry.getKey(), URLEncoder.encode((String) entry.getValue(), "utf-8"));
				}
			}
			int resultCode = conn.getResponseCode();
			if (resultCode != HttpURLConnection.HTTP_OK) {
				throw new IOException(conn.getResponseMessage());
			}

			bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String sCurrentLine;
			while ((sCurrentLine = bufReader.readLine()) != null) {
				build.append(sCurrentLine);
			}
		} finally {
			if (bufReader != null) {
				bufReader.close();
				bufReader = null;
			}
		}

		return build.toString();
	}

	/**
	 * POST流请求
	 * 
	 * @param path
	 *            :请求路径
	 * @param outStr
	 *            ：输出字符串
	 * @return
	 * @throws IOException
	 */
	public static String httpPostReq(String path, String outStr) throws IOException {
		BufferedReader bufReader = null;
		DataOutputStream outputStream = null;
		StringBuilder build = new StringBuilder();
		try {
			if (StringUtil.isEmpty(path)) {
				throw new IOException("请求路径为空。");
			}
			if (StringUtil.isEmpty(outStr)) {
				throw new IOException("请求参数为空。");
			}
			URL url = new URL(path);
			// 建立连接
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			// 设置参数
			httpConn.setDoOutput(true);
			httpConn.setDoInput(true);
			httpConn.setUseCaches(false);
			httpConn.setRequestMethod("POST");

			// 设置请求属性
			httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			httpConn.setConnectTimeout(TIME_OUT);

			// 建立输出流
			outputStream = new DataOutputStream(httpConn.getOutputStream());
			outputStream.writeBytes(outStr);
			outputStream.flush();
			// 获得响应状态
			int resultCode = httpConn.getResponseCode();
			if (resultCode != HttpURLConnection.HTTP_OK) {
				throw new IOException(httpConn.getResponseMessage());
			}
			String readLine = null;
			bufReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = bufReader.readLine()) != null) {
				build.append(readLine);
			}
		} finally {
			if (bufReader != null) {
				bufReader.close();
				bufReader = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
		}

		return build.toString();
	}
}
