var jobsList;
var that;
sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/m/Button",
	"sap/m/MessageBox",
	"sap/m/MessageToast",
	"sap/m/Dialog",
	"sap/m/Label",
	"sap/m/Text",
	"sap/m/Input",
	"sap/m/TextArea",
	"sap/ui/core/Fragment",
	"mlRetainService/ml-retain-service/util/rester",
	"mlRetainService/ml-retain-service/formatter/formatter"
], function (Controller, Button, MessageBox, MessageToast, Dialog, Label, Text, Input, TextArea, Fragment, rester, formatter) {
	"use strict";

	return Controller.extend("mlRetainService.ml-retain-service.controller.jobs", {
		onInit: function () {
			that = this;
			jobsList = this.byId("allJobsList");
			this.getOwnerComponent().getRouter().getRoute("Jobs").attachPatternMatched(this._onObjectMatched, this);
		},

		_onObjectMatched: function (oEvent) {
			//	rester.getCall("/destinations/ml-serviceRetention/jobs",jobsList,"jobs");
			that = this;
			this.callJobsAPI("/jobs", "GET");
		},

		callJobsAPI: function (url, type) {
			jQuery.proxy(rester.getCall({
				"url": url,
				"type": type,
				"callbackSuccess": function (data) {
					if (type === "GET") {
						jobsList.setModel(new sap.ui.model.json.JSONModel(data));
					} else if (type === "DELETE") {
						//return("Success");
						that.deleteJobMessageToast(data);
					} else {
						that.addJobMessageToast(data);
					}
				}.bind(this),
				"callbackError": function (error) {
					MessageToast.show("Call to Jobs API failed.");
				}.bind(this)
			}), this);
		},

		availableState: function (sStateValue) {
			return formatter.availableState(sStateValue);
		},

		dateText: function (month, day, year, hour, minute, seconds) {

			return formatter.dateText(month, day, year, hour, minute, seconds);
		},

		navigateToMainPage: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("RouteinitialView");

		},

		deleteJob: function (oEvent) {
			var jobId = oEvent.getSource().getParent().getAggregation("cells")[0].getTitle();
			MessageBox.confirm("Are you sure you want to delete Job- " + jobId + " ?", {
				title: "Delete Job",
				actions: ["Yes", "Cancel"],
				onClose: function (sActionButton) {
					if (sActionButton === "Yes") {
						that.callJobsAPI("/jobs/" + jobId, "DELETE");

					}
				}
			});

		},

		deleteJobMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Job deleted successfully");
				this.callJobsAPI("/jobs", "GET");
			} else {
				MessageToast.show("Job can not be deleted");
			}

		},

		addJob: function (oEvent) {

			var oModel = new sap.ui.model.json.JSONModel();

			this.dialog = sap.ui.xmlfragment("mlRetainService.ml-retain-service.view.newJob", this);
			this.dialog.open();

			jQuery.ajax({
				method: "GET",
				contentType: "application/json",
				url: "/storage/getUserList",
				success: function (data, textStatus, jqXHR) {

					oModel.setData(data);
					this.dialog.getContent()[3].setModel(oModel);

				}.bind(this),
				error: function (xhr) {

					MessageToast.show("Failed to Fetch Jobs Data");
				}

			});

		},

		inputName: function () {
			var jobName = "";
			var jobPath = "";
			jobName = this.dialog.getContent()[1].getValue();
			jobPath = this.dialog.getContent()[3].getValue();
			if (jobName !== "" && jobPath !== "") {
				this.dialog.getBeginButton().setProperty("visible", true);
			} else {
				this.dialog.getBeginButton().setProperty("visible", false);
			}

		},

		onPressJobPath: function () {
			var jobName = "";
			var jobPath = "";
			jobName = this.dialog.getContent()[1].getValue();
			jobPath = this.dialog.getContent()[3].getValue();
			if (jobName !== "" && jobPath !== "") {
				this.dialog.getBeginButton().setProperty("visible", true);
			} else {
				this.dialog.getBeginButton().setProperty("visible", false);
			}
		},

		submitJob: function () {
			var jobName = this.dialog.getContent()[1].getValue();
			var jobPath = this.dialog.getContent()[3].getValue();
			this.callJobsAPI("/jobs?modelName=" + jobName + "&dataset=" + jobPath, "POST");
			this.cancelJob();
		},

		cancelJob: function () {
			this.dialog.close();
			this.dialog.destroy();
		},

		addJobMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Job added successfully");
				this.callJobsAPI("/jobs", "GET");
			} else {
				MessageToast.show("Job can not be added");
			}
		}

	});
});