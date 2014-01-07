package com.bitmonet.utilities;

import com.bitmonet.Bitmonet;
import com.bitmonet.Constants;

public class URLUtils {

	public static String constructAuthorizeURL() {
		return Constants.COINBASE_OAUTH_URL + "client_id=" + Bitmonet.getClientId() + "&redirect_uri="
				+ Bitmonet.getCallbackUrl();
	}

	public static String constructAuthRequestTokenURL(String code) {
		return Constants.COINBASE_REQUEST_TOKEN_URL + code + "&redirect_uri=" + Bitmonet.getCallbackUrl()
				+ "&client_id=" + Bitmonet.getClientId() + "&client_secret=" + Bitmonet.getClientSecret();
	}

	public static String constructSendMoneyURL() {
		return Constants.COINBASE_SEND_MONEY_URI + "send_money?access_token="
				+ UserProfileSettings.getInstance().retrieveAccessToken();
	}

	public static String contstructRefreshTokenURL() {
		return Constants.COINBASE_REFRESH_TOKEN_URL + UserProfileSettings.getInstance().retrieveRefreshToken()
				+ "&client_id=" + Bitmonet.getClientId() + "&client_secret=" + Bitmonet.getClientSecret();
	}
	
	public static String constructBitcoinIntentURL(double amount) {
		return "bitcoin:" + UserProfileSettings.getInstance().retrieveMerchantReceivingAddress() + "?amount=" + String.valueOf(amount);
	}

}
