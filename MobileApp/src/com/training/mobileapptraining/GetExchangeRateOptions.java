package com.training.mobileapptraining;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.digitalgeko.mobileapptraining.dto.response.GetCurrencyValuesResponse;
import com.digitalgeko.mobileapptraining.dto.response.GetCurrencyValuesResponse.VariableEnvelop;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateByDateResponse;
import com.digitalgeko.mobileapptraining.dto.response.GetExchangeRateResponse;
import com.training.mobileapptraining.R;
import com.training.mobileapptraining.webservice.GetCurrencyValuesClient;
import com.training.mobileapptraining.webservice.GetExchangeRateByDateClient;
import com.training.mobileapptraining.webservice.GetExchangeRateClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GetExchangeRateOptions extends Activity {
	public static final int EXCHANGE_SELECTION_DOLLAR_DAY_EXCHANGE = 0;
	public static final int EXCHANGE_SELECTION_DOLLAR_BY_DATE_EXCHANGE = 1;
	
	int count=0;
	long userId=0;
	ListView  listView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.get_exchange_rate_layout);
        setTitle(R.string.choose_your_rate_option);
        
        listView1 = (ListView) findViewById(R.id.listView1);
        Intent myIntent = getIntent(); // gets the previously created intent
        String user = myIntent.getStringExtra("userId"); //
        userId = Long.parseLong(user);
        
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				SelectExchanceOptionFragment optionSelection = new SelectExchanceOptionFragment();
				TextView tv = (TextView)view.findViewById(R.id.tv_currency);
				TextView tc = (TextView)view.findViewById(R.id.tv_description);
				String option = tv.getText().toString();
				String currency = tc.getText().toString();
				optionSelection.setOption(Integer.parseInt(option));
				optionSelection.setCurrency(currency);
				optionSelection.show(getFragmentManager(),"Exchange Rate Selection");
				// TODO Auto-generated method stub
			}
        });
        new GetCurrencyValues(this).execute(userId);
	}
	
	public Context getContext() {
		return this;
	}
	
	@SuppressWarnings("deprecation")
	public void showMessageDialog(String message){
		AlertDialog alertDialog = new AlertDialog.Builder(
		GetExchangeRateOptions.this).create();
		alertDialog.setTitle(R.string.app_title);
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
	public class GetCurrencyValues extends GetCurrencyValuesClient{

		public GetCurrencyValues(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		@Override
		protected void onSuccess(GetCurrencyValuesResponse response) {
			super.onSuccess(response);
			if (this.isSuccessful()) {
				// Save responsible
				List<GetCurrencyValuesResponse.VariableEnvelop> currencyValues = response.getVariables();			
				CurrencyAdapter adaptador = new CurrencyAdapter(getContext(),
					        android.R.layout.simple_list_item_1, currencyValues);
				listView1.setAdapter(adaptador);
				
			}
		}
		
	}
	public class GetTodayExchangeRate extends GetExchangeRateClient {
		public GetTodayExchangeRate(Context context) {
			super(context);
		}
		@Override
		protected void onSuccess(GetExchangeRateResponse response) {
			super.onSuccess(response);
			if (this.isSuccessful()) {
				// Save responsible
				showMessageDialog("Referencia al dia : "+response.getExchangeRate() );
			}
		}
	}
	public class GetExchangeRateByDate extends GetExchangeRateByDateClient {
		public GetExchangeRateByDate(Context context) {
			super(context);
		}
		@Override
		protected void onSuccess(GetExchangeRateByDateResponse response) {
			super.onSuccess(response);
			if (this.isSuccessful()) {
				// Save responsible
				showMessageDialog("Referencia de la fecha seleccionada: "+response.getExchangeRate());
				count = 0; 
			}
		}
	}
	
	public  class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {
		int option;
		public int getOption() {
			return option;
		}
		public void setOption(int option) {
			this.option = option;
		}
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		@Override
		 public  void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
			count++;
			if(count<2){
				new GetExchangeRateByDate(getContext()).execute(getOption(),day+"/"+(month+1)+"/"+year,userId);
				
			}
		}
	}

	
	public class CurrencyAdapter extends ArrayAdapter<GetCurrencyValuesResponse.VariableEnvelop> {

		public CurrencyAdapter(Context context, int resource,
				List<VariableEnvelop> objects) {
			super(context, resource, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder;
			if(convertView == null) {
				// Crear nuevo view
				LayoutInflater inflater = LayoutInflater.from(GetExchangeRateOptions.this);
				convertView = inflater.inflate(R.layout.item_currency, parent, false);
				
				// Crear holder
				holder = new ViewHolder();
				holder.tvCurrency = (TextView) convertView.findViewById(R.id.tv_currency);
				holder.tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
				
				// Guardar el holder
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			// Obtener currency actual
			GetCurrencyValuesResponse.VariableEnvelop variable = getItem(position);
			
			holder.tvCurrency.setText(Integer.toString(variable.getVariable().getMoneda()));
			holder.tvDescription.setText(variable.getVariable().getDescripcion());
			
			return convertView;
		}
		
	}
	
	public static class ViewHolder {
		public TextView tvCurrency;
		public TextView tvDescription;
	}
	
	public class SelectExchanceOptionFragment extends DialogFragment {
		int option;
		String currency;
	    public String getCurrency() {
			return currency;
		}
		public void setCurrency(String currency) {
			this.currency = currency;
		}
		public int getOption() {
			return option;
		}
		public void setOption(int option) {
			this.option = option;
		}
		@Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
	        builder.setMessage(getOption() + " " +getCurrency())
	               .setPositiveButton(R.string.day, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
							new GetTodayExchangeRate(getContext()).execute(userId,getOption()); 
	                   }
	               })
	               .setNegativeButton(R.string.date, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   DatePickerFragment newFragment = new DatePickerFragment();
	                	   newFragment.setOption(getOption());
						   newFragment.show(getFragmentManager(), "datePicker");
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
}
