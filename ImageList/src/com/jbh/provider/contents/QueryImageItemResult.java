package com.jbh.provider.contents;

import java.util.ArrayList;
import java.util.List;

//{"items": [], "more_available": false, "status": "ok"}

public class QueryImageItemResult {
	
	public static final String TAG_STATUS     = "status";   //상점시퀀스
	public static final String TAG_MOREAVAILABLE      = "more_available";    //상점모ID
	public static final String TAG_ITEMS     = "items";      
	      

	
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

