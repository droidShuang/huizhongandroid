package com.junova.huizhong.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class AppImageView extends ImageView {
	/**
	 * @param context
	 */
	public AppImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AppImageView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	public AppImageView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
	    if(getDrawable() != null){
	    	int drawableHeight = getDrawable().getIntrinsicHeight();
		    int drawableWidth = getDrawable().getIntrinsicWidth();
		    int height = width * drawableHeight / drawableWidth;
		    setMeasuredDimension(width, height);
	    }else{
	    	setMeasuredDimension(width, 0);
	    }
	}
}
