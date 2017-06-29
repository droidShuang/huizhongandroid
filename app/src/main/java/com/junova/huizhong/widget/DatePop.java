/**
 * @Title: DatePop.java
 * @Package com.loongjoy.huizhong.widget
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-8-31 上午10:40:48
 * @version V1.0
 */

package com.junova.huizhong.widget;

import java.util.Date;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junova.huizhong.R;
import com.junova.huizhong.adapter.CalanderAdapter;

/**
 * @ClassName: DatePop
 * @Description: 选择日期的Pop
 * @author hao_mo@loongjoy.com
 * @date 2015-8-31 上午10:40:48
 * 
 */

public class DatePop extends PopupWindow implements OnClickListener {
	TextView currentMonth;
	ImageView last;
	ImageView next;
	GridView calander;
	CalanderAdapter adapter;
	int year;
	int month;

	private TextView okTextView;
	private TextView cancelTextView;
	public DatePop(Activity context ) {
		View contentView = LayoutInflater.from(context).inflate(
				R.layout.pop_calander, null);
		setContentView(contentView);
		currentMonth = (TextView) contentView.findViewById(R.id.month);
		last = (ImageView) contentView.findViewById(R.id.img_last_month);
		next = (ImageView) contentView.findViewById(R.id.img_next_month);
		last.setOnClickListener(this);
		next.setOnClickListener(this);
		calander = (GridView) contentView.findViewById(R.id.grid_calander);
		Date date = new Date(System.currentTimeMillis());
		year = date.getYear() + 1900;
		month = date.getMonth() + 1;
		currentMonth.setText(year + "年" + month + "月");
		adapter = new CalanderAdapter(context, year, month, this);
		calander.setAdapter(adapter);
		// setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		cancelTextView = (TextView) contentView.findViewById(R.id.cancel);
		cancelTextView.setOnClickListener(this);
		okTextView = (TextView) contentView.findViewById(R.id.ok);
		okTextView.setOnClickListener(this);

	}


	public int getDay() {
		return adapter.getDay();
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public String getStartDate() {
		return adapter.getStartDate();
	}

	public String getEndDate() {
		return adapter.getEndDate();
	}

	/**
	 * 
	 * setSelectedTwo 调用此方法 设置日历可选择起始时间 TODO(描述)
	 * 
	 * @Title: setSelectedTwo
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 * @author hao_mo@loongjoy.com
	 */
	public void setSelectedTwo() {
		adapter.setSelecteTwo(true);
	}

	/*
	 * <p>Title: onClick</p> <p>Description: </p>
	 * 
	 * @param arg0
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.img_last_month:
			month--;
			if (month <= 0) {
				month = 12;
				year--;
			}
			adapter.update(year, month);
			currentMonth.setText(year + "年" + month + "月");
			break;
		case R.id.img_next_month:
			month++;
			if (month >= 13) {
				month = 1;
				year++;
			}
			adapter.update(year, month);
			currentMonth.setText(year + "年" + month + "月");
			break;
		case R.id.cancel:
			adapter.setFirstDate("");
			adapter.setSecondDate("");
			dismiss();
			break;
		case R.id.ok:
			dismiss();
			break;
		default:
			break;
		}

	}
}
