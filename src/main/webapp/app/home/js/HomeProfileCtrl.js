function HomeProfileCtrl($scope, UserService, MessageService, $routeParams, $translate) {
    $scope.getProfile = function () {
        $scope.profile = UserService.getProfile({userId: $routeParams.userId}, function (data) {
            $scope.master = angular.copy($scope.profile);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getProfile();

    $scope.clickToken = false;
    $scope.passwordToken = false;
    $scope.saveProfile = function () {
        $scope.clickToken = true;
        UserService.updateProfile({userId: $routeParams.userId}, $scope.profile, function () {
            $scope.clickToken = false;
            moment.lang($scope.profile.language.toLowerCase());
            $translate.use($scope.profile.language);
            MessageService.saveSuccess();
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.savePassword = function () {
        UserService.updatePassword({userId: $routeParams.userId}, $scope.password, function () {
//            $scope.passwordToken = true;
            MessageService.saveSuccess();
        }, function() {
            $scope.passwordToken = false;
        });
    };

}
HomeProfileCtrl.$inject = [ '$scope', 'UserService', 'MessageService', '$routeParams', '$translate'];