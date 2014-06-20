function I18nEditCtrl($scope, I18nService, MessageService, LocationTo, $routeParams) {
    $scope.getI18n = function () {
        $scope.i18n = I18nService.get({i18nId: $routeParams.i18nId}, function (data) {
            $scope.master = angular.copy($scope.i18n);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getI18n();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        I18nService.update({i18nId: $scope.i18n.i18nId}, $scope.i18n, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/i18n/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.i18n, $scope.master);
    };

    $scope.reload = function () {
        $scope.getI18n();
    };
}
I18nEditCtrl.$inject = [ '$scope', 'I18nService', 'MessageService', 'LocationTo', '$routeParams'];