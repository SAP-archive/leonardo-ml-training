package com.sap.cloud.s4hana.mlretrainservice.processflow.model;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "icon", "label", "position" })
@Data
public class Lane {

	@JsonProperty("id")
	private String id;
	@JsonProperty("icon")
	private String icon;
	@JsonProperty("label")
	private String label;
	@JsonProperty("position")
	private Integer position;
	
	/*@JsonProperty("state")
	private State state;*/
	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}