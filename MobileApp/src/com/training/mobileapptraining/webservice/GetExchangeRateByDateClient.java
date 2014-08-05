package com.training.mobileapptraining.webservice;

import android.content.Context;

import com.digitalgeko.mobileapptraining.dto.ServiceResponse;
import com.digitalgeko.mobileapptraining.dto.request.GetExchangeRateByDateRequest;
import com.digitalgeko.mobileapptraining.dto.request.GetExchangeRateRequest;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateByDateResponse;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class GetExchangeRateByDateClient extends TrainingBaseWebService<GetExchangeRateByDateRequest,GetExchangeRateByDateResponse> {

	private static final String URL = "Services/getexchangeratebydate";
	public GetExchangeRateByDateClient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void execute(int option,String date,long userId) {
		// Request
		GetExchangeRateByDateRequest request = new GetExchangeRateByDateRequest();
		request.setOption(option);
		request.setUserId(userId);
		// Specification
		request.setDate(date);
		execute(URL, request, new TypeReference<ServiceResponse<GetExchangeRateByDateResponse>>() {
		});
	}
}
