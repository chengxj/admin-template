function UserAddCtrl($scope, UserService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.user = angular.copy($scope.master);
    $scope.clickToken = false;


    $scope.save = function () {
        $scope.clickToken = true;
        UserService.save($scope.user, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/user/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.user, $scope.master);
    };

    $scope.roles = UserService.getAllRoles({}, function (data) {
        var options = [];
        angular.forEach(data, function (role, key) {
            options.push({id: role.roleId, text: role.roleName});
        });
        $scope.options = options
//        $(".select2").select2();
//        $scope.$broadcast("init", options);
    });
}
UserAddCtrl.$inject = [ '$scope', 'UserService', 'MessageService', 'LocationTo'];