function PermissionEditCtrl($scope, MenuService,ResourceService,RouteService, MessageService, LocationTo, $routeParams, $q) {
    $scope.selectRoutes = [];
    $scope.selectResources = [];

    $scope.getMenu = function () {
        $scope.menu = MenuService.get({menuId: $routeParams.menuId}, function (data) {
            $scope.master = angular.copy($scope.menu);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getMenu();

    $q.all([RouteService.query().$promise, MenuService.getRoutes({menuId : $routeParams.menuId}).$promise]).then(function(value) {
        _.each(value[0], function(route, i) {
            route.checked = _.some(value[1], function(v) {
                return route.routeId == v.routeId;
            });
        });
        $scope.routes = value[0];
    });

    $q.all([ResourceService.query().$promise, MenuService.getResources({menuId : $routeParams.menuId}).$promise]).then(function(value) {
        _.each(value[0], function(res, i) {
            res.checked = _.some(value[1], function(v) {
                return res.resourceId == v.resourceId;
            });
        });
        $scope.resources = value[0];
    });

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        $scope.menu.routeIds = _.pluck($scope.selectRoutes, 'routeId');
        $scope.menu.resourceIds = _.pluck($scope.selectResources, 'resourceId');
        MenuService.update({menuId: $scope.menu.menuId}, $scope.menu, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/menu/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.reload = function () {
        $scope.getMenu();
    };
}
PermissionEditCtrl.$inject = [ '$scope', 'MenuService','ResourceService','RouteService', 'MessageService', 'LocationTo', '$routeParams', '$q'];