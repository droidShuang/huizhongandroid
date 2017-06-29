/**
 * @Title: ExceptionParam.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-7 下午2:27:26
 * @version V1.0
 */

package com.junova.huizhong.model;

/**
 * @ClassName: ExceptionParam
 * @Description: TODO
 * @author hao_mo@loongjoy.com
 * @date 2015-9-7 下午2:27:26
 * 
 */

public class ExceptionParam {

	String id;
	String name;
	boolean isChecked;
	String photoes;
	String desc;

	public ExceptionParam(String id, String name, boolean isChecked,
			String photoes, String desc) {
		super();
		this.id = id;
		this.name = name;
		this.isChecked = isChecked;
		this.photoes = photoes;
		this.desc = desc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getPhotoes() {
		return photoes;
	}

	public void setPhotoes(String photoes) {
		this.photoes = photoes;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
