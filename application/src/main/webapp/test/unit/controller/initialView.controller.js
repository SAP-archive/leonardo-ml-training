/*global QUnit*/

sap.ui.define([
	"mlRetainService/ml-retain-service/controller/initialView.controller"
], function (Controller) {
	"use strict";

	QUnit.module("initialView Controller");

	QUnit.test("I should test the initialView controller", function (assert) {
		var oAppController = new Controller();
		oAppController.onInit();
		assert.ok(oAppController);
	});

});