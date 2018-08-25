package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.ak.app.wetzel.activity.R;

public class WebHTTPMethodClass {
	static int mTimeoutConnection = 60000;

	@SuppressLint("LongLogTag")
	public static String httpGetService(String serviceName, String param) {
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = mTimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = mTimeoutConnection;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet getMethod = new HttpGet(R.string.BASE_URL + serviceName
					+ "?" + param);
			Log.e(serviceName + " GetURL = ",  R.string.BASE_URL
					+ serviceName + "?" + param);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure: 401",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressLint("LongLogTag")
	public static String httpDeleteService(String serviceName, String param) {
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = mTimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = mTimeoutConnection;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpDelete getMethod = new HttpDelete(R.string.BASE_URL
					+ serviceName + "?" + param);
			Log.e(serviceName + " deleteURL = ", R.string.BASE_URL
					+ serviceName + "?" + param);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure:401 ",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");
			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressLint("LongLogTag")
	public static String executeHttPost(String serviceName,
										List<NameValuePair> mParams) {
		String responseData = null;

		for (int i = 0; i < mParams.size(); i++) {
			Log.d("fb-le6end4", "Params ["
					+ mParams.get(i).getName().toString() + "] :"
					+ mParams.get(i).getValue().toString());
		}

		HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				mTimeoutConnection);

		DefaultHttpClient client = new DefaultHttpClient(httpParameters);

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory
				.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		SingleClientConnManager mgr = new SingleClientConnManager(
				client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
				client.getParams());

		// Set verifier
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		// DefaultHttpClient client = new DefaultHttpClient(httpParameters);
		HttpPost post = new HttpPost(R.string.BASE_URL_HTTPS  + serviceName);
		Log.e(serviceName + " postURL = ", R.string.BASE_URL  + serviceName
				+ " " + mParams.toArray());
		try {
			post.setEntity(new UrlEncodedFormEntity(mParams/* , HTTP.UTF_8 */));
			HttpResponse response = httpClient.execute(post);
			responseData = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure:401 ",
						"" + responseData.toString());
				AppConstants.ERROR401 = response.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");
			responseData = responseData.trim();
			// responseData = checkFor401Error(response, responseData);
		} catch (Exception e) {
			responseData = "UnsupportedEncodingException";
			if (e != null)
				e.printStackTrace();
		}
		System.out.println(serviceName + " result = " + responseData);
		return responseData;
	}

	//
	// public static String executeHttPost(String serviceName,
	// List<NameValuePair> mParams) {
	// String responseData = null;
	// HttpParams httpParameters = new BasicHttpParams();
	// HttpConnectionParams.setConnectionTimeout(httpParameters,
	// mTimeoutConnection);
	// DefaultHttpClient client = new DefaultHttpClient(httpParameters);
	// HttpPost post = new HttpPost(AppConstants.BASE_URL_HTTPS + serviceName);
	// Log.e(serviceName + " postURL = ", AppConstants.BASE_URL + serviceName
	// + " " + mParams.toArray());
	// try {
	// post.setEntity(new UrlEncodedFormEntity(mParams/* , HTTP.UTF_8 */));
	// HttpResponse response = client.execute(post);
	// responseData = EntityUtils.toString(response.getEntity());
	// if (response.getStatusLine().getStatusCode() == 401) {
	// Log.i("Response Json Failure:401 ", "" + responseData.toString());
	// AppConstants.ERROR401 = response.getStatusLine().getStatusCode()+"";
	// }
	// else
	// Log.i("Response Json 401 not fount", "" + "httpResponse.toString()");
	// responseData = responseData.trim();
	// // responseData = checkFor401Error(response, responseData);
	// } catch (Exception e) {
	// responseData = "UnsupportedEncodingException";
	// if (e != null)
	// e.printStackTrace();
	// }
	// System.out.println(serviceName + " result = " + responseData);
	// return responseData;
	// }
	//
	// private static String checkFor401Error(HttpResponse response,
	// String responseData) {
	// try {
	// // String sResponse = AppConstants.inputStreamToString(
	// // response.getEntity().getContent()).toString();
	// JSONObject responseJson = new JSONObject(responseData);
	// if (response.getStatusLine().getStatusCode() == 401) {
	// Log.i("Response Json Failure:", "" + responseData.toString());
	// // AppConstants.showMessageDialogWithNewIntent(
	// // AppConstants.ERROR401SERVICES, HomePage.getInstance());
	// AppConstants.ERROR401 = response.getStatusLine().getStatusCode()+"";
	// responseData = "";
	// } else if (responseJson.getBoolean("status") == false) {
	// Log.i("Response Json Failure:", "" + responseJson);
	// if (responseJson.has("notice"))
	// message = responseJson.getString("notice");
	// if (message.equals("") && responseJson.has("message"))
	// message = responseJson.getString("message");
	// if (!message.equals("")
	// && message.equals("Unauthorized API request.")) {
	// // loginHandler.sendEmptyMessage(0);
	// // AppConstants.showMessageDialogWithNewIntent(message,
	// // HomePage.getInstance());
	// // message = responseJson.getString("notice");
	// responseData = "";
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return responseData;
	// }

	// static String message = "";
	// private static Handler loginHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// AppConstants.showMessageDialogWithNewIntent(message,
	// HomePage.getInstance());
	// message = "";
	// }
	// };

	public static String postAppJsonStringToServer(String jsonString, String URL) {
		HttpClient client = new DefaultHttpClient();
		HttpConnectionParams.setConnectionTimeout(client.getParams(),
				mTimeoutConnection);
		HttpResponse httpresponse;
		String response = "";
		try {
			HttpPost post = new HttpPost(URL);
			Log.e("jsonRequest:", jsonString);
			Log.e("URL:", URL);
			StringEntity se = new StringEntity(jsonString);
			post.setHeader("Content-type", "application/json");

			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));
			post.setEntity(se);
			httpresponse = client.execute(post);
			response = EntityUtils.toString(httpresponse.getEntity()).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(URL + " result = " + response);
		return response;
	}

	public static String callAuthServiceHttpPost(String customerId,
			String... params) {
		// Create a new HttpClient and Post Header
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = mTimeoutConnection;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = mTimeoutConnection;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(params[6] + "/Customers/" + customerId
				+ "/Payments");

		String responseBody = "";

		HttpResponse response = null;

		try {

			String base64EncodedCredentials = "Basic "
					+ Base64.encodeToString(
							(params[4] + ":" + params[5]).getBytes(),
							Base64.URL_SAFE | Base64.NO_WRAP);

			httppost.setHeader("Authorization", base64EncodedCredentials);
			httppost.setHeader("X-Api-CompanyCode", params[7]);
			httppost.setHeader(HTTP.CONTENT_TYPE, "application/json");

			JSONObject obj = new JSONObject();

			obj.put("MethodType", "0");
			obj.put("AccountNumber", params[0]);
			obj.put("ExpirationDate", params[1]);
			// obj.put("SecurityCode", params[3]);
			obj.put("ZipCode", params[2]);
			obj.put("PaymentMethodType", params[3]);

			httppost.setEntity(new StringEntity(obj.toString(), "UTF-8"));

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("response ok", "ok response :/");
			} else {
				Log.d("response not ok", "Something went wrong :/");
			}

			responseBody = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return responseBody;
	}

	@SuppressLint("LongLogTag")
	public static String httpGetServiceMobilePay(String serviceName,
												 String appkey, String token) {
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = mTimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = mTimeoutConnection;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet getMethod = new HttpGet(R.string.BASE_URL_MOBILEPAY
					+ serviceName);
			getMethod.setHeader("appkey", appkey);
			getMethod.setHeader("auth_token", token);

			Log.e(serviceName + " GetURL = ", R.string.BASE_URL_MOBILEPAY
					+ serviceName);

			Log.i("elang", "elang serviceName: "
					+ R.string.BASE_URL_MOBILEPAY  + serviceName);

			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);

			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure: 401",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			Log.i("elang", "elang 3");
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			Log.i("elang", "elang 4");
			// result = checkFor401Error(httpResponse, result);

		} catch (Exception e) {
			Log.i("elang", "elang error: " + e.getMessage());
			e.printStackTrace();
		}
		Log.i("elang", "elang result http: " + result);
		return result;
	}

	public static String executeHttPostMobilePay(String serviceName,
			String appkey, String token, String... params) {

		Log.d("wetzel", R.string.BASE_URL_MOBILEPAY + serviceName);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = mTimeoutConnection;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = mTimeoutConnection;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(R.string.BASE_URL_MOBILEPAY
				+ serviceName);

		String responseBody = "";

		HttpResponse response = null;

		httppost.setHeader("appkey", appkey);
		httppost.setHeader("auth_token", token);

		Log.i("elang", "elang appkey: " + appkey);
		Log.i("elang", "elang auth_token: " + token);
		Log.i("elang", "elang url: " + R.string.BASE_URL_MOBILEPAY
				+ serviceName);
		httppost.setHeader(HTTP.CONTENT_TYPE, "application/json");

		try {

			if (params.length > 0) {
				JSONObject obj = new JSONObject();

				try {
					obj.put("amount", params[0]);
					obj.put("cvv", params[1]);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				httppost.setEntity(new StringEntity(obj.toString(), "UTF-8"));

			}

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("response ok", "ok response :/");
				Log.i("elang", "elang ok response ");
			} else {
				Log.i("elang", "elang response not ok "
						+ response.getStatusLine().getStatusCode());
				Log.d("response not ok", "Something went wrong :/");
			}

			responseBody = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			Log.d("wetzel", "error:" + e.getMessage());
			Log.i("elang", "elang error 1: " + e.getMessage());

			e.printStackTrace();
		} catch (IOException e) {
			Log.d("wetzel", "error:" + e.getMessage());
			Log.i("elang", "elang error 2: " + e.getMessage());
			e.printStackTrace();
			/*
			 * } catch (JSONException e) { e.printStackTrace();
			 */}

		Log.i("elang", "elang responseBody" + responseBody);

		return responseBody;
	}

	public static String executeHttPutMobilePay(String serviceName,
			String appkey, String token, String mParams) {

		Log.d("wetzel", R.string.BASE_URL_MOBILEPAY + serviceName + "?"
				+ mParams);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = mTimeoutConnection;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = mTimeoutConnection;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPut httpput = new HttpPut(R.string.BASE_URL_MOBILEPAY
				+ serviceName + "?" + mParams);

		String responseBody = "";

		HttpResponse response = null;

		try {

			httpput.setHeader("appkey", appkey);
			httpput.setHeader("auth_token", token);
			/*
			 * httpput.setHeader("cardNumber", mParams[0]);
			 * httpput.setHeader("cardholderName", mParams[1]);
			 * httpput.setHeader("expMonth", mParams[2]);
			 * httpput.setHeader("expYear", mParams[3]);
			 * httpput.setHeader("cvv", mParams[4]);
			 * httpput.setHeader("streetAddress", mParams[5]);
			 * 
			 * if (mParams.length>0){ JSONObject obj = new JSONObject();
			 * 
			 * Log.d("wetzel","appkey:"+appkey);
			 * Log.d("wetzel","auth_token:"+token);
			 * Log.d("wetzel","cardNumber:"+mParams[0]);
			 * Log.d("wetzel","cardholderName:"+mParams[1]);
			 * Log.d("wetzel","expMonth:"+mParams[2]);
			 * Log.d("wetzel","expYear:"+mParams[3]);
			 * Log.d("wetzel","cvv:"+mParams[4]);
			 * Log.d("wetzel","streetAddress:"+mParams[5]);
			 * 
			 * //obj.put("appkey", appkey); //obj.put("auth_token", token);
			 * obj.put("cardNumber", mParams[0]); obj.put("cardholderName",
			 * mParams[1]); obj.put("expMonth", mParams[2]); obj.put("expYear",
			 * mParams[3]); obj.put("cvv", mParams[4]); obj.put("streetAddress",
			 * mParams[5]); //obj.put("city", mParams[6]); //obj.put("state",
			 * mParams[7]); //obj.put("country", mParams[8]);
			 * //obj.put("postalCode", mParams[9]);
			 * 
			 * httpput.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			 * 
			 * //httpput.setEntity(new UrlEncodedFormEntity(mParams)); }
			 */

			// Execute HTTP Post Request
			response = httpclient.execute(httpput);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("response ok", "ok response :/");
			} else {
				Log.d("response not ok", "Something went wrong :/");
			}

			responseBody = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			Log.d("wetzel", "error:" + e.getMessage());

			e.printStackTrace();
		} catch (IOException e) {
			Log.d("wetzel", "error:" + e.getMessage());
			e.printStackTrace();
			/*
			 * } catch (JSONException e) { e.printStackTrace();
			 */}

		return responseBody;
	}

	@SuppressLint("LongLogTag")
	public static String httpDeleteServiceMobilePay(String serviceName,
													String appkey, String token, String param) {
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = mTimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = mTimeoutConnection;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpDelete getMethod = new HttpDelete(
					R.string.BASE_URL_MOBILEPAY  + serviceName);

			getMethod.setHeader("appkey", appkey);
			getMethod.setHeader("auth_token", token);

			Log.e(serviceName + " deleteURL = ",
					R.string.BASE_URL_MOBILEPAY  + serviceName);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure:401 ",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");
			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@SuppressLint("LongLogTag")
	public static String httpGetServiceGiftCard(String serviceName,
												String params, String appkey, String token) {
		String result = "";
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Set the timeout in milliseconds until a connection is
			// established.
			// The default value is zero, that means the timeout is not used.
			int timeoutConnection = mTimeoutConnection;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = mTimeoutConnection;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet getMethod = new HttpGet(R.string.BASE_URL_GIFTCARD
					+ serviceName + "?" + params);

			getMethod.setHeader("appkey", appkey);
			getMethod.setHeader("auth_token", token);

			Log.e(serviceName + " GetURL = ", R.string.BASE_URL_GIFTCARD
					+ serviceName + "?" + params);
			BufferedReader in = null;
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
					.execute(getMethod);
			if (httpResponse.getStatusLine().getStatusCode() == 401) {
				Log.i("Response Json Failure: 401",
						"" + httpResponse.toString());
				AppConstants.ERROR401 = httpResponse.getStatusLine()
						.getStatusCode() + "";
			} else
				Log.i("Response Json 401 not fount", ""
						+ "httpResponse.toString()");

			in = new BufferedReader(new InputStreamReader(httpResponse
					.getEntity().getContent()));

			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			result = sb.toString();
			System.out.println(serviceName + " result = " + result);
			// result = checkFor401Error(httpResponse, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String executeHttPostGiftCard(String serviceName,
			String params, String appkey, String token) {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = mTimeoutConnection;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = mTimeoutConnection;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPost httppost = new HttpPost(R.string.BASE_URL_GIFTCARD
				+ serviceName + "?" + params);

		httppost.setHeader("appkey", appkey);
		httppost.setHeader("auth_token", token);

		String responseBody = "";

		HttpResponse response = null;

		try {

			// Execute HTTP Post Request
			response = httpclient.execute(httppost);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("response ok", "ok response :/");
			} else {
				Log.d("response not ok", "Something went wrong :/");
			}

			responseBody = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			/*
			 * } catch (JSONException e) { e.printStackTrace();
			 */
		}

		return responseBody;
	}

	public static String executeHttPutGiftCard(String serviceName,
			String params, String appkey, String token) {
		Log.d("wetzel", "url:" + R.string.BASE_URL_GIFTCARD   + serviceName
				+ "?" + params);

		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = mTimeoutConnection;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = mTimeoutConnection;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
		HttpPut httpput = new HttpPut(R.string.BASE_URL_GIFTCARD
				+ serviceName + "?" + params);

		httpput.setHeader("appkey", appkey);
		httpput.setHeader("auth_token", token);

		String responseBody = "";

		HttpResponse response = null;

		try {

			// Execute HTTP Post Request
			response = httpclient.execute(httpput);

			if (response.getStatusLine().getStatusCode() == 200) {
				Log.d("response ok", "ok response :/");
			} else {
				Log.d("response not ok", "Something went wrong :/");
			}

			responseBody = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			/*
			 * } catch (JSONException e) { e.printStackTrace();
			 */
		}

		return responseBody;
	}

}
