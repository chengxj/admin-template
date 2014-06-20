function RoleAddCtrl($scope, RoleService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.role = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        RoleService.save($scope.role, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/role/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.role, $scope.master);
    };
}
RoleAddCtrl.$inject = [ '$scope', 'RoleService', 'MessageService', 'LocationTo'];