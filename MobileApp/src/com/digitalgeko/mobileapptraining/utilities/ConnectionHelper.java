package com.digitalgeko.mobileapptraining.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.training.mobileapptraining.R;

import android.content.Context;
import android.util.Log;

public final class ConnectionHelper {

	public static Context context;

	public static String getJsonObject(Vector<NameValuePair> vars, String url)
			throws ClientProtocolException, IOException {

		String strResponse = null;
		String finalurl = url;

		Log.i("url", finalurl);
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(finalurl);
		request.addHeader("Accept", "application/json");
		HttpResponse response;

		HttpParams params = client.getParams();
		int timeoutConnection = 60000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		int timeoutSocket = 120000;
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);

		
		// este es el que se cambie para que acepte el nuevo server.
		client = new DefaultHttpClient(params);
		// client = new MyHttpClient(params);

		Log.d("parametros", vars.toString());
		request.setEntity(new UrlEncodedFormEntity(vars, "UTF-8"));

		response = client.execute(request);
		Log.i("Inicial Json", response.getStatusLine().toString());
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			InputStream instream = entity.getContent();
			strResponse = convertStreamToString(instream);
			Log.i("Result Stream", strResponse);

		}

		return strResponse;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * Clase para realizar conexiones hacia certificados autogenerados o no
	 * v???lidos, este debe ser envevido en la aplicaci???n para poder ser
	 * validado.
	 */

	static class MyHttpClient extends DefaultHttpClient {

		public MyHttpClient(HttpParams params) {
			super(params);
		}

		@Override
		protected ClientConnectionManager createClientConnectionManager() {
			SchemeRegistry registry = new SchemeRegistry();
			// registry.register(new Scheme("http",
			// PlainSocketFactory.getSocketFactory(), 80));
			// Register for port 443 our SSLSocketFactory with our keystore
			// to the ConnectionManager
			// registry.register(new Scheme("https", newSslSocketFactory(),
			// 9080));
			registry.register(new Scheme("https", newSslSocketFactory(), 443));
			return new SingleClientConnManager(getParams(), registry);
		}

		private SSLSocketFactory newSslSocketFactory() {
			try {
				// Get an instance of the Bouncy Castle KeyStore format
				KeyStore trusted = KeyStore.getInstance("BKS");
				
				// Get the raw resource, which contains the keystore with
				// your trusted certificates (root and any intermediate certs)
				InputStream in = context.getResources().openRawResource(R.raw.certificado );
				try {
					// Initialize the keystore with the provided trusted
					// certificates
					// Also provide the password of the keystore
					trusted.load(in, "dominos".toCharArray());
				} finally {
					in.close();
				}
				// Pass the keystore to the SSLSocketFactory. The factory is
				// responsible
				// for the verification of the server certificate.
				SSLSocketFactory sf = new SSLSocketFactory(trusted);
				// Hostname verification from certificate
				// http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				return sf;
			} catch (Exception e) {
				throw new AssertionError(e);
			}
			// return null;
		}
	}

}
