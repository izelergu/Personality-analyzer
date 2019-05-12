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
    $scope.id = "";
    $scope.password2 = "";

    $scope.pageOpen = function(){
        $scope.account.username = $window.sessionStorage.getItem("AccUsername");
        var usr = $http.get('/account/getAccount/' + $scope.account.username);
        usr.then(function (response) {
            $scope.account = response.data;
            $window.sessionStorage.setItem("AccUsername",$scope.account.username);
        });
    }

    $scope.findResultById = function(id){
        var usr = $http.get('/Result/getResultById/' + id);
        usr.then(function (response) {
            $scope.result = response.data;
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
                $scope.errorMessage = "";
                $window.sessionStorage.setItem("username",$scope.username);
                $window.sessionStorage.setItem("isLoading",$scope.isLoading);
                $scope.username = $window.sessionStorage.getItem("username");//session
                $scope.detail.username = $scope.username;
                $scope.createDetail($scope.detail);
                $scope.isLoading = true;
                $window.location.href = '/resultPage.html';
            }
            else {
                $scope.isLoading = false;
                alert($scope.errorMessage);
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
        if($scope.account.password != $scope.password2){
            alert("Parolalar uyuşmamaktadır !")
        }else {
            var acc = $http.post("/account/create", $scope.account);
            $window.sessionStorage.setItem("AccUsername",$scope.account.username);
            acc.then(function (response) {
                $window.location.href = '/logIn.html';
            });
        }
    }
});

