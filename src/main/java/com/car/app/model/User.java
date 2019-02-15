package com.car.app.model;

import com.car.app.annotation.IdFlag;
import com.car.app.annotation.TableName;

@TableName("car_user")
public class User {

	public User() {
		
	}
	
	@IdFlag
	private Integer id;
	private String account;
	private String password;
	private String nick;
	private String imei;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}

}
