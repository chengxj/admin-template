function DictAddItemCtrl($scope, DictService, MessageService, LocationTo, $routeParams) {
    $scope.dict = DictService.get({dictCode: $routeParams.dictCode}, function (data) {
        $scope.master = {parentCode: $routeParams.dictCode, dictCode: $routeParams.dictCode};
        $scope.dict = angular.copy($scope.master);
    }, function () {
        $scope.disabled = true;
    });

    $scope.clickToken = false;

    $scope.save = function () {
        $scope.clickToken = true;
        DictService.save($scope.dict, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/dict/items/" + $scope.dict.parentCode);
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.dict, $scope.master);
    };
}
DictAddItemCtrl.$inject = [ '$scope', 'DictService', 'MessageService', 'LocationTo', '$routeParams'];