package com.sap.cloud.s4hana.mlretrainservice.utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sap.apibhub.sdk.client.ApiClient;
import com.sap.apibhub.sdk.client.Configuration;
import com.sap.apibhub.sdk.client.auth.ApiKeyAuth;
import com.sap.apibhub.sdk.client.auth.Authentication;
import com.sap.apibhub.sdk.client.auth.OAuth;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

@Component
@DependsOn("leonardoConfig")
public class ApiClient_OAuth2 {

	private static final String OAUTH2_CLIENT_CREDENTIALS = "Oauth2_ClientCredentials";
	private static final String API_KEY = "APIKey";
	private static final Logger logger = CloudLoggerFactory.getLogger(ApiClient_OAuth2.class);
	private static final Long TIMEOUTS = 10L;
	private static final String ACCESS_TOKEN = "access_token";

	@Autowired
	private LeonardoConfig leonardoConfig;

	public ApiClient getApiClient_OAuth2() {

		ApiClient defaultClient = Configuration.getDefaultApiClient();

		// fix timeouts
		OkHttpClient okhttp = defaultClient.getHttpClient();
		okhttp.setReadTimeout(TIMEOUTS, TimeUnit.SECONDS);
		okhttp.setConnectTimeout(TIMEOUTS, TimeUnit.SECONDS);
		defaultClient.setHttpClient(okhttp);

		Map<String, Authentication> authentications = defaultClient.getAuthentications();

		// defaults to sandbox
		defaultClient.setBasePath(leonardoConfig.getTextClassifierRetrainigUrl());

		/*
		 * You can obtain your API key on the Settings page of SAP API Business Hub. In
		 * the Settings page, choose the Show API Key toggle button to display and copy
		 * your API key to application.properties. You have to be logged in to view your
		 * API Key.
		 */
		defaultClient.addDefaultHeader(API_KEY, leonardoConfig.getApihub_sandbox_apikey());
		authentications.put("APIBHUB_SANDBOX_APIKEY", new ApiKeyAuth("header", API_KEY));

		// call your API Endpoint: Oauth2_ClientCredentials
		authentications.put(OAUTH2_CLIENT_CREDENTIALS, new OAuth());
		OAuth Oauth2_ClientCredentials = (OAuth) defaultClient.getAuthentication(OAUTH2_CLIENT_CREDENTIALS);

		try {
			Oauth2_ClientCredentials.setAccessToken(this.fetchBearerToken());

		} catch (Exception e) {
			logger.error("Exception occured in the method getApiClient_OAuth2 ::" + e);
		}

		return defaultClient;
	}

	public String fetchBearerToken() throws IOException {

		String clientCredentials = "grant_type=" + leonardoConfig.getGrant_type() + "&client_id="
				+ leonardoConfig.getClient_id() + "&client_secret=" + leonardoConfig.getClient_secret();

		RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), clientCredentials);
		Request request = new Request.Builder().url(leonardoConfig.getOauthTokenUrl()).post(body)
				.addHeader("Content-Type", "application/application/json").addHeader("cache-control", "no-cache")
				.build();

		OkHttpClient client = new OkHttpClient();
		client.setConnectTimeout(TIMEOUTS, TimeUnit.SECONDS);
		client.setReadTimeout(TIMEOUTS, TimeUnit.SECONDS);
		Response response = client.newCall(request).execute();

		return new JSONObject(response.body().string()).getString(ACCESS_TOKEN);
	}

}
