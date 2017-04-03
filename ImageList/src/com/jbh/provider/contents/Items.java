package com.jbh.provider.contents;

import java.io.Serializable;


/**
 * @author 장보훈
 * @file Items.java
 * @brief api 통신 내용중 image 정보를 담고 있는 부분에 대한 정의 클래스 
 */

public class Items implements Serializable{
	
	private static final long serialVersionUID = 4220461820168818967L; 
	public static final String TAG_ID       =  "id";       //이미지 id      
	public static final String TAG_IMAGES    =  "images";     //이미지 JSONArray
	public static final String TAG_STANARD_RESOLUTION    =  "standard_resolution";     //
	public static final String TAG_URL     =  "url";      // 이미지 URL
	public static final String TAG_IMAGES_WIDTH    =  "width";     //이미지 JSONArray
	public static final String TAG_IMAGES_HEIGHT    =  "height";     //이미지 JSONArray

	
	private String id        = "";
	private String url     = "";
	private String imagewidth     = "";
	private String imageheight     = "";
		
	
	public Items(){
	}
	
	public String getId(){
		return id;
	}
	
	public void setnId(String data){
		if(data != null)
			id = data;
	}
	
	public String getUrl(){
		return url;
	}
	
	public void setUrl(String data){
		if(data != null)
			url = data;
	}
	
	public String getImageWidth(){
		return imagewidth;
	}
	
	public void setImageWidth(String data){
		if(data != null)
			imagewidth = data;
	}
	
	public String getImageHeight(){
		return imageheight;
	}
	
	public void setImageHeight(String data){
		if(data != null)
			imageheight = data;
	}
	

}
