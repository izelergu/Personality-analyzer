var app = angular.module('DetailApp', []);
app.controller('DetailCtrl', function($scope,$http,$window) {

    $scope.username
    $scope.details ={};

    $scope.pageOpen = function () {
        var usr = $http.get('/Details/getDetails'+ $scope.username);
        usr.then(function (response) {
            $scope.details = response.data;
        });
    }
});