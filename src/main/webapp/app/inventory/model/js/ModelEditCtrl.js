function ModelEditCtrl($scope, MessageService, LocationTo, $routeParams) {


    $scope.getModel = function () {
        /*UI Demo*/

        $scope.model = {modelId: 1, "safeModel" : "A", "manufacturers": "csst"};
        /*.UI Demo*/
    };
    $scope.getModel();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        LocationTo.path("/inventory/model/list");
        /*.UI Demo*/
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.model, $scope.master);
    };

    $scope.reload = function () {
        $scope.getModel();
    };
}
ModelEditCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo', '$routeParams'];