package com.sap.cloud.s4hana.mlretrainservice.mlmodel;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.api.ModelsApi;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.ModelVersions;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Models;
import com.sap.cloud.s4hana.mlretrainservice.utils.ApiClient_OAuth2;

@Service
public class ModelService {

	private ModelsApi modelsApi;

	@Autowired
	private ApiClient_OAuth2 apiClient_OAuth2;

	@PostConstruct
	public void initIt() {
		this.modelsApi = new ModelsApi();
		setApiClient();
	}

	/**
	 * @return {@link ModelsApi#getModelVersions(String)} model versions
	 * 
	 */
	public ModelVersions getModelVersionsbyName(String modelName) throws ApiException {
		setApiClient();
		return modelsApi.getModelVersions(modelName);
	}

	/**
	 * @return {@link ModelsApi#getModels()} enlists all model
	 * 
	 */
	public Models getAllModels() throws ApiException {
		setApiClient();
		return modelsApi.getModels();
	}

	/**
	 * Call {@link ModelsApi#deleteModelById(String, String)} Deletes model.
	 */
	public void deleteModel(String modelName, String version) throws ApiException {
		setApiClient();
		modelsApi.deleteModelById(modelName, version);
	}

	/**
	 * Sets Oauth configuration for the JobsApi
	 */
	private void setApiClient() {
		this.modelsApi.setApiClient(apiClient_OAuth2.getApiClient_OAuth2());
	}

}
