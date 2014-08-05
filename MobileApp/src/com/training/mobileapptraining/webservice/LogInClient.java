package com.training.mobileapptraining.webservice;

import android.content.Context;

import com.digitalgeko.mobileapptraining.dto.ServiceResponse;
import com.digitalgeko.mobileapptraining.dto.request.GetExchangeRateRequest;
import com.digitalgeko.mobileapptraining.dto.request.LogInRequest;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateResponse;
import com.digitalgeko.mobileapptraining.dto.response.LogInResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class LogInClient  extends TrainingBaseWebService<LogInRequest, LogInResponse>{

	private static final String URL = "Services/login";
	public LogInClient(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void execute(String username,String password){
		LogInRequest request = new LogInRequest();
		request.setPassword(password);
		request.setUsername(username);
		// Specification
		execute(URL, request, new TypeReference<ServiceResponse<LogInResponse>>() {
		});
	}

}
