package com.car.app.model;

import com.car.app.annotation.IdFlag;
import com.car.app.annotation.TableName;

@TableName("friends")
public class Friends {

	public Friends() {
		
	}
	
	@IdFlag
	private Integer id;
	private String account;
	private String friendaccount;
	private String operation;
	private String mesend;
	private String mereceive;
	private String uType;
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
	public String getFriendaccount() {
		return friendaccount;
	}
	public void setFriendaccount(String friendaccount) {
		this.friendaccount = friendaccount;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	public String getMesend() {
		return mesend;
	}
	public void setMesend(String mesend) {
		this.mesend = mesend;
	}
	public String getMereceive() {
		return mereceive;
	}
	public void setMereceive(String mereceive) {
		this.mereceive = mereceive;
	}

}
