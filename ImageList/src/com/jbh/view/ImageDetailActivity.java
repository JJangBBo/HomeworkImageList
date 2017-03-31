package com.jbh.view;


import java.util.ArrayList;

import com.jbh.imagelist.R;
import com.jbh.provider.contents.Items;
import com.jbh.provider.contents.QueryImageItemResult;
import com.jbh.provider.network.ImageLoader;
import com.jbh.provider.network.ServerConnection;
import com.jbh.util.ScalableImageView;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;



public class ImageDetailActivity extends Activity { 
	
	
    /** 추가 로딩이 가능한지 판단 */
    Boolean bMoreAvailableFlag = false;
    /** 추가 로딩중인지 판단 */
    Boolean mLoadingFlag = false;
    
    /** 검색할 아이디를 저장하는 스트링 */
    String mSearchIDString ="";
    
    ViewPager mPager;					//뷰 페이저
    
    PagerAdapterClass pagerAdapter;
    
    ImageLoader imageLoader= null;
    
    ArrayList<Items> mResourceMap= null;
    
    int mPosition = 0;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_detail);
		
		
		mResourceMap = (ArrayList<Items>) getIntent().getSerializableExtra("RESOURCEMAP");
		bMoreAvailableFlag = getIntent().getExtras().getBoolean("MORE_FLAG");
		mSearchIDString = getIntent().getStringExtra("SEARCH_ID");
		setMainUI();

    }

    private void setMainUI()
    {
    	try 
    	{
    		imageLoader = new ImageLoader(getApplicationContext());
    		mPager = (ViewPager)findViewById(R.id.pager);
    		pagerAdapter = new PagerAdapterClass(getApplicationContext());
    		mPager.setAdapter(pagerAdapter);//PagerAdapter로 설정
    		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//아이템이 변경되면, gallery나 listview의 onItemSelectedListener와 비슷
    			@Override 
    			public void onPageSelected(int position)
    			{
    				if(position ==  mResourceMap.size()-1)
    				{
    					if(!mLoadingFlag && bMoreAvailableFlag) 
                        {
                        	getItemSearch(mSearchIDString, mResourceMap.get(mResourceMap.size()-1).getId().toString() );
                        }	
    				}
                    
    			}
    			@Override
    			public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
    			@Override
    			public void onPageScrollStateChanged(int state) {}
    		});
    		mPager.setCurrentItem(getIntent().getIntExtra("RESOURCEMAP_POSITION", 0));
    		pagerAdapter.notifyDataSetChanged();
    		

		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    
    private void getItemSearch(String searchid,String itemid) 
    {
    	try 
    	{
    		mLoadingFlag = true;
    		new GetItemProcess(searchid, itemid).execute();
    		
    	}
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    
    private class GetItemProcess extends AsyncTask<String, Void, QueryImageItemResult> {
        String searchId = null;
        String itemId = null;
        
       


        public GetItemProcess(String searchId, String itemId) {

            this.searchId = searchId;
            this.itemId = itemId;
            
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            

        }

        @Override
        protected QueryImageItemResult doInBackground(String... params) {
            try {
                return ServerConnection.getInstance().queryImageItem(searchId, itemId);
            }
            catch (Exception e) {
                
                e.printStackTrace();
                return null;
            }
            
        }

        protected void onPostExecute(QueryImageItemResult result) {
            super.onPostExecute(result);
            
            if(result != null)
            {
            	 if (result.getStatus().equals("ok")) 
                 {
                 	mLoadingFlag = false;
                 	if(result.getItemsList().size() > 0 )
                 	{
                 		bMoreAvailableFlag = result.getMoreAvailable().toString().equals("true")? true : false;
                 		mResourceMap.addAll(result.getItemsList());
                 		pagerAdapter.notifyDataSetChanged();
                 		
                 	}
                 	else
                 	{
                 		showToastMake("검색된 결과가 없습니다.");
                 	}
                 }
            }
           
            
        }
    }
    

    
    /**
	 * PagerAdapter 
	 */
	private class PagerAdapterClass extends PagerAdapter{
		
		private LayoutInflater mInflater;

		public PagerAdapterClass(Context c){
			super();
			mInflater = LayoutInflater.from(c);
		}
		
		@Override
		public int getCount() {
			return (mResourceMap == null)? 0: mResourceMap.size();
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			View inflaterView = null;
			final Items listitem = mResourceMap.get(position);
			inflaterView = mInflater.inflate(R.layout.item_pager_image, null);
			
			ScalableImageView image = (ScalableImageView)inflaterView.findViewById(R.id.pager_item_imageview);
            imageLoader.DisplayImage(listitem.getUrl().toString(), image);
			
            RelativeLayout mColoseBtn = (RelativeLayout)inflaterView.findViewById(R.id.pager_item_close_btn);
    		mColoseBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});
    		((ViewPager)pager).addView(inflaterView);
    		
    		return inflaterView; 
		}

		@Override
		public void destroyItem(View pager, int position, Object view) {	
			((ViewPager)pager).removeView((View)view);
		}
		
		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj; 
		}

		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
		@Override public void finishUpdate(View arg0) {}
	}

    private void showToastMake(CharSequence message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            
        }
    }
    
    
    
    
    
    @Override
    protected void onDestroy() {

    	if(mResourceMap !=null)
    		mResourceMap.clear();

        super.onDestroy();
    }

    @Override
    public void onRestart() {

    	super.onRestart();
    }

    @Override
	protected void onResume() {
				
	    super.onResume();
	   
	}
    
	@Override
	public void onPause() {
		super.onPause();
		

	}
}