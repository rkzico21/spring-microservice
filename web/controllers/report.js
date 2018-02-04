app.controller('reportCtrl',['$scope', '$http', '$sce', '$window', function($scope, $http, $sce, $window) {
	
	$scope.init = function() {
		var url = "http://localhost:8060/jasperserver/flow.html?standAlone=true&decorate=no&_flowId=viewReportFlow&reportUnit=/reports/interactive/Meeting_List&j_username=jasperadmin&j_password=jasperadmin";
		//$scope.reportUrl = $sce.trustAsResourceUrl(url) ;
		document.getElementById("frame1").src = url;
	}

}]);