'use strict';

var commonDirective = angular.module('app.directives', ["app.services"]);
commonDirective.directive('edNavbar', function (ShareService) {
    return {
        restrict: 'AE',
        templateUrl: 'app/partials/navbar.html',
        link: function postLink(scope, iElement, iAttrs) {
            scope.webAppName = ShareService.webAppName;
        }
    };
});

commonDirective.directive('edPageTitle', function () {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            $('#page-header .page-title').remove();
            iElement.prependTo('#page-header');
        }
    };
});

commonDirective.directive('edFocus', function () {
    var FOCUS_CLASS = "ng-focused";
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, element, attrs, ctrl) {
            ctrl.$focused = false;

            if (element.hasClass("select2")) {
                element.on('select2-focus',function (evt) {
                    element.addClass(FOCUS_CLASS);
                    scope.$apply(function () {
                        ctrl.$focused = true;
                    });
                }).on('select2-blur', function (evt) {
                        element.removeClass(FOCUS_CLASS);
                        scope.$apply(function () {
                            ctrl.$focused = false;
                        });
                    });
            } else {
                element.bind('mouseenter',function (evt) {
                    element.addClass(FOCUS_CLASS);
                    scope.$apply(function () {
                        ctrl.$focused = true;
                    });
                }).bind('mouseleave', function (evt) {
                        element.removeClass(FOCUS_CLASS);
                        scope.$apply(function () {
                            ctrl.$focused = false;
                        });
                    });
            }

        }
    };
});

var INTEGER_REGEXP = /^\-?\d+$/;
commonDirective.directive('edInteger', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                if (INTEGER_REGEXP.test(viewValue)) {
                    // it is valid
                    ctrl.$setValidity('integer', true);
                    return viewValue;
                } else {
                    // it is invalid, return undefined (no model update)
                    ctrl.$setValidity('integer', false);
                    return undefined;
                }
            });
        }
    };
});

var POSITIVE_INTEGER_REGEXP = /^\d+$/;
commonDirective.directive('edPositiveInteger', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                if (POSITIVE_INTEGER_REGEXP.test(viewValue)) {
                    // it is valid
                    ctrl.$setValidity('positiveInteger', true);
                    return viewValue;
                } else {
                    // it is invalid, return undefined (no model update)
                    ctrl.$setValidity('positiveInteger', false);
                    return undefined;
                }
            });
        }
    };
});

var NUMBER_REGEXP = /^[0-9]+(.[0-9]{1,2})?$/;
commonDirective.directive('edNumber', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            ctrl.$parsers.unshift(function (viewValue) {
                if (NUMBER_REGEXP.test(viewValue)) {
                    // it is valid
                    ctrl.$setValidity('number', true);
                    return viewValue;
                } else {
                    // it is invalid, return undefined (no model update)
                    ctrl.$setValidity('number', false);
                    return undefined;
                }
            });
        }
    };
});

commonDirective.directive('edEnsureUnique', ['$http', function ($http) {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elm, attrs, ctrl) {
            scope.$watch(attrs.ngModel, function (value) {
                if (value) {
                    $http({
                        method: 'GET',
                        url: attrs.edEnsureUnique,
                        params: {'field': value, 'time': new Date().getTime()}
                    }).success(function (data, status, headers, config) {
                            ctrl.$setValidity('unique', data === "true");
                        }).error(function (data, status, headers, config) {
                            ctrl.$setValidity('unique', false);
                        });
                }

            });
        }
    };
}]);

commonDirective.directive('edDelete', ['$http', 'MessageService', function ($http, MessageService) {
    return {
        restrict: 'A',
        scope: {
            clickFun: '&',
            successFun: '&'
        },
        link: function (scope, elm, attrs, ctrl) {
            elm.click(function () {
                $.confirm({
                    text: "确认删除?",
                    confirm: function (button) {
                        scope.clickFun()/*.$promise */
                            .then(function () {
                                MessageService.removeSuccess();
                                scope.successFun();
                            });
                    },
                    cancel: function (button) {

                    }
                });
            });
        }
    };
}]);

commonDirective.directive('edDeleteAll', ['$q', 'MessageService', function ($q, MessageService) {
    return {
        restrict: 'A',
        scope: {
            clickFun: '&',
            successFun: '&',
            modelArray: '='
        },
        link: function (scope, elm, attrs, ctrl) {
            elm.click(function () {
                $.confirm({
                    text: "确认删除?",
                    confirm: function (button) {
                        var array = [];
                        angular.forEach(scope.modelArray, function (v, i) {
                            array.push(scope.clickFun({model: v}));
                        });
                        $q.all(array).then(function (value) {
                            MessageService.removeSuccess();
                            scope.modelArray = [];
                            scope.successFun();
                        }, function (value) {
                            scope.modelArray = [];
                            scope.successFun();
                        });
                    },
                    cancel: function (button) {

                    }
                });
            });
        }
    };
}]);

commonDirective.directive('edMultiSelect', function () {
    return {
        restrict: 'AE',
        scope: {
            options: '='
        },
        require: 'ngModel',
        link: function postLink(scope, iElement, iAttrs, ctrl) {
            scope.$watch("options", function (value) {
                if (value) {
                    iElement.select2({data: value, multiple: true});
                    var  selectedOptions = _.filter(value, function(v, i) {
                        return v.selected == true;
                    });
                    var val =  _.pluck(selectedOptions, "id");
                    iElement.select2("val", val);
                    if (val && val.length > 0) {
                        ctrl.$setValidity('required', true);
                    } else {
                        ctrl.$setValidity('required', false);
                    }

                }
            });
        }
    };
});

commonDirective.directive('edSelect2', function () {
    return {
        restrict: 'AE',
        scope: {
            options: '='
        },
        require: 'ngModel',
        link: function postLink(scope, iElement, iAttrs, ctrl) {
            scope.$watch("options", function (value) {
                if (value) {
                    iElement.select2({data: value});
                    var  selectedOptions = _.filter(value, function(v, i) {
                        return v.selected == true;
                    });
                    var val =  _.pluck(selectedOptions, "id");
                    iElement.select2("val", val);
                    if (val && val.length > 0) {
                        ctrl.$setValidity('required', true);
                    } else {
                        ctrl.$setValidity('required', false);
                    }

                }
            });
        }
    };
});

commonDirective.directive('edTooltip', function () {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            iElement.tooltip({
                placement: "bottom"
            });
        }
    };
});

commonDirective.directive('edCheckbox', function ($timeout) {
    return {
        restrict: 'AE',
        scope: {
            ngModel: '=',
            modelArray: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            iElement.on('ifChecked', function (event) {
                scope.$apply(function () {
                    scope.modelArray.push(scope.ngModel);
                });
            });
            iElement.on('ifUnchecked', function (event) {
                scope.$apply(function () {
                    angular.forEach(scope.modelArray, function (v, i) {
                        if (angular.equals(scope.ngModel, v)) {
                            scope.modelArray.splice(i, 1);
                        }
                    });
                });
            });
            iElement.iCheck({
                checkboxClass: 'icheckbox_minimal-green',
                radioClass: 'iradio_minimal-green'
            });

            $timeout(function () {
                if (scope.ngModel.checked) {
                    iElement.iCheck('check');
                }
            }, 100);

            iElement.parents('tr').on('click', function () {
                iElement.iCheck('toggle');
            });
            if (iAttrs.showcontextmenu == undefined || iAttrs.showcontextmenu == 'true') {
                iElement.parents('tr').on('contextmenu', function () {
                    var f = true;
                    angular.forEach(scope.modelArray, function (v, i) {
                        if (angular.equals(scope.ngModel, v)) {
                            f = false;
                            return;
                        }
                    });
                    if (f) {
                        $('[name=' + iElement.attr("name") + ']:checkbox').iCheck('uncheck');
                        iElement.iCheck('check');
                    }
                });
            }


        }
    };
});

commonDirective.directive('edCheckboxTree', function ($timeout) {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            iElement.on('ifChecked', function (event) {
                var classes = iElement.parents('tr').attr("class").split(" ");
                for (var i = 0, l = classes.length; i < l; i++) {
                    if (classes[i].indexOf("treegrid-parent-") >= 0) {
//                        var parentClass = "treegrid-" + classes[i].substr(16);
//                        console.log(parentClass);
//                        $("tr." + parentClass + " :checkbox").iCheck('check');
                    } else if (classes[i].indexOf("treegrid-") >= 0) {
                        var subClass = "treegrid-parent-" + classes[i].substr(9);
                        $("tr." + subClass + " :checkbox").iCheck('check');
                    }

                }

            });
            iElement.on('ifUnchecked', function (event) {
                var classes = iElement.parents('tr').attr("class").split(" ");
                for (var i = 0, l = classes.length; i < l; i++) {
                    if (classes[i].indexOf("treegrid-parent-") >= 0) {
                    } else if (classes[i].indexOf("treegrid-") >= 0) {
                        var subClass = "treegrid-parent-" + classes[i].substr(9);
                        $("tr." + subClass + " :checkbox").iCheck('uncheck');
                    }

                }
            });
        }
    };
});

commonDirective.directive('edCheckAll', function ($timeout) {
    return {
        restrict: 'AE',
        scope: {
            elName: '@'
        },
        link: function postLink(scope, iElement, iAttrs) {
            iElement.on('ifChecked', function (event) {
                if (scope.elName == undefined) {
                    scope.elName = "pk";
                }
                $('[name=' + scope.elName + ']:checkbox').iCheck('check');
            });
            iElement.on('ifUnchecked', function (event) {
                if (scope.elName == undefined) {
                    scope.elName = "pk";
                }
                $('[name=' + scope.elName + ']:checkbox').iCheck('uncheck');
            });
            $timeout(function () {
                iElement.iCheck({
                    checkboxClass: 'icheckbox_minimal-green',
                    radioClass: 'icheckbox_minimal-green'
                });
            }, 100);
            scope.$on('unCheckedAll', function () {
                $timeout(function () {
                    iElement.iCheck('uncheck');
                }, 100);

            })
        }
    };
});
commonDirective.directive('edDisabled', function () {
    return {
        restrict: 'AE',
        scope: {
            edDisabled: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            scope.$watch("edDisabled", function (value) {
                if (value) {
                    iElement.children().find("input, a").attr("disabled", "disabled");
                }
            })
        }
    };
});


commonDirective.directive('edTouchSpin', function () {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            iElement.TouchSpin();
        }
    };
});

commonDirective.directive('edDateRange', function ($translate) {
    return {
        restrict: 'AE',
        scope: {
            startDate : '=',
            endDate : '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            $translate(['Today','Yesterday','Last 7 Days','Last 30 Days','This Month','Last Month'
                ,'label.common.op.accept','label.common.op.cancel','label.common.from'
                ,'label.common.to', 'label.common.range']).then(function (translations) {
                var today = translations['Today'];
                var ranges = {};
                ranges[translations['Today']] = [moment(), moment()];
                ranges[translations['Yesterday']] = [moment().subtract('days', 1), moment().subtract('days', 1)];
                ranges[translations['Last 7 Days']] = [moment().subtract('days', 6), moment()];
                ranges[translations['Last 30 Days']] = [moment().subtract('days', 29), moment()];
                ranges[translations['This Month']] = [moment().startOf('month'), moment().endOf('month')];
                ranges[translations['Last Month']] = [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')];
                iElement.daterangepicker(
                    {
                        ranges: ranges,
                        startDate: moment().subtract('days', 29),
                        endDate: moment(),
                        locale : {
                        applyLabel: translations['label.common.op.accept'],
                        cancelLabel: translations['label.common.op.cancel'],
                        fromLabel:  translations['label.common.from'],
                        toLabel: translations['label.common.to'],
                        weekLabel: 'W',
                        customRangeLabel: translations['label.common.range'],
                        daysOfWeek: moment()._lang._weekdaysMin.slice(),
                        monthNames: moment()._lang._monthsShort.slice(),
                        firstDay: 0
                    }
                    },
                    function(start, end) {
                        scope.startDate = start.format('YYYY-MM-DD');
                        scope.endDate = end.format('YYYY-MM-DD');
                        iElement.find('span').html(scope.startDate + ' - ' + scope.endDate);
                    }
                );
            });

        }
    };
});

commonDirective.directive('edTreegrid', function ($timeout) {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            if (scope.$last === true) {
                $timeout(function () {
                    $('.tree').treegrid();
                });
            }
        }
    };
});
commonDirective.directive('edReload', function () {
    return {
        restrict: 'AE',
        scope: {
            reloadFun: '&'
        },
        link: function postLink(scope, iElement, iAttrs) {
            iElement.click(function () {
                scope.reloadFun();
            })
        }
    };
});
/**
 * pageSize的默认值是10
 */
commonDirective.directive('edPage', function ($http, ShareService) {
    return {
        restrict: 'A',
        scope: {
            queryParam: '=',
            pagination: '=',
            pageSize: '@',
            url: '@queryUrl',
            shareKey: '@'/*,
            blockEl: '@'*/
        },
        templateUrl: 'app/partials/page.html',
        link: function postLink(scope, iElement, iAttrs) {
            scope.pageSize = scope.pageSize || 10;
            scope.$on("query", function () {
                scope.gotoPage(1);
            });
            scope.$on("reload", function () {
                scope.gotoPage(scope.queryParam.page);
            });
            scope.gotoPage = function (page) {
                if (typeof page != 'number') {
                    return;
                }
                angular.extend(scope.queryParam, {
                    page: page,
                    pageSize: scope.pageSize
                });
                if (scope.shareKey) {
                    ShareService[scope.shareKey] = scope.queryParam;
                }
                scope.query(scope.queryParam);
            };
            scope.query = function (queryParam) {
                angular.extend(queryParam, {
//                    timestamp: new Date().getTime()
                });
//                if (scope.blockEl) {
//                    App.blockUI(scope.blockEl);
//                }
                $http({
                    method: 'GET',
                    url: scope.url,
                    params: queryParam
                }).success(function (data, status, headers, config) {
                        scope.pagination = data;
//                        if (scope.blockEl) {
//                            App.unblockUI(scope.blockEl);
//                        }
                    }).error(function (data, status, headers, config) {
//                        if (scope.blockEl) {
//                            App.unblockUI(scope.blockEl);
//                        }
                    });
            };
            if (scope.shareKey && ShareService[scope.shareKey]) {
                angular.extend(scope.queryParam, ShareService[scope.shareKey]);
                scope.query(scope.queryParam);
            } else {
                scope.gotoPage(1);
            }
        }
    };
});

commonDirective.directive('edSimplePage', function ($http, ShareService) {
    return {
        restrict: 'A',
        scope: {
            queryParam: '=',
            pagination: '=',
            pageSize: '@',
            url: '@queryUrl',
            shareKey: '@',
            blockEl: '@'
        },
        templateUrl: 'app/partials/simple_page.html',
        link: function postLink(scope, iElement, iAttrs) {
            scope.pageSize = scope.pageSize || 10;
            scope.$on("query", function () {
                scope.gotoPage(1);
            });
            scope.$on("reload", function () {
                scope.gotoPage(scope.queryParam.page);
            });
            scope.gotoPage = function (page) {
                if (typeof page != 'number') {
                    return;
                }
                angular.extend(scope.queryParam, {
                    page: page,
                    pageSize: scope.pageSize
                });
                if (scope.shareKey) {
                    ShareService[scope.shareKey] = scope.queryParam;
                }
                scope.query(scope.queryParam);
            };
            scope.query = function (queryParam) {
                angular.extend(queryParam, {
//                    timestamp: new Date().getTime()
                });
                if (scope.blockEl) {
                    App.blockUI(scope.blockEl);
                }
                $http({
                    method: 'GET',
                    url: scope.url,
                    params: queryParam
                }).success(function (data, status, headers, config) {
                        scope.pagination = data;
                        if (scope.blockEl) {
                            App.unblockUI(scope.blockEl);
                        }
                    }).error(function (data, status, headers, config) {
                        if (scope.blockEl) {
                            App.unblockUI(scope.blockEl);
                        }
                    });
            };
            if (scope.shareKey && ShareService[scope.shareKey]) {
                angular.extend(scope.queryParam, ShareService[scope.shareKey]);
                scope.query(scope.queryParam);
            } else {
                scope.gotoPage(1);
            }
        }
    };
});

commonDirective.directive('edDictSelect', function ($http, ShareService, $translate) {
    return {
        restrict: 'A',
        scope: {
            parentCode: '@',
            code: '=ngModel'
        },
        link: function postLink(scope, iElement, iAttrs) {
            var key = "dict_" + scope.parentCode;
            scope.$watch('code', function (value) {
                iElement.select2("val", scope.code);
            });
            if (ShareService[key]) {
                var options = ShareService[key];
                iElement.select2({data: options});
                if (scope.value) {
                    iElement.select2("val", scope.code);
                }
            } else {
                $http({
                    method: 'GET',
                    url: 'sys/dict/items/' + scope.parentCode
                }).success(function (data, status, headers, config) {

                        var options = [];
                        var dictNames = _.pluck(data, "dictName");
                        $translate(dictNames).then(function (names) {
                            angular.forEach(data, function (dict, key) {
                                options.push({id: dict.dictCode, text: names[dict.dictName]});
                            });
                            ShareService[key] = options;
                            iElement.select2({data: options});
                            if (scope.code) {
                                iElement.select2("val", scope.code);
                            }
                        });

                    }).error(function (data, status, headers, config) {
                        iElement.attr("disabled", "disabled");
                    });
            }
        }
    };
});

commonDirective.directive('edDictRadio', function ($http, $timeout, ShareService, $translate) {
    return {
        restrict: 'A',
        scope: {
            parentCode: '@',
            ngModel: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            scope.$watch('ngModel', function(value) {
                if(value != undefined) {
                    $timeout(function() {
                        iElement.find(':radio[value=' + value +']').iCheck('check');
                    })
                }
            });
            var setRadioHtml = function(options) {
                var radio = "";
                angular.forEach(options, function (option, key) {
                    radio += '<label><input type="radio" name="' + iAttrs.name + '" value="' + option.id + '"';
//                    if (scope.ngModel == option.id) {
//                        radio += ' checked="checked"';
//                    }
                    radio += '">' + option.text + '</label>';
                });
                iElement.html(radio);
                iElement.find(':radio').iCheck({
                    checkboxClass: 'icheckbox_minimal-green',
                    radioClass: 'iradio_minimal-green'
                });
                iElement.find(':radio').on('ifChecked', function (event) {
                    scope.$apply(function () {
                        var value = $(event.target).attr('value');
                        scope.ngModel = value;
                    });
                });
            }
            var key = "dict_" + scope.parentCode;
            if (ShareService[key]) {
                var options = ShareService[key];
                setRadioHtml(options);
            } else {
                $http({
                    method: 'GET',
                    url: 'sys/dict/items/' + scope.parentCode
                }).success(function (data, status, headers, config) {
                        var dictNames = _.pluck(data, "dictName");
                        $translate(dictNames).then(function (names) {
                            var options = [];
                            angular.forEach(data, function (dict, key) {
                                options.push({id: dict.dictCode, text: names[dict.dictName]});
                            });
                            ShareService[key] = options;
                            setRadioHtml(options);
                        });

                    }).error(function (data, status, headers, config) {
                        iElement.attr("disabled", "disabled");
                    });
            }
        }
    };
});

commonDirective.directive('edDictCheckbox', function ($http,$timeout, ShareService, $translate) {
    return {
        restrict: 'A',
        scope: {
            parentCode: '@',
            ngModel: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            scope.$watch('ngModel', function(value) {
                if(value) {
                    _.each(value, function(v, i) {
                        $timeout(function() {
                            iElement.find(':checkbox[value=' + v +']').iCheck('check');
                        });
                    });
                }
            });
            var setCheckboxHtml = function(options) {
                var checkbox = "";
                angular.forEach(options, function (option, key) {
                    checkbox += '<label><input type="checkbox" name="' + iAttrs.name + '" value="' + option.id + '"';
//                    if (_.contains(scope.ngModel, option.id)) {
//                        checkbox += ' checked="checked"';
//                    }
                    checkbox += '">' + option.text + '</label>';
                });
                iElement.html(checkbox);
                iElement.find(':checkbox').iCheck({
                    checkboxClass: 'icheckbox_minimal-green',
                    radioClass: 'iradio_minimal-green'
                });
                iElement.find(':checkbox').on('ifChecked', function (event) {
                    scope.$apply(function () {
                        var value = $(event.target).attr('value');
                        scope.ngModel.push(value);
                    });
                }).on('ifUnchecked', function (event) {
                    scope.$apply(function () {
                        var value = $(event.target).attr('value');
                        angular.forEach(scope.ngModel, function (v, i) {
                            if (angular.equals(value, v)) {
                                scope.ngModel.splice(i, 1);
                            }
                        });
                    });
                });
            }
            var key = "dict_" + scope.parentCode;
            if (ShareService[key]) {
                var options = ShareService[key];
                setCheckboxHtml(options);
            } else {
                $http({
                    method: 'GET',
                    url: 'sys/dict/items/' + scope.parentCode
                }).success(function (data, status, headers, config) {
                        var dictNames = _.pluck(data, "dictName");
                        $translate(dictNames).then(function (names) {
                            var options = [];
                            angular.forEach(data, function (dict, key) {
                                options.push({id: dict.dictCode, text: names[dict.dictName]});
                            });
                            ShareService[key] = options;
                            setCheckboxHtml(options);
                        });

                    }).error(function (data, status, headers, config) {
                        iElement.attr("disabled", "disabled");
                    });
            }
        }
    };
});

commonDirective.directive('edPermisson', function ($rootScope, $timeout) {
    return {
        restrict: 'A',
        link: function postLink(scope, iElement, iAttrs) {
            $rootScope.$watch("loginUser", function(value) {
                if (value) {
                    var perm = iAttrs.perm;
                    if (!_.contains($rootScope.loginUser.permissions, perm)) {
                        iElement.remove();
                    }
                }
            });
        }
    };
});
