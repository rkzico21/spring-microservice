'use strict';
app.controller('navigationController', function ($rootScope,$scope, $http, $sce, $window, $cookies) {

	$scope.authenticated = false;
	 if($cookies.get("access_token")) {
		$scope.authenticated = true;
	 }
	
	$rootScope.$on('loginEvent', function(event, data){
        
		if($cookies.get("access_token")) {
		 $scope.authenticated = true;
	    }
    });
	
	$rootScope.$on('logoutEvent', function(event, data){
        $scope.authenticated = false;
	   
    });
	
    $scope.isActive = function (path) {
		 return true;
        //return $window.location.path().substr(0, path.length) == path;
    };

    //$scope.hasUserInCtx = function () {
        
    //    return (!placesDataService.getUserInContext()) ? true : false;
    //};
});