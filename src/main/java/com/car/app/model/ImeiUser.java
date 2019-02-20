package com.car.app.model;

import com.car.app.annotation.IdFlag;
import com.car.app.annotation.TableName;

@TableName("imei_user")
public class ImeiUser {

	public ImeiUser() {
		
	}
	
	@IdFlag
	private Integer id;
	private String account;
	private String imei;
	private String uType;
	private Integer count;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}

	
}
