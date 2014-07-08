function ModelListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.states = [{id: "1", text: "A"},{id: "2", text: "B"}
        ,{id: "3", text: "C"}]
    $scope.pagination = {"records": [
        {modelId: 1, "safeModel" : "A", "manufacturers": "csst"},
        {modelId: 1,"safeModel" : "B", "manufacturers": "csst"},
        { modelId: 1,"safeModel" : "C", "manufacturers": "csst"}
    ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 1, "pageList": [1], "nextPage": 1, "prevPage": 1}
    /*.UI Demo*/
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

    }

    //监听选择事件
    $scope.$watchCollection("selectModels", function (value) {
        if (value) {
            $scope.model = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectModels = [];
    });

}
ModelListCtrl.$inject = [ '$scope', 'MessageService'];