package com.car.app.common.request;

import java.util.List;

import com.car.app.common.BaseRequest;

public class FriendManagerRequest extends BaseRequest{

	private String account;
	
	private String friendaccount;
	
	private String operation;
	
	private String mesend;
	private String mereceive;
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
