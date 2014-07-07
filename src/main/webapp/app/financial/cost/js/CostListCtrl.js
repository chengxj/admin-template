function CostListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.startDate = moment().startOf('day');
    $scope.endDate = moment();
    $scope.types = [{id: "1", text: "月服务"},{id: "2", text: "押运费"}]
    $scope.states = [{id: "1", text: "已划扣"},{id: "2", text: "未划扣"}]
    $scope.pagination = {"records": [
        {"idCardNo": 1403066374000, "costId": 762, "customerName": "张雨舟", "cost" : 100.00, type : "月服务","safeNo" : "A", "costTime": "20140701000000", state : "已划扣"},
        {"idCardNo": 1403066374000, "costId": 762, "customerName": "潘龙宝", "cost" : 50.00, type : "押运费","safeNo" : "B", "costTime": "20140705112000", state : "未划扣"}
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
    $scope.$watchCollection("selectCosts", function (value) {
        if (value) {
            $scope.cost = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectCosts = [];
    });

}
CostListCtrl.$inject = [ '$scope', 'MessageService'];