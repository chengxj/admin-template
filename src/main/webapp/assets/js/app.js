'use strict';

function browserSupportsCors() {
    if ("withCredentials" in new XMLHttpRequest())
        return true;
    else if (typeof XDomainRequest == "object")
        return true;
    else
        return false;
}

if (!browserSupportsCors()) {
    alert("浏览器不支持CORS！");
}

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
    .module('rootApp', ["ngRoute", "app.directives", "app.filters", "routeResolverServices", "app.services", "pascalprecht.translate"])
    .run(
    function ($route, $rootScope, $http, $timeout, $interval, $translate, $routeProvider, $routeResolverProvider) {
        $( "body").on("login", function() {
            $(".login").hide();
        }).on("unlogin", function() {
            $rootScope.loginUser = undefined;
            $(".login").show();
        });

        $rootScope.loginSuccess = function(data) {
            $.localStorage.set("accessToken", data.accessToken);
            $.localStorage.set("refreshToken", data.refreshToken);
            $.localStorage.set("secretKey", data.secretKey);
            $.localStorage.set("serverTime", data.serverTime);
            $.localStorage.set("clientTime", new Date().getTime());
            $rootScope.accessToken = data.accessToken;
            $rootScope.secretKey =data.secretKey;
            $rootScope.refreshToken = data.refreshToken;

            var expiresIn = new Number(data.expiresIn) - 3 * 60 * 1000;
            if (expiresIn <= 0) {
                $rootScope.expiresIn = data.expiresIn - 1000;
            } else {
                $rootScope.expiresIn = expiresIn;
            }
            if (!$rootScope.loginUser) {
                $rootScope.loadUser();
            }
            if ($rootScope.refresh) {
                $interval.cancel($rootScope.refresh);
            }
            $rootScope.refresh = $interval(function() {
                if ($rootScope.accessToken && $rootScope.refreshToken) {
                    $rootScope.refreshTokenFun();
                }
            }, $rootScope.expiresIn);
        }

        $rootScope.$on("login", function(event, data) {
            $rootScope.loginSuccess(data);
        });

        $rootScope.refreshTokenFun = function() {
            $http.get("https://10.4.7.15/auth/refresh",{params: {accessToken : $rootScope.accessToken, refreshToken : $rootScope.refreshToken}} ).success(
                function (data) {
                    $rootScope.loginSuccess(data);

                }).error(function() {
                    if ($rootScope.refresh) {
                        $interval.cancel($rootScope.refresh);
                    }
                });
        }
        $rootScope.refreshToken = $.localStorage.get("refreshToken");
        $rootScope.accessToken = $.localStorage.get("accessToken");
        if ($rootScope.refreshToken &&  $rootScope.accessToken) {
            $rootScope.refreshTokenFun();
        }

        $rootScope.loadRoute = function(routes) {
            var route = $routeResolverProvider.route
            $.each(routes, function (i, v) {
                $routeProvider.when(v.url, route.resolve(v.basename, v.path))
            });
            $routeProvider.when("/home/index", route.resolve('index', '/home'));
            $routeProvider.when("/home/profile/:userId", route.resolve('profile', '/home'));
            $routeProvider.otherwise({redirectTo: '/order/task/list'});
            $route.reload();
        };

        $rootScope.loadUser = function() {
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
                $rootScope.loadRoute(data.routes);
                if (data.user.profile && data.user.profile.language) {
                    $translate.use(data.user.profile.language);
                }
                $rootScope.loginUser = data.user;
                $("body").trigger("login");
                $timeout(function () {
                    App.init();
                });

            });
        }
        $rootScope.$on('$viewContentLoaded', function () {
//        $templateCache.removeAll();
            App.runHeight();
        });


        $rootScope.$watch("accessToken", function(value) {
            if (!value) {
                $("body").trigger("unlogin");
            }
        });

        $rootScope.logout = function () {
            $http.get('auth/logout').success(function (data) {
                $.localStorage.remove("accessToken");
//                $.localStorage.remove("refreshToken");
                $.localStorage.remove("secretKey");
                $rootScope.loginUser = null;
                $rootScope.accessToken =null;
                $rootScope.secretKey = null;
                $rootScope.refreshToken = null;
                $("body").trigger("unlogin");
                if ($rootScope.refresh) {
                    $interval.cancel($rootScope.refresh);
                }
            }).error(function () {
                $timeout(function () {
                    $("body").trigger("unlogin");
                }, 500);
            });

        };

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

        moment.lang("en");
    }).config(
        [
            '$provide', '$routeProvider', '$httpProvider', 'routeResolverProvider','$compileProvider','$translateProvider',
            function ($provide, $routeProvider, $httpProvider, routeResolverProvider, $compileProvider, $translateProvider) {
                //将config使用$provide.factory转移到run中执行，并不是angular推荐的方式
                $provide.factory('$routeProvider', function () {
                    return $routeProvider;
                });
                httpInterceptor($httpProvider);
                $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|file|javascript):/);
                routeResolverProvider.routeConfig.setBaseDirectories('app', 'app');
                var route = routeResolverProvider.route;
                $provide.factory('$routeResolverProvider', function () {
                    return routeResolverProvider;
                });

                $translateProvider.useStaticFilesLoader({
                    prefix: 'app/i18n/locale-',
                    suffix: '.json'
                });
                $translateProvider
                    .preferredLanguage("en");
            } ]);

function hmac256(baseString, secretKey) {
    return CryptoJS.HmacSHA256(baseString, secretKey).toString();
}

function httpInterceptor($httpProvider) {
    $httpProvider.interceptors.push(function ($q) {
        return {
            'request': function(config) {
                if (config.url.indexOf(".html") > 0) {
                    return config;
                }
                if (config.url.indexOf(".js") > 0) {
                    return config;
                }
                if (config.url.indexOf(".css") > 0) {
                    return config;
                }
                var serverTime = $.localStorage.get("serverTime");
                var clientTime = $.localStorage.get("clientTime");
                var timeDiff = clientTime - serverTime;
                if (config && config.params) {
                    config.params.timestamp = new Date().getTime() - timeDiff;
                } else {
                    config.params = {timestamp:new Date().getTime() - timeDiff};
                }
                if (config.url.indexOf("/auth/login") > 0) {
                    return config;
                }
                if (config.url.indexOf("/auth/refresh") > 0) {
                    return config;
                }

                //增加随机数
                config.params.nonce = CryptoJS.lib.WordArray.random(128/8).toString()
                //增加TOKEN
                var accessToken = $.localStorage.get("accessToken");
                var secretKey = $.localStorage.get("secretKey");
                if (accessToken == undefined || accessToken == null) {
                    accessToken = "";
                }
                if (secretKey == undefined || secretKey == null) {
                    secretKey = "";
                }
                config.params.accessToken = accessToken;
                //HMAC签名
                //对排序后与URL一起签名
                if (config.url.indexOf(".html") < 0 && config.url.indexOf(".json") < 0) {
                    var queryArray = [];
                    if (config.params) {
                        queryArray = queryArray.concat(getArray(config.params));
                    }
                    var baseString = config.method + config.url + "?" + queryArray.join("&");
                    config.params.digest = hmac256(baseString, secretKey);
                }

                return config;
            },
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
                    $( "body").trigger("unlogin");
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
                        $( "body").trigger("unlogin");;
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