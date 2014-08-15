'use strict';
function LoginCtrl($scope, $http) {
    // $scope.menus = $http.get("route");
    $scope.user = {username : 1, password : 2};
    $scope.login = function () {
        $http.post("auth/login", $scope.user).success(
            function (data) {
                var storage = $.localStorage;
                storage.set("accessToken", data.accessToken);
                storage.set("refreshToken", data.refreshToken);
                storage.set("secretKey", data.secretKey);
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
LoginCtrl.$inject = [ '$scope', '$http' ];