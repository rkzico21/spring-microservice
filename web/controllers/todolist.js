app.controller('todoCtrl', function($scope, $http, $sce, $window, $cookies) {
    $scope.todoList = [];
	$scope.showForm = false;
	$scope.showList = false;
	
	var url = $window.localStorage["todolistUri"] || false;
	var userId = $window.localStorage["userId"] || false;
	if(url) {
		var trustedurl = $sce.trustAsResourceUrl(url);
	
		$http.get(url)
		.then(function(response) {
			$scope.showList = true;
			if(response.data._embedded) {
		angular.forEach(response.data._embedded.todoListItems, function(item) {
	        $scope.todoList.push({todoText:item.title, done:false, url:item._links.self.href});
			
		});}
	  });
	}
	
    $scope.todoAdd = function() {
	  var dataObj = {
				title: $scope.todoItemTitle,
				userId:userId,
		};	
		
		//var url = "http://localhost:8888/api/todolist";
		var url = $window.localStorage["todolistUri"];
		$sce.trustAsResourceUrl(url);
		$http.post(url, dataObj)
		.then(function(response) {
			$scope.todoList.push({todoText:response.data.title, done:false, url:response.data._links.self.href});
			$scope.todoItemTitle = "";
			$scope.showForm = false;
			$scope.showList = true;
		});;
		
        
    };
	
	$scope.deletelist = function(url) {
	    $http.delete(url)
		 .then(function(response) {
		     var element = document.getElementById(url);//angular.element(document.querySelector('#'+url));
			 element.parentNode.removeChild(element);;
		 });
	};
	
	$scope.displayForm = function (display) {
		$scope.showForm = display;
		$scope.showList = !display;
	};

    /*$scope.remove = function() {
        var oldList = $scope.todoList;
        $scope.todoList = [];
        angular.forEach(oldList, function(x) {
            if (!x.done) $scope.todoList.push(x);
        });
		
	  
    };*/
});