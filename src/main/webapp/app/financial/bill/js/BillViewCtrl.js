function BillViewCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.customer = {customerName : "UI Demo", mobileNo : "123456", address : "UI Demo Address", arrears : 150.00};
    $scope.pagination = {"records": [
        {"idCardNo": 1403066374000, "costId": 762, "customerName": "张雨舟", "cost" : 100.00, type : "月服务","safeNo" : "A", "costTime": "20140701000000", state : "已划扣"},
        {"idCardNo": 1403066374000, "costId": 762, "customerName": "潘龙宝", "cost" : 50.00, type : "押运费","safeNo" : "B", "costTime": "20140705112000", state : "未划扣"}
    ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 1, "pageList": [1], "nextPage": 1, "prevPage": 1}
    $scope.mobilesafes = [
        {"createdTime": 1403066374000, "safeNo": 762, "tariff": "A，100元/月，50元/次，5次/月", "purchaseTime" : "20140606112000"},
        {"createdTime": 1403066374000, "safeNo": 763, "tariff": "B，100元/月，50元/次，5次/月", "purchaseTime" : "20140606112000"}
    ]
    /*.UI Demo*/

}
BillViewCtrl.$inject = [ '$scope', 'MessageService'];