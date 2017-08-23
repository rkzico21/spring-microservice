var app = angular.module('demoapp'); 
app.controller('meetingsCtrl', ['$scope', '$http', '$sce', '$window', 'fileUpload', function($scope, $http, $sce, $window, fileUpload) {
    $scope.meetings = [];
	var url = "http://localhost:8888/api/meeting";
	var trustedurl = $sce.trustAsResourceUrl(url);
	//$scope.userid=1;
	$http.get(url)
    .then(function(response) {
	  
	   if(response.data._embedded) {
	  
		angular.forEach(response.data._embedded.meetings, function(meeting) {
	        $scope.meetings.push({url:meeting._links.self.href, subject:meeting.subject, location:meeting.location});
		
	  });
	  }
    });
	
    $scope.createMeeting = function() {
	  var dataObj = {
				subject: $scope.subject,
				location: $scope.meetingLocation,
		};	
		
		var url = "http://localhost:8888/api/meeting";
	
		$http.post(url, dataObj)
		.then(function(response) {
       	    var meeting = response.data;
			$scope.meetings.push({url:meeting._links.self.href, subject:meeting.subject, location:meeting.location});
			$scope.upload(meeting._links.fileUpload.href);
			$scope.subject = "";
			$scope.meetingLocation="";
			});;
		
        
    };
	
	$scope.upload = function(uploadUrl){
        var file = $scope.file;
        console.log('file is ' );
        console.dir(file);
		if($scope.file) {
        var promise = fileUpload.uploadFileToUrl(file, uploadUrl);
		
		promise.then( function(response){
			 var file = response.data;
			console.log("file uploaded");
		});
		}
    };
	
	$scope.handleClick = function(meetingUrl){
	    $window.localStorage['meetingUrl'] = meetingUrl;
		$window.location.href = '/meeting.html';
	 };

}]);