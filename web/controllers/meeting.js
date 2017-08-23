var app = angular.module('demoapp'); 
app.controller('meetingCtrl',['$scope', '$http', '$sce', '$window', 'fileUpload'  , function($scope, $http, $sce, $window, fileUpload) {
    $scope.meetings = [];
	$scope.files=[];
	var url =  $window.localStorage["meetingUrl"];
	$scope.resourceUrl = url;
	var trustedurl = $sce.trustAsResourceUrl(url);
	//$scope.userid=1;
	$http.get(url)
    .then(function(response) {
	   var meeting = response.data;
	   if(meeting) {
	  
		    $scope.subject = meeting.subject;
			$scope.meetingLocation = meeting.location;
			$scope.fileUploadUrl = meeting._links.fileUpload.href;
			loadFiles(meeting._links.files.href);
		
	  };
	  }
    );
	
    
    loadFiles = function(url) {
		$http.get(url)
    .then(function(response) {
	   if(response.data._embedded) {
	  
		angular.forEach(response.data._embedded.files, function(file) {
	        $scope.files.push({url:file._links.self.href, name:file.name, contentUrl:file._links.content.href});
		
	  });
	  }
    })
	};		
	
	$scope.download = function(url){
	    window.open(url, '_blank', '');  
    };
	
	
	$scope.update = function(url) {
	  var dataObj = {
				subject: $scope.subject,
				location: $scope.meetingLocation,
		};	
		
		
		$http.put(url, dataObj)
		.then(function(response) {
       	    var data = response.data;
			});
			
			
			$scope.upload($scope.fileUploadUrl);
		
        
    };
	
	
	$scope.upload = function(uploadUrl){
        var file = $scope.file;
        console.log('file is ' );
        console.dir(file);
		if($scope.file) {
        var promise = fileUpload.uploadFileToUrl(file, uploadUrl);
		
		promise.then( function(response){
			 var file = response.data;
			 $scope.files.push({url:file._links.self.href, name:file.name, contentUrl:file._links.content.href});
		
		});
		}
    };

}]);