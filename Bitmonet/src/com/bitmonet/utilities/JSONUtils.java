package com.bitmonet.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	public static String extractStringValueFromJSONResponse(HttpResponse response, String key) throws JSONException,
			IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();

		// Extract access token, refresh token and the refresh time
		JSONObject jObject = new JSONObject(json);
		try {
			return jObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(false);
		return null;
	}

	public static Boolean extractBooleanValueFromJSONResponse(HttpResponse response, String key) throws JSONException,
			IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();

		// Extract access token, refresh token and the refresh time
		JSONObject jObject = new JSONObject(json);
		try {
			return jObject.getBoolean(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(false);
		return false;
	}

	public static int extractIntValueFromJSONResponse(HttpResponse response, String key) throws JSONException,
			IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		String json = reader.readLine();

		// Extract access token, refresh token and the refresh time
		JSONObject jObject = new JSONObject(json);
		try {
			return jObject.getInt(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		UserProfileSettings.getInstance().saveCoinbaseOAuthStatus(false);
		return -1;
	}

}
