package com.flower.cn.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class HttpRequest {
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param params
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, Map<String, String> params) {
		String result = "";
		BufferedReader in = null;
		try {
			StringBuilder sb = new StringBuilder();
			if (params != null) {
				Set<Map.Entry<String, String>> set = params.entrySet();
				for (Map.Entry k : set) {
					sb.append(k.getKey()).append("=").append(k.getValue()).append("&");
				}
			}
			System.out.println("参数：" + sb.toString());
			String urlNameString = url + "?" + sb.toString();
			URL realUrl = new URL(urlNameString);
			trustAllHttpsCertificates();
			HttpsURLConnection.setDefaultHostnameVerifier(hv);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param params
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, Map<String, String> params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());

			StringBuilder sb = new StringBuilder();
			if (params != null) {
				Set<Map.Entry<String, String>> set = params.entrySet();
				for (Map.Entry k : set) {
					sb.append(k.getKey()).append("=").append(k.getValue()).append("&");
				}
			}

			// 发送请求参数
			out.print(sb.toString());
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	static HostnameVerifier hv = new HostnameVerifier() {
		public boolean verify(String urlHostName, SSLSession session) {
			System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
			return true;
		}
	};

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}
	/**
	 * http传输json
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(String url, String param) throws Exception {  
//      byte[] data = param.getBytes();  
//      DataOutputStream outs = null;  
		PrintWriter out = null;
      try {  
          URL javaUrl = new URL(url);  
          HttpURLConnection http = (HttpURLConnection) javaUrl.openConnection();  
          http.setConnectTimeout(10 * 1000);  
          http.setReadTimeout(20 * 1000);  
          http.setRequestMethod("POST");  
          http.setDoOutput(true);
          http.setUseCaches(false);
          http.setRequestProperty("Connection", "Keep-Alive");  
          http.setRequestProperty("Charset", "utf-8");  
//          http.setRequestProperty("Content-Length", String.valueOf(data.length));  
          http.setRequestProperty("Content-Type", "application/json");  
          http.setRequestProperty("User-Agent",  
                  "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.149 Safari/537.36");  
//          outs = new DataOutputStream(http.getOutputStream());  
//          outs.write(data); 
//          outs.flush();
			out = new PrintWriter(http.getOutputStream());
          out.print(param);
          out.flush();  
          if (http.getResponseCode() == 200) {  
              return new String(parseStream(http.getInputStream()), "utf-8");  
          }  
      } finally {  
//          if (outs != null) {  
//              outs.close();  
//          }  
          if (out != null) {  
              out.close();  
          }  
      }  
      return null;  
  }  
  private static byte[] parseStream(InputStream ins) throws Exception {  
      int len = -1;  
      byte[] data = null;  
      byte[] buffer = new byte[1024];  
      ByteArrayOutputStream outs = null;  
      try {  
          outs = new ByteArrayOutputStream();  
          while ((len = ins.read(buffer)) != -1) {  
              outs.write(buffer, 0, len);  
          }  
          outs.flush();  
          data = outs.toByteArray();  
      } finally {  
          if (outs != null) {  
              outs.close();  
          }  
          if (ins != null) {  
          	ins.close();  
          }  
      }  
      return data;  
  }  

	public static void main(String[] args) {
		Map<String, String> m = new HashMap<String, String>();
		m.put("client_secret", "151f3c0e79034d77ab93dc0b992f30b6");
		m.put("account", "YCXD01");
		// 发送 GET 请求
		String s = HttpRequest.sendGet("https://mi.juxinli.com/api/access_token", m);
		System.out.println(s);
		// 发送 POST 请求
		// String
		// sr=HttpRequest.sendPost("http://localhost:6144/Home/RequestPostString",
		// "key=123&v=456");
		// System.out.println(sr);
	}
}