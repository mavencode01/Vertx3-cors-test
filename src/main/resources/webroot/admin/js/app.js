'use strict';

var kApp = angular.module('kApp', ["ngAnimate", "ngSanitize", "ngCookies", "kApp.controllers", "ui.router", "http-auth-interceptor"])
.config(['$urlRouterProvider','$httpProvider', '$locationProvider', '$stateProvider', function($urlRouterProvider, $httpProvider,  $locationProvider, $stateProvider){

  $urlRouterProvider.otherwise("/login");  

	//$httpProvider.defaults.withCredentials = true;
	$httpProvider.defaults.useXDomain = true;
	delete $httpProvider.defaults.headers.common['X-Requested-With'];

  $stateProvider
	.state('login', {
        url: "/login",
        templateUrl: "partials/login.html",
        controller: 'LoginCtrl'       
    })
  
}])
.run(['$rootScope', '$location', function($rootScope, $location) {
	
	$rootScope.$on('event:auth-loginRequired', function() {                                        
		$location.path('/login');		
	});                
}]);