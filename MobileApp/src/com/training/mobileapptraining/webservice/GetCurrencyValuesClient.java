package com.training.mobileapptraining.webservice;

import android.content.Context;

import com.digitalgeko.mobileapptraining.dto.ServiceResponse;
import com.digitalgeko.mobileapptraining.dto.request.GetCurrencyValuesRequest;
import com.digitalgeko.mobileapptraining.dto.response.GetCurrencyValuesResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class GetCurrencyValuesClient extends TrainingBaseWebService<GetCurrencyValuesRequest,GetCurrencyValuesResponse>{
	
	private static final String URL = "Services/getcurrencyvalues";
	
	public GetCurrencyValuesClient(Context context) {
		super(context);
	}
	
	public void execute(long userId) {
		// Request
		GetCurrencyValuesRequest request = new GetCurrencyValuesRequest();
			request.setUserId(userId);
			execute(URL, request, new TypeReference<ServiceResponse<GetCurrencyValuesResponse>>() {
		});
	}
	

}
