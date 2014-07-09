'use strict';

var commonDirective = angular.module('app.directives', ["app.services"]);

/*
 * ed-page-title指令,将元素自动添加到 #page-header 元素中
 * 用法: <div class="page-title" ed-page-title>
 * */
commonDirective.directive('edPageTitle', function () {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            $('#page-header .page-title').remove();
            iElement.prependTo('#page-header');
        }
    };
});

/*
 * ed-form-toggle指令,实现查询表单的显示和隐藏的切换
 * 参数:minus 是否隐藏,如果设置了参数minus='true',则查询表单默认为隐藏状态
 * 用法:默认隐藏表单:<form role="form" class="form-horizontal" ed-form-toggle minus="true">
 *       默认显示表单:<form role="form" class="form-horizontal" ed-form-toggle>
 * */
commonDirective.directive('edFormToggle', function () {
    return {
        restrict: 'AE',
        scope: {
            'minus': '@'
        },
        link: function postLink(scope, iElement, iAttrs) {
            if (scope.minus == 'true') {
                iElement.addClass("form-minus");
                iElement.find('.title').prepend('<i class="fa fa-caret-right"></i><label class="blank5">&nbsp;</label>');
                iElement.find('.title').toggle(function () {
                    iElement.find('.title').find('i').removeClass("fa-caret-right").addClass("fa-caret-down");
                    iElement.removeClass("form-minus");
                }, function () {
                    iElement.find('.title').find('i').removeClass("fa-caret-down").addClass("fa-caret-right");
                    iElement.addClass("form-minus");
                });
            } else {
                iElement.removeClass("form-minus");
                iElement.find('.title').prepend('<i class="fa fa-caret-down"></i><label class="blank5">&nbsp;</label>');
                iElement.find('.title').toggle(function () {
                    iElement.find('.title').find('i').removeClass("fa-caret-down").addClass("fa-caret-right");
                    iElement.addClass("form-minus");
                }, function () {
                    iElement.find('.title').find('i').removeClass("fa-caret-right").addClass("fa-caret-down");
                    iElement.removeClass("form-minus");
                });
            }

        }
    };
});

/*
 * ed-focus指令:监听input对象的鼠标事件,当鼠标进入元素时,设置model的$focused为true;离开元素时,设置model的$focused为false
 * input元素监听mouseenter和mouseleave
 * select2元素监听select2-focus和select2-blur
 * 用法:<input ng-model="dict.dictName" ed-focus>
 * */
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

/*
 * ed-integer指令:校验输入的值是否是整数,如果是整数,设置model的integer校验为true
 * 用法:<input ng-model="menu.sorted" ed-integer ed-focus>
 * */
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

/*
 * ed--positive-integer指令:校验输入的值是否是正整数,如果是正整数,设置model的positiveInteger校验为true
 * 用法:<input ng-model="commodity.monthlyFreeEscort" ed-focus ed-positive-integer>
 * */
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

/*
 * ed--number:校验输入的值是否是>0的数值,如果是,设置model的number校验为true
 * 用法:<input ng-model="commodity.escortFee" ed-focus ed-number>
 * */
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

/*
 * ed-ensure-unique:唯一性校验,如果是唯一值,设置model的unique校验为true
 * 用法:<input ng-model="dict.dictCode" ed-focus ed-ensure-unique="sys/dict/check/dictCode">
 * 参数:ed-ensure-unique属性值是校验唯一性的后台服务API
 * */
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

/*
 * ed-delete:删除按钮
 * 用法:<a ed-delete click-fun="remove(i18n)" success-fun="reload()"></a>
 * 参数:click-fun :点击按钮后调用的方法,此方法需要返回$promise
 如：$scope.remove = function (model) {
 return I18nService.remove({i18nId : model.i18nId, updatedTime:model.updatedTime}).$promise;
 }
 *       success-fun :删除成功后调用的方法
 * */
commonDirective.directive('edDelete', function ($http, MessageService, $translate) {
    return {
        restrict: 'A',
        scope: {
            clickFun: '&',
            successFun: '&'
        },
        link: function (scope, elm, attrs, ctrl) {
            $translate(['help.common.delete.confirm']).then(function (translations) {
                elm.click(function () {
                    $.confirm({
                        text: translations["help.common.delete.confirm"],
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
                });

        }
    };
});

/*
 * ed-delete-all:批量删除按钮
 * 用法:<a ed-delete-all click-fun="remove(i18n)" success-fun="reload()" model-array="selectI18ns"></a>
 * 参数:click-fun :点击按钮后调用的方法,此方法需要返回$promise
 如：$scope.remove = function (model) {
 return I18nService.remove({i18nId : model.i18nId, updatedTime:model.updatedTime}).$promise;
 }
 *       success-fun :删除成功后调用的方法
 *model-array: 需要删除的model
 * */
commonDirective.directive('edDeleteAll', function ($q, MessageService, $translate) {
    return {
        restrict: 'A',
        scope: {
            clickFun: '&',
            successFun: '&',
            modelArray: '='
        },
        link: function (scope, elm, attrs, ctrl) {
            $translate(['help.common.delete.confirm']).then(function (translations) {
                elm.click(function () {
                    $.confirm({
                        text: translations["help.common.delete.confirm"],
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
            });

        }
    };
});

/*
 * ed-multi-select:多选的select
 * 用法:<input ng-model="user.roleIds" ed-multi-select options="options">
 * 参数:options :select2的下拉列表
 如：[{id: "1", text: "未领取", selected : true},{id: "2", text: "进行中"}]
 * */
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
                    var selectedOptions = _.filter(value, function (v, i) {
                        return v.selected == true;
                    });
                    var val = _.pluck(selectedOptions, "id");
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

/*
 * ed-select2:多选的select
 * 用法:<input ng-model="user.state" ed-select2 options="states">
 * 参数:options :select2的下拉列表
 如：[{id: "1", text: "未领取", selected : true},{id: "2", text: "进行中"}]
 * */
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
                    var selectedOptions = _.filter(value, function (v, i) {
                        return v.selected == true;
                    });
                    var val = _.pluck(selectedOptions, "id");
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

/*
 * ed-tooltip:向下的tooltip
 * 用法:<a ed-tooltip data-original-title="删除"> </a>
 * */
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
/*
 * ed-icheck:美化checkbox和radio
 * 用法:<a ed-tooltip data-original-title="删除"> </a>
 * */
commonDirective.directive('edIcheck', function ($timeout) {
    return {
        restrict: 'AE',
        scope : {
          ngModel : '=',
          modelArray : 'modelArray'
        },
        link: function postLink(scope, iElement, iAttrs) {
            $timeout(function () {
                iElement.iCheck({
                    checkboxClass: 'icheckbox_minimal-green',
                    radioClass: 'iradio_minimal-green'
                });
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
            }, 100);
        }
    };
});

/*
 * ed-checkbox:列表的checkbox
 * 用法:<input type="checkbox" ng-model="i18n" model-array="selectI18ns" ed-checkbox/>
 * 参数:ng-model:checkbox对应的model
 * model-array:选中的model数组
 * showcontextmenu : 是否启用右键菜单,默认为启用<input type="checkbox" ng-model="route" model-array="selectRoutes" ed-checkbox showcontextmenu="false"/>
 * */
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

/*
 * ed-check-all:列表的全选checkbox
 * 用法:<input type="checkbox" ed-check-all/>
 * 参数：elName,全选是需要选中的checkbox的name,默认值:pk,<input type="checkbox" ed-check-all el-name="pk"/>
 * */
commonDirective.directive('edCheckAll', function ($timeout) {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            var elName = iAttrs.elName;
            if (elName == undefined || elName == '') {
                elName = "pk"
            }
            iElement.on('ifChecked', function (event) {
                $('[name=' + elName + ']:checkbox').iCheck('check');
            });
            iElement.on('ifUnchecked', function (event) {
                $('[name=' + elName + ']:checkbox').iCheck('uncheck');
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

/**    ed-touch-spin
 * 用法
 * <input
 value="0"
 data-bts-min="0"
 data-bts-max="100"
 data-bts-init-val=""
 data-bts-step-interval="100"
 data-bts-button-down-class="btn btn-violet"
 data-bts-button-up-class="btn btn-violet"
 ed-touch-spin>
 */
commonDirective.directive('edTouchSpin', function () {
    return {
        restrict: 'AE',
        link: function postLink(scope, iElement, iAttrs) {
            iElement.TouchSpin();
        }
    };
});

/**    ed-date-range 日期范围指令
 * 用法
 * <div class="btn btn-default" ed-date-range start-date="startDate" end-date="endDate">
 <i class="fa fa-calendar"></i>
 <span>{{startDate}} - {{endDate}}</span> <b class="caret"></b>
 </div>
 参数 : startDate:开始日期,endDate:结束日期
 */
commonDirective.directive('edDateRange', function ($translate) {
    return {
        restrict: 'AE',
        scope: {
            startDate: '=',
            endDate: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            $translate(['Today', 'Yesterday', 'Last 7 Days', 'Last 30 Days', 'This Month', 'Last Month'
                , 'label.common.op.accept', 'label.common.op.cancel', 'label.common.from'
                , 'label.common.to', 'label.common.range']).then(function (translations) {
                    var today = translations['Today'];
                    var ranges = {};
                    ranges[translations['Today']] = [moment(), moment()];
                    ranges[translations['Yesterday']] = [moment().subtract('days', 1), moment().subtract('days', 1)];
                    ranges[translations['Last 7 Days']] = [moment().subtract('days', 6), moment()];
                    ranges[translations['Last 30 Days']] = [moment().subtract('days', 29), moment()];
                    ranges[translations['This Month']] = [moment().startOf('month'), moment()];
                    ranges[translations['Last Month']] = [moment().subtract('month', 1).startOf('month'), moment().subtract('month', 1).endOf('month')];
                    iElement.daterangepicker(
                        {
                            ranges: ranges,
                            startDate: scope.startDate,//.subtract('days', 29),
                            endDate: scope.endDate,
                            locale: {
                                applyLabel: translations['label.common.op.accept'],
                                cancelLabel: translations['label.common.op.cancel'],
                                fromLabel: translations['label.common.from'],
                                toLabel: translations['label.common.to'],
                                weekLabel: 'W',
                                customRangeLabel: translations['label.common.range'],
                                daysOfWeek: moment()._lang._weekdaysMin.slice(),
                                monthNames: moment()._lang._monthsShort.slice(),
                                firstDay: 0
                            }
                        },
                        function (start, end) {
                            scope.$apply(function() {
                                scope.startDate = start;
                                scope.endDate = end;
                            });
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

/**    ed-page 分页指令,显示详细的页码
 * 用法
 * <div ed-page page-size="10"
 pagination="pagination" query-param="queryParam" query-url="sys/i18n/pagination"
 share-key="RoleList" block-el=".panel-body">
 参数 : page-size:每页数量,默认值10;
 pagination:数据源
 query-param:查询条件
 query-url:分页查询的Rest API
 share-key:页面缓存的key,默认值null,不使用这个属性则代表不启用缓存
 block-el:blockUI的DOM元素,默认值null,不使用这个属性则代表不启用BlockUI
 */
commonDirective.directive('edPage', function ($http, ShareService) {
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

/**    ed-simple-page 简单分页指令,值显示上一页,下一页
 * 用法
 * <div ed-simple-page page-size="10"
 pagination="pagination" query-param="queryParam" query-url="sys/i18n/pagination"
 share-key="RoleList" block-el=".panel-body">
 参数 : page-size:每页数量,默认值10;
 pagination:数据源
 query-param:查询条件
 query-url:分页查询的Rest API
 share-key:页面缓存的key,默认值null,不使用这个属性则代表不启用缓存
 block-el:blockUI的DOM元素,默认值null,不使用这个属性则代表不启用BlockUI
 */
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
            scope.p = "";
            iElement.find("#page-input").keypress(function(event) {
               return (event.charCode >=48 && event.charCode <= 57) || event.keyCode == 8;
            });
            scope.pageSize = scope.pageSize || 10;
            scope.$on("query", function () {
                scope.gotoPage(1);
            });
            scope.$on("reload", function () {
                scope.gotoPage(scope.queryParam.page);
            });
            scope.enterPageValue = function ($event) {
                if ($event.keyCode == "13") {
                    var _p = new Number(scope.p).valueOf();
                    if (_p == NaN) {
                        return false;
                    }
                    scope.gotoPage(_p);
                }
            };
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
                        scope.p = "";
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
            scope.$watch('ngModel', function (value) {
                if (value != undefined) {
                    $timeout(function () {
                        iElement.find(':radio[value=' + value + ']').iCheck('check');
                    })
                }
            });
            var setRadioHtml = function (options) {
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

commonDirective.directive('edDictCheckbox', function ($http, $timeout, ShareService, $translate) {
    return {
        restrict: 'A',
        scope: {
            parentCode: '@',
            ngModel: '='
        },
        link: function postLink(scope, iElement, iAttrs) {
            scope.$watch('ngModel', function (value) {
                if (value) {
                    _.each(value, function (v, i) {
                        $timeout(function () {
                            iElement.find(':checkbox[value=' + v + ']').iCheck('check');
                        });
                    });
                }
            });
            var setCheckboxHtml = function (options) {
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
                iElement.find(':checkbox').on('ifChecked',function (event) {
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
            $rootScope.$watch("loginUser", function (value) {
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
