function RouteListCtrl($scope, RouteService) {
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
        return RouteService.remove({routeId : model.routeId, updatedTime:model.updatedTime}).$promise;
    }
    //监听选择事件
    $scope.$watchCollection("selectRoutes", function (value) {
        if (value) {
            $scope.route = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectRoutes = [];
    });

}
RouteListCtrl.$inject = [ '$scope', 'RouteService'];