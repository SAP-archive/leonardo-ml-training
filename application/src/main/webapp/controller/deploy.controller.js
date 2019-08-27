var deploymentsList;
var that;
var modelData;
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
	"sap/ui/codeeditor/CodeEditor",
	"sap/ui/core/Fragment",
	"mlRetainService/ml-retain-service/util/rester",
	"mlRetainService/ml-retain-service/formatter/formatter"
], function (Controller, Button, MessageBox, MessageToast, Dialog, Label, Text, Input, TextArea, CodeEditor, Fragment, rester, formatter) {
	"use strict";

	return Controller.extend("mlRetainService.ml-retain-service.controller.deploy", {
		onInit: function () {
			that = this;
			deploymentsList = this.byId("allDeployList");
			this.getOwnerComponent().getRouter().getRoute("Deployments").attachPatternMatched(this._onObjectMatched, this);

		},

		_onObjectMatched: function (oEvent) {
			this.callDeploymentsAPI("/deployments", "GET");
			that = this;
		},

		callDeploymentsAPI: function (url, type) {
			jQuery.proxy(rester.getCall({
				"url": url,
				"type": type,
				"callbackSuccess": function (data) {
					// deploymentsList.setModel(new sap.ui.model.json.JSONModel(data));
					if (type === "GET") {
						deploymentsList.setModel(new sap.ui.model.json.JSONModel(data));
					} else if (type === "DELETE") {
						that.deleteDeploymentMessageToast(data);
					} else {
						that.addDeploymentMessageToast(data);
					}
				}.bind(this),
				"callbackError": function (error) {
					MessageToast.show("No Deployments found.");
				}.bind(this)
			}), this);
		},

		availableState: function (sStateValue) {
			return formatter.availableState(sStateValue);
		},

		navigateToMainPage: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("RouteinitialView");

		},

		deleteDeploymentMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Deployment deleted successfully");
				this.callDeploymentsAPI("/deployments", "GET");
			} else {
				MessageToast.show("Deployment can not be deleted");
			}

		},

		onItemPress: function (oEvent) {

			var mName = (oEvent.getParameters().listItem.getCells()[2].getTitle()).substring(6);
			var mVersion = (oEvent.getParameters().listItem.getCells()[2].getText()).substring(9);
			var testText;
			var obj = "";
			var str = "";

			var contentforDialog1 = new sap.ui.layout.VerticalLayout({
				content: [
					new Label({
						text: "Model Name: ",
						labelFor: "modelName"

					}),
					new Input("modelName", {
						width: "100%",
						value: mName,
						editable: false

					}).addStyleClass("sapUiSmallMarginBottom"),

					new Label({
						text: "Model Version: ",
						labelFor: "modelVersion"

					}),
					new Input("modelVersion", {
						width: "100%",
						value: mVersion,
						editable: false

					}).addStyleClass("sapUiSmallMarginBottom"),

					new Label({
						text: "Text: ",
						labelFor: "testText"

					}),
					new TextArea("testText", {
						id: "testText",
						width: "100%",
						placeholder: "Input Text to test the model.",
						editable: true,
						liveChange: function (otestText) {
							otestText.getSource().getParent().getParent().getBeginButton().setEnabled();
						}

					}).addStyleClass("sapUiSmallMarginBottom")
				]
			});

			var dialog = new Dialog({
				title: "Sentiment Results",
				type: "Message",

				beginButton: new Button({
					text: "OK",
					enabled: false,
					press: function () {
						dialog.setBusy(true);
						testText = sap.ui.getCore().byId("testText").getValue();
						dialog.removeAllContent();
						dialog.destroyBeginButton();
						

						jQuery.ajax({
							method: "POST",
							contentType: "application/json",
							url: "/textclassify?modelname=" + mName + "&version=" + mVersion + "&texts=" + testText,
							success: function (data, textStatus, jqXHR) {
								obj = data;
								str  =  obj.replace(/([{])/g,  "\\$1");
								dialog.insertContent(new sap.ui.codeeditor.CodeEditor({
									value: str,
									type: "json",
									editable: false,
									height: "250px"
								}));
								
								dialog.setBusy(false);
							},
							error: function (xhr) {
								str = xhr;
								dialog.insertContent(new sap.ui.codeeditor.CodeEditor({
									value: str,
									type: "json",
									editable: false,
									height: "250px"
								}));
								dialog.setBusy(false);
							}

						});
					}
				}),

				endButton: new Button({
					text: "Cancel",
					press: function () {
						dialog.close();
					}
				}),
				afterClose: function () {
					dialog.destroy();
					contentforDialog1.destroyContent();
				}
			});

			dialog.open();
			dialog.addContent(contentforDialog1);

		},

		deleteDeployment: function (oEvent) {
			var deploymentId = oEvent.getSource().getParent().getAggregation("cells")[0].getTitle();

			MessageBox.confirm("Are you sure you want to delete Deployment- " + deploymentId + " ?", {
				title: "Delete Model",
				actions: ["Yes", "Cancel"],
				onClose: function (sActionButton) {
					if (sActionButton === "Yes") {
						that.callDeploymentsAPI("/deployments/" + deploymentId, "DELETE");
					}
				}
			});
		},

		addDeployment: function (oEvent) {

			var oModel = new sap.ui.model.json.JSONModel();

			this.dialog2 = sap.ui.xmlfragment("mlRetainService.ml-retain-service.view.newDeployment", this);
			this.dialog2.open();

			jQuery.ajax({
				method: "GET",
				contentType: "application/json",
				url: "/models",
				success: function (data, textStatus, jqXHR) {

					oModel.setData(data);

				}.bind(this),
				error: function (xhr) {

					MessageToast.show("Failed to Fetch Deployment Data");
				}

			});

			modelData = oModel;
			this.dialog2.getContent()[1].setModel(oModel, "modelData");

		},

		onPressModelName: function (oEvent) {

			var modelName = this.dialog2.getContent()[1].getValue();
			var modelVersion;
			var modelVersionData = new sap.ui.model.json.JSONModel();
			var modelSelected = oEvent.getSource().getValue();
			for (var i = 0; i < modelData.getData().count; i++) {
				if (modelData.getData().models[i].name === modelSelected) {
					modelVersionData.setData(modelData.getData().models[i]);
				}
			}
			this.dialog2.getContent()[3].setProperty("editable");
			this.dialog2.getContent()[3].setModel(modelVersionData, "modelVersionData");
			modelVersion = this.dialog2.getContent()[3].getValue();

			if (modelName !== "" && modelVersion !== "") {
				this.dialog2.getBeginButton().setProperty("visible", true);
			} else {
				this.dialog2.getBeginButton().setProperty("visible", false);
			}

		},

		onPressModelVersion: function () {

			var modelName = this.dialog2.getContent()[1].getValue();
			var modelVersion = this.dialog2.getContent()[3].getValue();
			if (modelName !== "" && modelVersion !== "") {
				this.dialog2.getBeginButton().setProperty("visible", true);
			} else {
				this.dialog2.getBeginButton().setProperty("visible", false);
			}
		},

		submitDeployment: function (oEvent) {
			var modelName = this.dialog2.getContent()[1].getValue();
			var modelVersion = this.dialog2.getContent()[3].getValue();

			that.callDeploymentsAPI("/deployments?modelName=" + modelName + "&modelVersion=" +
				modelVersion, "POST");
			this.dialog2.close();
			this.dialog2.destroy();
		},

		cancelDeployment: function () {
			this.dialog2.close();
			this.dialog2.destroy();
		},

		addDeploymentMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Deployment successfull");
				this.callDeploymentsAPI("/deployments", "GET");
			} else {
				MessageToast.show("Deployment unsuccessfull");
			}
		}

	});
});