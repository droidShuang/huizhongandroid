/**
 * @Title: PartParam.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-6 下午2:33:13
 * @version V1.0
 */

package com.junova.huizhong.model;

/**
 * @ClassName: PartParam
 * @Description: TODO
 * @author hao_mo@loongjoy.com
 * @date 2015-9-6 下午2:33:13
 * 
 */

public class PartParam {
	String id;
	int station;
	String name;

	public PartParam(String id, int station, String name) {
		super();
		this.id = id;
		this.station = station;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
