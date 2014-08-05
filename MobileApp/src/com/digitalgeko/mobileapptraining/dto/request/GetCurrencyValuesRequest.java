package com.digitalgeko.mobileapptraining.dto.request;

import com.digitalgeko.mobileapptraining.dto.BaseRequest;

public class GetCurrencyValuesRequest extends BaseRequest{
	long userId;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}
