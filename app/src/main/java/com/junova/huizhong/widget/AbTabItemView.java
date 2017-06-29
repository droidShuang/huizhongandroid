/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.junova.huizhong.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTabItemView.java 
 * 描述：表示一个TAB
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbTabItemView extends LinearLayout {
	
	/**  UI设计的基准宽度. */
	public static int UI_WIDTH = 720;
	
	/**  UI设计的基准高度. */
	public static int UI_HEIGHT = 1280;
	/**  UI设计的密度. */
	public static int UI_DENSITY = 2;
	/** The m context. */
	private Context mContext;
	
    /** 当前的索引. */
	private int mIndex;
	
    /** 包含的TextView. */
    private TextView mTextView;
    
    /** 图片. */
    private Drawable mLeftDrawable,mTopDrawable,mRightDrawable,mBottomDrawable;
	
    /** Bounds. */
    private int leftBounds,topBounds,rightBounds,bottomBounds;
    /**
     * Instantiates a new ab tab item view.
     *
     * @param context the context
     */
    public AbTabItemView(Context context) {
		this(context,null);
	}

	/**
	 * Instantiates a new ab tab item view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        this.setPadding(10, 10, 10, 10);
        this.mContext = context;
        mTextView = new TextView(context);
        mTextView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        mTextView.setLayoutParams(params);
       
        mTextView.setFocusable(true);
        mTextView.setPadding(0 , 0, 0, 0);
        mTextView.setCompoundDrawablePadding(10);
        mTextView.setSingleLine();
        this.addView(mTextView);
    }

    /**
     * Inits the.
     *
     * @param index the index
     * @param text the text
     */
    public void init(int index,String text) {
        mIndex = index;
        mTextView.setText(text);
    }


    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return mIndex;
    }


	/**
	 * Gets the text view.
	 *
	 * @return the text view
	 */
	public TextView getTextView() {
		return mTextView;
	}

    /**
     * 描述：设置文字大小.
     *
     * @param tabTextSize the new tab text size
     */
	public void setTabTextSize(int tabTextSize) {
		setTextSize(mTextView, tabTextSize);
	}

	/**
     * 缩放文字大小,这样设置的好处是文字的大小不和密度有关，
     * 能够使文字大小在不同的屏幕上显示比例正确
     * @param textView button
     * @param sizePixels px值
     * @return
     */
    public void setTextSize(TextView textView,float sizePixels) {
    	float scaledSize = scaleTextValue(textView.getContext(),sizePixels);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,scaledSize);
    }
	
    /**
	 * 描述：根据屏幕大小缩放文本.
	 *
	 * @param context the context
	 * @param pxValue the px value
	 * @return the int
	 */
	public int scaleTextValue(Context context, float value) {
		DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
		//为了兼容尺寸小密度大的情况
		if(mDisplayMetrics.scaledDensity > 2){
			//缩小到密度分之一
			//value = value*(1.1f - 1.0f/mDisplayMetrics.scaledDensity);
		}
		return scale(mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels, value);
	}
	/**
	 * 描述：根据屏幕大小缩放.
	 *
	 * @param displayWidth the display width
	 * @param displayHeight the display height
	 * @param pxValue the px value
	 * @return the int
	 */
	
	
	public int scale(int displayWidth, int displayHeight, float pxValue) {
		if(pxValue == 0 ){
			return 0;
		}
		float scale = 1;
		try {
			float scaleWidth = (float) displayWidth / UI_WIDTH;
			float scaleHeight = (float) displayHeight / UI_HEIGHT;
			scale = Math.min(scaleWidth, scaleHeight);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Math.round(pxValue * scale + 0.5f);
	}
	
	/**
     * 获取屏幕尺寸与密度.
     *
     * @param context the context
     * @return mDisplayMetrics
     */
    public DisplayMetrics getDisplayMetrics(Context context) {
        Resources mResources;
        if (context == null){
            mResources = Resources.getSystem();
            
        }else{
            mResources = context.getResources();
        }
        //DisplayMetrics{density=1.5, width=480, height=854, scaledDensity=1.5, xdpi=160.421, ydpi=159.497}
        //DisplayMetrics{density=2.0, width=720, height=1280, scaledDensity=2.0, xdpi=160.42105, ydpi=160.15764}
        DisplayMetrics mDisplayMetrics = mResources.getDisplayMetrics();
        return mDisplayMetrics;
    }
	
	/**
	 * 描述：设置文字颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		mTextView.setTextColor(tabColor);
	}
	
	/**
	 * 描述：设置文字的图片.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		mLeftDrawable = left;
		mTopDrawable = top;
		mRightDrawable = right;
		mBottomDrawable = bottom;
		
		if(mLeftDrawable!=null){
			mLeftDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mTopDrawable!=null){
			mTopDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mRightDrawable!=null){
			mRightDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mBottomDrawable!=null){
			mBottomDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		mTextView.setCompoundDrawables(mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable);
	}
	
	/**
	 * 描述：设置图片尺寸.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabCompoundDrawablesBounds(int left, int top, int right, int bottom) {
		leftBounds = scaleValue(mContext, left);
		topBounds = scaleValue(mContext, top);
		rightBounds = scaleValue(mContext, right);
		bottomBounds = scaleValue(mContext, bottom);
	}
	
	/**
	 * 描述：根据屏幕大小缩放.
	 *
	 * @param context the context
	 * @param pxValue the px value
	 * @return the int
	 */
	public int scaleValue(Context context, float value) {
		DisplayMetrics mDisplayMetrics = getDisplayMetrics(context);
		//为了兼容尺寸小密度大的情况
		if(mDisplayMetrics.scaledDensity > UI_DENSITY){
			//密度
			if(mDisplayMetrics.widthPixels > UI_WIDTH){
				value = value*(1.3f - 1.0f/mDisplayMetrics.scaledDensity);
			}else if(mDisplayMetrics.widthPixels < UI_WIDTH){
				value = value*(1.0f - 1.0f/mDisplayMetrics.scaledDensity);
			}
		}
		return scale(mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels, value);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
		this.setBackgroundResource(resid);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param d the new tab background drawable
	 */
	public void setTabBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
    
}