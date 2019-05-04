var app = angular.module('ResultApp', []);
app.controller('ResultCtrl', function($scope,$http,$window) {

    $scope.results = {};

    $scope.username = $window.sessionStorage.getItem("username");

    $scope.showDetails = function(){
        $window.location.href = '/detailPage.html';
    }

    $scope.getResults = function () {
        var res = $http.get('/Result/getResult/'+ $scope.username);
        res.then(function (response) {
            $scope.results = response.data;
        });
    }
});