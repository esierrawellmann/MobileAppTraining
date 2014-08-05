package com.digitalgeko.mobileapptraining.webservice;
import com.digitalgeko.mobileapptraining.dto.ServiceResponse;

public class UnsuccessfulWebServiceConsumeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ServiceResponse<?> response;
	
	public UnsuccessfulWebServiceConsumeException(ServiceResponse<?> response) {
		super(response.getMessage());
		this.response = response;
	}
	
	public ServiceResponse<?> getResponse() {
		return response;
	}
	
}
