package com.jbh.provider.network;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.jbh.util.Logger;



public class HttpClient {

	/**
     * @brief Constructor
     */
    public HttpClient() {
        super();
    }
    
    /**
     * @brief GET방식 Http 통신을 요청한다
     * @param url 접속URL, b (0이면 GET, 1이면 POST방식)
     * @return {@link String} 형식의 결과 값 
     * @throws IOException 
     * @throws Exception
     */
    public String getHttpResult(String url,String method) throws IOException, Exception {
        Logger.p("REQUEST URL : " + url);
        	return getHttpResult(url, method, null);       
    }

   
    
    /**
     * @brief 서버 통신 결과값을 반환한다
     * @param url 접속 URL
     * @param method 접속 방식 "GET","POST"
     * @return {@link String} 형식의 결과 값
     * @throws IOException
     * @throws Exception 
     */
    public String getHttpResult(String url, String method, String params) throws IOException, Exception {
        HttpURLConnection Http = null;
        
        Logger.p("getHttpResult" );

        Http = getHttpConnection(url, method, params);
        InputStream is = Http.getInputStream();
        String result = null;

        int respLen = Http.getContentLength();
        byte[] buff = null;

        if(respLen != -1) {
            buff = new byte[respLen];                   
            int readLen = 0;
            int nRead = 0;
            while(readLen < respLen && (nRead = is.read(buff, readLen, respLen-readLen)) >= 0) {
                readLen += nRead;           
            }
            result = new String(buff, 0 , buff.length);
        } else {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            StringBuffer output = new StringBuffer();
            String temp = null;

            while((temp = br.readLine()) != null)
                output.append(temp);

            result = output.toString();
        }

        Http.disconnect();

        return result;
    }
    
    
   
    
    
    /**
     * @brief HttpURLConnection을 반환한다.
     * @param url 접속 URL
     * @param method 접속 방식 "GET","POST"
     * @param params POST 방식일 경우 파라메터 값
     * @return {@link HttpURLConnection} 
     * @throws IOException 
     * @throws MalformedURLException 
     * @throws Exception
     */
    public HttpURLConnection getHttpConnection(String url, String method, String params)
            throws MalformedURLException, IOException,Exception {

        HttpURLConnection Http = null;

        try{
        Http = (HttpURLConnection)new URL(url).openConnection();
        Http.setRequestMethod(method);
        Http.setConnectTimeout(NetworkDefines.SERVER_CONNECT_TIMEOUT);
        Http.setReadTimeout(NetworkDefines.SERVER_CONNECT_TIMEOUT * 3);
        	
        Http.setDoInput(true);		
        Http.setRequestProperty("Accept", "application/json");
        
       
        
        if (params != null) {
            
            Http.connect();
            OutputStream os = Http.getOutputStream();
            os.write(params.getBytes());
            Logger.p(method + " : " + params);
            os.flush();
        } 

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Http;
    }


}
