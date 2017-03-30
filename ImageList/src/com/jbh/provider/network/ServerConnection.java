package com.jbh.provider.network;


import java.io.IOException;

import com.jbh.provider.contents.QueryImageItemResult;
import com.jbh.util.Logger;

/**
 * @author woorim kim
 * @file ServerConnection.java
 * @brief 서버 통신 관리 클래스
 */

public class ServerConnection {
    static ServerConnection _ServerConnection;

    /**
     * @return ServerConnection 싱글턴 객체
     * @brief ServerConnection의 싱글턴 객체를 얻는다.
     */
    public static ServerConnection getInstance() {
        if (_ServerConnection == null)
            _ServerConnection = new ServerConnection();
        return _ServerConnection;
    }

    // ----------------------------------------------------------------------------------------------------------------[

    /**
     * @param url 통신하려는 URL method- 0이면 GET/ 1이면 POST auth - 0이면 어플리케이션키만 사용/1이면 어플리케이션 키,인증키 둘다사용
     * @return String Http통신 결과 값
     * @throws IOException
     * @throws Exception
     * @brief HttpClient로 서버 통신을 한다
     */
    String getHttpResult(String url, String method, Boolean auth) throws IOException, Exception {
        String result = "";
        result = new HttpClient().getHttpResult(url, method);
        Logger.p(result);
        return result;

    }

    
 
    /**
     * @param shop_seq (required) [in] 상점시퀀스 search_word (option ) [in] 검색단어(이름) cursor (option ) [in] 마지막 n_seq값 (2번페이지 로드시
     *                 필수값) cnt (option ) [in] 한화면에 리스트갯수 notice_type(option ) [in] 공지사항 타입 (04 : APP)
     * @return {@link QueryImageItemResult}
     * @throws IOException
     * @throws Exception
     * @brief 공지사항
     */
    public QueryImageItemResult queryImageItem(String userid, String pagenum) throws IOException, Exception {

        return JsonParser.getInstance().parseQueryImageItem(getHttpResult(UrlProvider.getInstance().getInstagramMediaUrl(userid,pagenum), "GET", true));
    }

  
}
