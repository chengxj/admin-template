function RolePermissionCtrl($scope,MenuService, PermissionService, RoleService,MessageService, LocationTo, $routeParams, $timeout, $q) {

    $scope.selectPermissions = [];
    $scope.clickToken = false;
    RoleService.get({roleId : $routeParams.roleId}, function(data) {
        $scope.menus = PermissionService.getMenus({roleId : $routeParams.roleId});

    }, function() {
        $scope.disabled = true;
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