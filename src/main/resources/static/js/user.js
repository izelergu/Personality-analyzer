var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";

    $scope.pageOpen = function(){
        var usr = $http.get('/User/findAll');
        usr.then(function (response) {
            $scope.userList = response.data;
        });

    }

    $scope.getTwitterUsername = function(username){
        var usr = $http.get('/User/getUserTweets/' + $scope.username);
        usr.then(function (response) {
            $scope.username = "";
        });
    }

    $scope.findUserByName = function () {
        var usr = $http.get('/User/findUserByUsername/' + $scope.username);
        usr.then(function (response) {
            $scope.user = response.data;
            $scope.username = "";
        });
    }

    $scope.findUserById = function() {
        var usr = $http.get('/User/findUserByID/' + $scope.userId);
        usr.then(function (response) {
            $scope.employeeList = response.data;
            $scope.employeeId ="";

        });
    }
});

