package com.bitmonet.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.bitmonet.Bitmonet;
import com.bitmonet.Constants;

public class UserProfileSettings {

	private static final String COINBASE_ACCESS_TOKEN_KEY = "CoinbaseAccessToken";
	private static final String COINBASE_REFRESH_TOKEN_KEY = "CoinbaseRefreshToken";
	private static final String COINBASE_REFRESH_TIME_KEY = "CoinbaseRefreshTime";
	private static final String COINBASE_OAUTH_STATUS_KEY = "CoinbaseOAuthStatus";
	private static final String COINBASE_ACCESS_TOKEN_TIMESTAMP = "CoinbaseAccessTokenTimestamp";
	private static final String MERCHANT_RECEIVING_ADDRESS = "MerchantReceivingAddress";
	private static final String MERCHANT_TRANSACTION_CURRENCY = "MerchantTransactionCurrency";

	private static volatile UserProfileSettings sInstance = null;

	private UserProfileSettings() {

	}

	public static UserProfileSettings getInstance() {
		if (sInstance == null) {
			synchronized (UserProfileSettings.class) {
				if (sInstance == null) {
					sInstance = new UserProfileSettings();
				}
			}
		}
		return sInstance;
	}

	private void saveStringPreference(String key, String value) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void saveIntPreference(String key, int value) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	private void saveBooleanPreference(String key, boolean value) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private void saveLongPreference(String key, long value) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	private String retrieveStringPreference(String key) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		return sharedPreferences.getString(key, null);
	}

	private int retrieveIntPreference(String key) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		return sharedPreferences.getInt(key, 0);
	}

	private Boolean retrieveBooleanPreference(String key) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(key, false);
	}

	private long retrieveLongPreference(String key) {
		Context context = Bitmonet.getBitmonetContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.BITMONET_SHARED_PREFERENCE,
				Context.MODE_PRIVATE);
		return sharedPreferences.getLong(key, 0);
	}

	public void saveAccessToken(String accessToken) {
		saveStringPreference(COINBASE_ACCESS_TOKEN_KEY, accessToken);
	}

	public String retrieveAccessToken() {
		return retrieveStringPreference(COINBASE_ACCESS_TOKEN_KEY);
	}

	public void saveRefreshToken(String refreshToken) {
		saveStringPreference(COINBASE_REFRESH_TOKEN_KEY, refreshToken);
	}

	public String retrieveRefreshToken() {
		return retrieveStringPreference(COINBASE_REFRESH_TOKEN_KEY);
	}

	public void saveRefreshTime(int refreshTime) {
		saveIntPreference(COINBASE_REFRESH_TIME_KEY, refreshTime);
	}

	public int retrieveRefreshTime() {
		return retrieveIntPreference(COINBASE_REFRESH_TIME_KEY);
	}

	public void saveCoinbaseOAuthStatus(boolean satus) {
		saveBooleanPreference(COINBASE_OAUTH_STATUS_KEY, satus);
	}

	public boolean retrieveOauthStatus() {
		return retrieveBooleanPreference(COINBASE_OAUTH_STATUS_KEY);
	}

	public void saveMerchantReceivingAdrress(String address) {
		saveStringPreference(MERCHANT_RECEIVING_ADDRESS, address);
	}

	public String retrieveMerchantReceivingAddress() {
		return retrieveStringPreference(MERCHANT_RECEIVING_ADDRESS);
	}

	public void saveMerchantTransactionCurrency(String currency) {
		saveStringPreference(MERCHANT_TRANSACTION_CURRENCY, currency);
	}

	public String retrieveMerchantTransactionCurrency() {
		//  Default currency is USD
		String currency = retrieveStringPreference(MERCHANT_TRANSACTION_CURRENCY);
		if (currency == null) {
			return Constants.DEFAULT_TRANSACTION_CURRENCY;
		}

		return currency;
	}

	public void saveCoinbaseAccessTokenTimestamp(long timestamp) {
		saveLongPreference(COINBASE_ACCESS_TOKEN_TIMESTAMP, timestamp);
	}

	public long retrieveCoinbaseAccessTokenTimestamp() {
		return retrieveLongPreference(COINBASE_ACCESS_TOKEN_TIMESTAMP);
	}
	
}
