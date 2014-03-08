package com.bitmonet.coinbase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bitmonet.Bitmonet;
import com.bitmonet.Constants;
import com.bitmonet.R;
import com.bitmonet.utilities.OAuthHelperUtils;
import com.bitmonet.utilities.URLUtils;

@SuppressLint("SetJavaScriptEnabled")
public class CoinbaseWebView extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_coinbase_webview);
		webView = (WebView) findViewById(R.id.coinbseWebview);

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new bitmonetWebViewcClient());
		webView.loadUrl(URLUtils.constructAuthorizeURL());
	}

	private void showLoadingSpinner() {
		findViewById(R.id.spinnerWebView).setVisibility(View.VISIBLE);
		findViewById(R.id.loadingText).setVisibility(View.VISIBLE);
	}

	private void hideLoadingSpinner() {
		findViewById(R.id.spinnerWebView).setVisibility(View.GONE);
		findViewById(R.id.loadingText).setVisibility(View.GONE);
	}

	private class bitmonetWebViewcClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (url.contains(Bitmonet.getCallbackUrl()) && url.contains(Constants.COINBASE_AUTH_RESPONSE_URL_SUFFIX)) {
				Context context = CoinbasePaymentProcessor.getInstance().getCallingActivityContext();
				OAuthHelperUtils.getInstance().requestAccesTokenFromCoinbase(context, getCode(url));
				CoinbaseWebView.this.finish();
				return;
			}
			showLoadingSpinner();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			hideLoadingSpinner();
		}

		private String getCode(String url) {
			return url.substring((Bitmonet.getCallbackUrl() + "/" + Constants.COINBASE_AUTH_RESPONSE_URL_SUFFIX).length());
		}
	}

}
