'use strict';
function LoginCtrl($scope, $http, RootScope) {
    $scope.user = {};
    $scope.login = function () {
        $http.post("https://10.4.7.15/auth/login", $scope.user).success(
            function (data) {
                $scope.$emit("login", data);
            });
    }
        $scope.enterLogin = function ($event) {
            if ($scope.user.username && $scope.user.password) {
                if ($event.keyCode == "13") {
                    $scope.login();
                }
            }
        }
}
LoginCtrl.$inject = [ '$scope', '$http', 'RootScope' ];