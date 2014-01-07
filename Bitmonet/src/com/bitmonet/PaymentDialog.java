package com.bitmonet;

import java.util.Currency;

import com.bitmonet.utilities.UserProfileSettings;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PaymentDialog extends Dialog {

	private String mItem;
	private double mAmount;
	private String mCurrency;
	private Button mBuyButton;
	private Context mContext;
	private TransferMoneyListener mListener = null;
	private ProgressBar mProgressBar;
	private TextView mProcessingTransaction;
	private TextView mTransactionResult;

	private TextView mItemText;
	private TextView mPriceText;

	public PaymentDialog(Context context, String item, double amount) {
		super(context);
		mItem = item;
		mAmount = amount;
		mContext = context;
	}

	public void setTransferMoneyListener(TransferMoneyListener listener) {
		mListener = listener;
	}

	private String getSymbol(String currency) {
		if (Constants.BITCOIN_CURRENCY_SYMBOL.equals(currency)) {
			return Constants.BITCOIN_CURRENCY_SYMBOL;
		} else {
			return Currency.getInstance(currency).getSymbol();

		}
	}

	private void updateDialogLayout() {
		mItemText.setVisibility(View.GONE);
		mPriceText.setVisibility(View.GONE);
		mBuyButton.setVisibility(View.GONE);

		TextView wallet = (TextView) findViewById(R.id.wallet);
		wallet.setVisibility(View.GONE);

		mProgressBar.setVisibility(View.VISIBLE);
		mProcessingTransaction.setVisibility(View.VISIBLE);
	}
	
	public void updateDialogLayoutForSuccesfulTransaction() {
		mProgressBar.setVisibility(View.GONE);
		mProcessingTransaction.setVisibility(View.GONE);
		mTransactionResult.setVisibility(View.VISIBLE);
		mTransactionResult.setText(Constants.PAYMENT_SUCCESSFUL);
	}
	
	public void updateDialogLayoutForUnsuccesfulTransaction() {
		mProgressBar.setVisibility(View.GONE);
		mProcessingTransaction.setVisibility(View.GONE);
		mTransactionResult.setVisibility(View.VISIBLE);
		mTransactionResult.setText(Constants.PAYMENT_UNSUCCESSFUL);
		mTransactionResult.setTextColor(Color.RED);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_payment_layout);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		// Set the currency
		mCurrency = UserProfileSettings.getInstance().retrieveMerchantTransactionCurrency();

		// Set the item
		mItemText = (TextView) findViewById(R.id.item);
		mItemText.setText(mItem);

		// Set the amount
		mPriceText = (TextView) findViewById(R.id.price);
		mPriceText.setText(getSymbol(mCurrency) + " " + String.valueOf(mAmount));

		// Set the listener
		mBuyButton = (Button) findViewById(R.id.buyButton);
		mBuyButton.setOnClickListener(onClickListener);
		
		mProgressBar = (ProgressBar) findViewById(R.id.transactionProgressBar);
		mProcessingTransaction = (TextView) findViewById(R.id.processingTransaction);
		mTransactionResult = (TextView) findViewById(R.id.transactionResult);
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			if (view.equals(mBuyButton)) {
				updateDialogLayout();
				PaymentDialog.this.setCancelable(false);
				mListener.transferMoney(mContext, mItem, mAmount);
			}
		}
	};

}
