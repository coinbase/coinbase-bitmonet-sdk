package com.bitmonet.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;

import com.bitmonet.Bitmonet;
import com.bitmonet.BitmonetOAuthStatusListener;
import com.bitmonet.Constants;

public class OAuthHelperUtils {

	private static volatile OAuthHelperUtils sInstance = null;
	private AlarmManager am = null;
	private PendingIntent pi = null;

	private OAuthHelperUtils() {

	}

	public static OAuthHelperUtils getInstance() {
		if (sInstance == null) {
			synchronized (OAuthHelperUtils.class) {
				if (sInstance == null) {
					sInstance = new OAuthHelperUtils();
				}
			}
		}
		return sInstance;
	}

	@SuppressWarnings("unused")
	private void setupRefreshAccessTokenListener() {
		Context context = Bitmonet.getBitmonetContext();
		// Convert the time to ms
		int refreshTime = UserProfileSettings.getInstance().retrieveRefreshTime() * 1000;
		refreshTime = refreshTime - Constants.REFRESH_TIME_DELTA;

		context.registerReceiver(refreshTokenReceiver, new IntentFilter(Constants.ACCESS_TOKEN_RECV));
		pi = PendingIntent.getBroadcast(context, 0, new Intent(Constants.ACCESS_TOKEN_RECV), 0);
		am = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
		am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.currentThreadTimeMillis() + refreshTime,
				refreshTime, pi);
	}

	private void sendOAuthResponse(final Context context, final boolean status) {
		Handler applicationHandler = new Handler(context.getMainLooper());
		Runnable applicationRunnable = new Runnable() {
			@Override
			public void run() {
				((BitmonetOAuthStatusListener) context).walletOAuthStatusListener(status);
			}
		};
		applicationHandler.post(applicationRunnable);
	}

	private BroadcastReceiver refreshTokenReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context c, Intent i) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(false);
				}
			}).start();

		}
	};

	private void extractOAuthParamatersFromJSON(HttpResponse response) throws JSONException, IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();

		// Extract access token, refresh token and the refresh time
		JSONObject jObject = new JSONObject(json);
		try {
			UserProfileSettings.getInstance().saveAccessToken(jObject.getString(Constants.ACCESS_TOKEN));
			UserProfileSettings.getInstance().saveRefreshToken(jObject.getString(Constants.REFRESH_TOKEN));
			UserProfileSettings.getInstance().saveRefreshTime(jObject.getInt(Constants.ACCESS_TOKEN_EXPIRY_TIME));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean shouldRequestNewAccessToken() {
		long currentTimestamp = System.currentTimeMillis();
		long lastSavedTimestamp = UserProfileSettings.getInstance().retrieveCoinbaseAccessTokenTimestamp();
		long delta = UserProfileSettings.getInstance().retrieveRefreshTime() * 1000;

		if ((currentTimestamp - lastSavedTimestamp) >= delta) {
			return true;
		} else {
			return false;
		}
	}

	public void refreshAccessToken() {
		String path = URLUtils.contstructRefreshTokenURL();
		HttpResponse response = HTTPUtils.makeHttpPostRequest(path);
		try {
			extractOAuthParamatersFromJSON(response);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void requestAccesTokenFromCoinbase(final Context context, final String code) {

		if (code == null) {
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				String path = URLUtils.constructAuthRequestTokenURL(code);
				HttpResponse response = HTTPUtils.makeHttpPostRequest(path);
				if (response != null && response.getStatusLine().getStatusCode() == 200) {
					try {
						extractOAuthParamatersFromJSON(response);
						UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(true);
						UserProfileSettings.getInstance().saveCoinbaseAccessTokenTimestamp(System.currentTimeMillis());
						sendOAuthResponse(context, true);
						return;
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sendOAuthResponse(context, false);
				UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(false);
			}
		}).start();
	}
}
