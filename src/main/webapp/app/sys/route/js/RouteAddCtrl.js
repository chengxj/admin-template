function RouteAddCtrl($scope, RouteService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.route = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        RouteService.save($scope.route, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/route/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.route, $scope.master);
    };
}
RouteAddCtrl.$inject = [ '$scope', 'RouteService', 'MessageService', 'LocationTo'];