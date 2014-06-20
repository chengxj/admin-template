function MenuAddChildCtrl($scope, MenuService, MessageService, LocationTo, $routeParams) {
    MenuService.get({menuId : $routeParams.menuId}, function(data) {
        $scope.master = {parentId : data.menuId};
        $scope.menu = angular.copy($scope.master);
        $scope.clickToken = false;
    }, function() {
        $scope.disabled = true;
    });
    $scope.save = function () {
        $scope.clickToken = true;
        MenuService.save($scope.menu, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/menu/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.menu, $scope.master);
    };
}
MenuAddChildCtrl.$inject = [ '$scope', 'MenuService', 'MessageService', 'LocationTo', '$routeParams'];