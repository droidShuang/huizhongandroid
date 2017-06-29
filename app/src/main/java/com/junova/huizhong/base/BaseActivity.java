package com.junova.huizhong.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.common.Logger;

public class BaseActivity extends FragmentActivity {

	public Activity activity;
	public Logger logger = Logger.getInstance();

	/**
	 * bottomLayout LinearLayout 底部控件
	 */
	private LinearLayout bottomLayout;
	/**
	 * centerLayout 内容控件
	 */
	private LinearLayout contentLayout;

	private RelativeLayout layoutTop;

	private Button leftBtn, rightBtn;
	private TextView centerText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		activity = this;
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		bottomLayout = (LinearLayout) findViewById(R.id.bottomBar);
		leftBtn = (Button) findViewById(R.id.topBarLeftBtn);
		rightBtn = (Button) findViewById(R.id.topBarRightBtn);
		centerText = (TextView) findViewById(R.id.topBarTitleView);
		layoutTop = (RelativeLayout) findViewById(R.id.layout_top);

	}

	/**
	 * 设置导航条背景
	 * 
	 * @param resId
	 */
	public void setTopBarBg(int resId) {
		// TODO Auto-generated method stub
		if (resId != 0) {
			layoutTop.setBackgroundResource(resId);
		}
	}

	/**
	 * 加载视图
	 * 
	 * @param v
	 */
	public void setCenterContentView(View v, int layoutId) {
		// TODO Auto-generated method stub
		contentLayout.setVisibility(View.VISIBLE);
		if (null != v) {
			contentLayout.addView(v,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		} else {
			View child = LayoutInflater.from(activity).inflate(layoutId, null);
			contentLayout.addView(child,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		}
	}

	/**
	 * 底部视图
	 * 
	 * @param v
	 *            if v=null gone else show
	 */
	public void setBottomContentView(View v, int layoutId) {
		if (null != v || layoutId != 0) {
			bottomLayout.setVisibility(View.VISIBLE);
			if (null != v) {
				bottomLayout.addView(v);
			} else {
				View child = LayoutInflater.from(activity).inflate(layoutId,
						null);
				bottomLayout.addView(child, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
			}
		}
	}
//
	/**
	 * topBar title
	 * 
	 * @param title
	 *            标题
	 * @param id
	 *            字体颜色
	 */
	public void initTopBarTitle(String title, int color, int size) {
		// TODO Auto-generated method stub
		layoutTop.setVisibility(View.VISIBLE);
		if (color != 0) {
			centerText.setTextColor(getResources().getColor(color));
		}
		if (size != 0) {
			centerText.setTextSize(size);
		}
		if (title != null) {
			centerText.setText(title);
		}
	}

	/**
	 * 初始化top bar left 按钮<br/>
	 * 配合setTopLeftCanUsed(bool)使用
	 * 
	 * @param title
	 *            标题
	 * @param bg
	 *            背景
	 * @param listener
	 *            监听 if null do finish else do listener
	 */
	public void initTopLeftBtn(String title, int resId, OnClickListener listener) {
		if (title != null) {
			leftBtn.setText(title);
		} else {
			leftBtn.setText("");
		}
		if (resId != 0) {

			leftBtn.setBackgroundResource(resId);
		}
		if (listener != null) {
			leftBtn.setOnClickListener(listener);
		} else {
			leftBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}

	}

	/**
	 * 初始化top bar left 按钮<br/>
	 * 配合setTopLeftCanUsed(bool)使用
	 * 
	 * @param title
	 *            标题
	 * @param resId
	 *            背景
	 * @param listener
	 *            监听if null do finish else do listener
	 * @param tColor
	 *            文字颜色
	 * @param tSize
	 *            文字大小
	 */
	public void initTopLeftBtn(String title, int resId,
			OnClickListener listener, int tColor, int tSize) {
		if (title != null) {
			leftBtn.setText(title);
		} else {
			leftBtn.setText("");
		}
		if (resId != 0) {

			leftBtn.setBackgroundResource(resId);
		}
		if (listener != null) {
			leftBtn.setOnClickListener(listener);
		} else {
			leftBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		if (tColor != 0) {
			leftBtn.setTextColor(getResources().getColor(tColor));
		}
		if (tSize != 0) {
			leftBtn.setTextSize(tSize);
		}
	}

	/**
	 * 初始化top bar right 按钮 <br/>
	 * 配合setTopRightCanUsed(bool)使用
	 * 
	 * @param title
	 *            标题
	 * @param bg
	 *            背景
	 * @param listener
	 *            监听 if null do nothing else do listener
	 */
	public void initTopRightBtn(String title, int resId,
			OnClickListener listener) {
		if (title != null) {
			rightBtn.setText(title);
		} else {
			rightBtn.setText("");
		}
		if (resId != 0) {

			rightBtn.setBackgroundResource(resId);
		}
		if (listener != null) {
			rightBtn.setOnClickListener(listener);
		}

	}

	/**
	 * 初始化top bar right 按钮 <br/>
	 * 配合setTopRightCanUsed(bool)使用
	 * 
	 * @param title
	 *            标题
	 * @param resId
	 *            背景
	 * @param listener
	 *            监听 if null do nothing else do listener
	 * @param tColor
	 *            文字颜色
	 * @param tSize
	 *            文字大小
	 */
	public void initTopRightBtn(String title, int resId,
			OnClickListener listener, int tColor, int tSize) {
		if (title != null) {
			rightBtn.setText(title);
		} else {
			rightBtn.setText("");
		}
		if (resId != 0) {

			rightBtn.setBackgroundResource(resId);
		}
		if (listener != null) {
			rightBtn.setOnClickListener(listener);
		}
		if (tColor != 0) {
			rightBtn.setTextColor(getResources().getColor(tColor));
		}
		if (tSize != 0) {
			rightBtn.setTextSize(tSize);
		}
	}

	/**
	 * 左边按钮是否可用
	 * 
	 * @param isShow
	 *            true 显示，false 不显示
	 */
	public void setTopLeftCanUsed(boolean isShow) {
		// TODO Auto-generated method stub
		leftBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/**
	 * 右边按钮是否可用
	 * 
	 * @param isShow
	 *            true 显示，false 不显示
	 */
	public void setTopRightCanUsed(boolean isShow) {
		// TODO Auto-generated method stub
		rightBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/**
	 * topBar是否使用
	 * 
	 * @param isShow
	 *            true 显示，false 不显示
	 */
	public void setTopBarCanUsed(boolean isShow) {
		layoutTop.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}
}
