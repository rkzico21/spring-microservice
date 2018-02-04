app.controller('userCtrl', ['$scope', '$http', '$sce', '$window', '$location', '$cookies', 'apiService', function($scope, $http, $sce, $window, $location, $cookies, apiService) {
    $scope.userApi;
	
	$scope.init = function() {
        $scope.userList = [];
        $scope.isAdmin = false;
        $scope.showForm = false;
        $scope.showUser = false;
		
		$http.get("http://localhost:8888/api").then(function(response){
			$http.get(response.data._links.user_service.href).then(function(response){
			    userApi = response.data;
				loadUser(userApi._links.users.href);
			})	
		});
				
	};
	
	loadUser = function(url) {
		var token = $cookies.get('access_token')
        if (token) {
            var decoded = jwt_decode(token);
            if (decoded.authorities) {
                angular.forEach(decoded.authorities, function(authority) {
                    if (authority == "admin") {
                        $scope.isAdmin = true;
                    }
                });
            }
        }

        var trustedurl = $sce.trustAsResourceUrl(url);
        $http.get(url).then(function(response) {
            if (response.data._embedded) {

                angular.forEach(response.data._embedded.users, function(user) {
                    addUserToList(user);
                });
            }
        });
	
	}

    clearFields = function() {
        $scope.username = "";
        $scope.useremail = "";
        $scope.password = "";
        $scope.userfullname = "";
        $scope.presentAddress = "";
        $scope.permanantAddress = "";
        $scope.designation = "";
        $scope.department = "";
    }

    addUserToList = function(user) {
        var userTodolist = user._links.todolist ? user._links.todolist.href : null;
        $scope.showUser = true;
        $scope.showForm = false;

        var userObj = {
            userId: user.id,
            userName: user.name,
            userFullName: user.fullName,
            userEmail: user.email,
            userTodolist: userTodolist,
            userDesignation: user.designation
        }

        $scope.userList.push(userObj);
    };

    $scope.displayForm = function(display) {
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
        var url = userApi._links.users.href;

        $http.post(url, dataObj)
            .then(function(response) {
                var user = response.data;
                addUserToList(user);
                clearFields();

            });;


    };



    $scope.handleClick = function(todolistUri, userId) {
       $location.path("/todolist").search('user', userId).search('uri', todolistUri);
    };

}]);