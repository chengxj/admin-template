function CustomerListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.states = [{id: "1", text: "服务中"},{id: "2", text: "停止服务"}]
    $scope.pagination = {"records": [
        {"idCardNo": 1403066374000, "customerId": 762, "customerName": "张雨舟", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", state : "服务中"},
        {"idCardNo": 1403066374000, "customerId": 762, "customerName": "潘龙宝", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", state : "停止服务"}
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
    $scope.$watchCollection("selectCustomers", function (value) {
        if (value) {
            $scope.customer = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectCustomers = [];
    });

}
CustomerListCtrl.$inject = [ '$scope', 'MessageService'];