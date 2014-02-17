package com.testapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.bitmonet.Bitmonet;
import com.bitmonet.BitmonetOAuthStatusListener;
import com.bitmonet.BitmonetPaymentStatusListener;

/*
 * A sample application using the Bitmonet Android SDK. Implement the BitmonetOAuthStatuslistener
 * and BitmonetPaymentStatusListener, to get the status of OAuth2 and transactions respectively
 */
public class MainActivity extends Activity implements BitmonetPaymentStatusListener, BitmonetOAuthStatusListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.oauth_launcher);

		// Initialize the Bitmonet SDK
		Bitmonet.initialize(getApplicationContext(), "YOUR CLIENT ID", "YOUR CLINET SECRET", "YOUR CALLBACK URL"); 

		// Set the address where you want to receive your Bitcoins
		Bitmonet.setReceivingAddress("YOUR RECEIVING ADDRESS");

		// Set the transaction currency
		Bitmonet.setTransactionCurrency("BTC");

		Button authorize = (Button) findViewById(R.id.button1);
		authorize.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Request Wallet Authorization
				Bitmonet.requestWalletAuthorization(MainActivity.this);
			}
		});

		Button sendMoney = (Button) findViewById(R.id.button2);
		sendMoney.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send money and ask the user for a confirmation
				Bitmonet.sendMoney(MainActivity.this, "1000 Gold", 0.001);
			}
		});

		Button sendMoneyInBackground = (Button) findViewById(R.id.button3);
		sendMoneyInBackground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send money in the background
				Bitmonet.sendMoneyInBackground(MainActivity.this, "1000 Gold", 0.001);
			}
		});
		
		Button sendMoneyError = (Button) findViewById(R.id.button4);
		sendMoneyError.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Generate an error in the sendmoney call
				Bitmonet.sendMoney(MainActivity.this, "1000 Gold", 0.00000001);
			}
		});

	}

	@Override
	public void paymentFailure(String[] errors) {
		String displayError = "";

		for (int i = 0; i < errors.length; i++) {
			displayError = displayError + errors[i] + " ";
		}

		Toast.makeText(getApplicationContext(), "Errors: " + displayError, Toast.LENGTH_LONG).show();
	}

	@Override
	public void walletOAuthStatusListener(boolean status) {
		Toast.makeText(getApplicationContext(), "OAuth Complete: " + String.valueOf(status), Toast.LENGTH_LONG).show();
	}

	@Override
	public void paymentSuccess(String hash) {
		Toast.makeText(getApplicationContext(), "Transaction Hash: " + hash, Toast.LENGTH_LONG).show();
	}

}
