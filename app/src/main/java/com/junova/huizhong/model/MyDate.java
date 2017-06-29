package com.junova.huizhong.model;

public class MyDate {
	int day;
	int state;// 0 上月和下月 ，灰色显示；1 本月

	public MyDate(int day, int state) {
		this.day = day;
		this.state = state;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
