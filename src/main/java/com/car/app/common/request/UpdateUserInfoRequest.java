package com.car.app.common.request;

import java.util.List;

import com.car.app.common.BaseRequest;

public class UpdateUserInfoRequest extends BaseRequest{

	private List<String> imeis;
	
	private String account;
	
	private String nick;
	
	private String type;

	public List<String> getImeis() {
		return imeis;
	}

	public void setImeis(List<String> imeis) {
		this.imeis = imeis;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}
