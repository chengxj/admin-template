function I18nListCtrl($scope, I18nService, MessageService) {
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
        return I18nService.remove({i18nId : model.i18nId, updatedTime:model.updatedTime}).$promise;
    }

    $scope.saveToJson = function() {
        I18nService.saveToJson(function() {
            MessageService.saveSuccess();
        });
    }

    //监听选择事件
    $scope.$watchCollection("selectI18ns", function (value) {
        if (value) {
            $scope.i18n = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectI18ns = [];
    });

}
I18nListCtrl.$inject = [ '$scope', 'I18nService', 'MessageService'];