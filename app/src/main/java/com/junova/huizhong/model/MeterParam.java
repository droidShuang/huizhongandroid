package com.junova.huizhong.model;

import java.io.Serializable;
import java.util.List;

/**
 * 设备 or 仪表仪器
 * @author Administrator
 *
 */
public class MeterParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String title;
	private String no;
	private String detail;
	private int status;
	private String logo;
	private List<CheckParam> checkList;
	
	/**
	 * 设备 OR 仪器 params
	 * @param id 
	 * @param title 标题
	 * @param no 设备编号
	 * @param detail 设备详情描述
	 * @param status 设备状态
	 * @param checkParams 设备检查项
	 */
	public MeterParam(String id,String logo,String title,String no,String detail,int status,List<CheckParam> checkParams) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.title = title;
		this.no = no ;
		this.detail = detail;
		this.status = status;
		this.checkList = checkParams;
		this.logo = logo;
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
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<CheckParam> getCheckList() {
		return checkList;
	}
	public void setCheckList(List<CheckParam> checkList) {
		this.checkList = checkList;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
}
