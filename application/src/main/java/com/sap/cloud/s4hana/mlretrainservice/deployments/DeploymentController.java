package com.sap.cloud.s4hana.mlretrainservice.deployments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeployResponseOk;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentDescription;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentResponseOk;

@RestController
@RequestMapping("/deployments")
public class DeploymentController {

	@Autowired
	private DeploymentService deploymentService;

	@GetMapping
	public DeploymentResponseOk getAllDeployments() throws ApiException {
		return deploymentService.getAllDeployments();
	}

	@GetMapping("/{id}")
	public DeploymentDescription getDeploymentsById(@PathVariable String id) throws ApiException {
		return deploymentService.getDeploymentsById(id);
	}

	@PostMapping
	public DeployResponseOk postDeployments(@RequestParam String modelName, @RequestParam String modelVersion)
			throws ApiException {
		return deploymentService.postDeployments(modelName, modelVersion);
	}

	@DeleteMapping("/{id}")
	public void deleteDeploymentsById(@PathVariable String id) throws ApiException {
		deploymentService.deleteDeploymentsById(id);
	}
}
