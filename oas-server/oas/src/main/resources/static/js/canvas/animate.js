/**
 * 动画
 */
 function moveElement() {
	 //elementID, 
	 var final_x=0 
	 var final_y=0 
	 //interval
	 var elementID="picLi_10"
        if (!document.getElementById) {
        	
            return false;
        }
        if (!document.getElementById(elementID)) {
        	
            return false;
        }
        var elem = document.getElementById(elementID);
        if (elem.movement) {//先判断是否在运动，先结束上次运行的这段函数
        	
            //clearTimeout(elem.movement);//clearTimeout() 方法可取消由 setTimeout() 方法设置的 timeout。
            cancelAnimationFrame(elem.movement);
        }
        if (!elem.style.left) {
            elem.style.left = "0px";
        }
        if (!elem.style.top) {
            elem.style.top = "0px";
        }
        var xpos = parseInt(elem.style.left);
        var ypos = parseInt(elem.style.top);
        if (xpos == final_x && ypos == final_y) {
        	console.log(222)
            moveing = false;
            return true;
        }
        if (xpos < final_x) {
            var dist = Math.ceil((final_x - xpos) / 5);//每运动一次的数值，返回大于等于参数x的最小整数,即对浮点数向上取整
            xpos = xpos + dist;
        }
        if (xpos > final_x) {
            var dist = Math.ceil((xpos - final_x) / 5);
            xpos = xpos - dist;
        }
        if (ypos < final_y) {
            var dist = Math.ceil((final_y - ypos) / 5);
            ypos = ypos + dist;
        }
        if (ypos > final_y) {
            var dist = Math.ceil((ypos - final_y) / 5);
            ypos = ypos - dist; 
        }
        elem.style.left = xpos + "px";
        elem.style.top = ypos + "px";
       //console.log("left",elem.style.left)
        //var repeat = "moveElement('" + elementID + "'," + final_x + "," + final_y + "," + interval + ")";
        //elem.movement = setTimeout(repeat, interval);//再次运行
       elem.movement=requestAnimationFrame( moveElement)
    }
    function classNormal(ibannerIndex) {
        var btnList = getEvent('ibanner_btn' + ibannerIndex).getElementsByTagName('span');
        for (var i = 0; i < btnList.length; i++) {
            btnList[i].className = 'normal';
        }
    }
    function addBtn(ibannerIndex) {
        if (!getEvent('ibanner' + ibannerIndex) || !getEvent('ibanner_pic' + ibannerIndex)) {
            return;
        }
        var picList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('a');
        if (picList.length == 0) {
            return;
        }
        var btnBox = document.createElement('div');
        btnBox.setAttribute('id', 'ibanner_btn' + ibannerIndex);
        btnBox.setAttribute('class', 'ibanner_btn');
        var SpanBox = '';
        for (var i = 1; i <= picList.length; i++) {
            var spanList = '<span class="normal">' + i + '</span>';
            SpanBox += spanList;
        }
        btnBox.innerHTML = SpanBox;
        getEvent('ibanner' + ibannerIndex).appendChild(btnBox);
        getEvent('ibanner_btn' + ibannerIndex).getElementsByTagName('span')[0].className = 'current';
        for (var m = 0; m < picList.length; m++) {
            var attributeValue = 'picLi_' + ibannerIndex + m;
            picList[m].setAttribute('id', attributeValue);
        }
    }
    var autoKey = false;
    function iBanner(ibannerIndex) {
        if (!getEvent('ibanner' + ibannerIndex) || !getEvent('ibanner_pic' + ibannerIndex) || !getEvent('ibanner_btn' + ibannerIndex)) {
            return;
        }
        getEvent('ibanner' + ibannerIndex).onmouseover = function () {//鼠标移动到图片执行代码
            autoKey = true;
        };
        getEvent('ibanner' + ibannerIndex).onmouseout = function () {//鼠标移出图片执行代码
            autoKey = false;
        };
        var btnList = getEvent('ibanner_btn' + ibannerIndex).getElementsByTagName('span');
        var picList = getEvent('ibanner_pic' + ibannerIndex).getElementsByTagName('a');
        if (picList.length == 1) {
            return;
        }
        picList[0].style.zIndex = '2';
        for (var m = 0; m < btnList.length; m++) {
            btnList[m].onmouseover = function () {
                for (var n = 0; n < btnList.length; n++) {
                    if (btnList[n].className == 'current') {
                        var currentNum = n;
                    }
                }
                classNormal(ibannerIndex);
                picZ(ibannerIndex);
                console.log("1",this)
                this.className = 'current';
                picList[currentNum].style.zIndex = '2';
                var z = this.childNodes[0].nodeValue - 1;
                picList[z].style.zIndex = '3';
                if (currentNum != z) {
                    picList[z].style.left = '650px';
                    moveElement('picLi_' + ibannerIndex + z, 0, 0, 10);
                }
            }
        }
    }
    function addLoadEvent(func) {
        var oldonload = window.onload;
        if (typeof window.onload != 'function') {
            window.onload = func;
        }
        else {
            window.onload = function () {//文档加载完处理
                oldonload();
                func();
            }
        }
    }