function onLoadFunction(){
    gapi.client.setApiKey('AIzaSyAGIhDP8UulrqklrVaJSyULxs2XZUUD0YM');
    gapi.client.load('plus','v1',function(){});
}

window.fbAsyncInit = function() {
    FB.init({
        appId            : '2020634968187025',
        autoLogAppEvents : true,
        xfbml            : true,
        version          : 'v3.0',
        status: true
    });

    FB.getLoginStatus(function(response){
       if(response.status === 'connected') {
           //connected
       }else if(response.status === 'not_authorized'){
           //not auth
       }else{
           //not logged in to facebook
       }
    });

};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));