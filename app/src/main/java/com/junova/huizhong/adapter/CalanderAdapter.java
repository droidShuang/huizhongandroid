package com.junova.huizhong.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.junova.huizhong.R;
import com.junova.huizhong.common.Logger;
import com.junova.huizhong.model.MyDate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CalanderAdapter extends BaseAdapter {
	Context con;
	int year;
	int month;
	List<MyDate> list;
	List<TextView> views;
	int day;
	PopupWindow pop;
	boolean canSelecteTwo;
	String firstDate = "";
	String secondDate = "";
	int clickTimes = 0;
	private String dateString = "";

	public void setSelecteTwo(boolean canSelecteTwo) {
		this.canSelecteTwo = canSelecteTwo;
	}

	public String getStartDate() {
		if (canSelecteTwo) {
			if (firstDate != "" && secondDate != "") {
				return Integer.valueOf(firstDate) > Integer.valueOf(secondDate) ? secondDate
						: firstDate;
			} else {
				return "";
			}
		} else {
			return "";
		}

	}

	public String getEndDate() {
		if (canSelecteTwo) {
			if (firstDate != "" && secondDate != "") {
				return Integer.valueOf(firstDate) > Integer.valueOf(secondDate) ? firstDate
						: secondDate;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public CalanderAdapter(Context con, int year, int month, PopupWindow pop) {
		this.con = con;
		this.year = year;
		this.month = month;
		this.pop = pop;
		initList();
		views = new ArrayList<TextView>();

		Date curdate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dateString = sdf.format(curdate);
		Log.e("dateString", dateString);
	}

	public void update(int year, int month) {
		this.year = year;
		this.month = month;
		initList();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0).getDay();
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public int getDay() {
		return day;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		arg1 = LayoutInflater.from(con).inflate(R.layout.grid_item, null);
		final TextView tv = (TextView) arg1.findViewById(R.id.day);
		// views.add(tv);
		final MyDate date = list.get(arg0);
		final int state = date.getState();
		switch (state) {
		case 0:
			tv.setText("");
			break;
		case 1:
			if (date.getDay() != 0) {

				tv.setText(date.getDay() + "");
				int week = getWeek(year, month, date.getDay());
				if (week == 0 || week == 6) {
					// 周末的位置
				}
			}
			break;
		default:
			break;
		}

		arg1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int yue = Integer.parseInt(dateString.split("-")[1]);
				int nian = Integer.parseInt(dateString.split("-")[0]);
				int ri = Integer.parseInt(dateString.split("-")[2]);
				Logger.getInstance().e("日期",
						"日" + ri + "           月" + yue + "======" + month);
				
				if (tv.getText().toString() != "" ) {
					if ( year < nian||( year ==nian &&  month < yue )|| (month ==yue && Integer.parseInt(tv.getText().toString()) <= ri )) {
						
					}else {
						return;
					}
				}else {
					return;
				}
				
				
				

				views.add(tv);
				day = date.getDay();
				String year1 = String.valueOf(year);
				String month1 = String.valueOf(month).length() >= 2 ? String
						.valueOf(month) : "0" + String.valueOf(month);
				String day1 = String.valueOf(day).length() >= 2 ? String
						.valueOf(day) : "0" + String.valueOf(day);
				int n = clickTimes % 2;// 余数为0 则是第一次点击 为1则是第二次点击
				clickTimes++;
				if (n == 0) {
					firstDate = year1 + month1 + day1;
				} else {
					secondDate = year1 + month1 + day1;
				}
				if (views.size() > 2) {
					TextView v1 = views.get(views.size() - 2);
					TextView V2 = views.get(views.size() - 1);
					TextView V3 = views.get(views.size() - 3);
					// 还原最开始填充颜色的View
					V3.setBackgroundResource(R.color.white);
					V3.setTextColor(con.getResources().getColor(R.color.black));
					views.clear();
					views.add(v1);
					views.add(V2);
				}
				initBackground();
			}

			// @Override
			// public void onClick(View arg0) {
			// if (state == 0) {
			// return;
			// }
			// day = date.getDay();
			// if (!canSelecteTwo) {
			// if (pop != null) {
			// pop.dismiss();
			// }
			// resetViews();
			// tv.setBackgroundResource(R.color.calander_selected);
			// tv.setTextColor(con.getResources().getColor(R.color.white));
			// } else {
			// int n = clickTimes % 2;// 余数为0 则是第一次点击 为1则是第二次点击
			// clickTimes++;
			// String year1 = String.valueOf(year);
			// String month1 = String.valueOf(month).length() >= 2 ? String
			// .valueOf(month) : "0" + String.valueOf(month);
			// String day1 = String.valueOf(day).length() >= 2 ? String
			// .valueOf(day) : "0" + String.valueOf(day);
			// if (n == 0) {
			// resetViews();
			// tv.setBackgroundResource(R.color.calander_selected);
			// tv.setTextColor(con.getResources().getColor(
			// R.color.white));
			// firstDate = year1 + month1 + day1;
			// } else {
			// tv.setBackgroundResource(R.color.calander_selected);
			// tv.setTextColor(con.getResources().getColor(
			// R.color.white));
			// secondDate = year1 + month1 + day1;
			// if (pop != null) {
			// pop.dismiss();
			// }
			// }
			//
			// }
			//
			// }
		});

		return arg1;

	}

	/**
	 * 填充选中颜色
	 */
	protected void initBackground() {
		if (views != null) {

			for (int i = 0; i < views.size(); i++) {
				TextView tv = views.get(i);
				tv.setBackgroundResource(R.color.calander_selected);
				tv.setTextColor(con.getResources().getColor(R.color.white));
			}
		}

	}

	private void resetViews() {
		if (views != null) {

			for (int i = 0; i < views.size(); i++) {
				TextView tv = views.get(i);
				tv.setBackgroundResource(R.color.white);
				tv.setTextColor(con.getResources().getColor(R.color.black));
			}
		}

	}

	private int getWeek(int year, int month, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(year + "-" + month + "-" + day);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calender = Calendar.getInstance();
		calender.setTime(date);
		int week = calender.get(Calendar.DAY_OF_WEEK) - 1;
		return week;

	}

	private int getStartPostion(int year, int month) {
		return getWeek(year, month, 1) + 1;
	}

	private int getMaxDay(int year, int month) {
		int days = 0;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			days = 31;
		} else if (month == 2) {
			if (year % 4 == 0 && year % 100 != 0) {
				days = 29;
			} else {
				days = 28;
			}
		} else {
			days = 30;
		}
		return days;
	}

	private void initList() {
		list = new ArrayList<MyDate>();
		int startPostion = getStartPostion(year, month);
		int days = getMaxDay(year, month);
		int lastMonth = getMaxDay(year, month - 1);
		int more = (startPostion + days - 1) % 7;

		for (int i = 1; i < startPostion; i++) {
			MyDate date = new MyDate(lastMonth - startPostion + i + 1, 0);
			list.add(date);
		}
		for (int i = 1; i <= days; i++) {
			MyDate date = new MyDate(i, 1);
			list.add(date);
		}
		if (more != 0) {
			for (int i = 1; i <= 7 - more; i++) {
				MyDate date = new MyDate(i, 0);
				list.add(date);
			}
		}

	}

	public void setFirstDate(String firstDate) {
		this.firstDate = firstDate;
	}

	public void setSecondDate(String secondDate) {
		this.secondDate = secondDate;
	}

	public String getFirstDate() {
		return firstDate;
	}

	public String getSecondDate() {
		return secondDate;
	}

}
