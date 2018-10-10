/**
 * 菜单处理
 */
$(function() {
	
    $('#side-menu').metisMenu();   
    //固定菜单栏
    $(function() {
        $('.sidebar-collapse').slimScroll({
            height: '100%',
            railOpacity: 0.9,
            alwaysVisible: false
        });
    });

    // 菜单切换
    $('.navbar-minimalize').click(function() {
        $("body").toggleClass("mini-navbar");
        SmoothlyMenu();
    });

    $('#side-menu>li').click(function() {
        if ($('body').hasClass('mini-navbar')) {
            NavToggle();
        }
    });
    $('#side-menu>li li a').click(function() {
        if ($(window).width() < 769) {
            NavToggle();
        }
    });

    $('.nav-close').click(NavToggle);

    //ios浏览器兼容性处理
    if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
        $('#content-main').css('overflow-y', 'auto');
    }

});

$(window).bind("load resize",
function() {
    if ($(this).width() < 769) {
        $('body').addClass('mini-navbar');
        $('.navbar-static-side').fadeIn();
    }
});

function NavToggle() {
    $('.navbar-minimalize').trigger('click');
}

function SmoothlyMenu() {
    if (!$('body').hasClass('mini-navbar')) {
        $('#side-menu').hide();
        setTimeout(function() {
            $('#side-menu').fadeIn(500);
        },
        100);
    } else if ($('body').hasClass('fixed-sidebar')) {
        $('#side-menu').hide();
        setTimeout(function() {
            $('#side-menu').fadeIn(500);
        },
        300);
    } else {
        $('#side-menu').removeAttr('style');
    }
}

/**
 * iframe处理
 */
$(function() {
    //计算元素集合的总宽度
    function calSumWidth(elements) {
        var width = 0;
        $(elements).each(function() {
            width += $(this).outerWidth(true);
        });
        return width;
    }

    //滚动到指定选项卡
    function scrollToTab(element) {
        var marginLeftVal = calSumWidth($(element).prevAll()),
        marginRightVal = calSumWidth($(element).nextAll());
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").outerWidth() < visibleWidth) {
            scrollVal = 0;
        } else if (marginRightVal <= (visibleWidth - $(element).outerWidth(true) - $(element).next().outerWidth(true))) {
            if ((visibleWidth - $(element).next().outerWidth(true)) > marginRightVal) {
                scrollVal = marginLeftVal;
                var tabElement = element;
                while ((scrollVal - $(tabElement).outerWidth()) > ($(".page-tabs-content").outerWidth() - visibleWidth)) {
                    scrollVal -= $(tabElement).prev().outerWidth();
                    tabElement = $(tabElement).prev();
                }
            }
        } else if (marginLeftVal > (visibleWidth - $(element).outerWidth(true) - $(element).prev().outerWidth(true))) {
            scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        },
        "fast");
    }

    //查看左侧隐藏的选项卡
    function scrollTabLeft() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) { //找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
                while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                    offsetVal += $(tabElement).outerWidth(true);
                    tabElement = $(tabElement).prev();
                }
                scrollVal = calSumWidth($(tabElement).prevAll());
            }
        }
        $('.page-tabs-content').animate({
            marginLeft: 0 - scrollVal + 'px'
        },
        "fast");
    }

    //查看右侧隐藏的选项卡
    function scrollTabRight() {
        var marginLeftVal = Math.abs(parseInt($('.page-tabs-content').css('margin-left')));
        // 可视区域非tab宽度
        var tabOuterWidth = calSumWidth($(".content-tabs").children().not(".menuTabs"));
        //可视区域tab宽度
        var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
        //实际滚动宽度
        var scrollVal = 0;
        if ($(".page-tabs-content").width() < visibleWidth) {
            return false;
        } else {
            var tabElement = $(".menuTab:first");
            var offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) <= marginLeftVal) { //找到离当前tab最近的元素
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            offsetVal = 0;
            while ((offsetVal + $(tabElement).outerWidth(true)) < (visibleWidth) && tabElement.length > 0) {
                offsetVal += $(tabElement).outerWidth(true);
                tabElement = $(tabElement).next();
            }
            scrollVal = calSumWidth($(tabElement).prevAll());
            if (scrollVal > 0) {
                $('.page-tabs-content').animate({
                    marginLeft: 0 - scrollVal + 'px'
                },
                "fast");
            }
        }
    }

    //通过遍历给菜单项加上data-index属性
    $(".menuItem").each(function(index) {
        if (!$(this).attr('data-index')) {
            $(this).attr('data-index', index);
        }
    });

    function menuItem() {
        // 获取标识数据
    	
        var dataUrl = $(this).attr('href'),
        dataIndex = $(this).data('index'),
        menuName = $.trim($(this).text()),
        
        flag = true;
      //alert("111"+dataUrl);
      //alert("dataIndex"+dataIndex);
      //alert("menuname"+menuName);
        console.log(dataUrl);
        if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;

        // 选项卡菜单已存在
        
        $('.menuTab').each(function() {
        	
        	
            if ($(this).data('id') == dataUrl) {
            	//.data（）是获取元素的数据或向其添加数据
                if (!$(this).hasClass('active')) {
                    $(this).addClass('active').siblings('.menuTab').removeClass('active');
                    scrollToTab(this);
                    // 显示tab对应的内容区
                    $('.mainContent .RuoYi_iframe').each(function() {
                        if ($(this).data('id') == dataUrl) {
                        	
                           
                        	$(this).show().siblings('.RuoYi_iframe').hide();//.siblings（）是筛选出同级元素
                             
//                            if(dataUrl=="multiTransfer"){
//                            	//document.getElementById("multiTransfer").contentWindow.initsymbol();
//                            	
//                            	 a="iframeMultiTransfer";
//                            
//                            	//a.window.initsymbol();
//                            	document.getElementById(a).contentWindow.initsymbol();
//                            	//$("#'+a+'")[0].contentWindow.initsymbol();
//                            }
                            return false;
                        }
                    });
                }
                //alert("2");
                flag = false;
                return false;
            }
        });
        // 选项卡菜单不存在
        if (flag) {
        	 //alert("jin111");
            var str = '<a href="javascript:;" class="active menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-times-circle"></i></a>';
            $('.menuTab').removeClass('active');
            $('.menuTabs .page-tabs-content').append(str);
            // 添加选项卡对应的iframe
            var id;
            if(dataUrl=="multiTransfer"){
            	id="iframeMultiTransfer";
            }else{
            	id="iframe"+dataIndex;
            }
           
            var str1 = '<iframe class="RuoYi_iframe" name="iframe' + dataIndex + '" id="' + id + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
            $('.mainContent').find('iframe.RuoYi_iframe').hide().parents('.mainContent').append(str1);
            //var tabProduct = document.getElementById("content-main");
            //tabProduct.find('iframe').hide();
            //alert("2");
           // tabProduct.appendChild(str1);
            //$('.row .mainContent').append(str1);
            //alert("jin添加"+dataUrl);
//           $.modal.loading("数据加载中，请稍后...");
//            
//            $('.mainContent iframe:visible').load(function () {
//            	$.modal.closeLoading();
//            	alert("123");
//            });
           
            // 添加选项卡
            
            scrollToTab($('.menuTab .active'));
            //alert("3");
            
        }
        return false;
    }
    $('.menuItem').on('click', menuItem);

  

    // 关闭选项卡菜单
    function closeTab() {
    	//console.log(111);
        var closeTabId = $(this).parents('.menuTab').data('id');
        var currentWidth = $(this).parents('.menuTab').width();

        // 当前元素处于活动状态
        if ($(this).parents('.menuTab').hasClass('active')) {

            // 当前元素后面有同辈元素，使后面的一个元素处于活动状态
            if ($(this).parents('.menuTab').next('.menuTab').length) {

                var activeId = $(this).parents('.menuTab').next('.menuTab:eq(0)').data('id');
                $(this).parents('.menuTab').next('.menuTab:eq(0)').addClass('active');

                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.RuoYi_iframe').hide();
                        return false;
                    }
                });

                var marginLeftVal = parseInt($('.page-tabs-content').css('margin-left'));
                if (marginLeftVal < 0) {
                    $('.page-tabs-content').animate({
                        marginLeft: (marginLeftVal + currentWidth) + 'px'
                    },
                    "fast");
                }

                //  移除当前选项卡
                $(this).parents('.menuTab').remove();

                // 移除tab对应的内容区
                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }

            // 当前元素后面没有同辈元素，使当前元素的上一个元素处于活动状态
            if ($(this).parents('.menuTab').prev('.menuTab').length) {
                var activeId = $(this).parents('.menuTab').prev('.menuTab:last').data('id');
                $(this).parents('.menuTab').prev('.menuTab:last').addClass('active');
                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == activeId) {
                        $(this).show().siblings('.RuoYi_iframe').hide();
                        return false;
                    }
                });

                //  移除当前选项卡
                $(this).parents('.menuTab').remove();

                // 移除tab对应的内容区
                $('.mainContent .RuoYi_iframe').each(function() {
                    if ($(this).data('id') == closeTabId) {
                        $(this).remove();
                        return false;
                    }
                });
            }
        }
        // 当前元素不处于活动状态
        else {
            //  移除当前选项卡
            $(this).parents('.menuTab').remove();

            // 移除相应tab对应的内容区
            $('.mainContent .RuoYi_iframe').each(function() {
                if ($(this).data('id') == closeTabId) {
                    $(this).remove();
                    return false;
                }
            });
            scrollToTab($('.menuTab.active'));
        }
        return false;
    }

    $('.menuTabs').on('click', '.menuTab i', closeTab);
    //$('.menuTabs').on('click', '.menuTab', closeTab);

    //关闭其他选项卡
    function closeOtherTabs() {
        $('.page-tabs-content').children("[data-id]").not(":first").not(".active").each(function() {
            $('.RuoYi_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').css("margin-left", "0");
    }
    $('.tabCloseOther').on('click', closeOtherTabs);

    //滚动到已激活的选项卡
    function showActiveTab() {
        scrollToTab($('.menuTab.active'));
    }
    $('.tabShowActive').on('click', showActiveTab);

    // 点击选项卡菜单
    function activeTab() {
        if (!$(this).hasClass('active')) {
            var currentId = $(this).data('id');
            // 显示tab对应的内容区
            $('.mainContent .RuoYi_iframe').each(function() {
                if ($(this).data('id') == currentId) {
                    $(this).show().siblings('.RuoYi_iframe').hide();
                    return false;
                }
            });
            $(this).addClass('active').siblings('.menuTab').removeClass('active');
            scrollToTab(this);
        }
    }

    // 点击选项卡菜单
    $('.menuTabs').on('click', '.menuTab', activeTab);

    //刷新iframe
    function refreshTab() {
    	var currentId = $('.page-tabs-content').find('.active').attr('data-id');
    	var target = $('.RuoYi_iframe[data-id="' + currentId + '"]');
        var url = target.attr('src');
        target.attr('src', url).ready();
    }
    
    // 全屏显示
    $('#fullScreen').on('click', function () {
    	fullScreen();
    });
    
    function fullScreen(){
    	var docElm = document.documentElement;
        if(docElm.requestFullscreen) {
            docElm.requestFullscreen();
          }

          //FireFox
          else if(docElm.mozRequestFullScreen) {
            docElm.mozRequestFullScreen();
          }

          //Chrome等
          else if(docElm.webkitRequestFullScreen) {
            docElm.webkitRequestFullScreen();
          }

          //IE11
          else if(elem.msRequestFullscreen) {
            elem.msRequestFullscreen();
          }
    }
    
        
    // 刷新按钮
    $('.tabReload').on('click', refreshTab);

    $('.menuTabs').on('dblclick', '.menuTab', refreshTab);

    // 左移按扭
    $('.tabLeft').on('click', scrollTabLeft);

    // 右移按扭
    $('.tabRight').on('click', scrollTabRight);
    
    // 关闭当前
    $('.tabCloseCurrent').on('click', function () {
    	//alert("关闭");
       $('.page-tabs-content').find('.active i').trigger("click")
    	//.each(function() {
     //   $('.RuoYi_iframe[data-id="' + $(this).data('id') + '"]').remove();
       // $(this).remove();
   // });
    });

    // 关闭全部
    $('.tabCloseAll').on('click', function() {
        $('.page-tabs-content').children("[data-id]").not(":first").each(function() {
            $('.RuoYi_iframe[data-id="' + $(this).data('id') + '"]').remove();
            $(this).remove();
        });
        $('.page-tabs-content').children("[data-id]:first").each(function() {
            $('.RuoYi_iframe[data-id="' + $(this).data('id') + '"]').show();
            $(this).addClass("active");
        });
        $('.page-tabs-content').css("margin-left", "0");
    });
    
});

/** 设置全局ajax超时处理 */
$.ajaxSetup({
    complete: function(XMLHttpRequest, textStatus) {
        if (textStatus == "parsererror") {
        	window.parent.location.href= "login";
//          $.modal.confirm("登陆超时！请重新登陆！", function() {
//            window.location.href = ctx + "login";
//          })
        }
    }
});