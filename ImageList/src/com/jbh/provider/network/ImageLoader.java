package com.jbh.provider.network;



import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jbh.imagelist.R;
import com.jbh.util.MemoryCache;
import com.jbh.util.ScalableImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;




/*
 * LazyList 사용
 * FileCache 부분을 제거 하여 사용
 * http 통신후 bitmap 처리 부분 수정 하여 사용 
 */
 
public class ImageLoader {
     
    // Initialize MemoryCache
    MemoryCache memoryCache = MemoryCache.getInstance();
     
    //FileCache fileCache;
     
    //Create Map (collection) to store image and image url in key value pair
    private Map<ScalableImageView, String> imageViews = Collections.synchronizedMap(
                                           new WeakHashMap<ScalableImageView, String>());
    ExecutorService executorService;
     
    //handler to display images in UI thread
    Handler handler = new Handler();
     
    public ImageLoader(Context context){
         
        //fileCache = new FileCache(context);
         
        // Creates a thread pool that reuses a fixed number of 
        // threads operating off a shared unbounded queue.
        executorService=Executors.newFixedThreadPool(5);
         
    }
     
    // default image show in list (Before online image download)
    final int stub_id=R.drawable.bt_close_normal;
     
    public void DisplayImage(String url, ScalableImageView imageView)
    {
        //Store image and url in Map
        imageViews.put(imageView, url);
         
        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Bitmap bitmap = memoryCache.get(url);
         
        if(bitmap!=null){
            // if image is stored in MemoryCache Map then
            // Show image in listview row
//        	imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));

        	imageView.setImageBitmap(bitmap);
        }
        else
        {
            //queue Photo to download from url
            queuePhoto(url, imageView);
             
            //Before downloading image show default image 
            imageView.setBackgroundResource(stub_id);
        }
    }
         
    private void queuePhoto(String url, ScalableImageView imageView)
    {
        // Store image and url in PhotoToLoad object
    	
        PhotoToLoad p = new PhotoToLoad(url, imageView);
         
        // pass PhotoToLoad object to PhotosLoader runnable class
        // and submit PhotosLoader runnable to executers to run runnable
        // Submits a PhotosLoader runnable task for execution  
         
        executorService.submit(new PhotosLoader(p));
    }
     
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ScalableImageView imageView;
        public PhotoToLoad(String u, ScalableImageView i){
            url=u; 
            imageView=i;
        }
    }
     
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
         
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }
         
        @Override
        public void run() {
            try{
                //Check if image already downloaded
                if(imageViewReused(photoToLoad))
                    return;
                // download image from web url
                Bitmap bmp = getBitmap(photoToLoad.url);
                 
                // set image data in Memory Cache
                memoryCache.put(photoToLoad.url, bmp);
                 
                if(imageViewReused(photoToLoad))
                    return;
                 
                // Get bitmap to display
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                 
                // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue. 
                // The runnable will be run on the thread to which this handler is attached.
                // BitmapDisplayer run method will call
                handler.post(bd);
                 
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
     
    private Bitmap getBitmap(String url) 
    {
        // Download image file from web
        try {
             
            Bitmap bitmap=null;
			BitmapFactory.Options option = new BitmapFactory.Options(); 
			option.inSampleSize = 1;    // Decoding 시 Sampling 비율 옵션
			option.inPurgeable = true;  // 메모리를 줄여주는 옵션
			option.inDither = true;     // 이미지를 깔끔하게 처리해서 보여주는 옵션 
			option.inPreferredConfig = Bitmap.Config.ARGB_8888;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream imgInputStream=conn.getInputStream();
                        
            byte imsibuffer[] = new byte[conn.getContentLength()];
			int startLen = 0;
			while(startLen < imsibuffer.length)
			{
				startLen += imgInputStream.read(imsibuffer , startLen , (imsibuffer.length - startLen));
			}
			
			bitmap = BitmapFactory.decodeByteArray(imsibuffer, 0, imsibuffer.length,option);	
            
            
            conn.disconnect();

             
            return bitmap;
             
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }
 

    boolean imageViewReused(PhotoToLoad photoToLoad){
         
        String tag=imageViews.get(photoToLoad.imageView);
        //Check url is already exist in imageViews MAP
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
     
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
             
            // Show bitmap on UI
            if(bitmap!=null)
            	photoToLoad.imageView.setImageBitmap(bitmap);
            else
            {
            	photoToLoad.imageView.setImageResource(stub_id);
            	
            }
                
        }
    }
 
    public void clearCache() {
        memoryCache.clear();
    }
 
    public void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
             
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              //Read byte from input stream
                 
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
               
              //Write byte from output stream
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}

