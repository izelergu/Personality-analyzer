var app = angular.module('ResultApp', []);
app.controller('ResultCtrl', function($scope,$http,$window) {

    $scope.result = {};
    $scope.results = {};
    $scope.username = $window.sessionStorage.getItem("username");
    $scope.isLoading = $window.sessionStorage.getItem("isLoading");

    $scope.getResults = function () {
        $scope.isLoading = false;
        var res = $http.get('/Result/getResult/'+ $scope.username);
        res.then(function (response) {
            $scope.result = response.data;
        });
    }

    $scope.showDetails = function(){
        $window.location.href = '/detailPage.html';
    }
});