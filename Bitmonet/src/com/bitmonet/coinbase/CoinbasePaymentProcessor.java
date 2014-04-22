package com.bitmonet.coinbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import com.bitmonet.Bitmonet;
import com.bitmonet.BitmonetPaymentStatusListener;
import com.bitmonet.Constants;
import com.bitmonet.PaymentDialog;
import com.bitmonet.TransferMoneyListener;
import com.bitmonet.utilities.HTTPUtils;
import com.bitmonet.utilities.OAuthHelperUtils;
import com.bitmonet.utilities.URLUtils;
import com.bitmonet.utilities.UserProfileSettings;
import com.google.gson.GsonBuilder;

public class CoinbasePaymentProcessor implements TransferMoneyListener {

	private static volatile CoinbasePaymentProcessor sInstance = null;
	private static PaymentDialog sDialog = null;
	private static Context sCallingActivityContext = null;
	boolean status = false;
	private String hash = null;
	private String[] errors = null;

	private CoinbasePaymentProcessor() {

	}

	public static CoinbasePaymentProcessor getInstance() {
		if (sInstance == null) {
			synchronized (CoinbasePaymentProcessor.class) {
				if (sInstance == null) {
					sInstance = new CoinbasePaymentProcessor();
				}
			}
		}
		return sInstance;
	}

	private void processSucessfulTransaction(Context context, String hash) {
		((BitmonetPaymentStatusListener) context).paymentSuccess(hash);
	}

	private void processUnsucessfulTransaction(Context context, String[] errors) {
		((BitmonetPaymentStatusListener) context).paymentFailure(errors);
	}

	private String[] getErrorsArrayFromJSONObject(JSONObject jObject) {
		List<String> errors = new ArrayList<String>();
		try {
			JSONArray jArray = jObject.getJSONArray(Constants.COINBASE_UNSUCESSFUL_TRANSACTION_ERRORS);
			for (int i = 0; i < jArray.length(); i++) {
				errors.add(jArray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return errors.toArray(new String[0]);
	}

	private String[] getErrorsAarrayFromString(String error) {
		List<String> errors = new ArrayList<String>();
		errors.add(error);
		return errors.toArray(new String[0]);
	}

	private void processResponse(final Context context, final HttpResponse response) {

		if (response != null) {
			if (response.getStatusLine().getStatusCode() == 200) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),
							"UTF-8"));
					String json = reader.readLine();
					JSONObject jObject = new JSONObject(json);
					status = jObject.getBoolean(Constants.COINBASE_SEND_MONEY_RESPONSE_STATUS);
					if (status) {
						hash = jObject.getString(Constants.COINBASE_SUCCESSFUL_TRANSACTION_HASH);
					} else {
						errors = getErrorsArrayFromJSONObject(jObject);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				errors = getErrorsAarrayFromString(response.getStatusLine().toString());
			}
		} else {
			errors = getErrorsAarrayFromString(Constants.ERROR_NULL_RESPOSNE_FROM_COINBASE);
		}

		Handler applicationHandler = new Handler(context.getMainLooper());
		Runnable applicationRunnable = new Runnable() {
			@Override
			public void run() {
				if (status) {
					if(sDialog != null) {
						sDialog.updateDialogLayoutForSuccesfulTransaction();
						showDialogAndDismiss(1500);
					}
					processSucessfulTransaction(context, hash);
				} else {
					if (sDialog != null) {
						sDialog.updateDialogLayoutForUnsuccesfulTransaction();
						showDialogAndDismiss(1500);
					}
					processUnsucessfulTransaction(context, errors);
				}
				status = false;
				hash = null;
				errors = null;
			}
		};
		applicationHandler.post(applicationRunnable);
	}

	private boolean isCoinbaseSetupComplete(Context context) {
		if (!Bitmonet.isInitialized()) {
			((BitmonetPaymentStatusListener) context)
					.paymentFailure(getErrorsAarrayFromString(Constants.ERROR_INITIALIZE_BITMONET));
			return false;
		}

		// OAuth is not done even once
		if (!UserProfileSettings.getInstance().retrieveOauthStatus()
				&& UserProfileSettings.getInstance().retrieveAccessToken() == null) {
			((BitmonetPaymentStatusListener) context)
					.paymentFailure(getErrorsAarrayFromString(Constants.ERROR_COMPLETE_AUTHENTICATION));
			return false;
		}

		if (UserProfileSettings.getInstance().retrieveMerchantReceivingAddress() == null) {
			((BitmonetPaymentStatusListener) context)
					.paymentFailure(getErrorsAarrayFromString(Constants.ERROR_INVALID_RECEIVING_ADDRESS));
			return false;
		}

		return true;
	}

	private String constructJSONForSendMoney(final String item, final double amount) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("to", UserProfileSettings.getInstance().retrieveMerchantReceivingAddress());
		params.put("amount", String.valueOf(amount));
		params.put("notes", item);
		params.put("amount_currency_iso", UserProfileSettings.getInstance().retrieveMerchantTransactionCurrency());

		String json = new GsonBuilder().create().toJson(params, Map.class);
		return json;
	}

	public Context getCallingActivityContext() {
		return sCallingActivityContext;
	}

	public void startCoinbaseOAuthProcess(Context context) {
		if (UserProfileSettings.getInstance().retrieveOauthStatus()) {
			Toast.makeText(context, Constants.COINBASE_OAUTH_DONE, Toast.LENGTH_LONG).show();
			return;
		}

		sCallingActivityContext = context;

		Intent intent = new Intent(context, CoinbaseWebView.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	public void transferMoney(final Context context, final String item, final double amount) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// If the access token has expired, we request a new access
					// token
					if (OAuthHelperUtils.getInstance().shouldRequestNewAccessToken()) {
						OAuthHelperUtils.getInstance().refreshAccessToken();
					}

					String path = URLUtils.constructSendMoneyURL();
					String json = constructJSONForSendMoney(item, amount);
					HttpResponse response = HTTPUtils.makeHttpPostWithJSONRequest(path, json);
					processResponse(context, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void showModalAndTransferMoney(Context context, final String item, final double amount) {
		sDialog = new PaymentDialog(context, item, amount);
		sDialog.setTransferMoneyListener(sInstance);
		sDialog.show();
	}

	private void showDialogAndDismiss(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		sDialog.dismiss();
		sDialog = null;
	}

	public void sendMoneyInBackground(Context context, String item, double amount) {
		if (!isCoinbaseSetupComplete(context)) {
			return;
		}

		transferMoney(context, item, amount);
	}

	public void sendMoney(Context context, String item, double amount) {
		if (!isCoinbaseSetupComplete(context)) {
			return;
		}

		showModalAndTransferMoney(context, item, amount);
	}
	
	public void checkWalletAndSendMoney(Context context, String item, double amount) {
		// Check if there is a valid receiving address
		if (UserProfileSettings.getInstance().retrieveMerchantReceivingAddress() == null) {
			((BitmonetPaymentStatusListener) context)
					.paymentFailure(getErrorsAarrayFromString(Constants.ERROR_INVALID_RECEIVING_ADDRESS));
			return;
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(URLUtils.constructBitcoinIntentURL(amount)));
		
		// Check if there a wallet to handle the intent
		PackageManager manager = context.getPackageManager();
		List<ResolveInfo> activities = manager.queryIntentActivities(intent, 0);
		if (activities.size() > 0) {
			context.startActivity(intent);
		} else {
			showModalAndTransferMoney(context, item, amount);
		}
	}
}
