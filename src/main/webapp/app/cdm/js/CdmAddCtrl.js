function CdmAddCtrl($scope, MessageService, LocationTo) {
    $scope.master = {};
    $scope.dict = angular.copy($scope.master);
    $scope.clickToken = false;

    $scope.save = function () {
        DictService.save($scope.dict, function () {
            $scope.clickToken = true;
            MessageService.saveSuccess();
            LocationTo.path("/sys/dict/list");
        });
    };

    $scope.isUnchanged = function (dict) {
        return angular.equals(dict, $scope.master);
    };
    $('#rootwizard').bootstrapWizard({
        'nextSelector': '.button-next',
        'previousSelector': '.button-previous',
        onTabClick: function (tab, navigation, index, clickedIndex) {
            var total = navigation.find('li').length;
            var current = index + 1;
            // set done steps
            jQuery('li', $('#rootwizard')).removeClass("done");
            var li_list = navigation.find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }

            if (current == 1) {
                $('#rootwizard').find('.button-previous').hide();
            } else {
                $('#rootwizard').find('.button-previous').show();
            }

            if (current >= total) {
                $('#rootwizard').find('.button-next').hide();
                $('#rootwizard').find('.button-submit').show();

            } else {
                $('#rootwizard').find('.button-next').show();
                $('#rootwizard').find('.button-submit').hide();
            }
        },
        onNext: function (tab, navigation, index) {
            var total = navigation.find('li').length;
            var current = index + 1;
            console.log(total)
            console.log(current)
            // set done steps
            jQuery('li', $('#rootwizard')).removeClass("done");
            var li_list = navigation.find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }

            if (current == 1) {
                $('#rootwizard').find('.button-previous').hide();
            } else {
                $('#rootwizard').find('.button-previous').show();
            }

            if (current >= total) {
                $('#rootwizard').find('.button-next').hide();
                $('#rootwizard').find('.button-submit').show();

            } else {
                $('#rootwizard').find('.button-next').show();
                $('#rootwizard').find('.button-submit').hide();
            }
        },
        onPrevious: function (tab, navigation, index) {
            var total = navigation.find('li').length;
            var current = index + 1;
            // set done steps
            jQuery('li', $('#rootwizard')).removeClass("done");
            var li_list = navigation.find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }

            if (current == 1) {
                $('#rootwizard').find('.button-previous').hide();
            } else {
                $('#rootwizard').find('.button-previous').show();
            }

            if (current >= total) {
                $('#rootwizard').find('.button-next').hide();
                $('#rootwizard').find('.button-submit').show();

            } else {
                $('#rootwizard').find('.button-next').show();
                $('#rootwizard').find('.button-submit').hide();
            }
        },
        onTabShow: function (tab, navigation, index) {
            var total = navigation.find('li').length;
            var current = index + 1;
            // set done steps
            jQuery('li', $('#rootwizard')).removeClass("done");
            var li_list = navigation.find('li');
            for (var i = 0; i < index; i++) {
                jQuery(li_list[i]).addClass("done");
            }

            if (current == 1) {
                $('#rootwizard').find('.button-previous').hide();
            } else {
                $('#rootwizard').find('.button-previous').show();
            }

            if (current >= total) {
                $('#rootwizard').find('.button-next').hide();
                $('#rootwizard').find('.button-submit').show();

            } else {
                $('#rootwizard').find('.button-next').show();
                $('#rootwizard').find('.button-submit').hide();
            }
        }
    });
}
CdmAddCtrl.$inject = [ '$scope', 'MessageService', 'LocationTo'];
