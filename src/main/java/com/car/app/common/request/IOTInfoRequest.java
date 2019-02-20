package com.car.app.common.request;

import com.car.app.common.BaseRequest;

public class IOTInfoRequest extends BaseRequest{

	private String imei;
	
	private String sim;
	
	private String time;
	
	private String longitude;
	
	private String latitude;
	
	private String iotstate;
	
	private String type;

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
