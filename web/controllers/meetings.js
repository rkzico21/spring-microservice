app.controller('meetingsCtrl', ['$scope', '$http', '$sce', '$window', '$location', 'fileUpload', '$cookies', function($scope, $http, $sce, $window, $location, fileUpload, $cookies) {
	$scope.meetingApi;
    

	
	$scope.init = function() {

		$scope.showForm = false;
        $scope.showList = false;
		
		$scope.meetings = [];
        //var url = "http://localhost:8888/api/meeting";
        //var trustedurl = $sce.trustAsResourceUrl(url);
		
		$http.get("http://localhost:8888/api").then(function(response){
			$http.get(response.data._links.meeting_service.href).then(function(response){
			    $scope.meetingApi = response.data;
				loadMeetings($scope.meetingApi._links.meetings.href);
			})	
		})
    };
	
	
	loadMeetings = function(url) {
	    $http.get(url)
            .then(function(response) {
				$scope.showList = true;
                if (response.data && response.data._embedded) {
					$scope.showList = true;
                    angular.forEach(response.data._embedded.meetings, function(meeting) {
                        $scope.meetings.push({
                            url: meeting._links.self.href,
                            subject: meeting.subject,
                            location: meeting.location
                        });

                    });
                }
            });
	}

    $scope.createMeeting = function() {
        var dataObj = {
            subject: $scope.subject,
            location: $scope.meetingLocation,
            description: $scope.meetingDescription,
            dateTime: $scope.meetingDateTime
        };

        var url = $scope.meetingApi._links.meetings.href;

        $http.post(url, dataObj)
            .then(function(response) {
                var meeting = response.data;
				console.log(meeting);
                $scope.meetings.push({
                    url: meeting._links.self.href,
                    subject: meeting.subject,
                    location: meeting.location
                });
				
				$window.localStorage['meetingUrl'] = meeting._links.self.href;
				
				if ($scope.file) {
                    $scope.upload(meeting);
                } else {
                    //$window.location.href = '/meeting.html';
                
					loadMeeting( meeting._links.self.href);
				};
				
				$scope.subject = "";
                $scope.meetingLocation = "";


            });
    };
	
	

    $scope.upload = function(meeting) {
		var uploadUrl = meeting._links.fileUpload.href;
        var file = $scope.file;
        //console.log('file is ');
        //console.dir(file);

        if ($scope.file) {
            var promise = fileUpload.uploadFileToUrl(file, uploadUrl);
            promise.then(function(response) {
                var file = response.data;
                
				loadMeeting(meeting._links.self.href);
			});
        }


    };
	
	$scope.displayForm = function(display) {
        $scope.showForm = display;
        $scope.showList = !display;
    };


    $scope.handleClick = function(meetingUrl) {
      loadMeeting(meetingUrl);
	};
	
	loadMeeting = function(meetingUrl) {
		$location.path("/meeting").search("url", meetingUrl);
	};

}]);