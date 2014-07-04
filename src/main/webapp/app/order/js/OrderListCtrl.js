function OrderListCtrl($scope, ShareService) {
    /*UI Demo*/
    $scope.startDate = moment().startOf('day');
    $scope.endDate = moment();
    $scope.states = [{id: "1", text: "提交订单"},{id: "2", text: "出库"}
        ,{id: "3", text: "出库押运"},{id: "4", text: "客户签收"}
        ,{id: "5", text: "入库押运"},{id: "6", text: "入库"}
        ,{id: "7", text: "完成"}]
    $scope.types = [{id: "1", text: "开户订单"},{id: "2", text: "退订订单"}
        ,{id: "3", text: "押运订单"}]
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