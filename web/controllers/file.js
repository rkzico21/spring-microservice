//var app = angular.module('demoapp'); 

app.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}]);


app.service('fileUpload', ['$http', function ($http) {
    this.uploadFileToUrl = function(file, uploadUrl){
        var fd = new FormData();
        fd.append('file', file);
        promise = $http.post(uploadUrl, fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        
		return promise;
    }
}]);

app.controller('fileCtrl', ['$scope', 'fileUpload', function($scope, fileUpload){
    
    $scope.upload = function(uploadUrl){
        var file = $scope.file;
        console.log('file is ' );
        console.dir(file);
        fileUpload.uploadFileToUrl(file, uploadUrl);
    };
    
}]);


