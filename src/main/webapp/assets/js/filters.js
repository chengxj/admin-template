'use strict';

angular.module('myApp.filters', []).filter('interpolate',
		[ 'version', function(version) {
			return function(text) {
				return String(text).replace(/\%VERSION\%/mg, version);
			};
		} ]);

var commonFilter = angular.module('app.filters', []);

commonFilter.filter('money', function() {
	return function(input) {
		if (input) {
			return input + "元";			
		} else {
			return input;
		}
	}
});
commonFilter.filter('chargeperiod', function() {
	return function(input) {		
		if (input) {
			return input + "个月"		
		} else {
			return input;
		}
	}
});

commonFilter.filter('yesorno', function() {
	return function(input) {
		if (input == "1") {
			return "是";
		}
		return "否"
	}
});
commonFilter.filter('hasornot', function() {
	return function(input) {
		if (input == "1") {
			return "有";
		}
		return "无"
	}
});

commonFilter.filter('datetimeformat', function() {
	return function(input) {
		if (input == undefined) {
			return "";
		}
		var out = "";
		if (input.length == 14) {
			for ( var i = 0; i < input.length; i++) {
				out = out + input[i];
				if (i == 3) {
					out += "-"
				}
				if (i == 5) {
					out += "-"
				}
				if (i == 7) {
					out += " "
				}
				if (i == 9) {
					out += ":"
				}
				if (i == 11) {
					out += ":"
				}

			}
		}
		return out;
	}
});

commonFilter.filter('dateformat', function() {
	return function(input) {
		if (input == undefined) {
			return "";
		}
		var out = "";
		if (input.length == 14 || input.length == 8) {
			for ( var i = 0; i < input.length; i++) {
				if (i < 8) {
					out = out + input[i];
				}
				if (i == 3) {
					out += "-"
				}
				if (i == 5) {
					out += "-"
				}
				if (i == 7) {
					out += " "
				}
			}
		}
		return out;
	}
});

commonFilter.filter('limit', function() {
	return function(input, start, end) {
		if (_.isArray(input)) {
			return input.slice(start, end);
		} else {
			return input;
		}
	}
});

commonFilter.filter('pluckAndSum', function() {
	return function(input, property) {
		if (_.isArray(input)) {
			return _.reduce(input, function(memo, v){ 
				var s = new Number(v[property]);
				return memo + s;
				}, 0);
		} else {
			return "";
		}
	}
});