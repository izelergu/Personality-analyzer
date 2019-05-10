var app = angular.module('logIn', ['vcRecaptcha']);
app.controller("loginCtrl", function ($http, $scope, vcRecaptchaService, $window) {
    var vm = this;
    var i = 0;
    var isAcc = false;
    $scope.gmail= {
        username: "",
        email: ""
    };

    $scope.accountList=[];

    //If the recaptcha value is empty alert error else alert the recaptcha resonse
    vm.signup = function () {
        if (vcRecaptchaService.getResponse() === "") {
            alert("Please resolve the captcha and submit!")
        } else {
            alert(vcRecaptchaService.getResponse());
        }
    }

    $scope.onGoogleLogin = function(){
        var params = {
            'clientid':'396978027546-eaik0b1t8ku1vau85peglvp999ffdu7s.apps.googleusercontent.com',
            'cookiepolicy':'single_host_origin',
            'callback': function(response){
                if(response['status']['signed_in']){
                    var request = gapi.client.plus.people.get({
                            'userId':'me'
                        });
                    request.execute(function(resp){
                        $scope.$apply(function(){
                            $scope.gmail.username = resp.displayName;
                            $scope.gmail.email = resp.emails[0].value;
                        });
                    });
                }
            },
            'approvalprompt':'force',
            'scope':'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
        };
        gapi.auth.signIn(params);
    }

    $scope.login = function(){
        var emplog = $http.get('/account/login');
        emplog.then(function (response) {
            $scope.accountList = response.data;
             for(i =0;i<$scope.accountList.length;i++){
                if($scope.accountList[i].username == $scope.account.username && $scope.accountList[i].password == $scope.account.password){
                    isAcc = true;
                    if(vcRecaptchaService.getResponse() !== "" && isAcc == true) {
                        $window.location.href = '/homePage.html';
                    }
                    break;
                }
            }
            if(isAcc == false){
                 alert("Username or password is incorrect");
            }
        });

    }
});

