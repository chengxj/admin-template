function MenuEditCtrl($scope, MenuService, MessageService, LocationTo, $routeParams) {
    $scope.getMenu = function () {
        $scope.menu = MenuService.get({menuId: $routeParams.menuId}, function (data) {
            $scope.master = angular.copy($scope.menu);
        }, function() {
            $scope.disabled = true;
        });
    };
    $scope.getMenu();

    $scope.clickToken = false;
    $scope.save = function () {
        $scope.clickToken = true;
        MenuService.update({menuId: $scope.menu.menuId}, $scope.menu, function () {
            MessageService.saveSuccess();
            LocationTo.path("/sys/menu/list");
        }, function() {
            $scope.clickToken = false;
        });
    };

    $scope.isUnchanged = function () {
        return angular.equals($scope.menu, $scope.master);
    };

    $scope.reload = function () {
        $scope.getMenu();
    };
}
MenuEditCtrl.$inject = [ '$scope', 'MenuService', 'MessageService', 'LocationTo', '$routeParams'];