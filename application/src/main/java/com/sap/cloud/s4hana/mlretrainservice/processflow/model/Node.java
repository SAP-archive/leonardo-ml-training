package com.sap.cloud.s4hana.mlretrainservice.processflow.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "lane", "title", "children", "state", "stateText", "focused", "texts", "titleAbbreviation" })
@Data
public class Node {

	@JsonProperty("id")
	private String id;
	@JsonProperty("lane")
	private String lane;
	@JsonProperty("title")
	private String title;
	@JsonProperty("children")
	private ArrayList<Integer> children;
	@JsonProperty("state")
	private String state;
	@JsonProperty("stateText")
	private String stateText;
	@JsonProperty("focused")
	private Boolean focused;
	@JsonProperty("texts")
	private List<String> texts = null;
	@JsonProperty("titleAbbreviation")
	private String titleAbbreviation;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	public void addChildren(String child) {
		ArrayList<Integer> current = getChildren();
		if(current == null) {
			current = new ArrayList<>();
		}
		current.add(Integer.parseInt(child));
		setChildren(current);
	}
}
