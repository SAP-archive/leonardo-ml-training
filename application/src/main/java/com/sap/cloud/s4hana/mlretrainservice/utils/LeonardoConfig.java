package com.sap.cloud.s4hana.mlretrainservice.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "leo")

@Getter
@Setter
public class LeonardoConfig {

	private String grant_type;
	private String client_id;
	private String client_secret;
	private String oauthTokenUrl;
	private String retainedTextClassifierServiceUrl;
	private String textClassifierRetrainigUrl;
	private String apihub_sandbox_apikey;
}
