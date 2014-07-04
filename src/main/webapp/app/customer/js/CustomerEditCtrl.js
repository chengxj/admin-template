function CustomerEditCtrl($scope, MessageService, LocationTo, $routeParams) {


    $scope.getCustomer = function () {
        /*UI Demo*/

        $scope.customer = {"idCardNo": 1403066374000, "customerId": 762, "customerName": "张雨舟", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", state : "服务中"};
        /*.UI Demo*/
    };
    $scope.getCustomer();

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
CustomerEditCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo', '$routeParams'];