sap.ui.define([
	"sap/ui/core/mvc/Controller"
], function (Controller) {
	"use strict";

	return Controller.extend("mlRetainService.ml-retain-service.controller.initialView", {
		onInit: function () {

		},
		StoragrTilePressed: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("Storage");
		},

		jobsTilePressed: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("Jobs");
		},
		modelsTilePressed: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("Models");
		},

		processflowTilePressed: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("ProcessFlow");
		},
		deployTilePressed: function (oEvent) {
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("Deployments");
		}
	});
});