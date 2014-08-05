function PermissionAddCtrl($scope, MenuService, ResourceService,RouteService, MessageService, LocationTo, $routeParams) {
    $scope.selectRoutes = [];
    $scope.selectResources = [];
    MenuService.get({menuId : $routeParams.menuId}, function(data) {
        $scope.master = {parentId : data.menuId};
        $scope.menu = angular.copy($scope.master);
        $scope.clickToken = false;
    }, function() {
        $scope.disabled = true;
    });
    $scope.resources = ResourceService.query();
    $scope.routes = RouteService.query();
    $scope.save = function () {
        $scope.clickToken = true;
        $scope.menu.routeIds = _.pluck($scope.selectRoutes, 'routeId');
        $scope.menu.resourceIds = _.pluck($scope.selectResources, 'resourceId');
        MenuService.save($scope.menu, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/menu/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.menu, $scope.master);
    };
}
PermissionAddCtrl.$inject = [ '$scope', 'MenuService','ResourceService','RouteService', 'MessageService', 'LocationTo', '$routeParams'];