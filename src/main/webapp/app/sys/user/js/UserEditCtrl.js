function UserEditCtrl($scope, UserService, RoleService, MessageService, LocationTo, $routeParams, $q) {
    $scope.getUser = function () {
        $scope.user = UserService.get({userId: $routeParams.userId}, function (data) {
            $scope.master = angular.copy($scope.user);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getUser();

    $q.all([RoleService.query().$promise, UserService.getRoles({userId: $routeParams.userId}).$promise]).then(function(value) {
        var options = [];
        angular.forEach(value[0], function (role, key) {
            var o = {};
            o.id = role.roleId;
            o.text = role.roleName;
            angular.forEach(value[1], function (userRole, key) {
                if (userRole.roleId == o.id) {
                    o.selected = true;
                }
            })
            options.push(o);
        });
        $scope.options = options;
    }, function() {
        $scope.disabled = true;
    });

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        UserService.update({userId: $scope.user.userId}, $scope.user, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/user/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.user, $scope.master);
    };

    $scope.reload = function () {
        $scope.getUser();
    };
}
UserEditCtrl.$inject = [ '$scope', 'UserService', 'RoleService', 'MessageService', 'LocationTo', '$routeParams', '$q'];