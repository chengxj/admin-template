'use strict';
function LeftMenuCtrl($scope, $http) {
	// $scope.menus = $http.get("route");
	$http({
		method : 'GET',
		url : 'route'
	}).success(function(data, status, headers, config) {
		$scope.menus = data;
	}).error(function(data, status, headers, config) {
	});
}
LeftMenuCtrl.$inject = [ '$scope', '$http' ];