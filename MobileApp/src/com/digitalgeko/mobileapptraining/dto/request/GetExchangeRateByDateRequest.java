package com.digitalgeko.mobileapptraining.dto.request;

import com.digitalgeko.mobileapptraining.dto.BaseRequest;

public class GetExchangeRateByDateRequest extends BaseRequest {
	String date;
	long userId;
	int option;
	public int getOption() {
		return option;
	}

	public void setOption(int option) {
		this.option = option;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
