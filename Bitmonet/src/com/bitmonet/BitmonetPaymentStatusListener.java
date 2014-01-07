package com.bitmonet;

/*
 * Implement this interface to get the status of the sendMoney calls
 */
public interface BitmonetPaymentStatusListener {
	/*
	 * This function is called on successful completion of the
	 * sendMoney(InBackground) api calls. Also, this is called on the same
	 * thread as that of the calling activity.
	 * 
	 * @param hash of the transaction
	 */
	public void paymentSuccess(String hash);

	/*
	 * This function is called on un-successful completion of the
	 * sendMoney(InBackground) api calls. Also, this is called on the same
	 * thread as that of the calling activity.
	 * 
	 * @param error an array of error strings
	 */
	public void paymentFailure(String[] error);
}
