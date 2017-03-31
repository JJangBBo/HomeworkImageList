package com.jbh.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
 
public class ScalableImageView extends ImageView {
     
 
    public ScalableImageView(Context context) {
        super(context);
    }
 
    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public ScalableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try
        {
            Drawable drawable = getDrawable();
 
            if (drawable == null)
            {
            	
            	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            else
            {
            	
    			int width = MeasureSpec.getSize(widthMeasureSpec);

    			int height = (int) Math.ceil((float) width * (float) drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth());

    			setMeasuredDimension(width, height);

            }
        }
        catch (Exception e)
        {
            
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

