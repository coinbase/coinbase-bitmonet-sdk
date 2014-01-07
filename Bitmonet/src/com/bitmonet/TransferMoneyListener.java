package com.bitmonet;

import android.content.Context;

public interface TransferMoneyListener {
	public void transferMoney(Context context, String item, double amount);
}
