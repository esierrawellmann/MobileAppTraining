package com.digitalgeko.mobileapptraining.dto.request;

import com.digitalgeko.mobileapptraining.dto.BaseRequest;

public class LogInRequest extends BaseRequest {
	String username;
	String password;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
