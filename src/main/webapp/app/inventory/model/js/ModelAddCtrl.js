function ModelAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {isRoot : false};
    $scope.model = angular.copy($scope.master);
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

}
ModelAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];