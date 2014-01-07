Coinbase Android SDK developed in collaboration with [Bitmonet Open-source project](http://www.bitmonet.com) is the first and the only SDK that allows any Android developer to monetize their Android app with Bitcoins. 

### Features
 1. Monetize virtually anything with Bitcoins
 2. Accept payments with just one click
 3. Transfer money without showing the modal after receiving the authorization   
 4. Seamless OAuth and token management

### Set-up

 1. Create a Coinbase account and Register an App
 2. `git clone git@github.com:coinbase/coinbase-android-sdk.git`
 3.	Import the code into your Android Workspace: File > Import... > Android > Existing Code into Android Workspace
 4.	Add the bitmonet project as a library project to the App that you wish to monetize: Properties > Android > Library > Add

### Using the SDK

1. Initialize the SDK, when you initialize your app using the `initialize` API call
2. Set the receiving address using the `setReceivingAddress` API call
3. Request the user to access their wallet using `requestWalletAuthorization` call and implement the `BitmonetOAuthStatusListener` interface to get the status of OAuth process. 
4. To execute a transaction use the `sendMoney` or the `sendMoneyInBackground` API and implement the `BitmonetPaymentStatusListener` interface to get the status of a transaction

### Sample Application

Please check the TestApplication in this repo for an example on how to use the SDK

### API

* Initialize the SDK
``` java 
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
	public static void initialize(Context activtityContenxt, String clientId, String clientSecret, String callbackUrl)
```

* Set the receiving address
```java
/*
	 * Set the receiving address. This needs to set to the receive the Bitcoins.
	 * 
	 * @param receivingAddress Bitcoin public address where all the bitcoins
	 * would be sent
	 */
	public static void setReceivingAddress(String receivingAddress)
```

* Set the transaction currency
```java
/*
	 * Set the transaction currency. The default currency is USD. But you can
	 * use any currency supported by Coinbase
	 * 
	 * @param currency Currency to be shown to the customer and used during
	 * transactions
	 */
	public static void setTransactionCurrency(String currency)
```

* Request Authorization to access the wallet
```java
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
  public static void requesWalletAuthorization(Context activityContext) 
```

* Transfer money from the user account to the specified receiving address
```java
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
	public static void sendMoney(Context activityContext, String itemName, double amount)
```

* Transfer money from the user account to the specified receiving address without showing the modal
```java
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
	public static void sendMoneyInBackground(Context activityContext, String itemName, double amount)
```


### Warning

Please use the SDK at your own discretion, and read the Google Play guidelines before publishing your app
