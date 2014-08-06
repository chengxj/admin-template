function RolePermissionCtrl($scope,MenuService, PermissionService, RoleService,MessageService, LocationTo, $routeParams, $timeout, $q) {

    $scope.selectPermissions = [];
    $scope.clickToken = false;
//    RoleService.get({roleId : $routeParams.roleId}, function(data) {
//        PermissionService.getMenus({roleId : $routeParams.roleId}, function(data) {
//            var menus = [];
//            var copyData = angular.copy(data);
//            var stripeIndex = 0;
//            _.each(data, function(v, i) {
//                if (v.parentId == -1) {
//                    v.children = [];
//                    _.each(copyData, function(c, j) {
//                        if (c.parentId == v.menuId) {
//                            v.children.push(c);
//                        }
//                    });
//                    menus.push(v);
//                }
//            });
//            $scope.menus = menus;
//        });
//
//    }, function() {
//        $scope.disabled = true;
//    });

    $q.all([MenuService.query().$promise, PermissionService.getMenus({roleId : $routeParams.roleId}).$promise]).then(function(value) {
        var data = value[0];
        var copyData = angular.copy(data);
        var stripeIndex = 0;
        var menus = _.filter(data, function(v, i) {
            v.checked = _.some(value[1], function(m) {
                return m.menuId == v.menuId;
            });
            return v;
        });
        $scope.menus = menus;
    });

    $scope.savePermission = function () {
        var permissionIds = _.pluck($scope.selectPermissions, "menuId");
        PermissionService.save({roleId: $routeParams.roleId, permissionIds: permissionIds}, function () {
            MessageService.saveSuccess();
        }, function () {
            $scope.clickToken = false;
        });
    };

    $scope.selectAll = function() {
        $timeout(function() {
            $("[name=resource]").iCheck('check');
        })
    }
    $scope.unSelectAll = function() {
        $timeout(function() {
            $("[name=resource]").iCheck('uncheck');
        })
    }
}
RolePermissionCtrl.$inject = [ '$scope', 'MenuService', 'PermissionService','RoleService', 'MessageService', 'LocationTo', '$routeParams', '$timeout', '$q'];