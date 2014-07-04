function CommodityListCtrl($scope, MessageService) {

    /*UI Demo*/
    $scope.states = [{id: "1", text: "A"},{id: "2", text: "B"}
        ,{id: "3", text: "C"}]
    $scope.pagination = {"records": [
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "A，100元/月，50元/次，5次/月", "safeModel" : "A", "monthlyFee": "100", "escortFee": "50", "monthlyFreeEscort": "1","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "B，200元/月，50元/次，5次/月", "safeModel" : "B", "monthlyFee": "200", "escortFee": "50", "monthlyFreeEscort": "2","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "C，300元/月，50元/次，5次/月", "safeModel" : "C", "monthlyFee": "300", "escortFee": "50", "monthlyFreeEscort": "3","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "A，400元/月，50元/次，5次/月", "safeModel" : "A", "monthlyFee": "400", "escortFee": "50", "monthlyFreeEscort": "4","updatedTime": 1403929798000},
        {"createdTime": 1403066374000, "commodityId": 762, "commodityName": "B，500元/月，50元/次，5次/月", "safeModel" : "B", "monthlyFee": "500", "escortFee": "50", "monthlyFreeEscort": "5","updatedTime": 1403929798000}
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
    $scope.$watchCollection("selectCommodities", function (value) {
        if (value) {
            $scope.commodity = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectCommodities = [];
    });

}
CommodityListCtrl.$inject = [ '$scope', 'MessageService'];