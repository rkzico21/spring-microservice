app.controller('todoCtrl', function($scope, $http, $sce, $window, $cookies, $route, $location) {
    var userId = 0;

	$scope.init = function() {
		$scope.pageData;
		$scope.currentPage = 1;
        $scope.itemsPerPage = 10;
        $scope.maxSize = 5;
		$scope.todoList = [];
        $scope.showForm = false;
        $scope.showList = false;
		$scope.totalItems = 0;
		
		var params = $location.search();
		
		var url = false;
		
		if(params && params.uri && params.user) {
			url = params.uri;
			userId = params.user;
		} else {
			url = $window.localStorage["todolistUri"] || false;
			userId = $window.localStorage["userId"] || false;
		}
		
		loadTodoList(url,$scope.currentPage,$scope.itemsPerPage);
	};

    $scope.todoAdd = function() {
        var dataObj = {
            title: $scope.todoItemTitle,
            userId: userId,
        };

        var url = $window.localStorage["todolistUri"];
        $sce.trustAsResourceUrl(url);
        $http.post(url, dataObj)
            .then(function(response) {
                $scope.todoList.push({
                    todoText: response.data.title,
                    done: false,
                    url: response.data._links.self.href
                });
                $scope.todoItemTitle = "";
                $scope.showForm = false;
                $scope.showList = true;
            });;


    };

    $scope.deletelist = function(url) {
        $http.delete(url)
            .then(function(response) {
                var element = document.getElementById(url); //angular.element(document.querySelector('#'+url));
                element.parentNode.removeChild(element);;
            });
    };

    $scope.displayForm = function(display) {
        $scope.showForm = display;
        $scope.showList = !display;
    };
	
	$scope.changePage = function() {
		var pageUrl;    
		if($scope.currentPage === ($scope.pageData.page + 2)) {
			pageUrl = $scope.pageData._links.next.href;
		}
		
		if($scope.currentPage === $scope.pageData.page){
			pageUrl = $scope.pageData._links.prev.href;
		}
		
		loadTodoList(pageUrl, $scope.currentPage - 1, $scope.itemsPerPage);
	};
	
	
	
	loadTodoList = function(url, page, size) {
		if(url) {
			var	params = {
					page: page - 1,
					size: size
			};
			
			
			$http.get(url, {params: params})
			    .then(function(response) {
                    $scope.showList = true;
                    $scope.totalItems = response.data.totalResults;
					$scope.pageData = response.data;
					if (response.data._embedded) {
						$scope.todoList = [];
						angular.forEach(response.data._embedded.items, function(item) {
                            $scope.todoList.push({
                                todoText: item.title,
                                done: false,
                                url: item._links.self.href
                            });
						});
					}
                });
		}
	};
}); 