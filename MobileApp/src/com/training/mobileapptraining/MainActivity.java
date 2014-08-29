package com.training.mobileapptraining;


import com.pixate.freestyle.PixateFreestyle;
import com.training.mobileapptraining.R;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateByDateResponse;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateResponse;
import com.digitalgeko.mobileapptraining.dto.response.LogInResponse;
import com.training.mobileapptraining.webservice.GetExchangeRateByDateClient;
import com.training.mobileapptraining.webservice.GetExchangeRateClient;
import com.training.mobileapptraining.webservice.LogInClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final Activity thisActivity = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		PixateFreestyle.init(this);
        setContentView(R.layout.activity);
        setTitle(R.string.app_title);
	}
	@Override
	protected void onStart(){
		super.onStart();
	}
	public void logIn(View view){
		
		EditText username   = (EditText)findViewById(R.id.editText1);
		EditText password   = (EditText)findViewById(R.id.editText2);
	    new LogIn(this).execute(username.getText().toString(), password.getText().toString());
	}
	
	public class LogIn extends LogInClient {
		public LogIn(Context context) {
			super(context);
		}
		@Override
		protected void onSuccess(LogInResponse response) {
			super.onSuccess(response);
			if (this.isSuccessful()) {
				if(response.getLoginSucceed()){
					
					Intent intent = new Intent(thisActivity, GetExchangeRateOptions.class);
					intent.putExtra("userId",""+response.getUserId());
				    startActivity(intent);
			    }else{
			    	showMessageDialog(getResources().getString(R.string.combination));
			    }
			}
		}
	}
	public void showMessageDialog(String message){
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle(R.string.default_error_message);
		alertDialog.setMessage(message);
		alertDialog.setIcon(R.drawable.ic_launcher);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				//Toast.makeText(getApplicationContext(), R.string.getting_information, Toast.LENGTH_SHORT).show();
				}
		});
		
		// Showing Alert Message
		alertDialog.show();
	}
}

