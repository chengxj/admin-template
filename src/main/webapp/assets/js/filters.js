'use strict';

angular.module('myApp.filters', []).filter('interpolate',
		[ 'version', function(version) {
			return function(text) {
				return String(text).replace(/\%VERSION\%/mg, version);
			};
		} ]);

var commonFilter = angular.module('app.filters', []);

/*
 * 格式化日期，默认的格式为YYYY-MM-DD h:mm:ss
 * 用法：{{startDate | datetimeformat:'YYYYMMDDHHmmss'}}
 * {{endDate | datetimeformat}}
 * */
commonFilter.filter('datetimeformat', function() {
    return function(input, format) {
        if (input == undefined) {
            return "";
        }
        if (format == undefined) {
            format = "YYYY-MM-DD h:mm:ss";
        }
        var date = input;
        if (typeof input == 'string') {
            if (input.length == 8) {
                date = moment(input, 'YYYYMMDD');
            }
            if (input.length == 14) {
                date = moment(input, 'YYYYMMDDHHmmss');
            }
        }
        return date.format(format)
    }
});

/*
* 格式化日期，默认的格式为YYYY-MM-DD
* 用法：{{startDate | dateformat:'YYYYMMDDHHmmss'}}
* {{endDate | dateformat}}
* */
commonFilter.filter('dateformat', function() {
	return function(input, format) {
		if (input == undefined) {
			return "";
		}
        if (format == undefined) {
            format = "YYYY-MM-DD";
        }
        var date = input;
        if (typeof input == 'string') {
            if (input.length == 8) {
                date = moment(input, 'YYYYMMDD');
            }
            if (input.length == 14) {
                date = moment(input, 'YYYYMMDDHHmmss');
            }
        }
		return date.format(format)
	}
});
