app.controller('meetingCtrl', ['$scope', '$http', '$sce', '$window', '$location', 'fileUpload', function($scope, $http, $sce, $window, $location, fileUpload) {
    $scope.init = function() {
		$scope.files = [];
		$scope.participants = [];
		var params = $location.search();
        var url = params.url; //$window.localStorage["meetingUrl"];
        $scope.resourceUrl = url;
        
		var trustedurl = $sce.trustAsResourceUrl(url);
        $http.get(url).then(function(response) {
            var meeting = response.data;
            if (meeting) {

                $scope.subject = meeting.subject;
                $scope.meetingLocation = meeting.location;
                $scope.fileUploadUrl = meeting._links.fileUpload.href;
                $scope.participantsUrl = meeting._links.participants.href;
                $scope.meetingDescription = meeting.description,
                    $scope.meetingDateTime = new Date(meeting.dateTime);
                loadFiles(meeting._links.files.href);
                loadParticipants(meeting._links.participants.href);

            };
        });
    };

    loadFiles = function(url) {
        $http.get(url)
            .then(function(response) {
                if (response.data._embedded) {

                    angular.forEach(response.data._embedded.files, function(file) {
                        $scope.files.push({
                            url: file._links.self.href,
                            name: file.name,
                            contentUrl: file._links.content.href
                        });

                    });
                }
            })
    };



    loadParticipants = function(url) {
        $http.get(url)
            .then(function(response) {
                if (response.data._embedded) {
                    populateParticipants(response.data._embedded.participants);
                };
            })
    };

    populateParticipants = function(participants) {
        $scope.participants = [];
        angular.forEach(participants, function(participant) {
            $scope.participants.push({
                email: participant.email,
                name: participant.name,
                organization: participant.organization
            });

        });
    }


    $scope.download = function(url) {
        window.open(url, '_blank', '');
    };


    $scope.update = function(url) {
        var dataObj = {
            subject: $scope.subject,
            location: $scope.meetingLocation,
			description: $scope.meetingDescription
        };


        $http.put(url, dataObj)
            .then(function(response) {
                var data = response.data;
            });


        $scope.upload($scope.fileUploadUrl);

        $scope.updateParticipants($scope.participantsUrl);


    };


    $scope.addParticipant = function() {
        $scope.participants.push({
            name: $scope.participantName,
            organization: $scope.participantOrganization,
            email: $scope.participantEmail
        });

        $scope.participantName = "";
        $scope.participantOrganization = "";
        $scope.participantEmail = "";
    }


    $scope.updateParticipants = function(url) {
        var participants = $scope.participants;
		console.log(participants);
        $http.put(url, participants)
            .then(function(response) {
                if (response.data && response.data._embedded)
                    populateParticipants(response.data._embedded.participants);
            });
    }

    $scope.upload = function(uploadUrl) {
        var file = $scope.file;
        console.log('file is ');
        console.dir(file);
        if ($scope.file) {
            var promise = fileUpload.uploadFileToUrl(file, uploadUrl);

            promise.then(function(response) {
                $scope.file = "";
                var file = response.data;
                $scope.files.push({
                    url: file._links.self.href,
                    name: file.name,
                    contentUrl: file._links.content.href
                });

            });
        }
    };

    $scope.$watch('selectedUser', function() {
        if ($scope.selectedUser && $scope.selectedUser.name) {
            $scope.participants.push({
                name: $scope.selectedUser.fullName,
				organization: $scope.selectedUser.department,
                position: $scope.selectedUser.designation,   
                email: $scope.selectedUser.email,
                participantId: $scope.selectedUser.id
            });

        }

    }, true);


    $scope.getUsers = function(val) {
		var userApi =  $window.localStorage["userApi"];
        if(!userApi)
		   return;

	    console.log(userApi);
		var uri =  JSON.parse(userApi)._links.search.href;
        var arr = uri.split("{");
		return $http.get(arr[0], {
            params: {
                fullname: val
            }
        }).then(function(response) {
            if (response.data._embedded) {
                return response.data._embedded.users;
            }
        });
    };

}]);