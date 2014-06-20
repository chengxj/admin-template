function JobListCtrl($scope, JobService, $rootScope) {
    $scope.queryParam = {};
    $scope.enterQueryValue = function ($event) {
        if ($event.keyCode == "13") {
            $scope.query();
        }
    };
    $scope.query = function () {
        $scope.$broadcast("query");
        $scope.$broadcast("unCheckedAll");
    };
    $scope.reload = function () {
        $scope.$broadcast("reload");
        $scope.$broadcast("unCheckedAll");
    };

    //删除
    $scope.remove = function (model) {
        return JobService.remove({jobId: model.jobId, updatedTime: model.updatedTime}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectJobs", function (value) {
        if (value) {
            $scope.job = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectJobs = [];
    });

}
JobListCtrl.$inject = [ '$scope', 'JobService', '$rootScope'];