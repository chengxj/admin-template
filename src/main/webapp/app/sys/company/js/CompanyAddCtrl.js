function CompanyAddCtrl($scope, CompanyService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.company = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        CompanyService.save($scope.company, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/company/list");
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.company, $scope.master);
    };
}
CompanyAddCtrl.$inject = [ '$scope', 'CompanyService', 'MessageService', 'LocationTo'];