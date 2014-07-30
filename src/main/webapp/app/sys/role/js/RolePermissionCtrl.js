function RolePermissionCtrl($scope, PermissionService, RoleService,MessageService, LocationTo, $routeParams, $q) {

    $scope.selectResources = [];
    $scope.clickToken = false;



    RoleService.get({roleId : $routeParams.roleId}, function(data) {
        $scope.menus = PermissionService.getMenus({roleId : $routeParams.roleId});
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
}
RolePermissionCtrl.$inject = [ '$scope', 'PermissionService','RoleService', 'MessageService', 'LocationTo', '$routeParams', '$q'];