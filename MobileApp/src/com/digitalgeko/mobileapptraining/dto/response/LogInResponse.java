package com.digitalgeko.mobileapptraining.dto.response;

public class LogInResponse {
	boolean loginSucceed;
	long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean getLoginSucceed() {
		return loginSucceed;
	}

	public void setLoginSucceed(boolean loginSucceed) {
		this.loginSucceed = loginSucceed;
	}
}
