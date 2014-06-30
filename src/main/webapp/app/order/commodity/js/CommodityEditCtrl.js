function CommodityEditCtrl($scope, MessageService, LocationTo, $routeParams) {


    $scope.getCommodity = function () {
        /*UI Demo*/

        $scope.commodity = {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "A，100元/月，50元/次，5次/月", "safeModel" : "A", "monthlyFee": "100", "escortFee": "50", "monthlyFreeEscort": "1","updatedTime": 1403929798000};
        /*.UI Demo*/
    };
    $scope.getCommodity();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        LocationTo.path("/order/commodity/list");
        /*.UI Demo*/
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.commodity, $scope.master);
    };

    $scope.reload = function () {
        $scope.getCommodity();
    };

    /*UI Demo*/
    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B", selected : true}, {id: 3, text: "C"}]
    /*.UI Demo*/
}
CommodityEditCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo', '$routeParams'];