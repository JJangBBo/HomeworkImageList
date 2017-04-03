package com.jbh.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
 

/**
 * @author 장보훈
 * @file ScalableImageView.java
 * @brief ImageView 를 생성 하여 onMeasure 호출시 그려진 이미지 크기를 바탕으로 ImageView 사이즈를 재조정 하도록 하는 클래스 
 */
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
 
    
    /*
     * 넘어오는 파라메터는 부모뷰로부터 결정된 치수제한을 의미한다.
     * 또한 파라메터에는 bit 연산자를 사용해서 모드와 크기를 같이 담고있다.
     * 모드는 MeasureSpec.getMode(spec) 형태로 얻어오며 다음과 같은 3종류가 있다.
     *         MeasureSpec.AT_MOST : wrap_content (뷰 내부의 크기에 따라 크기가 달라짐)
     *         MeasureSpec.EXACTLY : fill_parent, match_parent (외부에서 이미 크기가 지정되었음)
     *      MeasureSpec.UNSPECIFIED : MODE 가 셋팅되지 않은 크기가 넘어올때 (대부분 이 경우는 없다)
     *  
     *   fill_parent, match_parent 를 사용하면 윗단에서 이미 크기가 계산되어 EXACTLY 로 넘어온다.
     *   이러한 크기는 MeasureSpec.getSize(spec) 으로 얻어낼 수 있다.
     *   
     *   이 메소드에서는 setMeasuredDimension(measuredWidth,measuredHeight) 를 호출해 주어야 하는데
     *   super.onMeasure() 에서는 기본으로 이를 기본으로 계산하는 함수를 포함하고 있다.
     *   
     *   만약 xml 에서 크기를 wrap_content 로 설정했다면 이 함수에서 크기를 계산해서 셋팅해 줘야한다.
     *   그렇지 않으면 무조껀 fill_parent 로 나오게 된다.
     */

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

