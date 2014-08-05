package com.training.mobileapptraining.webservice;

import android.content.Context;
import com.digitalgeko.mobileapptraining.dto.BaseRequest;
import com.digitalgeko.mobileapptraining.webservice.BaseWebserviceClient;

public class TrainingBaseWebService<T extends BaseRequest, S> extends BaseWebserviceClient<T, S> {

	// private static final String BASE_URL = "http://192.168.10.117:9300/"; // Wario
	// private static final String BASE_URL = "http://192.168.10.45:9300/"; // Carlo O
	//private static final String BASE_URL = "http://mbanking.digitalgeko.com:9300/";
	private static final String BASE_URL = "http://192.168.10.106:9000/"; // Erik S
	// private static final String BASE_URL = "http://roya.anacafe.org/";

	public TrainingBaseWebService(Context context) {
		super(context);
	} 

	@Override
	protected String getBaseUrl() {
		return BASE_URL;
	}

	@Override
	protected void onError(Throwable throwable) {
		throwable.printStackTrace();

		super.onError(throwable);
	}
}
