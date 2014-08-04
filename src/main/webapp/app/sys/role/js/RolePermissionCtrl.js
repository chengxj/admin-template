function RolePermissionCtrl($scope, PermissionService, RoleService,MessageService, LocationTo, $routeParams, $timeout) {

    $scope.selectResources = [];
    $scope.clickToken = false;
    RoleService.get({roleId : $routeParams.roleId}, function(data) {
        PermissionService.getMenus({roleId : $routeParams.roleId}, function(data) {
            var menus = [];
            var copyData = angular.copy(data);
            var stripeIndex = 0;
            _.each(data, function(v, i) {
                if (v.parentId == -1) {
                    v.children = [];
                    _.each(copyData, function(c, j) {
                        if (c.parentId == v.menuId) {
                            v.children.push(c);
                        }
                    });
                    menus.push(v);
                }
            });
            $scope.menus = menus;
        });

    }, function() {
        $scope.disabled = true;
    });

    $scope.saveResourcePermission = function () {
        var resourceIds = _.pluck($scope.selectResources, "resourceId");
        PermissionService.saveResourcePermission({roleId: $routeParams.roleId, resourceIds: resourceIds}, function () {
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
RolePermissionCtrl.$inject = [ '$scope', 'PermissionService','RoleService', 'MessageService', 'LocationTo', '$routeParams', '$timeout'];