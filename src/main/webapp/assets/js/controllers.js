'use strict';
function LoginCtrl($scope, $http, $rootScope, StorageService) {
    // $scope.menus = $http.get("route");
    $scope.user = {username : 'root', password : '123456'};
    $scope.login = function () {
        $http.post("auth/login", $scope.user).success(
            function (data) {
                StorageService.set("accessToken", data.accessToken);
                StorageService.set("refreshToken", data.refreshToken);
                StorageService.set("secretKey", data.secretKey);
                $rootScope.accessToken = data.accessToken;
                $rootScope.secretKey = data.secretKey;
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
LoginCtrl.$inject = [ '$scope', '$http', '$rootScope', 'StorageService' ];