function MobilesafeListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.states = [{id: "1", text: "空闲"},{id: "1", text: "保管"},{id: "2", text: "出库"}
        ,{id: "3", text: "出库押运"},{id: "3", text: "客户签收"},{id: "3", text: "入库押运"},{id: "3", text: "入库"}]
    $scope.pagination = {"records": [
        {mobilesafeId: 1, "safeModel" : "A", safeNo : "NO.001", customerName: "张雨舟", idCardNo:123433556565, state : "保管"},
        {mobilesafeId: 1,"safeModel" : "B", safeNo : "NO.002", customerName: "张雨舟", idCardNo:123433556565, state : "客户签收"},
        { mobilesafeId: 1,"safeModel" : "C", safeNo : "NO.003", state : "空闲"}
    ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 1, "pageList": [1], "nextPage": 1, "prevPage": 1}
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

    //删除
    $scope.remove = function (model) {

    }

    //监听选择事件
    $scope.$watchCollection("selectMobilesafes", function (value) {
        if (value) {
            $scope.mobilesafe = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectMobilesafes = [];
    });

}
MobilesafeListCtrl.$inject = [ '$scope', 'MessageService'];