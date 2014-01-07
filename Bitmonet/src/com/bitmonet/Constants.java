package com.bitmonet;

public class Constants {
	/*
	 * Coinbase specific constants
	 */
	public static final String COINBASE_OAUTH_URL = "https://coinbase.com/oauth/authorize?response_type=code&";
	public static final String COINBASE_REQUEST_TOKEN_URL = "https://coinbase.com/oauth/token?grant_type=authorization_code&code=";
	public static final String COINBASE_SEND_MONEY_URI = "https://coinbase.com/api/v1/transactions/";
	public static final String COINBASE_REFRESH_TOKEN_URL = "https://coinbase.com/oauth/token?grant_type=refresh_token&refresh_token=";
	public static final String COINBASE_AUTH_RESPONSE_URL_SUFFIX = "?code=";

	/*
	 * Coinbase JSON paramneters
	 */
	public static final String COINBASE_SEND_MONEY_RESPONSE_STATUS = "success";
	public static final String COINBASE_SUCCESSFUL_TRANSACTION_HASH = "hash";
	public static final String COINBASE_UNSUCESSFUL_TRANSACTION_ERRORS = "errors";

	public static final String COINBASE_OAUTH_DONE = "Coinbase OAuth Done";

	public static final String ACCESS_TOKEN = "access_token";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String ACCESS_TOKEN_EXPIRY_TIME = "expires_in";
	public static final String ACCESS_TOKEN_RECV = "com.bitmonet.refresh_token";
	// Refresh the token 10 minutes before it expires
	public static final int REFRESH_TIME_DELTA = 10 * 60 * 1000;

	/*
	 * Payment Status constants
	 */
	public static final String PAYMENT_SUCCESSFUL = "Payment successful";
	public static final String PAYMENT_UNSUCCESSFUL = "Error in processing the Payment";
	public static final String DEFAULT_TRANSACTION_CURRENCY = "USD";

	/*
	 * Bitmonet shared preference file
	 */
	public static final String BITMONET_SHARED_PREFERENCE = "com.bitmonet.session";

	/*
	 * Generic Constants
	 */
	public static final String BITCOIN_CURRENCY_SYMBOL = "BTC";

	/*
	 * Error values
	 */
	public static final String ERROR_NULL_RESPOSNE_FROM_COINBASE = "Invalid response from Coinbase";
	public static final String ERROR_INITIALIZE_BITMONET = "Bitmonet not initalized";
	public static final String ERROR_COMPLETE_AUTHENTICATION = "Wallet is not authenticated";
	public static final String ERROR_INVALID_RECEIVING_ADDRESS = "Invalid receiving address";
}
