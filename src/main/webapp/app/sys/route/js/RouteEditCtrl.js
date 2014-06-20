function RouteEditCtrl($scope, RouteService, MessageService, LocationTo, $routeParams) {
    $scope.getRoute = function () {
        $scope.route = RouteService.get({routeId: $routeParams.routeId}, function (data) {
            $scope.master = angular.copy($scope.route);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getRoute();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        RouteService.update({routeId: $scope.route.routeId}, $scope.route, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/route/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.route, $scope.master);
    };

    $scope.reload = function () {
        $scope.getRoute();
    };
}
RouteEditCtrl.$inject = [ '$scope', 'RouteService', 'MessageService', 'LocationTo', '$routeParams'];