/**
 * @Title: Banzhu.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author deng_long_fei(longfei_deng@loongjoy.com) 
 * @date 2015年10月20日 下午1:41:35
 * @version V1.0
 */

package com.junova.huizhong.model;

/**
 * @ClassName: Banzhu
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年10月20日 下午1:41:35
 *
 */

public class Banzhu {

	private String date;
	private String level;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "{"+"date"+date+","+"level"+level+"}";
	}

}
