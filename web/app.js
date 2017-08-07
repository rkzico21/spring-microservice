'use strict';

var app = angular.module('demoapp', []);

app.config(function ($provide, $httpProvider) {
  
  // Intercept http calls.
  $provide.factory('MyHttpInterceptor', function ($q, $window, $location) {
    return {
      // On request success
      request: function (config) {
        // console.log(config); // Contains the data about the request before it is sent.
		config.headers = config.headers || {};
        if ($window.localStorage.getItem('token')) {
              // may also use sessionStorage
             config.headers.Authorization = 'Bearer ' + $window.localStorage.getItem('token');
        }
        
		// Return the config or wrap it in a promise if blank.
        return config || $q.when(config);
      },

      // On request failure
      requestError: function (rejection) {
        // console.log(rejection); // Contains the data about the error on the request.
        alert("error");
        // Return the promise rejection.
        return $q.reject(rejection);
      },

      // On response success
      response: function (response) {
        // console.log(response); // Contains the data from the response.
         
        // Return the response or promise.
        return response || $q.when(response);
      },

      // On response failture
      responseError: function (rejection) {
		
        console.log(rejection);
		// Return the promise rejection.
        if(rejection.status == 401)
		{
			$window.localStorage["redirectUrl"] =$window.location.pathname;
			if($window.location.pathname != '/login.html')
			{
				$window.location.href = '/login.html';
			}
		}
		return $q.reject(rejection);
      }
    };
  });

  // Add the interceptor to the $httpProvider.
  $httpProvider.interceptors.push('MyHttpInterceptor');

});