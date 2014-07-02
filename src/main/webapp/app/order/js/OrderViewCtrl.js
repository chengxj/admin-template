function OrderViewCtrl($scope) {
    $scope.customer = {customerName : "UI Demo", mobileNo : "123456", address : "UI Demo Address"};
    $scope.mobilesafe = {"createdTime": 1403066374000, "safeNo": 762, "tariff": "A，100元/月，50元/次，5次/月"};

}
OrderViewCtrl.$inject = [ '$scope'];