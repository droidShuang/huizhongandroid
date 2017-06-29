/**
 * @Title: DeviceParam.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-6 上午9:25:50
 * @version V1.0
 */

package com.junova.huizhong.model;

/**
 * @ClassName: DeviceParam
 * @Description: TODO
 * @author hao_mo@loongjoy.com
 * @date 2015-9-6 上午9:25:50
 * 
 */

public class DeviceParam {
	private String id;
	private int type;
	private String name;
	private String summray;
	private String specId;
	
	public DeviceParam(String id, int type, String name, String summray,
			String specId,int status) {
		super();
		this.id = id;
		this.type = type;//设备或隐患
		this.name = name;
		this.summray = summray;
		this.specId = specId;
		this.status = status;
	}

	private int status;// 设备状态(0待巡查，1巡查正常，2巡查异常，10待抽查，11抽查正常，12抽查异常)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummray() {
		return summray;
	}

	public void setSummray(String summray) {
		this.summray = summray;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public DeviceParam(String id, String name, String summray, String specId,
			int status) {
		super();
		this.id = id;
		this.name = name;
		this.summray = summray;
		this.specId = specId;
		this.status = status;
	}

}
