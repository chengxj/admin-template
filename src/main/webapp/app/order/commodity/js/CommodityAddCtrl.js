function CommodityAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {isRoot : false};
    $scope.commodity = angular.copy($scope.master);
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
    /*UI Demo*/
    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B"}, {id: 3, text: "C"}]
    /*.UI Demo*/

}
CommodityAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];