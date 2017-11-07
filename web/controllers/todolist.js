app.controller('todoCtrl', function($scope, $http, $sce, $window) {
    $scope.todoList = [];
	var url = $window.localStorage["todolistUri"] || false;
	var userId = $window.localStorage["userId"] || false;
	if(url) {
		var trustedurl = $sce.trustAsResourceUrl(url);
	
		$http.get(url)
		.then(function(response) {
			
			if(response.data._embedded) {
		angular.forEach(response.data._embedded.todoLists, function(item) {
	        $scope.todoList.push({todoText:item.title, done:false, url:item._links.self.href});
		});}
	  });
	}
	
    $scope.todoAdd = function() {
	  var dataObj = {
				title: $scope.todoInput,
				userId:userId,
		};	
		
		var url = "http://localhost:8888/api/todolist";
		$sce.trustAsResourceUrl(url);
		$http.post(url, dataObj)
		.then(function(response) {
			$scope.todoList.push({todoText:response.data.title, done:false, url:response.data._links.self.href});
			$scope.todoInput = "";
			});;
		
        
    };
	
	$scope.deletelist = function(url) {
	    $http.delete(url)
		 .then(function(response) {
		     var element = document.getElementById(url);//angular.element(document.querySelector('#'+url));
			 element.parentNode.removeChild(element);;
		 });
	};

    $scope.remove = function() {
        var oldList = $scope.todoList;
        $scope.todoList = [];
        angular.forEach(oldList, function(x) {
            if (!x.done) $scope.todoList.push(x);
        });
		
	  
    };
});