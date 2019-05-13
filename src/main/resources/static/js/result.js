var app = angular.module('ResultApp', []);
app.controller('ResultCtrl', function($scope,$http,$window) {

    $scope.result = {};
    $scope.results = {};
    $scope.username = $window.sessionStorage.getItem("username");
    $scope.isLoading = $window.sessionStorage.getItem("isLoading");
    $scope.account = {};

    $scope.getResults = function () {
        $scope.isLoading = false;
        var res = $http.get('/Result/getResult/'+ $scope.username);
        res.then(function (response) {
            $scope.result = response.data;
            $scope.findAccountByUsername();
        });
    }

    $scope.showDetails = function(){
        $window.location.href = '/detailPage.html';
    }

    $scope.findAccountByUsername = function(){
        $scope.account.username = $window.sessionStorage.getItem("AccUsername");
        var acc = $http.get('/account/findAccountByUsername/' + $scope.account.username);
        acc.then(function (response) {
            $scope.account = response.data;
            $scope.account.history.push($scope.result.id);
            $scope.updateAccount();
        });
    }

    $scope.updateAccount = function(){
        var acc = $http.post('/account/update/', $scope.account);
        acc.then(function(response){

        });
    }
});