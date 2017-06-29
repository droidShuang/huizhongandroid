package com.junova.huizhong.model;

public class ActiveParam {
	int type; // 0代表日活动 ，1 代表月度活动
	String title;
	String summary;
	int monthTimes; // 总数
	String id;
	int completeTimes; //完成
	private String day;



	public ActiveParam(int type, String title, String summary, int monthTimes,
			String id, int completeTimes, String day) {
		super();
		this.type = type;
		this.title = title;
		this.summary = summary;
		this.monthTimes = monthTimes;
		this.id = id;
		this.completeTimes = completeTimes;
		this.day = day;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getMonthTimes() {
		return monthTimes;
	}

	public void setMonthTimes(int monthTimes) {
		this.monthTimes = monthTimes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCompleteTimes() {
		return completeTimes;
	}

	public void setCompleteTimes(int completeTimes) {
		this.completeTimes = completeTimes;
	}
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
}
