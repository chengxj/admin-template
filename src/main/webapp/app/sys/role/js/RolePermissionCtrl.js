function RolePermissionCtrl($scope, RoleService, RouteService,ResourceService, PermissionService,MenuService, MessageService, LocationTo, $routeParams, $q) {
    $scope.selectRoutes = [];
    $scope.selectMenus = [];
    $scope.selectResources = [];
    $scope.clickToken = false;

    $scope.queryMenu = function(undefined) {
        $q.all([MenuService.query().$promise, PermissionService.getMenus({roleId : $routeParams.roleId}).$promise]).then(function(value) {
            var data = value[0];
            var copyData = angular.copy(data);
            var stripeIndex = 0;
            var menus = _.filter(data, function(v, i) {
                v.checked = _.some(value[1], function(m) {
                    return m.menuId == v.menuId;
                });
                return v.parentId != -1 || (v.parentId == -1 && v.menuPath != undefined);
            });
            $scope.menus = menus;
        });
    };

    $scope.queryRoute = function() {
        if(!$scope.checkPermisson("sys:route:**:read"))  {
            return;
        }
        $q.all([RouteService.query().$promise, PermissionService.getRoutes({roleId : $routeParams.roleId}).$promise]).then(function(value) {
            _.each(value[0], function(route, i) {
                route.checked = _.some(value[1], function(v) {
                    return route.routeId == v.routeId;
                });
            });
            $scope.routes = value[0];
        });
    };

    $scope.queryResource = function() {
        $q.all([ResourceService.query().$promise, PermissionService.getResources({roleId : $routeParams.roleId}).$promise]).then(function(value) {
            _.each(value[0], function(resource, i) {
                resource.checked = _.some(value[1], function(v) {
                    return resource.resourceId == v.resourceId;
                });
            });
            $scope.resources = value[0];
        });
    };
    RoleService.get({roleId : $routeParams.roleId}, function(data) {
        $scope.queryMenu();
        $scope.queryRoute();
        $scope.queryResource();
    }, function() {
        $scope.disabled = true;
    });

    $scope.saveRoutePermission = function () {
        if(!$scope.checkPermisson("sys:route:**:read"))  {
            return;
        }
        var routeIds = _.pluck($scope.selectRoutes, "routeId");
        PermissionService.saveRoutePermission({roleId: $routeParams.roleId, routeIds: routeIds}, function () {
            MessageService.saveSuccess();
            $scope.queryResource();
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.saveMenuPermission = function () {
        var menuIds = _.pluck($scope.selectMenus, "menuId");
        PermissionService.saveMenuPermission({roleId: $routeParams.roleId, menuIds: menuIds}, function () {
            MessageService.saveSuccess();
            $scope.queryRoute();
            $scope.queryResource();
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.saveResourcePermission = function () {
        var resourceIds = _.pluck($scope.selectResources, "resourceId");
        PermissionService.saveResourcePermission({roleId: $routeParams.roleId, resourceIds: resourceIds}, function () {
            MessageService.saveSuccess();
        }, function () {
            $scope.clickToken = false;
        });
    };
}
RolePermissionCtrl.$inject = [ '$scope','RoleService', 'RouteService','ResourceService', 'PermissionService','MenuService', 'MessageService', 'LocationTo', '$routeParams', '$q'];