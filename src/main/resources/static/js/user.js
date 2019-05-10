var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";
    $scope.detail = {};
    $scope.result = {};
    $scope.isLoading = false;
    $scope.errorMessage = "";
    $scope.account = {};


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
        var usrResponse = $http.get('/User/analyzeButton/' + $scope.username);
        usrResponse.then(function (response) {
            var stringResponse = response.data;
            $scope.errorMessage = stringResponse.response;
            if($scope.errorMessage === "Başarılı") {
                console.log("girdi")
                $scope.errorMessage = "";
                $window.sessionStorage.setItem("username",$scope.username);
                $window.sessionStorage.setItem("isLoading",$scope.isLoading);
                $scope.username = $window.sessionStorage.getItem("username");//session
                $scope.detail.username = $scope.username;
                $scope.createDetail($scope.detail);
                $scope.isLoading = false;
                $window.location.href = '/resultPage.html';
            }
            else {
                console.log("girmedi")
                $scope.isLoading = false;
            }
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

    $scope.accountCreate = function () {
        var acc = $http.post("/account/create", $scope.account);
        $window.sessionStorage.setItem("AccUsername",$scope.account.username);
        acc.then(function(response) {
            alert(response.data);
        });
    }
});

