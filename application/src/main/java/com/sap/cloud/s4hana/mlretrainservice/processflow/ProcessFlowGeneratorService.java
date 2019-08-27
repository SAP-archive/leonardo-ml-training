package com.sap.cloud.s4hana.mlretrainservice.processflow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sap.apibhub.sdk.client.ApiException;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.DeploymentDescription;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Model;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.ModelVersion;
import com.sap.apibhub.sdk.text_linear_retrain_api.model.Status;
import com.sap.cloud.s4hana.mlretrainservice.deployments.DeploymentService;
import com.sap.cloud.s4hana.mlretrainservice.jobs.JobService;
import com.sap.cloud.s4hana.mlretrainservice.mlmodel.ModelService;
import com.sap.cloud.s4hana.mlretrainservice.processflow.model.Lane;
import com.sap.cloud.s4hana.mlretrainservice.processflow.model.Node;
import com.sap.cloud.s4hana.mlretrainservice.processflow.model.ProcessFlowNodes;
import com.sap.cloud.s4hana.mlretrainservice.processflow.model.State;
import com.sap.cloud.sdk.cloudplatform.logging.CloudLoggerFactory;

@Service
public class ProcessFlowGeneratorService {

	private static final String SAP_ICON_BEGIN = "sap-icon://begin";
	private static final String SAP_ICON_INSTANCE = "sap-icon://instance";
	private static final String SAP_ICON_CHEVRON_PHASE = "sap-icon://chevron-phase";

	private static final Logger logger = CloudLoggerFactory.getLogger(ProcessFlowGeneratorService.class);

	@Autowired
	private ModelService modelService;

	@Autowired
	private JobService jobService;

	@Autowired
	private DeploymentService deploymentService;

	private static int count;

	public ProcessFlowNodes createProcessFlow() throws ApiException {
		count = 0;
		List<Node> nodes = new LinkedList<>();
		ProcessFlowNodes processFlowNodes = new ProcessFlowNodes();
		List<Lane> lanes = new LinkedList<>();

		// get all deployments
		List<Node> dep_list = createDeploymentsNode(deploymentService.getAllDeployments().getDeployments(), "2");
		nodes.addAll(dep_list);

		// get all models
		List<Node> model_list = new ArrayList<>();
		List<Model> modellist = modelService.getAllModels().getModels();
		for (Model model : modellist) {
			model_list.addAll((createModelNode(model.getVersions(), "1", dep_list)));
		}
		nodes.addAll(model_list);

		// get all jobs
		nodes.addAll(createJobsNode(jobService.getAllJobs().getJobs(), "0", model_list));

		lanes.add(createLane("1", 1, SAP_ICON_CHEVRON_PHASE, "Models"));
		lanes.add(createLane("2", 2, SAP_ICON_INSTANCE, "Deployed"));
		lanes.add(createLane("0", 0, SAP_ICON_BEGIN, "Jobs"));

		processFlowNodes.setNodes(nodes);
		processFlowNodes.setLanes(lanes);
		return processFlowNodes;
	}

	private Lane createLane(String type, int position, String icon, String label) {
		Lane lane = new Lane();

		lane.setIcon(icon);
		lane.setId(type);
		lane.setLabel(label);
		lane.setPosition(position);
		// lane.setState(state);
		return lane;
	}

	private List<Node> createModelNode(List<ModelVersion> modelVersionList, String lane, List<Node> dep_list) {

		List<Node> nodeList = new LinkedList<>();

		for (ModelVersion modelVersion : modelVersionList) {
			List<String> texts = new LinkedList<>();
			Node modelNode = new Node();

			modelNode.setId(String.valueOf(count));
			modelNode.setTitle(modelVersion.getModelName());
			modelNode.setTitleAbbreviation(modelVersion.getVersion());
			modelNode.setState("Neutral");
			modelNode.setLane(lane);
			texts.add("Created : " + dateformatter(modelVersion.getCreatedAt()));
			texts.add("Updated : " + dateformatter(modelVersion.getUpdatedAt()));
			modelNode.setTexts(texts);

			for (Node node : dep_list) {
				if (node.getTitle().equalsIgnoreCase(modelVersion.getModelName())) {
					modelNode.addChildren(node.getId());
				}
			}

			// check id state and state text needed
			nodeList.add(modelNode);
			count++;
		}
		return nodeList;
	}

	private List<Node> createJobsNode(List<Status> statusList, String lane, List<Node> model_list) {
		List<Node> nodeList = new LinkedList<>();
		State state = new State();

		for (Status status : statusList) {
			List<String> texts = new LinkedList<>();
			Node modelNode = new Node();

			modelNode.setLane(lane);
			modelNode.setId(String.valueOf(count));
			modelNode.setTitle(status.getId());
			modelNode.setStateText(status.getStatus().toString());
			modelNode.setState(state.toNodeState(status.getStatus()).toString());
			texts.add("Submission : " + dateformatter(status.getSubmissionTime()));
			texts.add("Started : " + dateformatter(status.getStartTime()));
			texts.add("Finished : " + dateformatter(status.getFinishTime()));

			modelNode.setTexts(texts);

			for (Node node : model_list) {
				if (status.getId().startsWith(node.getTitle())) {
					modelNode.addChildren(node.getId());
				}
			}
			// check id state and state text needed
			nodeList.add(modelNode);
			count++;
		}
		return nodeList;
	}

	private List<Node> createDeploymentsNode(List<DeploymentDescription> deploymentDescriptions, String lane) {
		List<Node> nodeList = new LinkedList<>();
		State state = new State();

		for (DeploymentDescription deploydesc : deploymentDescriptions) {
			List<String> texts = new LinkedList<>();
			Node modelNode = new Node();

			modelNode.setLane(lane);
			modelNode.setId(String.valueOf(count));
			modelNode.setTitle(deploydesc.getModelName());
			modelNode.setTitleAbbreviation(deploydesc.getModelVersion());
			modelNode.setStateText(deploydesc.getStatus().getDescription());
			modelNode.setState(state.toNodeState(deploydesc.getStatus().getState()).toString());
			texts.add("Id : " + deploydesc.getId());
			// texts.add("version : " + deploydesc.getModelVersion());
			modelNode.setTexts(texts);

			nodeList.add(modelNode);
			count++;
		}
		return nodeList;
	}

	private String dateformatter(DateTime datetime) {
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		return dtf.print(datetime);
	}
}
