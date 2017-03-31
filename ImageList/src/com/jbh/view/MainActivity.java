package com.jbh.view;


import java.util.ArrayList;
import java.util.List;

import com.jbh.imagelist.R;
import com.jbh.provider.contents.Items;
import com.jbh.provider.contents.QueryImageItemResult;
import com.jbh.provider.network.ServerConnection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends Activity { 
	
	ArrayList<Items> mImageList = new ArrayList<Items>();
	/** 리스트뷰 어댑터  */
    ImageListAdapter mListAdapter = null;
    /** 결과 리스트뷰 */
    ListView mListview;
    /** 추가 로딩이 가능한지 판단 */
    Boolean bMoreAvailableFlag = false;
    /** 추가 로딩중인지 판단 */
    Boolean mLoadingFlag = false;
    
    /** 검색할 아이디를 저장하는 스트링 */
    String mSearchIDString ="";
    /** 검색할 아이디를 받을 EditText */
    EditText mSearchID;
    /** 검색버튼 */
    Button   mSearchBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setMainUI();
    }

    private void setMainUI()
    {
    	try 
    	{
    		mListview = (ListView)findViewById(R.id.list_image_item);
    		mSearchID = (EditText)findViewById(R.id.edit_search_id);
    		mSearchBtn = (Button)findViewById(R.id.btn_search);
    		mSearchBtn.setOnClickListener(btnClickListener);
    		mListAdapter = new ImageListAdapter(getApplicationContext(), R.layout.image_list_item, mImageList);
        	mListview.setAdapter(mListAdapter);
        	mListview.setOnScrollListener(mOnScrollListener);
        	mListview.setCacheColorHint(0);	
        	mListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					// TODO Auto-generated method stub
	        		Intent intent = new Intent(getApplicationContext(), ImageDetailActivity.class);
	    			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
					Bundle bundle = new Bundle();
					bundle.putSerializable("RESOURCEMAP",mImageList);
					intent.putExtras(bundle);
					intent.putExtra("RESOURCEMAP_POSITION", position);
					intent.putExtra("MORE_FLAG", bMoreAvailableFlag);
					intent.putExtra("SEARCH_ID", mSearchIDString);
					
	    			overridePendingTransition(0,0);
	    			startActivity(intent); 
				}
			});
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
    
    
    View.OnClickListener btnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) 
        {
        	if(v.getId() == R.id.btn_search)
        	{
        		mSearchIDString = mSearchID.getText().toString();
            	if(mSearchIDString.length() > 0)
            	{
            		if(mListAdapter!=null)
            		{
            			mListAdapter.setClearCache();
            		}
            		mSearchIDString = mSearchIDString.trim();
            		mImageList.clear();
            		hideKeyboard();
            		getItemSearch(mSearchIDString,"0");	
            	}
            	else
            	{
            		showToastMake("검색하실 ID를 입력하세요.");
            	}	
        	}
        	       	
        	
        }
    };
    

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
                 		mImageList.addAll(result.getItemsList());
                 		showListView();
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
     * 리스트뷰 바인딩 메서드
     * 
     * @param result
     *            서버에서 넘어온 데이터
     */
    private void showListView() {
//    	mImageList = result.getItemsList();
    	if(mListAdapter != null)
    	{
    		mListAdapter.setAppendList(mImageList);
    		mListAdapter.notifyDataSetChanged();
    	}
    }
    
    private AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {

        }

        @Override
        public void onScroll(AbsListView absListView, int firsVisibleItem, int visibleItemCount, int totalItemCount) {

            if(firsVisibleItem + visibleItemCount ==  totalItemCount && totalItemCount != 0 )
            {
                if(!mLoadingFlag && bMoreAvailableFlag) 
                {
                	getItemSearch(mSearchIDString, mImageList.get(mImageList.size()-1).getId().toString() );
                }
            }

            if(firsVisibleItem == 0 && absListView.getChildAt(0) !=null && absListView.getChildAt(0).getTop() == 0) {
            }
        }
    };
    
    private void hideKeyboard() {
    	try {
    		InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	
		} catch (Exception e) {
			// TODO: handle exception
		}
        
    }

    private void showToastMake(CharSequence message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            
        }
    }
    
    
    
    
    
    @Override
    protected void onDestroy() {

    	if(mImageList !=null)
    		mImageList.clear();
    	
		if(mListAdapter != null)
			mListAdapter.setClearCache();
		
		hideKeyboard();
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