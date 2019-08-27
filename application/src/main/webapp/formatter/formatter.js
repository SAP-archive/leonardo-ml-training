jQuery.sap.declare("mlRetainService.ml-retain-service.formatter.formatter");
sap.ui.define(function () {
	"use strict";

return{
		availableState: function (sStateValue) {
			var sStateValueToLower = sStateValue;
			switch (sStateValueToLower) {
			case "SUCCEEDED":
				return 8;
			case "FAILED":
				return 3;
			case "RUNNING":
				return 2;
			case "PENDING":
				return 1;
			default:
				return 9;
			}
		},
		
		dateText: function (month,day, year, hour,minute,seconds){
				switch (month) {
			case 1:
				return "January " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 2:
				return "February "+ day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 3:
				return "March " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 4:
				return "April " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
				case 5:
				return "May " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 6:
				return "June " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 7:
				return "July " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 8:
				return "August " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
				case 9:
				return "September "+ day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
				case 10:
				return "October " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 11:
				return "November " + day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			case 12:
				return "December "+ day + ", " + year + ", " + hour + ":" + minute + ":" + seconds;
			default:
				return "Invalid";
			}
		}
	};

});