

package com.training.mobileapptraining.webservice;

import android.content.Context;

import com.digitalgeko.mobileapptraining.dto.ServiceResponse;
import com.digitalgeko.mobileapptraining.dto.request.GetExchangeRateRequest;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateResponse;
import com.fasterxml.jackson.core.type.TypeReference;

public class GetExchangeRateClient extends TrainingBaseWebService<GetExchangeRateRequest, GetExchangeRateResponse> {

	private static final String URL = "Services/getexchangerate";

	// "backend/ResponbileWs/getAllResponsible";

	public GetExchangeRateClient(Context context) {
		super(context);
	}

	public void execute(long userId,int option) { 
		// Request
		GetExchangeRateRequest request = new GetExchangeRateRequest();
		request.setUserId(userId);
		request.setOption(option);

		// Specification
		execute(URL, request, new TypeReference<ServiceResponse<GetExchangeRateResponse>>() {
		});
	}
}
