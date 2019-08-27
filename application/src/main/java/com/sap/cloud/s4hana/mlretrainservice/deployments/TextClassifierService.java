package com.sap.cloud.s4hana.mlretrainservice.deployments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.cloud.s4hana.mlretrainservice.exceptions.SapException;
import com.sap.cloud.s4hana.mlretrainservice.utils.ApiClient_OAuth2;
import com.sap.cloud.s4hana.mlretrainservice.utils.LeonardoConfig;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Service
public class TextClassifierService {

	@Autowired
	private ApiClient_OAuth2 auth2;

	@Autowired
	private LeonardoConfig leonardoConfig;
	
	/**
	 * @returns Leonardo Text classifier custom trained service response for the Given text. 
	 * */
	public String getAnalysedSentiments(List<String> texts, String modelname, String version) throws SapException, IOException {

		if (texts.isEmpty()) {
			throw new SapException("Texts list is Empty");
		}

		List<NameValuePair> params = new ArrayList<NameValuePair>(texts.size());
		for (String message : texts) {
			params.add(new BasicNameValuePair("texts", message));
		}

		String url = leonardoConfig.getRetainedTextClassifierServiceUrl() + "/models/" + modelname + "/versions/"
				+ version;

		FormEncodingBuilder formBody = new FormEncodingBuilder();
		texts.forEach(text -> formBody.add("texts", text.toString()));

		final Request request = new Request.Builder().url(url).post(formBody.build())
				.addHeader("Authorization", "Bearer " + auth2.fetchBearerToken())
				.addHeader("Content-Type", "application/application/json").addHeader("cache-control", "no-cache")
				.build();
		OkHttpClient client = new OkHttpClient();
		// Execute request and get the response.
		Response response = client.newCall(request).execute();

		return response.body().string();

	}
}
