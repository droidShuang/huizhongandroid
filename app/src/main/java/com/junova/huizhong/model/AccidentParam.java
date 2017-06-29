package com.junova.huizhong.model;

public class AccidentParam {
	String desc;
	int level; // 1 安全 2 蓝色那个 3 可记录事故 黄色 4损失工作日 事故
	String url;
	String id;
	String time;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public AccidentParam(String desc, int level, String url, String id, String time) {
		super();
		this.desc = desc;
		this.level = level;
		this.url = url;
		this.id = id;
		this.time = time;
	}


}
