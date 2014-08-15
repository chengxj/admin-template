'use strict';

if (!String.prototype.trim) {
    String.prototype.trim = function () {
        return this.replace(/^\s+|\s+$/g, '');
    };
}
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function (searchElement, fromIndex) {
        if ( this === undefined || this === null ) {
            throw new TypeError( '"this" is null or not defined' );
        }

        var length = this.length >>> 0; // Hack to convert object.length to a UInt32

        fromIndex = +fromIndex || 0;

        if (Math.abs(fromIndex) === Infinity) {
            fromIndex = 0;
        }

        if (fromIndex < 0) {
            fromIndex += length;
            if (fromIndex < 0) {
                fromIndex = 0;
            }
        }

        for (;fromIndex < length; fromIndex++) {
            if (this[fromIndex] === searchElement) {
                return fromIndex;
            }
        }

        return -1;
    };
}

angular
    .module('rootApp', ["ngRoute", "app.directives", "app.filters", "routeResolverServices", "app.services", "pascalprecht.translate" ])
    .run(
    function ($route, $rootScope, $http, $timeout, $translate, MessageService) {
        $rootScope.checkPermisson = function(permission) {
            return _.contains($rootScope.loginUser.permissions, permission);
        }
        $rootScope.$on('$viewContentLoaded', function () {
//        $templateCache.removeAll();
            App.runHeight();
        });
        $rootScope.$on('$routeChangeSuccess', function () {

        });
        $rootScope.logout = function () {
            $http.post('auth/logout').success(function (data) {
                $translate("msg.logout.success").then(function (msg) {
                    MessageService.successMsg(msg);
                });
                $timeout(function () {
                   window.location.href = "login.html";
                }, 500);
            }).error(function () {
                    $translate("msg.logout.failed").then(function (msg) {
                        MessageService.errorMsg(msg);
                    });

                });

        };

        $rootScope.accessToken = $.cookie("accessToken");
        $rootScope.refreshToken = $.cookie("refreshToken");
        $rootScope.sessionSecret = $.cookie("sessionSecret");
        console.log($rootScope.accessToken);
        console.log($rootScope.refreshToken);
        console.log($rootScope.sessionSecret);

        $http.get("index/user").success(function (data) {
            var menus = [];
            var copyData = angular.copy(data.menus);
            _.each(data.menus, function (v, i) {
                if (v.parentId == -1) {
                    menus.push(v);
                    var childMenus = _.filter(copyData, function (c, j) {
                        return c.parentId == v.menuId;
                    });
                    v.childMenus = _.sortBy(childMenus, function(v, i) {
                        return v.sorted;
                    });
                    if (v.childMenus.length > 0) {
                        v["class"] = 'has-submenu';
                    }
                }
            });
            _.each(menus, function (v, i) {
                if (!v.icon) {
                    v.icon = 'fa-info'
                }
                if (v["class"]) {
                    v.menuPath = "javascript:;"
                } else if (v.menuPath) {
                    v.menuPath = "#" + v.menuPath;
                } else {
                    v.menuPath = "javascript:;"
                }
            });
            $rootScope.leftMenus = _.sortBy(menus, function(v, i) {
                return v.sorted;
            });
            data.user.routes = _.pluck(data.routes, "url");
            $rootScope.loginUser = data.user;
            $timeout(function () {
                App.init();
            })
        });

        //禁用页面上的backspace回退功能
        $(document).unbind('keydown').bind('keydown', function (event) {
            var doPrevent = false;
            if (event.keyCode === 8) {
                var d = event.srcElement || event.target;
                if ((d.tagName.toUpperCase() === 'INPUT' && (d.type.toUpperCase() === 'TEXT' || d.type.toUpperCase() === 'PASSWORD' || d.type.toUpperCase() === 'FILE'))
                    || d.tagName.toUpperCase() === 'TEXTAREA') {
                    doPrevent = d.readOnly || d.disabled;
                }
                else {
                    doPrevent = true;
                }
            }

            if (doPrevent) {
                event.preventDefault();
            }
        });
    }).config(
        [
            '$routeProvider', '$httpProvider', '$locationProvider', 'routeResolverProvider','$compileProvider','$translateProvider',
            function ($routeProvider, $httpProvider, $locationProvider, routeResolverProvider, $compileProvider, $translateProvider) {
                httpInterceptor($httpProvider);
                $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
                routeResolverProvider.routeConfig.setBaseDirectories('app', 'app');
                var route = routeResolverProvider.route;

                $.ajax({
                    url: 'index/user',
                    data : {random :  new Date().getTime()},
                    method: 'get',
                    async: false,
                    success: function (data) {
                        //设置路由
                        var routes = data.routes;
                        $.each(routes, function (i, v) {
                            $routeProvider.when(v.url, route.resolve(v.basename, v.path))
                        });
                        $routeProvider.when("/home/index", route.resolve('index', '/home'));
                        $routeProvider.when("/home/profile/:userId", route.resolve('profile', '/home'));
                        $routeProvider.otherwise({redirectTo: '/order/task/list'});
                        //设置Profile
                        var profile = data.user.profile;
                        $translateProvider.useStaticFilesLoader({
                            prefix: 'app/i18n/locale-',
                            suffix: '.json'
                        });
                        $translateProvider
                            .preferredLanguage(profile.language);
                        moment.lang(profile.language.toLowerCase());
                        httpInterceptorParam($httpProvider, profile);
                    }
                });
            } ]);

function httpInterceptor($httpProvider, profile) {
    $httpProvider.interceptors.push(function ($q) {
        return {
            response: function (response) {
                $(".form-body #error-alert").remove();
                if (response.headers()['content-type'] === "application/json;charset=UTF-8") {
                    //var data = examineJSONResponse(response); // assumes this function is available

//                    if(!data)
//                        return $q.reject(response);
//                    console.log(response.data.result);
                }
                return response;
            },
            responseError: function (response) {
                if (response.status == "401") {
                    //window.location.href= "spring_security_login";
                } else if (response.status == "403") {
                    if (response.data.exMessage && response.data.code == "100") {
                        $(".form-body #error-alert").remove();
                        var msg = '<div id="error-alert" class="alert alert-danger">';
                        angular.forEach(response.data.exMessage, function (value, key) {
                            var label = $(".form-group label[for=" + key + "]").text();
                            msg += label + " : " + value + "<br />";
                        });
                        msg += '</div>';

                        $(".form-body").prepend(msg);
                    } else if (response.data.code == "20") {
                        showWarning(response.data.message);
                    } else if (response.data.code == "30") {
                        window.location.href = "login.html";
                    } else {
                        showError(response.data.message);
                    }
                } else {
                    if (response.data.message) {
                        showError("Error : [" + response.data.message + "]");
                    } else if (response.data) {
                        showError("Error : [" + response.data + "]");
                    } else {
                        showError("Error : ");
                    }
                }
                return $q.reject(response);
            }
        };
    });
}

function httpInterceptorParam($httpProvider, profile) {
    $httpProvider.interceptors.push(function () {
        return {
            'request': function(config) {
                if (config && config.params) {
                    config.params.lang = profile.language;
                    config.params.random = new Date().getTime();
                } else {
                    config.params = {lang : profile.language, random:new Date().getTime()};
                }

                //增加TOKEN
                config.params.accessToken = $.cookie("accessToken");
                //HMAC签名
                //对排序后与URL一起签名
                if (config.url.indexOf(".html") < 0 && config.url.indexOf(".json") < 0) {
                    var queryArray = [];
                    if (config.params) {
                        queryArray = queryArray.concat(getArray(config.params));
                    }
                    if (config.data) {
                        var data = angular.toJson(config.data);
                        queryArray = queryArray.concat(getArray(angular.fromJson(data)));
                    }
                    var baseString = config.method + config.url + "?" + queryArray.join("&");
                    console.log(baseString);
                    config.params.digest = CryptoJS.HmacSHA256(baseString, "aaaaaaaaaaaaa").toString();
                }

                return config;
            }

        };
    });
}
function getArray(obj) {
    var queryArray = [];
    var keys = _.sortBy(_.keys(obj), function(k) {
        return k;
    });
    keys = _.without(keys, "digest");
    _.each(keys, function(v) {
        var value = obj[v];
        if (_.isArray(value)) {
            value = _.sortBy(value, function(va) {
                return va;
            } );
            value = value.join(",")
        }
        queryArray.push(v + "=" + value);
    });
    return queryArray;
}

function showSuccess(msg) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "timeOut": "2000",
        "extendedTimeOut": "500",
        "positionClass": "toast-bottom-center",
        "onclick": null
    }
    toastr.success(msg);
}

function showWarning(msg) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "timeOut": "2000",
        "extendedTimeOut": "500",
        "positionClass": "toast-bottom-center",
        "onclick": null
    }
    toastr.warning(msg);
}
function showError(msg) {
    toastr.options = {
        "closeButton": false,
        "debug": false,
        "timeOut": "2000",
        "extendedTimeOut": "500",
        "positionClass": "toast-bottom-center",
        "onclick": null
    }
    toastr.error(msg);
}