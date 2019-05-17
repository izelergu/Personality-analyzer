var app = angular.module('UserApp', []);
app.controller('UserCtrl', function($scope,$http,$window) {

    $scope.userList = [];
    $scope.user = {};
    $scope.username = "";
    $scope.userId = "";
    $scope.detail = {};
    $scope.resuld_id = "";
    $scope.resultList = [];
    $scope.isLoading = false;
    $scope.errorMessage = "";
    $scope.account = {};
    $scope.id = "";
    $scope.password2 = "";
    $scope.accountList = [];

    $scope.pageOpen = function(){
        $scope.account.username = $window.sessionStorage.getItem("AccUsername");
        var usr = $http.get('/account/findAccountByUsername/' + $scope.account.username);
        usr.then(function (response) {
            $scope.account = response.data;
            $window.sessionStorage.setItem("AccUsername",$scope.account.username);
            for (var i = 0; i < $scope.account.history.length; i++) {
                $scope.findResultById($scope.account.history[i]);
            }
        });
    }

    $scope.findResultById = function(id){
        var usr = $http.get('/Result/getResultById/' + id);
        usr.then(function (response) {
            $scope.resultList.push(response.data);
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
            $scope.resuld_id = stringResponse.data;
            if($scope.resuld_id != null)
                $window.sessionStorage.setItem("result_id",$scope.resuld_id);
            if($scope.errorMessage === "Başarılı") {
                $scope.errorMessage = "";
                $window.sessionStorage.setItem("username",$scope.username);
                $window.sessionStorage.setItem("isLoading",$scope.isLoading);
                $scope.username = $window.sessionStorage.getItem("username");//session
                $scope.detail.username = $scope.username;
                $window.location.href = '/resultPage.html';
            }
            else {
                $scope.isLoading = false;
                alert($scope.errorMessage);
            }
        });
    }

    $scope.accountCreate = function () {
        if($scope.account.password != $scope.password2){
            alert("Parolalar uyuşmamaktadır !")
        }else {
            var acc1 = $http.get("/account/findAll");
            acc1.then(function (response) {
                $scope.accountList = response.data;
                var i;
                var flag = true;
                for (i = 0; i < $scope.accountList.length; i++) {
                    if($scope.accountList[i].username == $scope.account.username || $scope.accountList[i].email == $scope.account.email){
                        flag = false;
                        alert("Bu kullanıcı adı veya email ile kayıtlı kişi var !");
                    }
                }
                if(flag == true) {
                    $scope.account.history = [];
                    var acc = $http.post("/account/create", $scope.account);
                    $window.sessionStorage.setItem("AccUsername", $scope.account.username);
                    acc.then(function (response) {
                        $window.location.href = '/logIn.html';
                    });
                }
            });
        }
    }

    $scope.logout = function(){
        $window.sessionStorage.setItem("AccUsername","");
        $window.sessionStorage.setItem("username","");
    }
});

