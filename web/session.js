  app.controller('sessionCtrl', function($scope, $http, $sce, $window, $cookies) {
	// $window.localStorage.clear();
    $scope.signin = function() {
	  
		var url = "http://localhost:8888/api/auth/login";
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
			 $cookies.put('access_token', data.access_token); 
			 $cookies.put('refresh_token', data.refresh_token); 
			 
			 if ($window.sessionStorage.getItem('redirectUrl')) {
              // may also use sessionStorage
			       $window.location.href = $window.sessionStorage.getItem('redirectUrl');
			  }
			});
		
        
    };
	
	$scope.signout = function() {
			 $window.sessionStorage.clear();
			 $cookies.remove("access_token");
			 $cookies.remove("refresh_token");
			 $window.location.href = "/home.html"
        
    };
});