package com.bitmonet;

/*
 * Implement this interface to determine the status of OAuth2 process
 */
public interface BitmonetOAuthStatusListener {
	/*
	 * This function returns the status of the OAuth2 process
	 * 
	 * @param status a boolean value indicating if the OAuth2 process completed
	 * succesfully or not
	 */
	public void walletOAuthStatusListener(boolean status);
}
