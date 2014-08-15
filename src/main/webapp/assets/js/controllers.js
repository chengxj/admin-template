'use strict';
function LoginCtrl($scope, $http, $rootScope) {
    // $scope.menus = $http.get("route");
    $scope.user = {username : 1, password : 2};
    $scope.login = function () {
        $http.post("auth/login", $scope.user).success(
            function (data) {
                $rootScope.accessToken = data.accessToken;
                $rootScope.refreshToken = data.refreshToken;
                $rootScope.sessionSecret = data.sessionSecret;
                $scope.$emit("login");
            }).error(function (data) {
            $("#success-msg").hide();
            $("#error-msg").html(data.responseJSON.message).show();
        });
    }
        $scope.enterLogin = function ($event) {
            if ($event.keyCode == "13") {
                console.log("login");
            }
        }
}
LoginCtrl.$inject = [ '$scope', '$http', "$rootScope" ];