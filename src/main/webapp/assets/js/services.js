'use strict';

var commonService = angular.module('app.services', [ 'ngResource' ]);

commonService.factory('ShareService', function () {
	  return {
          orders : [
              {"orderId": 1403066374000, "orderNo": 761, "idCardNo": "123456789012345678","customerName": "张雨舟", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.001", "state": "提交订单","stateCode" : "1","currentProcess": "提交订单", "nextProcessCode" : "2", type : "开户"},
              {"orderId": 1403066374000, "orderNo": 762, "idCardNo": "223456789012345678","customerName": "王志维", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.002", "state": "出库","stateCode" : "2", "currentProcess": "分配任务", "nextProcessCode" : "3", type : "开户"},
              {"orderId": 1403066374000, "orderNo": 763, "idCardNo": "323456789012345678","customerName": "严文林", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.003", "state": "出库","stateCode" : "2", "currentProcess": "出库", "nextProcessCode" : "4", type : "开户"},
              {"orderId": 1403066374000, "orderNo": 764, "idCardNo": "423456789012345678","customerName": "彭镭", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.004", "state": "出库押运","stateCode" : "3", "currentProcess": "领取保管箱", "nextProcessCode" : "5", type : "退订"},
              {"orderId": 1403066374000, "orderNo": 764, "idCardNo": "423456789012345678","customerName": "彭镭", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.004", "state": "出库押运","stateCode" : "3", "currentProcess": "出库押运", "nextProcessCode" : "6", type : "退订"},
              {"orderId": 1403066374000, "orderNo": 765, "idCardNo": "523456789012345678","customerName": "王世虎", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.005", "state": "客户签收","stateCode" : "4", "currentProcess": "客户签收", "nextProcessCode" : "7", type : "退订"},
              {"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.006", "state": "客户签收","stateCode" : "4", "currentProcess": "使用完毕", "nextProcessCode" : "8", type : "退订"},
              {"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.006", "state": "客户签收","stateCode" : "4", "currentProcess": "交还给押运员", "nextProcessCode" : "9", type : "押运"},
              {"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.006", "state": "客户签收","stateCode" : "4", "currentProcess": "押运员签收", "nextProcessCode" : "10", type : "押运"},
              {"orderId": 1403066374000, "orderNo": 767, "idCardNo": "723456789012345678","customerName": "吴颖峰", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.007", "state": "入库押运","stateCode" : "5", "currentProcess": "入库押运", "nextProcessCode" : "11", type : "押运"},
              {"orderId": 1403066374000, "orderNo": 767, "idCardNo": "723456789012345678","customerName": "吴颖峰", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.007", "state": "入库押运","stateCode" : "5", "currentProcess": "仓管员签收", "nextProcessCode" : "12", type : "押运"},
              {"orderId": 1403066374000, "orderNo": 768, "idCardNo": "823456789012345678","customerName": "谭军胜", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.008", "state": "入库","stateCode" : "6", "currentProcess": "入库", "nextProcessCode" : "13", type : "押运"},
              {"orderId": 1403066374000, "orderNo": 769, "idCardNo": "923456789012345678","customerName": "陈元元", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号光谷软件园企业公馆A2栋", "safeNo": "No.009", "state": "完成","stateCode" : "7", "currentProcess": "完结", "nextProcessCode" : "14", type : "押运"}
          ],
          tasks : [
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 761, "idCardNo": "123456789012345678","customerName": "张雨舟", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.001","type": "提交订单", state : "未领取"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 762, "idCardNo": "223456789012345678","customerName": "王志维", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.002", "type": "分配任务", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 763, "idCardNo": "323456789012345678","customerName": "严文林", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.003", "type": "出库", state : "完成"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 764, "idCardNo": "423456789012345678","customerName": "彭镭", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.004", "type": "领取保管箱",  state : "完成"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 764, "idCardNo": "423456789012345678","customerName": "彭镭", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.004", "type": "出库押运", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 765, "idCardNo": "523456789012345678","customerName": "王世虎", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.005", "type": "客户签收", state : "未领取"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.006", "type": "使用完毕", "nextProcessCode" : "8",state : "未领取"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.006", "type": "交还给押运员", "nextProcessCode" : "9", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 766, "idCardNo": "623456789012345678","customerName": "罗骁", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.006", "type": "押运员签收", "nextProcessCode" : "10", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 767, "idCardNo": "723456789012345678","customerName": "吴颖峰", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.007", "type": "入库押运", "nextProcessCode" : "11",state : "未领取"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 767, "idCardNo": "723456789012345678","customerName": "吴颖峰", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.007", "type": "仓管员签收", "nextProcessCode" : "12", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 768, "idCardNo": "823456789012345678","customerName": "谭军胜", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.008", "type": "入库", "nextProcessCode" : "13", state : "进行中"},
              {"taskId": 1403066374000,"orderId": 1403066374000, "orderNo": 769, "idCardNo": "923456789012345678","customerName": "陈元元", "mobileNo" : "13457632334", "address": "武汉市东湖高新技术开发区关山大道1号", "safeNo": "No.009", "type": "完结", "nextProcessCode" : "14", state : "进行中"}
          ]
      };
	});

commonService.factory('LocationTo', function ($location) {
    return {
        path : function(path) {
            $location.path(path);
        }
    };
});

commonService.factory('MessageService', function ($translate) {
    return {
        saveSuccess : function() {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            $translate("msg.save.success").then(function (msg) {
                toastr.success(msg);
            });

        },
        removeSuccess : function() {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            $translate("msg.delete.success").then(function (msg) {
                toastr.success(msg);
            });

        },
        successMsg : function(msg) {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "timeOut": "2000",
                "extendedTimeOut": "500",
                "positionClass": "toast-bottom-center",
                "onclick": null
            }
            toastr.success(msg);
        },
        errorMsg : function(msg) {
            toastr.options = {
                "closeButton": false,
                "debug": false,
                "positionClass": "toast-bottom-center",
                "onclick": null,
                "timeOut": "2000",
                "extendedTimeOut": "500"
            }
            toastr.error(msg);
        }
    };
});

commonService.factory(
    'DictService',
    function ($resource) {
        return $resource("sys/dict/:dictCode", {}, {
            update : {method : "put"},
            items : {url : 'sys/dict/items/:dictCode',method : "get", isArray: true}
        });
    });

commonService.factory(
    'I18nService',
    function ($resource) {
        return $resource("sys/i18n/:i18nId", {}, {
            update : {method : "put"},
            saveToJson : {url : 'sys/i18n/tojson', method : 'post'}
        });
    });

commonService.factory(
    'RouteService',
    function ($resource) {
        return $resource("sys/route/:routeId", {}, {
            update : {method : "put"},
            pagination : {method : 'get',params : {routeId : "pagination"}} ,
            getResources : {url : 'sys/route/resource/:routeId', method : 'get', isArray: true},
            saveRouteRes : {url : 'sys/route/resource',method : 'post'}
        });
    });

commonService.factory(
    'RoleService',
    function ($resource) {
        return $resource("sys/role/:roleId", {}, {
            update : {method : "put"},
            savePermission : {method : 'post'}
        });
    });

commonService.factory(
    'MenuService',
    function ($resource) {
        return $resource("sys/menu/:menuId", {}, {
            update : {method : "put"},
            getRoutes : {url : 'sys/menu/route/:menuId', method : 'get', isArray: true},
            saveMenuRoute : {url : 'sys/menu/route',method : 'post'}
        });
    });

commonService.factory(
    'PermissionService',
    function ($resource) {
        return $resource("sys/permission/:roleId", {}, {
            getMenus : {url : 'sys/permission/menu/:roleId',method : 'GET', isArray: true},
            saveResourcePermission : {method : 'post', params: {roleId : "resource"}}
        });
    });

commonService.factory(
    'UserService',
    function ($resource) {
        return $resource("sys/user/:userId", {}, {
            update : {method : "put"},
            getRoles : {method : "GET", url : "sys/user/role/:userId", isArray:true},
            getAllRoles : {method : "GET", url : "sys/user/roles", isArray:true},
            getProfile  : {method : "GET", url : "sys/user/profile/:userId"},
            updateProfile  : {method : "put", url : "sys/user/profile/:userId"},
            updatePassword  : {method : "put", url : "sys/user/password/:userId"}
        });
    });

commonService.factory(
    'ResourceService',
    function ($resource) {
        return $resource("sys/resource/:resourceId", {}, {
            update : {method : "put"}
        });
    });

commonService.factory(
    'JobService',
    function ($resource) {
        return $resource("sys/job/:jobId", {}, {
            update : {method : "put"}
        });
    });
