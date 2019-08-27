//var oModel = this.getOwnerComponent().getModel("modelsModel");
jQuery.sap.declare("mlRetainService.ml-retain-service.util.rester");
sap.ui.define(function () {
	"use strict";

return{
		getCall: function (sParams) {
		if (typeof sParams !== "object" || !sParams.hasOwnProperty("url") || !sParams.hasOwnProperty("type") || !sParams.hasOwnProperty("callbackSuccess") || !sParams.hasOwnProperty(
			"callbackError") || typeof sParams.url !== "string" || typeof sParams.type !== "string") {
		return;
	}
			jQuery.ajax({
				method: sParams.type,
				contentType: "application/json",
				url:sParams.url,
				success: function (data,textStatus,jqXHR) {
					if(sParams.type === "GET"){
					return sParams.callbackSuccess(data);
					}else{
						return sParams.callbackSuccess(textStatus);
					}
				},

				error: function (xhr) {
			
				}
			});
		}
	};

});
