var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";
    $scope.detail = {};
    $scope.result = {};

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
        $window.sessionStorage.setItem("username",$scope.username);
        $scope.username = $window.sessionStorage.getItem("username");//session
        $scope.detail.username = $scope.username;
        $window.location.href = '/resultPage.html';
        $scope.createDetail($scope.detail);
        usr.then(function (response) {
        });
    }

    $scope.createDetail = function(){
        var det = $http.post('/Detail/Create/', $scope.detail);
        det.then(function(response){
        });
    }

    $scope.createResult = function(){
        var res = $http.post('/Result/Create/', $scope.result);
        res.then(function(response){
        });
    }

    $scope.showDetails = function(){
        $window.location.href = '/detailPage.html';
    }

});

