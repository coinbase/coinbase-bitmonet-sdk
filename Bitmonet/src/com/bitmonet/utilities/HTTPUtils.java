package com.bitmonet.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HTTPUtils {

	public static HttpResponse makeHttpPostWithJSONRequest(String path, String json) {
		try {
			HttpPost httpPost = new HttpPost(path);
			httpPost.setEntity(new StringEntity(json));
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			return new DefaultHttpClient().execute(httpPost);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HttpResponse makeHttpPostRequest(String path) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(path);
		try {
			return httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
