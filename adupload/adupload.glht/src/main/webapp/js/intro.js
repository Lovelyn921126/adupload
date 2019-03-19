(function (window, document, jQuery) {
    "use strict";
    var introOptions = jQuery.extend(!![], {}, {
        'skipLabel': '<i class="fa fa-close"></i>',
        'doneLabel': '<i class="fa fa-close"></i>',
        'nextLabel': "下一个<i class='fa fa-angle-right'></i>",
        'prevLabel': "<i class='fa fa-angle-left'></i>上一个",
        'showBullets': ![],
        'hintButtonLabel': '完成',
        'steps': [{
            'intro': "让我们开始新手指引吧"
        }, {
            'element': '.navbar-minimalize',
            'position': 'right',
            'intro': "菜单按钮 <p class='content'>点击图标展开或收起左侧导航菜单</p>"
        }, {
            'element': '#toFaq',
            'position': 'left',
            'intro': '常见问题 <p class="content">收集了一些常见问题及解答</p>'
        }, {
            'element': '#toLogout',
            'position': 'left',
            'intro': '退出 <p class="content">点击这里，您可以安全的退出系统</p>'
        }, {
            'element': '.content-tabs',
            'position': 'bottom',
            'intro': '页签 <p class="content">方便您快速切换页面，小页签，大世界</p>'
        }, {
            'element': '#small-chat',
            'position': 'left',
            'intro': '反馈 <p class="content">如果FAQ中没能帮到您，您可以点击这个反馈问题</p>'
        }, {
            'element': '#toIntro',
            'position': 'left',
            'intro': '新手指引 <p class="content">最后，新手指引在这哈，以后可以在这开启</p>'
        }]
    });
    window.initTour = {
        'startTour': function (attribute) {
            if (typeof this.tour === 'undefined') {
                if (typeof introJs === 'undefined') {
                    return;
                }
                var options = introOptions,
                    $this = this;
                attribute = jQuery('body').css('overflow');
                this.tour = introJs();
                this.tour.onbeforechange = function () {
                    jQuery('body').css('overflow', 'hidden');
                };
                this.tour.oncomplete = function () {
                    jQuery('body').css('overflow', attribute);
                };
                this.tour.onexit = function () {
                    jQuery('body').css('overflow', attribute);
                };
                this.tour.setOptions(options);
                $(".site-tour-trigger").on('click', function () {
                    $this.tour.start();
                });
            }
            if (!(window.localStorage && window.localStorage.getItem('startTour') && attribute !== !![])) {
                this.tour.start();
                window.localStorage.setItem('startTour', !![]);
            }
        },
        'run': function () {
            this.startTour();
        }
    };
}(window, document, jQuery));
$(window).load(function () {
    window.initTour.run();
});