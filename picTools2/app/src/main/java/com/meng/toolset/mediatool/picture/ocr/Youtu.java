package com.meng.toolset.mediatool.picture.ocr;

/**
 *
 * @author tyronetao
 */
//public class Youtu {
//
//	private static class TrustAnyTrustManager implements X509TrustManager {
//
//		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//		}
//
//		public X509Certificate[] getAcceptedIssuers() {
//			return new X509Certificate[] {};
//		}
//	}
//
//	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
//		public boolean verify(String hostname, SSLSession session) {
//			return true;
//		}
//	}
//
//	public final static String API_YOUTU_END_POINT = "https://api.youtu.qq.com/youtu/";
//	public final static String API_YOUTU_CHARGE_END_POINT = "https://vip-api.youtu.qq.com/youtu/";
//
//	// 30 days
//	private static int EXPIRED_SECONDS = 2592000;
//	private String m_appid;
//	private String m_secret_id;
//	private String m_secret_key;
//	private String m_end_point;
//	private String m_user_id;
//	private boolean m_not_use_https;
//
//	/**
//	 * PicCloud 构造方法
//	 *
//	 * @param appid
//	 *            授权appid
//	 * @param secret_id
//	 *            授权secret_id
//	 * @param secret_key
//	 *            授权secret_key
//	 */
//	public Youtu(String appid, String secret_id, String secret_key, String end_point, String user_id) {
//		m_appid = appid;
//		m_secret_id = secret_id;
//		m_secret_key = secret_key;
//		m_end_point = end_point;
//		m_user_id = user_id;
//		m_not_use_https = !end_point.startsWith("https");
//	}
//
//	public String StatusText(int status) {
//
//		String statusText = "UNKOWN";
//
//		switch (status) {
//			case 0:
//				statusText = "CONNECT_FAIL";
//				break;
//			case 200:
//				statusText = "HTTP OK";
//				break;
//			case 400:
//				statusText = "BAD_REQUEST";
//				break;
//			case 401:
//				statusText = "UNAUTHORIZED";
//				break;
//			case 403:
//				statusText = "FORBIDDEN";
//				break;
//			case 404:
//				statusText = "NOTFOUND";
//				break;
//			case 411:
//				statusText = "REQ_NOLENGTH";
//				break;
//			case 423:
//				statusText = "SERVER_NOTFOUND";
//				break;
//			case 424:
//				statusText = "METHOD_NOTFOUND";
//				break;
//			case 425:
//				statusText = "REQUEST_OVERFLOW";
//				break;
//			case 500:
//				statusText = "INTERNAL_SERVER_ERROR";
//				break;
//			case 503:
//				statusText = "SERVICE_UNAVAILABLE";
//				break;
//			case 504:
//				statusText = "GATEWAY_TIME_OUT";
//				break;
//		}
//		return statusText;
//	}
//
//	private void GetBase64FromFile(String filePath, StringBuffer base64) throws IOException {
//		File imageFile = new File(filePath);
//		if (imageFile.exists()) {
//			InputStream in = new FileInputStream(imageFile);
//			byte data[] = new byte[(int) imageFile.length()];
//			in.read(data);
//			in.close();
//			base64.append(new String(Tools.Base64.encode(data)));
//		} else {
//			throw new FileNotFoundException(filePath + " not exist");
//		}
//	}
//
//	private JSONObject SendHttpRequest(JSONObject postData, String mothod)throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
//		StringBuffer mySign = new StringBuffer("");
//		YoutuSign.appSign(m_appid, m_secret_id, m_secret_key, System.currentTimeMillis() / 1000 + EXPIRED_SECONDS, m_user_id, mySign);
//		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
//		URL url = new URL(m_end_point + mothod);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestMethod("POST");
//		connection.setRequestProperty("accept", "*/*");
//		connection.setRequestProperty("user-agent", "youtu-java-sdk");
//		connection.setRequestProperty("Authorization", mySign.toString());
//		connection.setDoOutput(true);
//		connection.setDoInput(true);
//		connection.setUseCaches(false);
//		connection.setInstanceFollowRedirects(true);
//		connection.setRequestProperty("Content-Type", "text/json");
//		connection.connect();
//		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//		postData.put("app_id", m_appid);
//		out.write(postData.toString().getBytes("utf-8"));
//		out.flush();
//		out.close();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		String lines;
//		StringBuffer resposeBuffer = new StringBuffer("");
//		while ((lines = reader.readLine()) != null) {
//			lines = new String(lines.getBytes(), "utf-8");
//			resposeBuffer.append(lines);
//		}
//		reader.close();
//		connection.disconnect();
//		JSONObject respose = new JSONObject(resposeBuffer.toString());
//		return respose;
//
//	}
//
//	private JSONObject SendHttpsRequest(JSONObject postData, String mothod)throws NoSuchAlgorithmException, KeyManagementException, IOException, JSONException {
//		SSLContext sc = SSLContext.getInstance("SSL");
//		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
//		StringBuffer mySign = new StringBuffer("");
//		YoutuSign.appSign(m_appid, m_secret_id, m_secret_key, System.currentTimeMillis() / 1000 + EXPIRED_SECONDS, m_user_id, mySign);
//		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
//		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
//		URL url = new URL(m_end_point + mothod);
//		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//		connection.setSSLSocketFactory(sc.getSocketFactory());
//		connection.setHostnameVerifier(new TrustAnyHostnameVerifier());
//		connection.setRequestMethod("POST");
//		connection.setRequestProperty("accept", "*/*");
//		connection.setRequestProperty("user-agent", "youtu-java-sdk");
//		connection.setRequestProperty("Authorization", mySign.toString());
//		connection.setDoOutput(true);
//		connection.setDoInput(true);
//		connection.setUseCaches(false);
//		connection.setInstanceFollowRedirects(true);
//		connection.setRequestProperty("Content-Type", "text/json");
//		connection.connect();
//		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
//		postData.put("app_id", m_appid);
//		out.write(postData.toString().getBytes("utf-8"));
//		out.flush();
//		out.close();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//		String lines;
//		StringBuffer resposeBuffer = new StringBuffer("");
//		while ((lines = reader.readLine()) != null) {
//			lines = new String(lines.getBytes(), "utf-8");
//			resposeBuffer.append(lines);
//		}
//		reader.close();
//		connection.disconnect();
//		JSONObject respose = new JSONObject(resposeBuffer.toString());
//		return respose;
//	}
//	
//	public JSONObject GeneralOcr(String image_path)throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
//		JSONObject data = new JSONObject();
//		StringBuffer image_data = new StringBuffer("");
//		GetBase64FromFile(image_path, image_data);
//		data.put("image", image_data);
//		JSONObject response = m_not_use_https ? SendHttpRequest(data, "ocrapi/generalocr"): SendHttpsRequest(data, "ocrapi/generalocr");
//		return response;
//	}
//
//	public JSONObject GeneralOcrUrl(String image_url)throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
//		JSONObject data = new JSONObject();
//		data.put("url", image_url);
//		JSONObject response = m_not_use_https ? SendHttpRequest(data, "ocrapi/generalocr"): SendHttpsRequest(data, "ocrapi/generalocr");
//		return response;
//	}
//}

