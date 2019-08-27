sap.ui.define(['jquery.sap.global', 'sap/suite/ui/commons/library', 'sap/ui/core/mvc/Controller', 'sap/ui/model/json/JSONModel',
		'sap/m/MessageToast'
	],
	function (jQuery, SuiteLibrary, Controller, JSONModel, MessageToast) {
		"use strict";

		return Controller.extend("mlRetainService.ml-retain-service.controller.processflow", {
			onInit: function () {
				var oView = this.getView();
				var oProcessFlow2 = oView.byId("processflow2");
				jQuery.ajax({
					type: "GET",
					async: "true",
					contentType: "application/json",
					url: "/processflow",
					dataType: "json",
					success: function (data, textStatus, jqXHR) {
					//console.log(data);
					oProcessFlow2.setModel(new JSONModel(data),"pf2");
					},
					error: function (xhr) {
					//console.log ("No Data found");
					}
				});
				this.oProcessFlow2 = oProcessFlow2;
			},

			navigateToMainPage: function (oEvent) {
				var oRouter = this.getOwnerComponent().getRouter();
				oRouter.navTo("RouteinitialView");

			},

			onOnError: function (event) {
				MessageToast.show("Exception occurred: " + event.getParameters().text);
			},

			onHeaderPress: function (event) {
				var sDataPath = jQuery.sap.getModulePath("mlRetainService.ml-retain-service.mockdata", "/processflowdata.json");
				this.getView().getModel("pf2").loadData(sDataPath);
			},

			onNodePress: function (event) {
				MessageToast.show("Node " + event.getParameters().getNodeId() + " has been clicked.");
			},

			onZoomIn: function () {
				this.oProcessFlow2.zoomIn();

				MessageToast.show("Zoom level changed to: " + this.oProcessFlow2.getZoomLevel());
			},

			onZoomOut: function () {
				this.oProcessFlow2.zoomOut();

				MessageToast.show("Zoom level changed to: " + this.oProcessFlow2.getZoomLevel());
			}

		});
	});