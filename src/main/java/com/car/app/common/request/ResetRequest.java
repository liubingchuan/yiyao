package com.car.app.common.request;

import com.car.app.common.BaseRequest;

public class ResetRequest extends BaseRequest{

	private String account;
	
	private String password;
	
	private String type;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
