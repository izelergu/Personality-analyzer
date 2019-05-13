var app = angular.module('DetailApp', []);
app.controller('DetailCtrl', function($scope,$http,$window) {

    $scope.details = {};
    $scope.username = $window.sessionStorage.getItem("username");

    $scope.getDetails = function () {
        var det = $http.get('/Detail/getDetails/'+ $scope.username);
        det.then(function (response) {
            $scope.details = response.data;
            //$window.sessionStorage.clear();
        });
    }

    $scope.logout = function(){
        $window.sessionStorage.setItem("AccUsername","");
        $window.sessionStorage.setItem("username","");
    }
});