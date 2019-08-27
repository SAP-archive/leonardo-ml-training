package com.sap.cloud.s4hana.mlretrainservice.processflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.cloud.s4hana.mlretrainservice.processflow.model.ProcessFlowNodes;

@RestController
@RequestMapping("/processflow")
public class ProcessFlowController {

	@Autowired
	private ProcessFlowGeneratorService processFlowGenerator;

	@GetMapping
	public ProcessFlowNodes getProcessFlow() throws ApiException {
		return processFlowGenerator.createProcessFlow();
	}
}
