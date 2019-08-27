package com.sap.cloud.s4hana.mlretrainservice.storage;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.sap.apibhub.sdk.text_linear_retrain_api.api.StorageApi;

/**
 * Controller for {@link StorageApi} that returns AmazonS3 storage for
 * Customizable Text Classification
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

	private AmazonClient amazonClient;

	@Autowired
	StorageController(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	@PostMapping("/uploadFile")
	public void uploadFile(@RequestPart("file") MultipartFile file, @RequestHeader("Content-Type") String contentType)
			throws IOException {
		amazonClient.uploadFile(file.getOriginalFilename(), file.getInputStream(), contentType);
	}

	@DeleteMapping("/deleteFile")
	public void deleteFile(@RequestParam String key) {
		amazonClient.deleteObject(key);
	}
	
	/**
	 * @return for {@link AmazonS3#listObjects(String, String)} that returns AmazonS3 storage keys
	 */
	@GetMapping("/getUserList")
	public List<String> getBucketList() {
		return amazonClient.getUserDataBucketList();
	}
	
	/**
	 * @return file for the given AmazonS3 storage keys
	 */
	@GetMapping("/getFile")
	public void getBucketList(@RequestParam String key, HttpServletResponse response) throws IOException {

		S3Object s3Object = amazonClient.getObject(key);
		String contentType = s3Object.getObjectMetadata().getContentType();

		response.setStatus(200);
		response.setContentType(contentType != null ? contentType : "document");
		IOUtils.copy(s3Object.getObjectContent(), response.getOutputStream());
		response.flushBuffer();

	}
}