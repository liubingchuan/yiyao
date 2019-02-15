package com.car.app.model;

import com.car.app.annotation.IdFlag;
import com.car.app.annotation.TableName;

@TableName("information")
public class Information {

	public Information() {
		
	}
	
	@IdFlag
	private Integer id;
	private String imei;
	private String sim;
	private String collectTime;
	private String longitude;
	private String latitude;
	private String iotstate;
	private Long timestamps;
	private String uType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSim() {
		return sim;
	}
	public void setSim(String sim) {
		this.sim = sim;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getIotstate() {
		return iotstate;
	}
	public void setIotstate(String iotstate) {
		this.iotstate = iotstate;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	public Long getTimestamps() {
		return timestamps;
	}
	public void setTimestamps(Long timestamps) {
		this.timestamps = timestamps;
	}

	
}
