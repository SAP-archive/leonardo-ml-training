package com.sap.cloud.s4hana.mlretrainservice.storage;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.api.StorageApi;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.EndpointStatus;
import com.sap.cloud.s4hana.mlretrainservice.utils.ApiClient_OAuth2;

/**
 * Wrapper for {@link StorageApi} that returns AmazonS3 storage Credentials for
 * Customizable Text Classification
 */
@Component
@DependsOn("apiClient_OAuth2")
public class StorageService {

	private StorageApi storageApi;

	@Autowired
	private ApiClient_OAuth2 apiClient_OAuth2;

	@PostConstruct
	public void initIt() throws ApiException {
		this.storageApi = new StorageApi();
		setApiClient();
		createStorage();
	}

	/**
	 * @return {@link StorageApi#getStorage()} Gets AmazonS3 credentials
	 */
	public EndpointStatus getStorage() throws ApiException {
		setApiClient();
		return storageApi.getStorage();
	}

	/**
	 * @return {@link StorageApi#createStorage(String)} creates AmazonS3 storage
	 *         space
	 */
	public EndpointStatus createStorage() throws ApiException {
		// already the apiclient has been set
		setApiClient();
		return storageApi.createStorage(null);
	}

	/**
	 * Sets Oauth configuration for the JobsApi
	 */
	private void setApiClient() {
		this.storageApi.setApiClient(apiClient_OAuth2.getApiClient_OAuth2());
	}

}
