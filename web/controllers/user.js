//var app = angular.module('demoapp'); 
app.controller('userCtrl', function($scope, $http, $sce, $window, $cookies) {
    $scope.userList = [];
	$scope.isAdmin = false;
	$scope.showForm = false;
	$scope.showUser = false;
	
	var url = "http://localhost:8888/api/user";
    var token = $cookies.get('access_token')
	if(token) {
	  var decoded = jwt_decode(token);
      if(decoded.authorities) {
		 angular.forEach(decoded.authorities, function(authority) {
		    if(authority == "admin") {
				$scope.isAdmin = true;
			}
		});
	  }
	}
	
	
	var trustedurl = $sce.trustAsResourceUrl(url);
	$http.get(url)
    .then(function(response) {
	  
	   if(response.data._embedded) {
		   	
	   angular.forEach(response.data._embedded.users, function(user) {
		    addUserToList(user);
	  });
	  }
    });
	
	clearFields = function() {
		$scope.username = "";
		$scope.useremail="";
		$scope.password="";
		$scope.userfullname="";
		$scope.presentAddress="";
		$scope.permanantAddress="";
		$scope.designation="";
		$scope.department="";
	}
	
	addUserToList = function(user) {
		var userTodolist = user._links.todolist ? user._links.todolist.href : null;
		$scope.showUser = true;
		$scope.showForm = false;
		
		var userObj = {
			userId:user.id, 
			userName:user.name, 
			userFullName:user.fullName, 
			userEmail:user.email, 
			userTodolist: userTodolist,
			userDesignation: user.designation
		}
		
		$scope.userList.push(userObj);
	};
	
	$scope.displayForm = function (display) {
		$scope.showForm = display;
		$scope.showUser = !display;
	};
	
    $scope.userAdd = function() {
	  var address = {
		  presentAddress: $scope.presentAddress,
		  permanantAddress: $scope.permanantAddress
	  };
	  
	  var dataObj = {
				name: $scope.username,
				fullName: $scope.userfullname,
				email: $scope.useremail,
				password: $scope.password,
				address: address
				
		};	
		console.log(dataObj);
		var url = "http://localhost:8888/api/user";
	
		$http.post(url, dataObj)
		.then(function(response) {
       	    var user = response.data;
			addUserToList(user);
			clearFields();
			
			});;
		
        
    };
	
	
	
	$scope.handleClick = function(todolistUri, userId){
	    $window.localStorage['todolistUri'] = todolistUri;
		$window.localStorage['userId'] = userId;
		
		$window.location.href = '/todolist.html';
	 };

});