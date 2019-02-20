package com.yiyao.app.common.request;

import com.yiyao.app.common.BaseRequest;

public class RegisterRequest extends BaseRequest{

	private String account;
	
	private String imei;
	
	private String password;
	
	private String type;

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
