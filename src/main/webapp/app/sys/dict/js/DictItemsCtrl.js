function DictItemsCtrl($scope, DictService, MessageService, $routeParams) {
    $scope.dictCode = $routeParams.dictCode;
    $scope.queryParam = {sort: "sorted", fields: "dictCode,dictName,updatedTime", parentCode: $routeParams.dictCode};
    $scope.query = function () {
        $scope.dict = DictService.get({dictCode: $routeParams.dictCode}, function (data) {
            $scope.dicts = DictService.query($scope.queryParam);
        }, function () {
            $scope.disabled = true;
        });
    };

    $scope.query();
    $scope.reload = function () {
        $scope.query();
        $scope.$broadcast("unCheckedAll");
    };


    //删除
    $scope.remove = function (model) {
        return DictService.remove({dictCode: model.dictCode, updatedTime: model.updatedTime}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectDicts", function (value) {
        if (value) {
            $scope.dict = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectDicts = [];
    });

}
DictItemsCtrl.$inject = [ '$scope', 'DictService', 'MessageService', '$routeParams'];