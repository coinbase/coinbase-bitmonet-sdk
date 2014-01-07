## Android SDK

Coinbase Android SDK developed in collaboration with [Bitmonet Open-source project](http://www.bitmonet.com) is the first SDK that allows any Android developer to monetize their Android app with Bitcoins. 

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

1. Initialize the SDK using the `initialize` API call
2. Set the receiving address using the `setReceivingAddress` API call
3. Request the user to access their wallet using `requestWalletAuthorization` call and implement the `BitmonetOAuthStatusListener` interface to get the status of OAuth process
4. To execute a transaction use the `sendMoney` or the `sendMoneyInBackground` API and implement the `BitmonetPaymentStatusListener` interface to get the status of a transaction


### API's 

* Initiale the SDK: `initialize(Context context, String clientId, String clientSecret, String callbackUrl)`

* Set the receiving address: `setReceivingAddress(String receivingAddress)`

* Set the transaction currency: `setTransactionCurrency(String transactionCurrency)`

* Request Authorization to access the wallet: `requestWalletAuthorization(Context activityContext)`. Implement the `BitmonetOAuthStatusListener` interface to get the status of this call, and use the context of the calling activity as a parameter to this call.

* Transfer money from the user account to the specified receiving address: `sendMoney(Context activityContext, String itemName, double amount)`. Implement the `BitmonetPaymentStatusListener`interface to get the status of this call, and use the context of the calling activity as a parameter to this call.

* Transfer money from the user account to the specified receiving address without showing the modal: `sendMoneyInBackground(Context activityContext, String itemName, double amount)`. Implement the `BitmonetPaymentStatusListener` interface to get the status of this call, and use the context of the calling activity as a parameter to this call.


### Sample Application

Please check the TestApplication in this repo for an example on how to use the SDK.
