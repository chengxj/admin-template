function JobEditCtrl($scope, JobService, MessageService, LocationTo, $routeParams) {
    $scope.getJob = function () {
        $scope.job = JobService.get({jobId: $routeParams.jobId}, function (data) {
            $scope.master = angular.copy($scope.job);
        }, function () {
            $scope.disabled = true;
        });
    };
    $scope.getJob();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        JobService.update({jobId: $scope.job.jobId}, $scope.job, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/job/list");
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.job, $scope.master);
    };

    $scope.reload = function () {
        $scope.getJob();
    };
}
JobEditCtrl.$inject = [ '$scope', 'JobService', 'MessageService', 'LocationTo', '$routeParams'];