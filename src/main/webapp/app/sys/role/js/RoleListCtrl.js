function RoleListCtrl($scope, RoleService) {
    $scope.queryParam = {};
    $scope.enterQueryValue = function ($event) {
        if ($event.keyCode == "13") {
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
        return RoleService.remove({roleId : model.roleId, updatedTime:model.updatedTime}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectRoles", function (value) {
        if (value) {
            $scope.role = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectRoles = [];
    });

}
RoleListCtrl.$inject = [ '$scope', 'RoleService'];