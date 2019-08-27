package com.sap.cloud.s4hana.mlretrainservice.jobs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Status;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.StatusArray;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.TrainingResponseOk;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	private JobService jobService;
	
	@GetMapping
	public StatusArray getAllJobs() throws ApiException{
		return jobService.getAllJobs();
	}
	
	@GetMapping("/{id}")
	public Status getJobsbyId(@PathVariable String id) throws ApiException{
		return jobService.getJobs(id);
	}
	
	@PostMapping
	public TrainingResponseOk postJobs(@RequestParam String dataset,@RequestParam String modelName) throws ApiException{
		String datasetpath = dataset.substring(0, dataset.lastIndexOf("/"));
		return jobService.postJobs(datasetpath, modelName);
	}
	
	@DeleteMapping("/{id}")
	public void deleteJobsbyID(@PathVariable String id) throws ApiException {
		 jobService.deleteJobs(id);
	}
}
