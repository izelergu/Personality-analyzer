var app = angular.module('ResultApp', []);
app.controller('ResultCtrl', function($scope,$http,$window) {

    $scope.results = {};

    $scope.username = $window.sessionStorage.getItem("username");

    $scope.getResults = function () {
        var res = $http.get('/Result/getResults/'+ $scope.username);
        res.then(function (response) {
            $scope.results = response.data;
        });
    }
});