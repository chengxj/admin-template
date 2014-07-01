function OrderListCtrl($scope) {
    /*UI Demo*/
    $scope.pagination = {"records": [
        {"createdTime": 1403066374000, "orderNo": 761, "idCardNo": "123456789012345678","customerName": "张雨舟", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "新建","currentProcess": "准备出库"},
        {"createdTime": 1403066374000, "orderNo": 762, "idCardNo": "223456789012345678","customerName": "王志维", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "出库","currentProcess": "出库"},
        {"createdTime": 1403066374000, "orderNo": 763, "idCardNo": "323456789012345678","customerName": "严文林", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "出库","currentProcess": "等待押运"},
        {"createdTime": 1403066374000, "orderNo": 764, "idCardNo": "423456789012345678","customerName": "彭镭", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "出库押运","currentProcess": "押运中"},
        {"createdTime": 1403066374000, "orderNo": 765, "idCardNo": "523456789012345678","customerName": "王世虎", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "出库押运","currentProcess": "客户签收"},
        {"createdTime": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "入库押运","currentProcess": "等待押运"},
        {"createdTime": 1403066374000, "orderNo": 767, "idCardNo": "723456789012345678","customerName": "吴颖峰", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "入库押运","currentProcess": "押运中"},
        {"createdTime": 1403066374000, "orderNo": 768, "idCardNo": "823456789012345678","customerName": "谭军胜", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "入库","currentProcess": "入库"},
        {"createdTime": 1403066374000, "orderNo": 769, "idCardNo": "923456789012345678","customerName": "陈元元", "mobileNo" : "18627874615", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "完结","currentProcess": "完结"}
    ], "page": 1, "pageSize": 10, "totalRecords": 5, "totalPages": 2, "pageList": [1,2], "nextPage": 2, "prevPage": 1}
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
    $scope.$watchCollection("selectOrders", function (value) {
        if (value) {
            $scope.role = value[value.length - 1];
        }
    });
    $scope.$watch("pagination", function (value) {
        $scope.selectOrders = [];
    });

}
OrderListCtrl.$inject = [ '$scope'];