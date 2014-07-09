function DeliveryAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {};
    $scope.order = angular.copy($scope.master);
    $scope.clickToken = false;
    $scope.selectBoxes = [];

    $scope.enterSubmitValue = function ($event) {
        if ($event.keyCode == "13") {
            $scope.submitIdCardNo();
        }
    };

    $scope.submitIdCardNo = function() {
        if ($scope.idCardNo) {
            var idCardNo = $scope.idCardNo;
            /*UI Demo*/
            $scope.idCardNoDisabled = true;
            if (idCardNo === '123456') {
                $scope.noCustomer = true;
            } else {
                $scope.existCustomer = true;
                $scope.customer = {idCardNo : $scope.idCardNo ,customerName : "UI Demo", mobileNo : "123456", address : "UI Demo Address"}
            }
            /*.UI Demo*/
        }

    }


    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        $scope.cancel();
        /*.UI Demo*/
    };

    $scope.cancel = function() {
        $scope.noCustomer = false;
        $scope.existCustomer = false;
        $scope.customer = {}
        $scope.idCardNoDisabled = false;
        $scope.idCardNo = "";
        $scope.selectBoxes = [];
    }

    $scope.isUnchanged = function () {
        return angular.equals($scope.order, $scope.master);
    };

    /*UI Demo*/
    $scope.queryParam = {};
    $scope.mobilesafes = [
        {"createdTime": 1403066374000, "safeNo": 762, "tariff": "A，100元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 763, "tariff": "B，200元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 764, "tariff": "C，300元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 765, "tariff": "A，400元/月，50元/次，5次/月"},
        {"createdTime": 1403066374000, "safeNo": 766, "tariff": "B，500元/月，50元/次，5次/月"}
    ];
    /*.UI Demo*/

}
DeliveryAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];