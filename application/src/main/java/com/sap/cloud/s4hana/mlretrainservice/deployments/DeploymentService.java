package com.sap.cloud.s4hana.mlretrainservice.deployments;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.api.DeploymentsApi;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeployResponseOk;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentDescription;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentRequest;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentResponseOk;
import com.sap.cloud.s4hana.mlretrainservice.utils.ApiClient_OAuth2;

@Service
public class DeploymentService {

	private DeploymentsApi deploymentsApi;

	@Autowired
	private ApiClient_OAuth2 apiClient_OAuth2;

	@PostConstruct
	public void initIt() {
		this.deploymentsApi = new DeploymentsApi();
		setApiClient();
	}

	/**
	 * @return {@link DeploymentsApi#getDeploymentsById(String)} get deployed model
	 * 
	 */
	public DeploymentDescription getDeploymentsById(String id) throws ApiException {
		setApiClient();
		return deploymentsApi.getDeploymentsById(id);
	}

	/**
	 * @return {@link DeploymentsApi#getDeployments()} get all deployed models
	 * 
	 */
	public DeploymentResponseOk getAllDeployments() throws ApiException {
		setApiClient();
		return deploymentsApi.getDeployments();
	}

	/**
	 * @return {@link DeploymentsApi#postDeployments(DeploymentRequest)} post
	 *         deployment models
	 * 
	 */
	public DeployResponseOk postDeployments(String modelName, String modelVersion) throws ApiException {
		setApiClient();
		DeploymentRequest body = new DeploymentRequest();
		body.setModelName(modelName);
		body.setModelVersion(modelVersion);
		return deploymentsApi.postDeployments(body);
	}

	/**
	 * Calls {@link DeploymentsApi#deleteDeploymentsById(String)} delete deployment
	 * models
	 * 
	 */
	public void deleteDeploymentsById(String id) throws ApiException {
		setApiClient();
		deploymentsApi.deleteDeploymentsById(id);
	}

	/**
	 * Sets Oauth configuration for the JobsApi
	 */
	private void setApiClient() {
		this.deploymentsApi.setApiClient(apiClient_OAuth2.getApiClient_OAuth2());
	}

}
