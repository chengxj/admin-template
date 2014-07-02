function OrderListCtrl($scope, ShareService) {
    /*UI Demo*/
    $scope.pagination = {"records": ShareService.orders, "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 2, "pageList": [1,2], "nextPage": 2, "prevPage": 1}
    /*.UI Demo*/
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
    $scope.$watchCollection("selectOrders", function (value) {
        if (value) {
            $scope.order = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectOrders = [];
    });

}
OrderListCtrl.$inject = [ '$scope', 'ShareService'];