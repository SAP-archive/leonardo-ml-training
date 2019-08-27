package com.sap.cloud.s4hana.mlretrainservice.jobs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.api.JobsApi;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Status;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.StatusArray;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.TextJobCreationBody;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.TrainingResponseOk;
import com.sap.cloud.s4hana.mlretrainservice.utils.ApiClient_OAuth2;

/**
 * Wrapper for {@link JobsApi} that performs Training for Customizable Text
 * Classification
 */
@Service
public class JobService {

	private JobsApi jobsApi;

	@Autowired
	private ApiClient_OAuth2 apiClient_OAuth2;

	@PostConstruct
	public void initIt() {
		this.jobsApi = new JobsApi();
		setApiClient();

	}

	/**
	 * @return {@link JobsApi#getJobsById(String)} Gets status of retraining job
	 *         with the given ID
	 */
	public Status getJobs(String id) throws ApiException {
		setApiClient();
		return jobsApi.getJobsById(id);
	}

	/**
	 * @return {@link JobsApi#getJobs()} Gets status list for all retraining job
	 *         with the given ID
	 */
	public StatusArray getAllJobs() throws ApiException {
		setApiClient();
		return jobsApi.getJobs();
	}

	/**
	 * @return {@link JobsApi#postJobs(TextJobCreationBody)} Submit a retraining
	 *         job.
	 */
	public TrainingResponseOk postJobs(String dataset, String modelName) throws ApiException {
		setApiClient();
		TextJobCreationBody body = new TextJobCreationBody();
		body.setDataset(dataset);
		body.setModelName(modelName);
		return jobsApi.postJobs(body);
	}

	/**
	 * Calls {@link JobsApi#deleteJobsById(String)} Delete a retraining job with Id.
	 */
	public void deleteJobs(String id) throws ApiException {
		setApiClient();
		jobsApi.deleteJobsById(id);
	}

	/**
	 * Sets Oauth configuration for the JobsApi
	 */
	private void setApiClient() {
		this.jobsApi.setApiClient(apiClient_OAuth2.getApiClient_OAuth2());
	}

}
