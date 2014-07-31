
var App = function () {
    var open = true;
    var runSideMenu = function() {
        $(".has-submenu").click(function(e) {
            $('.sidebar-group li').removeClass('active');
            $(this).closest('li').addClass('active');
        });
      };
    var runToTop = function() {
        $(window).scroll(function () {
            if ($(this).scrollTop() < 100) {
                $('#totop').fadeOut();
            } else {
                $('#totop').fadeIn();
            }
        });
        $('#totop').on('click', function () {
            $('html, body').animate({scrollTop: 0}, 'fast');
            return false;
        });
    };
    var runResize = function() {
        $(window).bind("load resize", function () {
            var winHeight = $(window).height();
            var docHeight = $(document).height();
//            if(winHeight > docHeight) {
//                docHeight = winHeight;
//            }
            $("#sidebar").css('min-height', docHeight);
            $("#content").css('min-height', docHeight);
        });
    };

    var runHeight = function() {
        var winHeight = $(window).height();
        var docHeight = $(document).height();
        $("#sidebar").css('min-height', winHeight);
        $("#content").css('min-height', winHeight);
    };

    var runEnscroll = function() {
        $('.scrollbox').enscroll({
            verticalTrackClass: 'scrollbar-track',
            verticalHandleClass: 'scrollbar-handle'
        });
    };

    return {
        init: function () {
            runSideMenu();
            runToTop();
            runResize();
            runEnscroll();
        },
        runHeight : function() {
            runHeight();
        },
        // wrapper function to  block element(indicate loading)
        blockUI: function (el, centerY) {
            var el = jQuery(el);
            el.block({
                overlayCSS: {
                    backgroundColor: '#fff'
                },
                message: '<img src="assets/img/ajax-loader.gif" />',
                css: {
                    border: 'none',
                    color: '#333',
                    background: 'none'
                }
            });
//            window.setTimeout(function () {
//                el.unblock();
//            }, 1000);
//            e.preventDefault();
        },

        // wrapper function to  un-block element(finish loading)
        unblockUI: function (el) {
            jQuery(el).unblock({
                onUnblock: function () {
                    //jQuery(el).removeAttr("style");
                }
            });
        }
    };

}();