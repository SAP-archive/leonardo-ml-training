package com.sap.cloud.s4hana.mlretrainservice.processflow.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.ModelDeploymentStatus.StateEnum;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Status.StatusEnum;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "state" })
public class State {

	@JsonProperty("state")
	@Getter
	@Setter
	private static String state;

	public enum NodeState {
		Neutral, Negative, Planned, Positive,

	}

	private static HashMap<StatusEnum, NodeState> stateMapperStatus;
	private static HashMap<StateEnum, NodeState> stateMapperDeployment;

	public static HashMap<StatusEnum, NodeState> getStateMapper() {
		return stateMapperStatus;
	}

	public static void setStateMapper(HashMap<StatusEnum, NodeState> stateMapper) {
		stateMapper.put(StatusEnum.SUCCEEDED, NodeState.Positive);
		stateMapper.put(StatusEnum.PENDING, NodeState.Planned);
		stateMapper.put(StatusEnum.FAILED, NodeState.Negative);
		stateMapper.put(StatusEnum.RUNNING, NodeState.Neutral);

		State.stateMapperStatus = stateMapper;
	}

	public State() {
		setStateMapper(new HashMap<StatusEnum, NodeState>());
		setStateMapperDeployment(new HashMap<StateEnum, NodeState>());
	}

	public NodeState toNodeState(StatusEnum statusEnum) {
		return stateMapperStatus.get(statusEnum);
	}

	public NodeState toNodeState(StateEnum stateEnum) {
		return stateMapperDeployment.get(stateEnum);
	}

	public static HashMap<StateEnum, NodeState> getStateMapperDeployment() {
		return stateMapperDeployment;
	}

	public static void setStateMapperDeployment(HashMap<StateEnum, NodeState> stateMapperDeployment) {
		stateMapperDeployment.put(StateEnum.SUCCEEDED, NodeState.Positive);
		stateMapperDeployment.put(StateEnum.PENDING, NodeState.Planned);
		stateMapperDeployment.put(StateEnum.FAILED, NodeState.Negative);

		State.stateMapperDeployment = stateMapperDeployment;
	}
}
