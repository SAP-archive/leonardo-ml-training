package com.sap.cloud.s4hana.mlretrainservice.mlmodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.ModelVersions;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Models;

@RestController
@RequestMapping("/models")
public class ModelController {

	@Autowired
	private ModelService modelService;

	@GetMapping
	public Models getAllModels() throws ApiException {
		return modelService.getAllModels();
	}

	@GetMapping("/{modelName}")
	public ModelVersions getModelbyName(@PathVariable String modelName) throws ApiException {
		return modelService.getModelVersionsbyName(modelName);
	}

	@DeleteMapping("/{modelName}/{version}")
	public void deleteModelbyNameandVersion(@PathVariable String modelName, @PathVariable String version)
			throws ApiException {
		modelService.deleteModel(modelName, version);
	}
}
