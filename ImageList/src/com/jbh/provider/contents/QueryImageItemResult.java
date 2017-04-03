package com.jbh.provider.contents;

import java.util.ArrayList;
import java.util.List;

//{"items": [], "more_available": false, "status": "ok"}

/**
 * @author 장보훈
 * @file QueryImageItemResult.java
 * @brief api 통신 내용중 기본 골자가 되는 부분의 item 정의 클래스
 */

public class QueryImageItemResult {
	
	public static final String TAG_STATUS     = "status";   // API 응답 상태
	public static final String TAG_MOREAVAILABLE      = "more_available";    // API 추가 로딩 가능 여부
	public static final String TAG_ITEMS     = "items";      // API 응답 내용중 item에 해당
	      

	
	private String status    = "";
	private String moreavailable     = "";
	private List<Items> items 	= new ArrayList<Items>();
	
	
	public QueryImageItemResult(){		
	}
	
	public void setStatus(String data){
		if(data != null)
			status = data;
	}

	public String getStatus(){
		return status;
	}
	
	public void setMoreAvailable(String data){
		if(data != null)
			moreavailable = data;
	}

	public String getMoreAvailable(){
		return moreavailable;
	}


	
	public void setItemsList(Items item)
	{
		if(item == null) return;		
		items.add(item);
	}
	public List<Items> getItemsList()
    {
    	return items; 
    }
	

}

