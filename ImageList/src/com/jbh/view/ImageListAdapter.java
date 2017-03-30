package com.jbh.view;

import java.util.List;

import com.jbh.imagelist.R;
import com.jbh.provider.contents.Items;
import com.jbh.provider.network.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class ImageListAdapter extends ArrayAdapter<Items> {
    private Context mContext = null;
    private List<Items> ResourceMap= null;
    LayoutInflater mInflater= null;
    private int mLayout = 0;
    public ImageLoader imageLoader= null;

    public ImageListAdapter(Context context, int itemResourceID, List<Items> resourceMap) {
        super(context, itemResourceID, resourceMap);
        this.ResourceMap = resourceMap;
        this.mContext = context;
        this.mLayout = itemResourceID;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(this.mContext);
    }

    
	public void setAppendList(List<Items> resourceMap){
		
		this.ResourceMap = resourceMap;
	}
    
	public void setClearCache()
	{
		if(imageLoader != null)
		{
			try {
				imageLoader.clearCache();	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
    
   
    @Override
    public int getCount() {
        return ResourceMap.size();
    }

    @Override
    public Items getItem(int position) {
        return ResourceMap.get(position);
    }

    @Override
    public int getPosition(Items item) {
        return super.getPosition(item);
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Items listitem = ResourceMap.get(position);
        PersonViewHolder viewHolder;
        // 캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(mLayout, parent, false);
                viewHolder = new PersonViewHolder();
                viewHolder.uerimageview = (ImageView)convertView.findViewById(R.id.img_viewr);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (PersonViewHolder) convertView.getTag();
            }

            viewHolder.uerimageview.getLayoutParams().height = getDip(Integer.parseInt(listitem.getImageHeight().toString()));
            
            viewHolder.uerimageview.requestLayout();
            ImageView image = viewHolder.uerimageview;
            
            //DisplayImage function from ImageLoader Class
            imageLoader.DisplayImage(listitem.getUrl().toString(), image);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return convertView;
    }


	public class PersonViewHolder 
	{
		public ImageView uerimageview;
	}

	
	private int getDip(int pixel)
	{
		return (int) (pixel * mContext.getResources().getDisplayMetrics().density + 0.5f);
	}
}
