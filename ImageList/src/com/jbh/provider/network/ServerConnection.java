package com.jbh.provider.network;


import java.io.IOException;

import com.jbh.provider.contents.QueryImageItemResult;
import com.jbh.util.Logger;


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
     * @param 
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
     * @param 
     * @return {@link QueryImageItemResult}
     * @throws IOException
     * @throws Exception
     * @brief 
     */
    public QueryImageItemResult queryImageItem(String userid, String pagenum) throws IOException, Exception {

        return JsonParser.getInstance().parseQueryImageItem(getHttpResult(UrlProvider.getInstance().getInstagramMediaUrl(userid,pagenum), "GET", true));
    }

  
}
