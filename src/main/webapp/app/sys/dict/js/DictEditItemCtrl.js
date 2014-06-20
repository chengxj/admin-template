function DictEditItemCtrl($scope, DictService, MessageService, LocationTo, $routeParams) {
    $scope.getDict = function () {
        $scope.dict = DictService.get({dictCode: $routeParams.dictCode}, function (data) {
            $scope.master = angular.copy($scope.dict);
        }, function () {
            $scope.disabled = true;
        });
    };
    $scope.getDict();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        DictService.update({dictCode: $scope.dict.dictCode}, $scope.dict, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/dict/items/" + $scope.dict.parentCode);
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.dict, $scope.master);
    };

    $scope.reload = function () {
        $scope.getDict();
    };
}
DictEditItemCtrl.$inject = [ '$scope', 'DictService', 'MessageService', 'LocationTo', '$routeParams'];