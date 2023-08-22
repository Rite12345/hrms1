package com.dizitiveit.hrms.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.springframework.stereotype.Component;

@Component("otpSender")
public class OtpSender  {

	 private String username;
	  private String password; 
	  private String message;
	  private String type;
	  private String dlr; 
	  private String destination;
	  private String source; 
	  private String server; 
	  private int port;
	  
	  public OtpSender(String username, String password, String message, String  type, String dlr, String destination, String source, String server, int port)
	  { 
		  super(); 
		  this.username = username; 
		  this.password = password; 
		  this.message = message; 
		  this.type = type; 
		  this.dlr = dlr; 
		  this.destination = destination;
	  this.source = source;
	  this.server = server;
	  this.port = port; 
	  }
	  
	  public OtpSender() { // TODO Auto-generated constructor stub 
		  }
	  
	  
	  
	  
	  public String submitMessage() { 
		  HttpURLConnection httpConnection = null;
	  String message = null; 
	  try { 
		  // Url that will be called to submit the message
	  URL sendUrl = new URL("http://" + this.server + ":" + this.port + "/bulksms/bulksms");
	  
	  @SuppressWarnings("unused")
	  HostnameVerifier hostVerfier = new HostnameVerifier() { 
		  public boolean verify(String urlHostName, SSLSession session) { 
			  return true; 
			  } }; 
			  trustAllHttpsCertificates(); 
			  httpConnection = (java.net.HttpURLConnection) sendUrl.openConnection();
	  httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
	  httpConnection.setRequestMethod("POST"); 
	  httpConnection.setDoInput(true);
	  httpConnection.setDoOutput(true);
	  httpConnection.setUseCaches(false);
	  
	  DataOutputStream dataStreamToServer = new
	  DataOutputStream(httpConnection.getOutputStream());
	  dataStreamToServer.writeBytes("username=" + URLEncoder.encode(this.username,
	  "UTF-8") + "&password=" + URLEncoder.encode(this.password, "UTF-8") +
	  "&type=" + URLEncoder.encode(this.type, "UTF-8") + "&dlr=" +
	  URLEncoder.encode(this.dlr, "UTF-8") + "&destination=" +
	  URLEncoder.encode(this.destination, "UTF-8") + "&source=" +
	  URLEncoder.encode(this.source, "UTF-8") + "&message=" +
	  URLEncoder.encode(this.message, "UTF-8")); dataStreamToServer.flush();
	  dataStreamToServer.close();
	  
	  BufferedReader dataStreamFromUrl = new BufferedReader( new InputStreamReader(httpConnection.getInputStream()));
	  String dataFromUrl = "",
	  dataBuffer = "";
	  
	  while ((dataBuffer = dataStreamFromUrl.readLine()) != null) {
		  dataFromUrl += dataBuffer;
		  }
	  
	  dataStreamFromUrl.close();
	  
	  System.out.println("Response Success: " + dataFromUrl);
	  
	  message = dataFromUrl.substring(0, 4);
	  } catch (Exception ex) {
	  System.out.println("Response: " + ex.getMessage());
	  System.out.println("error message - > " + ex.getMessage());
	  ex.printStackTrace(); 
	  } finally { 
		  if (httpConnection != null) {
	  httpConnection.disconnect(); } 
		  } 
	  return message; 
	  }
	  
	  private static void trustAllHttpsCertificates() throws Exception {
		  // Create  a trust manager that does not validate certificate chains:
	  javax.net.ssl.TrustManager[] trustAllCerts = new
	  javax.net.ssl.TrustManager[1]; javax.net.ssl.TrustManager tm = new miTM();
	  
	  trustAllCerts[0] = tm; javax.net.ssl.SSLContext sc =
	  javax.net.ssl.SSLContext.getInstance("SSL"); sc.init(null, trustAllCerts, null);
	  
	  javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	  
	  }
	  
	  public static class miTM implements javax.net.ssl.TrustManager,
	  javax.net.ssl.X509TrustManager { public java.security.cert.X509Certificate[]
	  getAcceptedIssuers() { return null; }
	  
	  public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
	  return true; }
	  
	  public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
	  return true; }
	  
	  public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
	  String authType) throws java.security.cert.CertificateException { return; }
	  
	  public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
	  String authType) throws java.security.cert.CertificateException { return; } }
	  
	  public String getUsername() { return username; }
	  
	  public void setUsername(String username) { this.username = username; }
	  
	  public String getPassword() { return password; }
	  
	  @Override public String toString() { return "OtpSender [username=" + username
	  + ", password=" + password + ", message=" + message + ", type=" + type +
	  ", dlr=" + dlr + ", destination=" + destination + ", source=" + source +
	  ", server=" + server + ", port=" + port + "]"; }
	  
	  
	  
	  public void setPassword(String password) { this.password = password; }
	  
	  public String getMessage() { return message; }
	  
	  public void setMessage(String message) { this.message = message; }
	  
	  public String getType() { return type; }
	  
	  public void setType(String type) { this.type = type; }
	  
	  public String getDlr() { return dlr; }
	  
	  public void setDlr(String dlr) { this.dlr = dlr; }
	  
	  public String getDestination() { return destination; }
	  
	  public void setDestination(String destination) { this.destination =
	  destination; }
	  
	  public String getSource() { return source; }
	  
	  public void setSource(String source) { this.source = source; }
	  
	  public String getServer() { return server; }
	  
	  public void setServer(String server) { this.server = server; }
	  
	  public int getPort() { return port; }
	  
	  public void setPort(int port) { this.port = port; } 
	
}
