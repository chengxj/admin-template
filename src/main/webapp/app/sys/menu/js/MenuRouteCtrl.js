function MenuRouteCtrl($scope, RouteService, MenuService, MessageService, LocationTo, $routeParams, $q) {
    $scope.selectRoutes = [];
    $scope.clickToken = false;
    MenuService.get({menuId : $routeParams.menuId}, function(data) {

    }, function() {
        $scope.disabled = true;
    });

    $scope.saveRoutePermission = function () {
        var routeIds = _.pluck($scope.selectRoutes, "routeId");
        MenuService.saveMenuRoute({menuId: $routeParams.menuId, routeIds: routeIds}, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/menu/list");
        }, function () {
            $scope.clickToken = false;
        });
    };

    $q.all([RouteService.query().$promise, MenuService.getRoutes({menuId : $routeParams.menuId}).$promise]).then(function(value) {
        _.each(value[0], function(route, i) {
            route.checked = _.some(value[1], function(v) {
                return route.routeId == v.routeId;
            });
        });
        $scope.routes = value[0];
    });
}
MenuRouteCtrl.$inject = [ '$scope','RouteService', 'MenuService', 'MessageService', 'LocationTo', '$routeParams', '$q'];