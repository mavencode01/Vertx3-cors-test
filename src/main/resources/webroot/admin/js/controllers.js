'use strict';


/* Controllers */

angular.module('kApp.controllers', [])
.controller('LoginCtrl', ['$rootScope', '$scope','$http', '$location', '$window', 'authService', function($rootScope, $scope, $http, $location, $window, authService) {
	
	
	$scope.BASE_API = "http://localhost"
	
	$scope.reset = function(){
		$scope.username = "";
		$scope.password = "";
	}
	
	$scope.signIn = function(){
		//login stuff here...
		
		$http.get($scope.BASE_API + "/api/login")
		.success(function (data, status, headers, config)
		{
			console.log(data);
			authService.loginConfirmed();	
		})
		.error(function (data, status, headers, config)
		{
			console.log(data);
		});
		
			
	}
}])