function CustomerViewCtrl($scope, MessageService, LocationTo, $routeParams) {


    $scope.getCustomer = function () {
        /*UI Demo*/
        $scope.startDate = moment().startOf('day');
        $scope.endDate = moment();
        $scope.types = [{id: "1", text: "月服务"},{id: "2", text: "押运费"}]
        $scope.states = [{id: "1", text: "已划扣"},{id: "2", text: "未划扣"}]
        $scope.customer = {"idCardNo": 1403066374000, "customerId": 762,email : "zyz@higheasyrd.com", "customerName": "张雨舟", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", state : "服务中"};
        $scope.master = angular.copy($scope.customer);
        $scope.mobilesafes = [
            {"createdTime": 1403066374000, "safeNo": 762, "tariff": "A，100元/月，50元/次，5次/月"},
            {"createdTime": 1403066374000, "safeNo": 763, "tariff": "B，200元/月，50元/次，5次/月"},
            {"createdTime": 1403066374000, "safeNo": 764, "tariff": "C，300元/月，50元/次，5次/月"},
            {"createdTime": 1403066374000, "safeNo": 765, "tariff": "A，400元/月，50元/次，5次/月"},
            {"createdTime": 1403066374000, "safeNo": 766, "tariff": "B，500元/月，50元/次，5次/月"}
        ];
        $scope.pagination = {"records": [
            {"idCardNo": 1403066374000, "costId": 762, "customerName": "张雨舟", "cost" : 100.00, type : "月服务","safeNo" : "A", "costTime": "20140701000000", state : "已划扣"},
            {"idCardNo": 1403066374000, "costId": 762, "customerName": "潘龙宝", "cost" : 50.00, type : "押运费","safeNo" : "B", "costTime": "20140705112000", state : "未划扣"}
        ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 1, "pageList": [1], "nextPage": 1, "prevPage": 1}
        /*.UI Demo*/
    };
    $scope.getCustomer();
    $scope.queryParam = {};
    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        LocationTo.path("/customer/list");
        /*.UI Demo*/
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.customer, $scope.master);
    };

    $scope.reload = function () {
        $scope.getCustomer();
    };

    /*UI Demo*/
    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B", selected : true}, {id: 3, text: "C"}]
    /*.UI Demo*/
}
CustomerViewCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo', '$routeParams'];