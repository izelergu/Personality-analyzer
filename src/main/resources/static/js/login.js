var app = angular.module('logIn', ['vcRecaptcha']);
app.controller("loginCtrl", function ($http, $scope, vcRecaptchaService, $window) {
    var vm = this;
    var i = 0;
    $scope.isAcc = false;
    $scope.gmail= {
        username: "",
        email: ""
    };

    $scope.accountt=[];

    //If the recaptcha value is empty alert error else alert the recaptcha response
    vm.signup = function () {
        if (vcRecaptchaService.getResponse() === "") {
            alert("Please resolve the captcha and submit!")
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
                            $window.location.href = '/homePage.html';
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

        var isPass = {};
        var pass = $http.get('/account/checkPassword/'+ $scope.account.password +'/'+$scope.account.username);
        pass.then(function (response) {
            isPass = response.data;
            if(isPass.response === "true")
                $scope.isAcc = true;
            if(vcRecaptchaService.getResponse() !== "" && $scope.isAcc === true) {
                console.log(vcRecaptchaService.getResponse());
                $window.location.href = '/homePage.html';
                $window.sessionStorage.setItem("AccUsername",$scope.account.username);
            }
            else if($scope.isAcc === false){
                alert("Username or password is incorrect");
            }
        });


    }

});

