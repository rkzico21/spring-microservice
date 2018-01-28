'use strict';

var app = angular.module('demoapp', ['ngRoute', 'ngCookies','ngAnimate', 'ui.bootstrap']);

app.directive('a', function() {
    return {
        restrict: 'E',
        link: function(scope, elem, attrs) {
            if(attrs.ngClick || attrs.href === '' || attrs.href === '#'){
                elem.on('click', function(e){
                    e.preventDefault();
                });
            }
        }
   };
});

app.config(function ($provide, $httpProvider, $routeProvider) {
  // Intercept http calls.
  $provide.factory('interceptor', function ($q, $injector, $window, $location, $cookies) {
    return {
      // On request success
      request: function (config) {
        console.log(config); // Contains the data about the request before it is sent.
		if ($cookies.get('access_token')) {
              // may also use sessionStorage
			config.headers.Authorization = 'Bearer ' + $cookies.get('access_token');
        
		
		}
        
		// Return the config or wrap it in a promise if blank.
        return config || $q.when(config);
      },

      // On request failure
      requestError: function (rejection) {
        // console.log(rejection); // Contains the data about the error on the request.
        // Return the promise rejection.
		
		return $q.reject(rejection);
      },

      // On response success
      response: function (response) {
        console.log(response); // Contains the data from the response.
        
        // Return the response or promise.
        return response || $q.when(response);
      },

      // On response failture
      responseError: function (rejection) {
		// Return the promise rejection.
        if(rejection.status == 401)
		{
			var deferred = $q.defer();
			
			$cookies.remove("access_token");
			
			$window.sessionStorage["redirectUrl"] =$window.location.pathname;
			
			if($cookies.get("refresh_token")) {
				console.log("Making authentication request");
				var url = "http://localhost:8888/api/auth/token";
				var http = $injector.get("$http");
				http({
						method: 'POST',
						url: url,
						headers: {'Content-Type': 'application/x-www-form-urlencoded',},
						transformRequest: function(obj) {
						  var str = [];
						  for(var p in obj)
							 str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
							 return str.join("&");},
						data: {refresh_token: $cookies.get("refresh_token")}
					  })
					  .then(function(response) {
						  
						 console.log("Authentication succssfull");
						 var data = response.data;
						 $cookies.put('access_token', data.access_token); 
			             $cookies.put('refresh_token', data.refresh_token); 
							
					     http(rejection.config).then(function(resp) {
							   deferred.resolve(resp);
                            },function(resp) {
                                deferred.reject();
                            });
					  }, function errorCallback(response) {
							// called asynchronously if an error occurs
							// or server returns response with an error status.
							deferred.reject();
							$location.path( "/login" );
							
					}); 
					
					return deferred.promise;
		    }
			
			
			$location.path('/login');
			/*if($window.location.pathname != '/login.html')
			{
				$window.location.href = '/login.html';
			}*/
		}
		return rejection || $q.when(rejection);
      }
    };
  });
  
  //$httpProvider.defaults.withCredentials = true;

  // Add the interceptor to the $httpProvider.
  $httpProvider.interceptors.push('interceptor');
  
  //$httpProvider.defaults.useXDomain = true;
  //delete $httpProvider.defaults.headers.common['X-Requested-With'];
	
  $routeProvider.when("/user", {
        controller: "userCtrl",
        templateUrl: "/user.html"
    });

    $routeProvider.when("/todolist/", {
        controller: "todoCtrl",
        templateUrl: "todolist.html"
    });

    $routeProvider.when("/meetings", {
        controller: "meetingsCtrl",
        templateUrl: "meetings.html"
    });
	
	$routeProvider.when("/meeting", {
        controller: "meetingCtrl",
        templateUrl: "meeting.html"
    });
	
	$routeProvider.when("/login", {
        controller: "sessionCtrl",
        templateUrl: "login.html"
    });
	
	$routeProvider.when("/report", {
        controller: "reportCtrl",
        templateUrl: "report.html"
    });

    $routeProvider.otherwise({ redirectTo: "/todolist" });	
	
});


app.run(['$rootScope', '$location', '$cookies', function ($rootScope, $location, $cookies) {
    $rootScope.$on('$routeChangeStart', function (event, next, current) {
          
	
        if (!($cookies.get('access_token'))) {
            console.log('DENY');
            //event.preventDefault();
			// not going to #login, we should redirect now
			$location.path( "/login" );
		}
        else {
            //console.log('ALLOW');
            //$location.path('/todolist');
        }
    });
}]);