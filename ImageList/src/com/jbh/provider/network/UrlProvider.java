package com.jbh.provider.network;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * @author 장보훈
 * @file UrlProvider.java
 * @brief 서버 통신에 필요한 Url 제공 클래스
 */

public class UrlProvider {
    static UrlProvider m_UrlDefines;

    public static String SERVER = "";

    static final String INSTAGRAM_SERVER_DOMAIN = "https://www.instagram.com/";    //인스타 그램 주소
    static final String NAVER_SERVER_DOMAIN= "http://naver.com";       //추후 사용 URL

    

    /**
     * @return UrlProvider 싱글턴 객체
     * @brief UrlProvider 싱글턴 객체를 얻는다.
     */
    @SuppressWarnings("all")
    public static UrlProvider getInstance() {
        
    	SERVER = INSTAGRAM_SERVER_DOMAIN;

        if (m_UrlDefines == null)
            m_UrlDefines = new UrlProvider();
        return m_UrlDefines;
    }

    
    /**
     * @return URL
     * @throws UnsupportedEncodingException
     * @breif 사용자 아이디 통해 API 주소반환
     */
    public String getInstagramMediaUrl(String userid, String pagenum) throws UnsupportedEncodingException {
        return SERVER + URLEncoder.encode(userid, "UTF-8") + "/media/?max_id="+pagenum ;
    }

   
}

