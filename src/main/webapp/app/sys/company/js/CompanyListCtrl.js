function CompanyListCtrl($scope, CompanyService, $rootScope) {
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
        return CompanyService.remove({companyId: model.companyId}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectCompanies", function (value) {
        if (value) {
            $scope.job = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectCompanies = [];
    });

}
CompanyListCtrl.$inject = [ '$scope', 'CompanyService', '$rootScope'];