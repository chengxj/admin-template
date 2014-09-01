'use strict';
function LoginCtrl($scope, $http, RootScope) {
    $scope.user = {};
    $scope.login = function () {
        $http.post("https://10.4.7.15:8443/auth/login", $scope.user).success(
            function (data) {
                $.localStorage.set("accessToken", data.accessToken);
                $.localStorage.set("refreshToken", data.refreshToken);
                $.localStorage.set("secretKey", data.secretKey);
                RootScope.set("accessToken", data.accessToken);
                RootScope.set("secretKey", data.secretKey);
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