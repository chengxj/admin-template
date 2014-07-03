function TaskListCtrl($scope, ShareService) {

    /*UI Demo*/
    $scope.startDate = moment().format('YYYY-MM-DD');
    $scope.endDate = moment().format('YYYY-MM-DD');
    $scope.states = [{id: "1", text: "未领取"},{id: "2", text: "进行中"}
        ,{id: "3", text: "完成"}]
    $scope.types = [{id: "1", text: "提交订单"},{id: "2", text: "分配任务"}
        ,{id: "3", text: "出库"},{id: "4", text: "领取保管箱"}
        ,{id: "5", text: "出库押运"},{id: "6", text: "客户签收"}
        ,{id: "7", text: "使用完毕"},{id: "8", text: "交还给押运员"}
        ,{id: "9", text: "押运员签收"},{id: "10", text: "交还给押运员"}
        ,{id: "11", text: "入库押运"},{id: "12", text: "仓管员签收"}
        ,{id: "13", text: "入库"},{id: "14", text: "完结"}]
    $scope.pagination = {"records": ShareService.tasks, "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 2, "pageList": [1,2], "nextPage": 2, "prevPage": 1}
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

    //监听选择事件
    $scope.$watchCollection("selectTasks", function (value) {
        if (value) {
            $scope.task = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectTasks = [];
    });

}
TaskListCtrl.$inject = [ '$scope', 'ShareService'];