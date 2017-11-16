$(document).ready(function() {

    window.fbAsyncInit = function() {
        FB.init({
            appId: '353579634835412',
            xfbml: true,
            version: 'v2.3'
        });


        $('#fblogin').click(function(e) {
            e.preventDefault();
            FB.getLoginStatus(function(response) {

                /*directly login*/
                if (response.status === 'connected') {
                    console.log('Logged in.');
                    FB.api('/me', function(response) {
                        var request = $.ajax({
                            url: "fb_login.php",
                            type: "POST",
                            data: {
                                email: response.email,
                            },
                            dataType: "text"
                        });
                        request.done(function(data) {
                            console.log(data);
                            window.location.replace(data);
                        });
                        request.fail(function(jqXHR, textStatus) {
                            alert("Request failed: " + textStatus);
                        });
                    });
                } else {
                    /*register and then login*/
                    FB.login(function(response) {
                        if (response.status === 'connected') {
                            console.log('register and login success');
                            FB.api('/me', function(response) {
                                var request = $.ajax({
                                    url: "fb_register.php",
                                    type: "POST",
                                    data: {
                                        email: response.email,
                                        username: response.name,
                                        pwd: null
                                    },
                                    dataType: "text"
                                });
                                request.done(function(data) {
                                    console.log(data);
                                    window.location.replace(data);
                                });
                                request.fail(function(jqXHR, textStatus) {
                                    alert("Request failed: " + textStatus);
                                });
                            });
                        } else {
                            console.log('register and login error');
                        }

                    }, {
                        scope: 'email'
                    });
                }
            });
        });
    };



});
