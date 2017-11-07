//var app = angular.module('demoapp'); 
app.controller('userCtrl', function($scope, $http, $sce, $window) {
    $scope.showUser=false;
    $scope.userList = [];
	var url = "http://localhost:8888/api/user";
	var trustedurl = $sce.trustAsResourceUrl(url);
	$http.get(url)
    .then(function(response) {
	  
	   if(response.data._embedded) {
		   	
	   angular.forEach(response.data._embedded.users, function(user) {
		    addUserToList(user);
	  });
	  }
    });
	
	
	addUserToList = function(user) {
		$scope.userList.push({userId:user.id, userName:user.name, userFullName:user.fullName, userEmail:user.email, userTodolist: user._links.todolist.href});
	};
	
    $scope.userAdd = function() {
	  var dataObj = {
				name: $scope.username,
				fullName: $scope.userfullname,
				email: $scope.useremail,
				
		};	
		
		var url = "http://localhost:8888/api/user";
	
		$http.post(url, dataObj)
		.then(function(response) {
       	    var data = response.data;
			addUserToList(data.user);
			
			$scope.username = "";
			$scope.useremail="";
			$scope.userfullname="";
			});;
		
        
    };
	
	//For testing purpose
	$scope.reloadUser = function() {
		var url = "http://localhost:8888/api/user";
		$http.get(url)
		    .then(function(response) {
	  
			if(response.data._embedded) {
	  
				angular.forEach(response.data._embedded.userResources, function(item) {
					//addUserToList(item.user);
			});
	  }
    })
	};
	
	$scope.handleClick = function(todolistUri, userId){
	    $window.localStorage['todolistUri'] = todolistUri;
		$window.localStorage['userId'] = userId;
		
		$window.location.href = '/todolist.html';
	 };

});