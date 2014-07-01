function OpenAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {};
    $scope.order = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.enterSubmitValue = function ($event) {
        if ($event.keyCode == "13") {
            $scope.submitIdCardNo();
        }
    };

    $scope.submitIdCardNo = function() {
        var idCardNo = $scope.idCardNo;
        /*UI Demo*/
        $scope.idCardNoDisabled = true;
        if (idCardNo === '123456') {
            $scope.existCustomer = true;
            $scope.customer = {customerName : "UI Demo", mobileNo : "123456", address : "UI Demo Address"}
        } else {
            $scope.newCustomer = true;
        }
        /*.UI Demo*/
    }

    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        $scope.cancel();
        /*.UI Demo*/
    };

    $scope.cancel = function() {
        $scope.existCustomer = false;
        $scope.newCustomer = false;
        $scope.customer = {}
        $scope.idCardNoDisabled = false;
        $scope.idCardNo = "";
    }

    $scope.isUnchanged = function () {
        return angular.equals($scope.order, $scope.master);
    };
    /*UI Demo*/
//    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B"}, {id: 3, text: "C"}]
    /*.UI Demo*/

    /*UI Demo*/
    $scope.queryParam = {};
    $scope.commodities = [
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "A，100元/月，50元/次，5次/月", "safeModel" : "A", "monthlyFee": "100", "escortFee": "50", "monthlyFreeEscort": "1","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 763, "commodityName": "B，200元/月，50元/次，5次/月", "safeModel" : "B", "monthlyFee": "200", "escortFee": "50", "monthlyFreeEscort": "2","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 764, "commodityName": "C，300元/月，50元/次，5次/月", "safeModel" : "C", "monthlyFee": "300", "escortFee": "50", "monthlyFreeEscort": "3","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 765, "commodityName": "A，400元/月，50元/次，5次/月", "safeModel" : "A", "monthlyFee": "400", "escortFee": "50", "monthlyFreeEscort": "4","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 766, "commodityName": "B，500元/月，50元/次，5次/月", "safeModel" : "B", "monthlyFee": "500", "escortFee": "50", "monthlyFreeEscort": "5","updatedTime": 1403929798000}
    ];
    $scope.mobilesafes = [
        {"createdTime": 1403066374000, "safeNo": 762, "tariff": "A，100元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 763, "tariff": "B，200元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 764, "tariff": "C，300元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 765, "tariff": "A，400元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 766, "tariff": "B，500元/月，50元/次，5次/月"}
    ];
    /*.UI Demo*/

}
OpenAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];