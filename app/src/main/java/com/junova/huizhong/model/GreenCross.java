/**
 * @Title: GreenCross.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author deng_long_fei(longfei_deng@loongjoy.com) 
* @date 2015年10月20日 上午11:31:36
 * @version V1.0
 */

package com.junova.huizhong.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: GreenCross
 * @Description: TODO
 * @author longfei_deng@loongjoy.com
 * @date 2015年10月20日 上午11:31:36
 *
 */

public class GreenCross {
	private String teamName;
	private String partId;
	private ArrayList<Banzhu> banzList;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public List<Banzhu> getBanzList() {
		return banzList;
	}
	public void setBanzList(ArrayList<Banzhu> banzList) {
		this.banzList = banzList;
	}
	public String toString() {
		return "{"+"teamName:"+teamName+","+"banzList"+banzList.toString()+"}";
	}
	public String getTeamId(){
		return partId;
	}
	public void setTeamId(String partId){
		this.partId=partId;

	}
}

