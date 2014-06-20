function RoleEditCtrl($scope, RoleService, MessageService, LocationTo, $routeParams) {
    $scope.getRole = function () {
        $scope.role = RoleService.get({roleId: $routeParams.roleId}, function (data) {
            $scope.master = angular.copy($scope.role);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getRole();

    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        RoleService.update({roleId: $scope.role.roleId}, $scope.role, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/role/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.role, $scope.master);
    };

    $scope.reload = function () {
        $scope.getRole();
    };
}
RoleEditCtrl.$inject = [ '$scope', 'RoleService', 'MessageService', 'LocationTo', '$routeParams'];