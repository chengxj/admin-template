var Login = function () {
    var runSetDefaultValidation = function () {
        $.validator.setDefaults({
            errorElement: "span", // contain the error msg in a small tag
            errorClass: 'help-block',
            errorPlacement: function (error, element) { // render error placement for each input type
                if (element.attr("type") == "radio" || element.attr("type") == "checkbox") { // for chosen elements, need to insert the error after the chosen container
                    error.insertAfter($(element).closest('.form-group').children('div').children().last());
                } else if (element.attr("name") == "card_expiry_mm" || element.attr("name") == "card_expiry_yyyy") {
                    error.appendTo($(element).closest('.form-group').children('div'));
                } else {
                    error.insertAfter(element);
                    // for other inputs, just perform default behavior
                }
            },
            ignore: ':hidden',
            highlight: function (element) {
                $(element).closest('.help-block').removeClass('valid');
                // display OK icon
                $(element).closest('.form-group').removeClass('has-success').addClass('has-error').find('.symbol').removeClass('ok').addClass('required');
                // add the Bootstrap error class to the control group
            },
            unhighlight: function (element) { // revert the change done by hightlight
                $(element).closest('.form-group').removeClass('has-error');
                // set error class to the control group
            },
            success: function (label, element) {
                label.addClass('help-block valid');
                // mark the current input as valid and display OK icon
                $(element).closest('.form-group').removeClass('has-error');
            },
            highlight: function (element) {
                $(element).closest('.help-block').removeClass('valid');
                // display OK icon
                $(element).closest('.form-group').addClass('has-error');
                // add the Bootstrap error class to the control group
            },
            unhighlight: function (element) { // revert the change done by hightlight
                $(element).closest('.form-group').removeClass('has-error');
                // set error class to the control group
            }
        });
    };
    var runLoginValidator = function () {
        var form = $('.form-login');
        var errorHandler = $('.errorHandler', form);
        form.validate({
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            },
            submitHandler: function (form) {
                errorHandler.hide();
                form.submit();
            },
            invalidHandler: function (event, validator) { //display error alert on form submit
                errorHandler.show();
            },
            ignore:[]
        });
    };
    var focusUsername = function() {
        $("#username").focus();
    }
    return {
        //main function to initiate template pages
        init: function () {
            runSetDefaultValidation();
            runLoginValidator();
            focusUsername();
        }
    };
}();

function login() {
    if($("#login-form").valid()) {
        $.ajax({
            url : 'auth/login',
            dataType : 'json',
            type : 'post',
            data : {
                username : $('#username').val(),
                password : $('#password').val()
            },
            success : function(data) {
                $("#error-msg").hide();
                $("#success-msg").show();
                $.cookie("accessToken", data.accessToken);
                $.cookie("refreshToken", data.refreshToken);
                $.cookie("sessionSecret", data.sessionSecret);
                window.location.href = "index.html";
            },
            error : function(data) {
                $("#success-msg").hide();
                $("#error-msg").html(data.responseJSON.message).show();
            }
        });
    }
}
$(function() {
    Login.init();
    $("#loginBtn").click(function() {
        login();
    });
    $("#password, #username").keydown(function(event) {
        if (event.keyCode == '13') {
            login();
        }
    });
});