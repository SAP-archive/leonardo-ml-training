package com.sap.cloud.s4hana.mlretrainservice.deployments;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.cloud.s4hana.mlretrainservice.exceptions.SapException;

@RestController
@RequestMapping("/textclassify")
public class TextClassifierController {

	@Autowired
	private TextClassifierService textClassifier;

	@PostMapping()
	public String postTextClassification(@RequestParam String modelname, @RequestParam String version,
			@RequestParam List<String> texts) throws SapException, IOException {	
		
		return textClassifier.getAnalysedSentiments(texts, modelname, version);
	}
}
