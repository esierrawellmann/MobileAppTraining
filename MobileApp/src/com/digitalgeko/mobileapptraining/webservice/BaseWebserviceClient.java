package com.digitalgeko.mobileapptraining.webservice;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.UUID;
import java.util.Vector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.widget.Toast;

import com.training.mobileapptraining.R;
import com.digitalgeko.mobileapptraining.dto.BaseRequest;
import com.digitalgeko.mobileapptraining.dto.ServiceResponse;
import com.digitalgeko.mobileapptraining.utilities.ConnectionHelper;
import com.digitalgeko.mobileapptraining.utilities.GeneralMethods;
import com.digitalgeko.mobileapptraining.webservice.Try.Failure;
import com.digitalgeko.mobileapptraining.webservice.Try.Success;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseWebserviceClient<T extends BaseRequest, S> {

	protected static final String DEVICE = "android";

	public static final int SHOW_LOADING_IN_DIALOG = 10;
	public static final int SHOW_LOADING_NONE = 11;
	public static final int SHOW_ERROR_IN_DIALOG = 20;
	public static final int SHOW_ERROR_IN_TOAST = 21;

	private WeakReference<Context> mContext;
	private int showLoadingType;
	private int showErrorType;
	private WeakReference<Try<ServiceResponse<S>>> result;
	private ConsumeServiceAsyncTask task;

	public BaseWebserviceClient(Context context) {
		mContext = new WeakReference<Context>(context);

		showLoadingType = SHOW_LOADING_IN_DIALOG;
		showErrorType = SHOW_ERROR_IN_DIALOG;
	}

	protected Context getContext() {
		return mContext.get();
	}

	public int getShowLoadingType() {
		return showLoadingType;
	}

	public void setShowLoadingType(int showLoadingType) {
		this.showLoadingType = showLoadingType;
	}

	public int getShowErrorType() {
		return showErrorType;
	}

	public void setShowErrorType(int showErrorType) {
		this.showErrorType = showErrorType;
	}

	protected String getInitialDialogString() {
		return getContext().getString(R.string.basedialoginitial);
	}

	protected void onUpdateDialogMessage(String message) {
		if(task != null) {
			task.updateDialogMessage(message);
		}
	}

	public boolean isSuccessful() {
		return result != null && result.get() != null && result.get().isSuccess();
	}

	public boolean isFailure() {
		return result != null && result.get() != null && result.get().isFailure();
	}

	protected void execute(String url, T request, TypeReference<ServiceResponse<S>> returnType) {
		try {
			if (onValidateInputs()) {
				RequestSpecification specification = new RequestSpecification();
				specification.url = url;
				specification.request = request;
				specification.responseType = returnType;

				task = new ConsumeServiceAsyncTask();
				task.execute(specification);
			}
		} catch (Exception exception) {
			onError(exception);
		}
	}

	protected Success<ServiceResponse<S>> makeRequest(RequestSpecification specification) throws Exception {

		ServiceResponse<S> response;

		// Build base request
		buildRequest(specification.request);

		// Build parameters
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		Vector<NameValuePair> vars = new Vector<NameValuePair>();
		vars.add(new BasicNameValuePair("data", mapper.writeValueAsString(specification.request)));

		// Make request
		String strResponse = connectWithServer(specification.url, vars, getContext());

		// String -> ServiceResponse<S>
		response = mapper.readValue(strResponse, specification.responseType);

		return new Success<ServiceResponse<S>>(response);
	}

	protected void buildRequest(BaseRequest request) throws NameNotFoundException {
		// Obtain Unique Id
		SharedPreferences myPrefs = getContext().getSharedPreferences(getContext().getString(R.string.app_name),
				Context.MODE_PRIVATE);
		String uniqueId = myPrefs.getString("uniqueId", "");
		if (uniqueId.length() == 0) {
			UUID uniqueKey = UUID.randomUUID();
			uniqueId = uniqueKey.toString();

			// Save it
			SharedPreferences.Editor e = myPrefs.edit();
			e.putString("uniqueId", uniqueId); // add or overwrite someValue
			e.commit(); // this saves to disk and notifies observers
		}

		// Obtain App Version
		PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
		String version = pInfo.versionName;
		// Get Device Version
		String deviceVersion = android.os.Build.VERSION.RELEASE;

		// Set params
		request.setUniqueIdentifier(uniqueId);
		request.setDevice(DEVICE);
		request.setVersion(version);
		request.setDeviceVersion(deviceVersion);
		// if (token != null) {
		// request.setToken(token);
		// }
	}

	protected String connectWithServer(String url, Vector<NameValuePair> vars, Context context) throws ClientProtocolException,
			IOException {
		return ConnectionHelper.getJsonObject(vars, getBaseUrl() + url);
	}

	public class ConsumeServiceAsyncTask extends AsyncTask<RequestSpecification, String, Try<ServiceResponse<S>>> {

		private ProgressDialog dialog;

		@Override
		protected Try<ServiceResponse<S>> doInBackground(RequestSpecification... requests) {
			try {
				return makeRequest(requests[0]);
			} catch (Exception e) {
				return new Failure<ServiceResponse<S>>(e);
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if (showLoadingType == SHOW_LOADING_IN_DIALOG) {
				dialog = ProgressDialog.show(getContext(), "", getInitialDialogString());
				dialog.show();
			}
		}

		@Override
		protected void onPostExecute(Try<ServiceResponse<S>> result) {
			super.onPostExecute(result);

			// Process response
			BaseWebserviceClient.this.result = new WeakReference<Try<ServiceResponse<S>>>(result);
			onComplete(result);
			
			// Dialog
			if (showLoadingType == SHOW_LOADING_IN_DIALOG) {
				dialog.dismiss();
			}
		}

		public void updateDialogMessage(String message) {
			publishProgress(message);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			// Dialog
			if (showLoadingType == SHOW_LOADING_IN_DIALOG) {
				String message = values[0];
				dialog.setMessage(message);
			}
		}

	}

	/*
	 * 
	 */

	/**
	 * 
	 * @return true iff all the inputs are valid. false otherwise.
	 * @throws ValidationException
	 */
	protected boolean onValidateInputs() throws ValidationException {
		return true;
	}

	protected abstract String getBaseUrl();

	// protected abstract String getServiceUrl();

	protected void onComplete(Try<ServiceResponse<S>> result) {
		// Validate response
		if (result.isSuccess()) {
			if (!result.get().isSuccessful()) {
				onError(new UnsuccessfulWebServiceConsumeException(result.get()));
			} else {
				onSuccess(result.get().getData());
			}
		} else {
			onError(result.getThrowable());
		}
	}

	protected void onSuccess(S response) {
	}

	protected void onError(Throwable throwable) {
		if (throwable instanceof ValidationException) {
			onShowError(throwable.getMessage());
		} else if (throwable instanceof UnsuccessfulWebServiceConsumeException) {
			onShowError(((UnsuccessfulWebServiceConsumeException) throwable).getResponse().getMessage());
		} else {
			onShowError(getDefaultErrorMessage());
		}
	}

	public void onShowError(String errorMessage) {
		if (showErrorType == SHOW_ERROR_IN_DIALOG) {
			GeneralMethods.crearDialogoOk(errorMessage, getContext());
		} else if (showErrorType == SHOW_ERROR_IN_TOAST) {
			Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
		}
	}

	protected String getDefaultErrorMessage() {
		return getContext().getString(R.string.default_error_message);
	}

	/*
	 * 
	 */
	private class RequestSpecification {
		public T request;
		public String url;
		public TypeReference<ServiceResponse<S>> responseType;
	}
}
