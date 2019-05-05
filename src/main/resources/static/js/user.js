var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";
    $scope.detail = {};
    $scope.result = {};
    $scope.isLoading = false;

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
        $scope.isLoading = true;
        var usr = $http.get('/User/analyzeButton/' + $scope.username);
        usr.then(function (response) {
            $window.sessionStorage.setItem("username",$scope.username);
            $window.sessionStorage.setItem("isLoading",$scope.isLoading);
            $scope.username = $window.sessionStorage.getItem("username");//session
            $scope.detail.username = $scope.username;
            $window.location.href = '/resultPage.html';
            //$scope.isLoading = false;
            $scope.createDetail($scope.detail);
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

});

