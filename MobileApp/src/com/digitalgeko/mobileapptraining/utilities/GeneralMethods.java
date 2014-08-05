package com.digitalgeko.mobileapptraining.utilities;

import java.math.BigDecimal;

import android.R;

import org.json.JSONArray;
import org.json.JSONException;

import com.training.mobileapptraining.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.util.Log;

@SuppressLint("UseValueOf")
public final class GeneralMethods {

	public static boolean isKey(JSONArray nombres, String key) {
		boolean response = false;
		for (int i = 0; i < nombres.length(); i++) {
			try {
				if (nombres.getString(i).equals(key)) {
					response = true;
				}
			} catch (JSONException e) {
				Log.e("GeneralMethods - isKey", e.getMessage());
			}
		}
		return response;
	}

	public static String getAsteriscos(String cuenta) {
		String result = cuenta.substring(0, 4);
		for (int i = 0; i < cuenta.length() - 8; i++) {
			result += "X";
		}
		result += cuenta.substring(cuenta.length() - 4, cuenta.length());
		return result;
	}

	public static String getDate(String date) {
		if (date.length() == 8) {
			return date.substring(6) + "/" + date.substring(4, 6) + "/" + date.substring(0, 4);
		} else {
			return "00/00/0000";
		}
	}

	public static void crearDialogoOk(String message, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}

	public static void creadDialogFinish(String message, Context context, final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				activity.finish();
			}
		});
		builder.create().show();
	}

	public static void crearDialogoExit(String message, final Context context, final Activity activity) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				redirectToNewRoot(context, MainActivity.class);

				// AccountRepository.initVars("");
			}
		});
		builder.create().show();
	}

	public static void redirectToNewRoot(Context context, Class<?> cls) {
		Intent intent = new Intent(context.getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

		// Intent intentToBeNewRoot = new Intent(context, cls);
		// ComponentName cn = intentToBeNewRoot.getComponent();
		// Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
		// context.startActivity(mainIntent);
	}

	public static void exitOfSystem(final Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		// builder.setMessage(context.getString(R.string.cerrarSesion));
		builder.setPositiveButton("Aceptar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				// context.startActivity(new Intent().setComponent(new
				// ComponentName(context, ViewLogin.class)));
				// AccountRepository.initVars("");
			}
		});

		builder.setNegativeButton("Cancelar", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		builder.create().show();
	}

	public static String formatAmount(String valor) {
		return new BigDecimal(new Double(valor)).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
	}

	public static String formatAmount(BigDecimal valor) {
		return valor.setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
	}

	public static String putComas(String valor) {
		String result = valor;
		int index = result.indexOf(".");
		switch (index) {
		case 4:
			if (result.indexOf("-") < 0) {
				result = result.substring(0, 1) + "," + result.substring(1);
			}
			break;
		case 5:
			result = result.substring(0, 2) + "," + result.substring(2);
			break;
		case 6:
			result = result.substring(0, 3) + "," + result.substring(3);
			break;
		case 7:
			if (result.indexOf("-") < 0) {
				result = result.substring(0, 1) + "," + result.substring(1, 4) + "," + result.substring(4);
			} else {
				result = result.substring(0, 4) + "," + result.substring(4);
			}
			break;
		case 8:
			result = result.substring(0, 2) + "," + result.substring(2, 5) + "," + result.substring(5);
			break;
		case 9:
			result = result.substring(0, 3) + "," + result.substring(3, 6) + "," + result.substring(6);
			break;
		case 10:
			if (result.indexOf("-") < 0) {
				result = result.substring(0, 1) + "," + result.substring(1, 4) + "," + result.substring(4, 7) + ","
						+ result.substring(7);
			} else {
				result = result.substring(0, 4) + "," + result.substring(4, 7) + "," + result.substring(7);
			}
			break;
		case 11:
			result = result.substring(0, 2) + "," + result.substring(2, 5) + "," + result.substring(5, 8) + ","
					+ result.substring(8);
			break;
		}
		return result;
	}

	public static String quitComas(String valor) {
		String[] datos = valor.split(",");
		valor = "";
		for (int i = 0; i < datos.length; i++) {
			valor += datos[i];
		}
		return valor;
	}

	public static boolean validarString(String val) {
		return val.matches("[ A-Za-z]+");
	}

	public static boolean validarDocumento(String doc) {
		return doc.matches("[- A-Za-z0-9]+");
	}

}
