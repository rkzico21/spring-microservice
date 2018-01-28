app.controller('sessionCtrl', function($rootScope, $scope, $http, $sce, $window, $location, $cookies) {
	$scope.signin = function() {
	    var url = "http://localhost:8888/api/auth/login";
		$sce.trustAsResourceUrl(url);
		console.log("Making authentication request");
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
			 
			 var redirectUrl = $window.sessionStorage.getItem('redirectUrl');
			 //$window.location.href = (redirectUrl && redirectUrl !="/login.html") ? redirectUrl : "/todolist.html"
			 
			 $rootScope.$broadcast('loginEvent', {data: "login"});  
			 
			 var url = "http://localhost:8888/api/user?name="+ $scope.username;
			 loadUser(url);
			 
			 
		});
   };
   
    loadUser = function(url) {
		$http.get(url)
                .then(function(response) {
					var user = response.data;
					$rootScope.user = user;
                    $window.sessionStorage["user"] =  JSON.stringify(response.data);
					$window.localStorage["userId"] = user.id;
		            $window.localStorage['todolistUri'] = user._links.todolist ? user._links.todolist.href : null;
					$location.path("/todolist");
				});
	};
	
	$scope.signout = function() {
			 $window.sessionStorage.clear();
			 $window.sessionStorage.clear();
			 $cookies.remove("access_token");
			 $cookies.remove("refresh_token");
			 $rootScope.$broadcast('logoutEvent', {data: "logout"}); 
			 $location.path("/login");
        
    };
});