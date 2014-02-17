package com.bitmonet;

import android.content.Context;

import com.bitmonet.coinbase.CoinbasePaymentProcessor;
import com.bitmonet.utilities.UserProfileSettings;

public class Bitmonet {

	private static String clientId = null;
	private static String clientSecret = null;
	private static String callbackUrl = null;
	private static boolean initialized = false;
	private static Context context = null;

	/*
	 * Call this function to initialize the Bitmonet SDK. This function needs to
	 * be call before any API call.
	 * 
	 * @param context Context of the application usign Bitmonet
	 * 
	 * @param clientId ClientId value obtained from Coinbase
	 * 
	 * @param clientSecret ClientSecret obtained from Coinbase
	 * 
	 * @param callbackUrl Callback URL registered at Coinbase. Please ensure
	 * that the scheme used for the URL is unique to your application. E.g. The
	 * test aplication used the bitmonet://bitmonet. This is required to
	 * succesfully redirect the user back to the app during the OAuth2 process
	 */
	public static void initialize(Context activtityContenxt, String clientId, String clientSecret, String callbackUrl) {
		Bitmonet.clientId = clientId;
		Bitmonet.clientSecret = clientSecret;
		Bitmonet.callbackUrl = callbackUrl;
		Bitmonet.context = activtityContenxt;
		initialized = true;
	}

	public static boolean isInitialized() {
		return initialized;
	}

	public static String getClientId() {
		return clientId;
	}

	public static String getClientSecret() {
		return clientSecret;
	}

	public static String getCallbackUrl() {
		return callbackUrl;
	}

	public static Context getBitmonetContext() {
		return context;
	}

	/*
	 * Set the transaction currency. The default currency is USD. But you can
	 * use any currency supported by Coinbase
	 * 
	 * @param currency Currency to be shown to the customer and used during
	 * transactions
	 */
	public static void setTransactionCurrency(String currency) {
		UserProfileSettings.getInstance().saveMerchantTransactionCurrency(currency);
	}

	/*
	 * Set the receiving address. This needs to set the receive the Bitcoins.
	 * 
	 * @param receivingAddress Bitcoin public address where all the bitcoins
	 * would be sent
	 */
	public static void setReceivingAddress(String receivingAddress) {
		UserProfileSettings.getInstance().saveMerchantReceivingAdrress(receivingAddress);
	}

	/*
	 * Send money to the specified address after asking for a confirmation from
	 * the user. This method transfers the money from the user's account to the
	 * receiving address set by the merchant. A modal is shown asking the user
	 * to confirm the transaction. Implement the BitmonetPaymentStatusListener
	 * to get the status of this call.
	 * 
	 * Before this method can be called, ensure that Bitmonet is initialized,
	 * OAuth has been done, and a receiving address has been set.
	 * 
	 * @param context Context of the calling activity. For e.g. use
	 * YourActivity.this as the value when calling this method.
	 * 
	 * @param itemName Name of the item being purchased
	 * 
	 * @param amount Amount to charged
	 */
	public static void sendMoney(Context activityContext, String itemName, double amount) {
		CoinbasePaymentProcessor.getInstance().sendMoney(activityContext, itemName, amount);
	}

	/*
	 * Send money to the specified address without showing the confirmation
	 * modal. This method transfers the money from the user's account to the
	 * receiving address set by the merchant. A modal is shown asking the user
	 * to confirm the transaction. Implement the BitmonetPaymentStatusListener
	 * to get the status of this call.
	 * 
	 * Before this method can be called, ensure that Bitmonet is initialized,
	 * OAuth has been done, and a receiving address has been set.
	 * 
	 * @param context Context of the calling activity. For e.g. use
	 * YourActivity.this as the value when calling this method.
	 * 
	 * @param itemName Name of the item being purchased
	 * 
	 * @param amount Amount to charged
	 */
	public static void sendMoneyInBackground(Context activityContext, String itemName, double amount) {
		CoinbasePaymentProcessor.getInstance().sendMoneyInBackground(activityContext, itemName, amount);
	}

	/*
	 * Request an Authorization from the user to access the wallet. This needs
	 * to be done only once, and Bitmonet takes care of refreshing the access
	 * token. Implement the BitmonetOAuthPaymentStatuslistener to get a
	 * notification on the completion of OAuth2 process.
	 * 
	 * Before this method is called, ensure that Bitmonet has been initialized
	 * and CoinbaseWebview activity is declared in your manifest file.
	 * 
	 * @param context Context of the activity from where this method is called.
	 */
	public static void requestWalletAuthorization(Context activityContext) {
		CoinbasePaymentProcessor.getInstance().startCoinbaseOAuthProcess(activityContext);
	}

}
