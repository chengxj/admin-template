function RouteResourceCtrl($scope, RouteService, ResourceService, MessageService, LocationTo, $routeParams, $q) {
    $scope.selectResources = [];
    $scope.clickToken = false;
    RouteService.get({routeId : $routeParams.routeId}, function(data) {

    }, function() {
        $scope.disabled = true;
    });
    $scope.saveResourcePermission = function () {
        var resourceId = _.pluck($scope.selectResources, "resourceId");
        RouteService.saveRouteRes({routeId: $routeParams.routeId, resourceIds: resourceId}, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/route/list");
        }, function () {
            $scope.clickToken = false;
        });
    };
    $q.all([ResourceService.query().$promise, RouteService.getResources({routeId : $routeParams.routeId}).$promise]).then(function(value) {
        _.each(value[0], function(resource, i) {
            resource.checked = _.some(value[1], function(v) {
                return resource.resourceId == v.resourceId;
            });
        });
        $scope.resources = value[0];
    });
}
RouteResourceCtrl.$inject = [ '$scope','RouteService', 'ResourceService', 'MessageService', 'LocationTo', '$routeParams', '$q'];