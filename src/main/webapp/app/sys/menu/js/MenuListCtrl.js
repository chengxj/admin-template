function MenuListCtrl($scope, MenuService, $timeout) {
    $scope.query = function() {
        MenuService.query(function(data) {
            var menus = [];
            var copyData = angular.copy(data);
            var stripeIndex = 0;
            _.each(data, function(v, i) {
                if (v.parentId == -1) {
                    stripeIndex ++;
                    var clazz = "";
                    if (stripeIndex % 2 == 0) {
                        clazz = "active"
                    }
                    var parentIndex = menus.length;
                    v.class = clazz + " treegrid-" + parentIndex;
                    v.style = 'margin-left : 10px';
                    menus.push(v);
                    _.each(copyData, function(c, j) {
                        if (c.parentId == v.menuId) {
                            c.class =  clazz + " treegrid-" + menus.length + " treegrid-parent-" + parentIndex;
                            c.style = 'margin-left : 25px';
                            menus.push(c);
                        }
                    });
                }
            });
            $scope.menus = menus;
        });
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
