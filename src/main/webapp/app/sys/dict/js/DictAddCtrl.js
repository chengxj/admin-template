function DictAddCtrl($scope, DictService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.dict = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        DictService.save($scope.dict, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/dict/list");
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.dict, $scope.master);
    };
}
DictAddCtrl.$inject = [ '$scope', 'DictService', 'MessageService', 'LocationTo'];