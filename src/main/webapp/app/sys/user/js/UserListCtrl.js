function UserListCtrl($scope, UserService) {

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
        return UserService.remove({userId : model.userId, updatedTime:model.updatedTime}).$promise;
    }

    //监听选择事件
    $scope.$watchCollection("selectUsers", function (value) {
        if (value) {
            $scope.user = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectUsers = [];
    });

}
UserListCtrl.$inject = [ '$scope', 'UserService'];