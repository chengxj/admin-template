function ResourceListCtrl($scope, ResourceService) {
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

    //监听选择事件
    $scope.$watchCollection("selectResources", function (value) {
        if (value) {
            $scope.resource = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectResources = [];
    });

}
ResourceListCtrl.$inject = [ '$scope', 'ResourceService'];