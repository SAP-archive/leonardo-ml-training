var storageList;
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

	return Controller.extend("mlRetainService.ml-retain-service.controller.storage", {
		onInit: function () {
			that = this;
			storageList = this.byId("allStoragesList");
			this.getOwnerComponent().getRouter().getRoute("Storage").attachPatternMatched(this._onObjectMatched, this);
		},

		_onObjectMatched: function (oEvent) {
			that = this;
			this.callStorageAPI("/storage/getUserList", "GET");
		},

		callStorageAPI: function (url, type) {
			jQuery.proxy(rester.getCall({
				"url": url,
				"type": type,
				"callbackSuccess": function (data) {
					if (type === "GET") {
						storageList.setModel(new sap.ui.model.json.JSONModel(data));
					} else if (type === "DELETE") {
						//return("Success");
						that.deleteStorageMessageToast(data);
					} else {
						that.addStorageMessageToast(data);
					}
				}.bind(this),
				"callbackError": function (error) {
					MessageToast.show("Call to Storage API failed.");
				}.bind(this)
			}), this);
		},

		navigateToMainPage: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("RouteinitialView");

		},

		deleteStorage: function (oEvent) {
			var storagePath = oEvent.getSource().getParent().getAggregation("cells")[0].getTitle();
			MessageBox.confirm("Are you sure you want to delete Storage Path- " + storagePath + " ?", {
				title: "Delete Storage",
				actions: ["Yes", "Cancel"],
				onClose: function (sActionButton) {
					if (sActionButton === "Yes") {
						that.callStorageAPI("/storage/deleteFile?key=" + storagePath, "DELETE");

					}
				}
			});

		},

		deleteStorageMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Storage deleted successfully");
				this.callStorageAPI("/storage/getUserList", "GET");

			} else {
				MessageToast.show("Storage can not be deleted");
			}

		},

		addStorage: function (oEvent) {

			this.dialog2 = sap.ui.xmlfragment("mlRetainService.ml-retain-service.view.newStorage", this);
			this.dialog2.open();
		},

		addJobMessageToast: function (state) {
			if (state === "success") {
				MessageToast.show("Storage added successfully");
				this.callStorageAPI("/storage/getUserList", "GET");
			} else {
				MessageToast.show("Storage can not be added");
			}
		},

		filePathChanged: function (oEvent) {
			this.dialog2.getBeginButton().setProperty("visible", true);
		},

		handleUploadPress: function (oEvent) {

			var oFileUploader = this.dialog2.getContent()[0].getContent()[0];
			oFileUploader.upload();
		},

		uploadStart: function (oEvent) {

			this.dialog2.removeAllContent();
			this.progressBar = new sap.m.ProgressIndicator();
		},

		uploadProgress: function (oEvent) {

			this.dialog2.getBeginButton().setProperty("visible", false);
			this.dialog2.getEndButton().setProperty("visible", false);
			var total = oEvent.getParameter("total");
			var uploaded = oEvent.getParameter("loaded");
			var pUploaded = (uploaded / total) * 100;

			this.progressBar.setPercentValue(pUploaded);
			this.progressBar.setState("Success");
			this.dialog2.addContent(this.progressBar);

		},

		handleUploadComplete: function () {

			this.cancelUpload();
			this.callStorageAPI("/storage/getUserList", "GET");
			MessageToast.show("Storage added successfully");

		},

		uploadFailed: function () {
			this.cancelUpload();
			MessageToast.show("Storage could not be added successfully");
		},

		cancelUpload: function () {
			this.dialog2.close();
			this.dialog2.destroy();
		}

	});
});