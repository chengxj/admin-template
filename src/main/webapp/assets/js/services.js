'use strict';

var commonService = angular.module('app.services', [ 'ngResource' ]);

commonService.factory('ShareService', function () {
	  return {};
	});

commonService.factory('LocationTo', function ($location) {
    return {
        path : function(path) {
            $location.path(path);
        }
    };
});

commonService.factory('MessageService', function ($translate) {
    return {
        saveSuccess : function() {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            $translate("msg.save.success").then(function (msg) {
                toastr.success(msg);
            });

        },
        removeSuccess : function() {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            $translate("msg.delete.success").then(function (msg) {
                toastr.success(msg);
            });

        },
        successMsg : function(msg) {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            toastr.success(msg);
        },
        errorMsg : function(msg) {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "positionClass": "toast-bottom-center",
                "onclick": null,
                "timeOut": "2000",
                "extendedTimeOut": "500"
            }
            toastr.error(msg);
        }
    };
});

commonService.factory(
    'DictService',
    function ($resource) {
        return $resource("sys/dict/:dictCode", {}, {
            update : {method : "put"},
            items : {url : 'sys/dict/items/:dictCode',method : "get", isArray: true}
        });
    });

commonService.factory(
    'I18nService',
    function ($resource) {
        return $resource("sys/i18n/:i18nId", {}, {
            update : {method : "put"},
            saveToJson : {url : 'sys/i18n/tojson', method : 'post'}
        });
    });

commonService.factory(
    'RouteService',
    function ($resource) {
        return $resource("sys/route/:routeId", {}, {
            update : {method : "put"},
            pagination : {method : 'get',params : {routeId : "pagination"}} ,
            getResources : {url : 'sys/route/resource/:routeId', method : 'get', isArray: true},
            saveRouteRes : {url : 'sys/route/resource',method : 'post'}
        });
    });

commonService.factory(
    'RoleService',
    function ($resource) {
        return $resource("sys/role/:roleId", {}, {
            update : {method : "put"},
            savePermission : {method : 'post'}
        });
    });

commonService.factory(
    'MenuService',
    function ($resource) {
        return $resource("sys/menu/:menuId", {}, {
            update : {method : "put"},
            getRoutes : {url : 'sys/menu/route/:menuId', method : 'get', isArray: true},
            saveMenuRoute : {url : 'sys/menu/route',method : 'post'}
        });
    });

commonService.factory(
    'PermissionService',
    function ($resource) {
        return $resource("sys/permission/:roleId", {}, {
            saveRoutePermission : {method : 'post', params: {roleId : "route"}},
            saveMenuPermission : {method : 'post', params: {roleId : "menu"}},
            saveResourcePermission : {method : 'post', params: {roleId : "resource"}},
            getRoutes : {url : 'sys/permission/route/:roleId', method : 'get', isArray: true},
            getMenus : {url : 'sys/permission/menu/:roleId', method : 'get', isArray: true},
            getResources : {url : 'sys/permission/resource/:roleId', method : 'get', isArray: true}
        });
    });

commonService.factory(
    'UserService',
    function ($resource) {
        return $resource("sys/user/:userId", {}, {
            update : {method : "put"},
            getRoles : {method : "GET", url : "sys/user/role/:userId", isArray:true},
            getProfile  : {method : "GET", url : "sys/user/profile/:userId"},
            updateProfile  : {method : "put", url : "sys/user/profile/:userId"},
            updatePassword  : {method : "put", url : "sys/user/password/:userId"}
        });
    });

commonService.factory(
    'ResourceService',
    function ($resource) {
        return $resource("sys/resource/:resourceId", {}, {
            update : {method : "put"}
        });
    });

commonService.factory(
    'JobService',
    function ($resource) {
        return $resource("sys/job/:jobId", {}, {
            update : {method : "put"}
        });
    });
