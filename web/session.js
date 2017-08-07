var app = angular.module('demoapp'); 
app.controller('sessionCtrl', function($scope, $http, $sce, $window) {
	// $window.localStorage.clear();
    $scope.signin = function() {
	  
		var url = "http://localhost:9991/login";
		$sce.trustAsResourceUrl(url);
		$http({
				method: 'POST',
				url: url,
				headers: {'Content-Type': 'application/x-www-form-urlencoded',},
				transformRequest: function(obj) {
						var str = [];
						for(var p in obj)
							str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
							return str.join("&");},
				data: {username: $scope.username, password: $scope.password}
			})
		.then(function(response) {
			 var data = response.data;
			 $window.localStorage['token'] = data.access_token;
			 if ($window.localStorage.getItem('redirectUrl')) {
              // may also use sessionStorage
					$window.location.href = $window.localStorage.getItem('redirectUrl');
			  }
			});
		
        
    };
	
	$scope.signout = function() {
	  
			 $window.localStorage.clear();
			 $window.location.href = "/home.html"
        
    };
});