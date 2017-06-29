/**
 * @Title: ActiveItemParam.java
 * @Package com.loongjoy.huizhong.model
 * @Description: TODO
 * Copyright: Copyright 2015 loongjoy.inc
 * Company:上海龙照电子有限公司
 * 
 * @author hao_mo@loongjoy.com
 * @date 2015-9-9 下午3:16:08
 * @version V1.0
 */

package com.junova.huizhong.model;

import java.util.List;

/**
 * @ClassName: ActiveItemParam
 * @Description: TODO
 * @author hao_mo@loongjoy.com
 * @date 2015-9-9 下午3:16:08
 * 
 */

public class ActiveItemParam {
	String id;
	String title;
	List<ActiveContentParam> contents;

	public ActiveItemParam(String id, String title,
			List<ActiveContentParam> contents) {
		super();
		this.id = id;
		this.title = title;
		this.contents = contents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ActiveContentParam> getContents() {
		return contents;
	}

	public void setContents(List<ActiveContentParam> contents) {
		this.contents = contents;
	}
}
