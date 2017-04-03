package com.jbh.provider.network;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.jbh.provider.contents.Items;
import com.jbh.provider.contents.QueryImageItemResult;

import java.io.IOException;

/**
 * @author 장보훈
 * @file JsonParser.java
 * @brief Json Pareser 이며 data 응답값에 대해 UI 에 활용할수 있도록 처리 하는 부분 
 */

public class JsonParser {

    static JsonParser jsonParser;

    /**
     * @return JsonParser 싱글턴 객체
     * @brief JsonParser 싱글턴 객체를 얻는다.
     */
    public static JsonParser getInstance() {
        if (jsonParser == null)
        	jsonParser = new JsonParser();
        return jsonParser;
    }


    public QueryImageItemResult parseQueryImageItem(String json) throws JSONException, Exception {
    	QueryImageItemResult result = new QueryImageItemResult();
        JSONObject jsonObject = new JSONObject(json);
        
        result.setStatus(jsonObject.getString(QueryImageItemResult.TAG_STATUS));
        result.setMoreAvailable(jsonObject.getString(QueryImageItemResult.TAG_MOREAVAILABLE));
        

        if (!jsonObject.isNull(QueryImageItemResult.TAG_ITEMS))
        {
            JSONArray memList = jsonObject.getJSONArray(QueryImageItemResult.TAG_ITEMS);
            for (int i = 0; i < memList.length(); i++) 
            {
            	JSONObject mobject = memList.getJSONObject(i);

            	Items item = new Items();

                if (mobject.has(Items.TAG_ID))
                    item.setnId(mobject.getString(Items.TAG_ID));
                if (!mobject.isNull(Items.TAG_IMAGES))
                {
                	item.setUrl(mobject.getJSONObject(Items.TAG_IMAGES).getJSONObject(Items.TAG_STANARD_RESOLUTION).getString(Items.TAG_URL).toString());
                	item.setImageHeight(mobject.getJSONObject(Items.TAG_IMAGES).getJSONObject(Items.TAG_STANARD_RESOLUTION).getString(Items.TAG_IMAGES_HEIGHT).toString());
                	item.setImageWidth(mobject.getJSONObject(Items.TAG_IMAGES).getJSONObject(Items.TAG_STANARD_RESOLUTION).getString(Items.TAG_IMAGES_WIDTH).toString());
                }
             
                    result.setItemsList(item);
            }
        }

        return result;

    }
}
