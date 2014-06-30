function I18nAddCtrl($scope, I18nService, MessageService, LocationTo) {
    $scope.master = {isRoot : false};
    $scope.i18n = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        I18nService.save($scope.i18n, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/i18n/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.i18n, $scope.master);
    };
}
I18nAddCtrl.$inject = [ '$scope', 'I18nService', 'MessageService', 'LocationTo'];