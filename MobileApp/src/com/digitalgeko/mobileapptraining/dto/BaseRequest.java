package com.digitalgeko.mobileapptraining.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseRequest {

	protected String token;
	
	@JsonProperty(value = "deviceOS")
	protected String device;
	
	@JsonProperty(value = "deviceUID")
	protected String uniqueIdentifier;
	
	@JsonProperty(value = "appVersion")
	protected String version;
	
	@JsonProperty(value = "deviceOSVersion")
	protected String deviceVersion;

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
