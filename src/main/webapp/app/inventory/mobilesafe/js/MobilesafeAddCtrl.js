function MobilesafeAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {isRoot : false};
    $scope.mobilesafe = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        /*UI Demo*/
        MessageService.saveSuccess();
        LocationTo.path("/inventory/mobilesafe/list");
        /*.UI Demo*/
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.mobilesafe, $scope.master);
    };
    /*UI Demo*/
    $scope.options = [{id: 1, text: "A"},{id: 2, text: "B"}, {id: 3, text: "C"}]
    /*.UI Demo*/
}
MobilesafeAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];