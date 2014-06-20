function DictListCtrl($scope, DictService, $rootScope) {
    $scope.queryParam = {sort: "dictCode", fields: "dictCode,dictName,updatedTime", parentCode: "-1"};
    $scope.enterQueryValue = function ($event) {
        if ($event.keyCode == "13") {
//            $broadcast -- dispatches the event downwards to all child scopes,
//            $emit -- dispatches the event upwards through the scope hierarchy.
            $scope.query();
        }
    };
    $scope.query = function () {
        $scope.$broadcast("query");
        $scope.$broadcast("unCheckedAll");
    };
    $scope.reload = function () {
        $scope.$broadcast("reload");
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
DictListCtrl.$inject = [ '$scope', 'DictService', '$rootScope'];