var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";
    $scope.details = {};

    $scope.pageOpen = function(){
        var usr = $http.get('/User/findAll');
        usr.then(function (response) {
            $scope.userList = response.data;
        });

    }

    $scope.findUserByName = function () {
        var usr = $http.get('/User/findUserByUsername/' + $scope.username);
        usr.then(function (response) {
            $scope.user = response.data;
        });
    }

    $scope.analyzeButton = function () {
        var usr = $http.get('/User/analyzeButton/' + $scope.username);
        $scope.details.username = $scope.username;
        $scope.createDetail($scope.details);
        usr.then(function (response) {

        });
    }

    $scope.findUserById = function() {
        var usr = $http.get('/User/findUserByID/' + $scope.userId);
        usr.then(function (response) {
            $scope.employeeList = response.data;
            $scope.employeeId ="";

        });
    }

    $scope.createDetail = function(){
        var det = $http.post('/Detail/Create/', $scope.details);
        det.then(function(response){
            console.log("yazdıı");
        });
    }
});

