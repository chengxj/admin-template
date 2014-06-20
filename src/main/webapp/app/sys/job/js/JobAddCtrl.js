function JobAddCtrl($scope, JobService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.job = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        JobService.save($scope.job, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/job/list");
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.job, $scope.master);
    };
}
JobAddCtrl.$inject = [ '$scope', 'JobService', 'MessageService', 'LocationTo'];