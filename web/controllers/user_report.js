//var app = angular.module('demoapp'); 
app.controller('userReportCtrl', function($scope, $http, $sce, $window, $cookies) {
    var url = "http://localhost:8888/api/user/report";
	var trustedurl = $sce.trustAsResourceUrl(url);
	//$scope.userid=1;
	$http.get(url, 
			 
			 {responseType: 'arraybuffer'})
       .then(function (response) {
           var file = new Blob([response.data], {type: 'application/pdf'});
           var fileURL = URL.createObjectURL(file);
           $scope.content = $sce.trustAsResourceUrl(fileURL);
		   
		   
    });
	
	
	
	

});