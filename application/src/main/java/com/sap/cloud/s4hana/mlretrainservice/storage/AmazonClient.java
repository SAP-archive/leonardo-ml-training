package com.sap.cloud.s4hana.mlretrainservice.storage;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.sap.apibhub.sdk.client.ApiException;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;

import lombok.NonNull;

@Service
//@DependsOn(value= {"storageService","apiClient_OAuth2"})
public class AmazonClient {

	private static final Logger logger = CloudLoggerFactory.getLogger(AmazonClient.class);
	private AmazonS3 s3client;
	private EndpointConfiguration endpointConfiguration;
	private final String bucketName = "data";
	private String accessKey;
	private String secretKey;
	private final String dataBucketPrefix = "userdata";

	@Autowired
	private void AmazonS3(StorageService storageService) throws ApiException {
		this.accessKey = storageService.getStorage().getAccessKey();
		this.secretKey = storageService.getStorage().getSecretKey();
		this.endpointConfiguration = new EndpointConfiguration(storageService.getStorage().getEndpoint(),
				"eu-central-1");
	}

	@PostConstruct
	private void initializeAmazon() {
		final AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withEndpointConfiguration(endpointConfiguration).withPathStyleAccessEnabled(true).build();
	}
	
	/**
	 * Calls {@link AmazonS3#putObject(String, String, InputStream, ObjectMetadata)} for the current bucket
	 */
	public void uploadFile(@NonNull String key, @NonNull InputStream input, String contentType)
			throws AmazonServiceException {

		final ObjectMetadata metadata = new ObjectMetadata();
		if (contentType != null) {
			metadata.setContentType(contentType);
		}

		String[] tokens = key.split("\\.(?=[^\\.]+$)");
		if (!key.startsWith(dataBucketPrefix)) {
			key = dataBucketPrefix + "/" + tokens[0] + "/" + key;
		}

		logger.trace("Amazon S3Key Used : " + key);
		s3client.putObject(bucketName, key, input, metadata);

	}

	/**
	 * @return {@link AmazonS3#getObject(String, String)} for the current bucket
	 */
	public S3Object getObject(@NonNull String key) throws AmazonServiceException {
		return s3client.getObject(bucketName, key);
	}

	/**
	 * Calls {@link AmazonS3#deleteObject(String, String)} for the current bucket
	 */
	public void deleteObject(@NonNull String key) throws AmazonServiceException {
		logger.trace("Amazon Object Deleted :" + key);
		s3client.deleteObject(bucketName, key);
	}
	
	/**
	 * @return keys {@link AmazonS3#listObjects(String, String)} for the current bucket filtered
	 *
	 */
	public List<String> getUserDataBucketList() {

		List<S3ObjectSummary> summaries = new LinkedList<>();
		List<String> strings = new LinkedList<>();

		ObjectListing list = null;
		do {
			if (list == null) {
				list = s3client.listObjects(bucketName, dataBucketPrefix);
			} else {
				list = s3client.listNextBatchOfObjects(list);
			}

			summaries.addAll(list.getObjectSummaries());
		} while (list.isTruncated());

		summaries.forEach((s3ObjectSummary) -> {
			if (!s3ObjectSummary.getKey().endsWith("log.txt"))
				strings.add(s3ObjectSummary.getKey());
		});

		return strings;
	}
}
