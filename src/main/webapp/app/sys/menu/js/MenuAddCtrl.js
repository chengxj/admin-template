function MenuAddCtrl($scope, MenuService, MessageService, LocationTo) {
    $scope.master = {};
    $scope.menu = angular.copy($scope.master);
    $scope.clickToken = false;

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
MenuAddCtrl.$inject = [ '$scope', 'MenuService', 'MessageService', 'LocationTo'];