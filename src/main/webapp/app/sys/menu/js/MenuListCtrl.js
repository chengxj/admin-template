function MenuListCtrl($scope, MenuService, $timeout) {
    $scope.query = function() {
        $scope.menus = MenuService.query();
    }

    $scope.query();

    $scope.reload = function () {
        $scope.query();
    };

    //删除
    $scope.remove = function (model) {
        return MenuService.remove({menuId: model.menuId, updatedTime: model.updatedTime}).$promise;
    }
    //监听选择事件
    $scope.$watchCollection("selectMenus", function (value) {
        if (value) {
            $scope.menu = value[value.length - 1];
        }
    });
    $scope.$watchCollection("menus", function (value) {
        $scope.selectMenus = [];
        $scope.$broadcast("unCheckedAll");
    });
}
MenuListCtrl.$inject = [ '$scope', 'MenuService', '$timeout'];
