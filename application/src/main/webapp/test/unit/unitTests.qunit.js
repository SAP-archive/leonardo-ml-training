/* global QUnit */
QUnit.config.autostart = false;

sap.ui.getCore().attachInit(function () {
	"use strict";

	sap.ui.require([
		"mlRetainService/ml-retain-service/test/unit/AllTests"
	], function () {
		QUnit.start();
	});
});