var modelsList;
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
	"mlRetainService/ml-retain-service/formatter/formatter",
	"sap/ui/model/json/JSONModel"
], function (Controller, Button, MessageBox, MessageToast, Dialog, Label, Text, Input, TextArea, Fragment, rester, formatter, JSONModel) {
	"use strict";

	return Controller.extend("mlRetainService.ml-retain-service.controller.models", {
		onInit: function () {
			that = this;
			modelsList = this.byId("allModelsList");
			this.getOwnerComponent().getRouter().getRoute("Models").attachPatternMatched(this._onObjectMatched, this);
		},

		_onObjectMatched: function (oEvent) {
			that = this;
			this.callModelsAPI("/models", "GET");
		},

		callModelsAPI: function (url, type) {
			jQuery.proxy(rester.getCall({
				"url": url,
				"type": type,
				"callbackSuccess": function (data) {
					if (data !== "empty" && type === "GET") {
						that.createModel(data);
					} else if (data !== "empty" && type === "DELETE") {
						that.deleteModelMessageToast(data);
					} else {
						MessageBox.alert("Problem fetching the models.");
					}
				}.bind(this),
				"callbackError": function (error) {
					MessageToast.show("No models found.");
				}.bind(this)
			}), this);
		},

		createModel: function (data) {

			var oModel = new JSONModel({});
			var mProperty = [];
			oModel.setProperty("/models", mProperty);

			for (var i = 0; i < data.count; i++) {
				for (var j = 0; j < data.models[i].versions.length; j++) {
					mProperty.push(data.models[i].versions[j]);
				}
			}
			modelsList.setModel(oModel);

		},

		dateText: function (month, day, year, hour, minute, seconds) {
			return formatter.dateText(month, day, year, hour, minute, seconds);
		},

		navigateToMainPage: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("RouteinitialView");

		},

		deleteJob: function (oEvent) {
			var modelName = oEvent.getSource().getParent().getAggregation("cells")[0].getTitle();
			var modelVersion = oEvent.getSource().getParent().getAggregation("cells")[1].getText();
			MessageBox.confirm("Are you sure you want to delete Model- " + modelName + ", version -" + modelVersion + " ?", {
				title: "Delete Model",
				actions: ["Yes", "Cancel"],
				onClose: function (sActionButton) {
					if (sActionButton === "Yes") {
						that.callModelsAPI("/models/" + modelName + "/" + modelVersion, "DELETE");
					}
				}
			});
		},

		deleteModelMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Model deleted successfully");
				this.callModelsAPI("/models", "GET");
			} else {
				MessageToast.show("Model can not be deleted");
			}

		}

	});
});