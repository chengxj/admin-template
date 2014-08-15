'use strict';
function LoginCtrl($scope, $http, localStorageService) {
    // $scope.menus = $http.get("route");
    $scope.user = {username : 1, password : 2};
    $scope.login = function () {
        $http.post("auth/login", $scope.user).success(
            function (data) {
                localStorageService.accessToken = data.accessToken;
                localStorageService.refreshToken = data.refreshToken;
                localStorageService.secretKey = data.secretKey;
                accessToken = data.accessToken;
                secretKey = data.secretKey;
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
LoginCtrl.$inject = [ '$scope', '$http', "localStorageService" ];