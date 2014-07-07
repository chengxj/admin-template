function ChargeListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.pagination = {"records": [
        {"idCardNo": 1403066374000, "chargeId": 762, "chargeName": "张雨舟", "mobileNo" : "13457632334", state : "服务中","safeNo" : "A", "monthlyFee": "100", "escortFee": "50", "monthlyFreeEscort": "1"},
        {"idCardNo": 1403066374000, "chargeId": 762, "chargeName": "潘龙宝", "mobileNo" : "13457632334", state : "停止服务","safeNo" : "B", "monthlyFee": "200", "escortFee": "50", "monthlyFreeEscort": "1"}
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

    //监听选择事件
    $scope.$watchCollection("selectCharges", function (value) {
        if (value) {
            $scope.charge = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectCharges = [];
    });

}
ChargeListCtrl.$inject = [ '$scope', 'MessageService'];