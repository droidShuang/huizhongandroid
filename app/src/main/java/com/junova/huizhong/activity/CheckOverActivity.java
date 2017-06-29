package com.junova.huizhong.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.junova.huizhong.R;
import com.junova.huizhong.base.BaseActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckOverActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterContentView(null,R.layout.activity_check_over);
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		setTopBarBg(R.drawable.hz_img_btn_t1);
		setTopLeftCanUsed(true);
		setTopRightCanUsed(false);
		initTopBarTitle(
				getResources().getString(R.string.title_activity_meter_detail),
				R.color.white, 20);
		initTopLeftBtn("返回", R.drawable.left_btn, null, R.color.top_txt, 17);
		TextView addTime = (TextView) findViewById(R.id.addTime);
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
		addTime.setText("当前日期："+format.format(new Date()));
		TextView allIcon = (TextView) findViewById(R.id.allIcon);
		allIcon.setText("累计积分："+134+"分");
		TextView thisIcon = (TextView) findViewById(R.id.thisIcon);
		thisIcon.setText("当前设备检查得分："+20+"分");
		Button ok = (Button) findViewById(R.id.btn);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	
}
