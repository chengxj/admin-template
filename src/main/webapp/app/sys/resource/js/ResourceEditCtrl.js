function ResourceEditCtrl($scope, ResourceService, MessageService, LocationTo, $routeParams) {
    $scope.getResource = function () {
        $scope.resource = ResourceService.get({resourceId: $routeParams.resourceId}, function (data) {
            $scope.master = angular.copy($scope.resource);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getResource();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        ResourceService.update({resourceId: $scope.resource.resourceId}, $scope.resource, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/resource/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.resource, $scope.master);
    };

    $scope.reload = function () {
        $scope.getResource();
    };
}
ResourceEditCtrl.$inject = [ '$scope', 'ResourceService', 'MessageService', 'LocationTo', '$routeParams'];