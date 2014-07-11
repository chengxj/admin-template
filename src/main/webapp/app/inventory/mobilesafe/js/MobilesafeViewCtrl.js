function MobilesafeViewCtrl($scope, MessageService) {

    $scope.getMobilesafe = function () {
        /*UI Demo*/
        $scope.startDate = moment().startOf('day');
        $scope.endDate = moment();
        $scope.mobilesafe = {mobilesafeId: 1, "safeModel" : "A", safeNo : "NO.001", customerName: "张雨舟", idCardNo:123433556565, state : "保管"};
        $scope.master = angular.copy($scope.mobilesafe);
        $scope.pagination = {"records": [
            {"trackingTime": "20140701000000", "safeNo": 762, "desc": "出库"},
            {"trackingTime": "20140701000000", "safeNo": 763, "desc": "押运"},
            {"trackingTime": "20140701000000", "safeNo": 764, "desc": "客户签收"}
        ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 1, "pageList": [1], "nextPage": 1, "prevPage": 1}
        /*.UI Demo*/
    };
    $scope.getMobilesafe();
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
        $scope.getMobilesafe();
    };

    /*UI Demo*/
    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B", selected : true}, {id: 3, text: "C"}]
    /*.UI Demo*/

}
MobilesafeViewCtrl.$inject = [ '$scope', 'MessageService'];