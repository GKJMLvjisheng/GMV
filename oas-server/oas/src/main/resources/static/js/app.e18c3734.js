(function(t){function e(e){for(var a,r,o=e[0],l=e[1],c=e[2],g=0,d=[];g<o.length;g++)r=o[g],s[r]&&d.push(s[r][0]),s[r]=0;for(a in l)Object.prototype.hasOwnProperty.call(l,a)&&(t[a]=l[a]);u&&u(e);while(d.length)d.shift()();return i.push.apply(i,c||[]),n()}function n(){for(var t,e=0;e<i.length;e++){for(var n=i[e],a=!0,o=1;o<n.length;o++){var l=n[o];0!==s[l]&&(a=!1)}a&&(i.splice(e--,1),t=r(r.s=n[0]))}return t}var a={},s={app:0},i=[];function r(e){if(a[e])return a[e].exports;var n=a[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,r),n.l=!0,n.exports}r.m=t,r.c=a,r.d=function(t,e,n){r.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,e){if(1&e&&(t=r(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(r.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)r.d(n,a,function(e){return t[e]}.bind(null,a));return n},r.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(e,"a",e),e},r.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},r.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=e,o=o.slice();for(var c=0;c<o.length;c++)e(o[c]);var u=l;i.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var a=n("c21b"),s=n.n(a);s.a},"1eff":function(t,e){t.exports="data:image/png;base64,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"},"4dcb":function(t,e,n){},"56d7":function(t,e,n){"use strict";n.r(e);n("cadf"),n("551c"),n("097d");var a=n("2b0e"),s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("index")],1)},i=[],r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("scroller",{attrs:{"on-refresh":t.refresh,"on-infinite":t.infinite}},[n("header",[n("div",{staticClass:"left"},[n("img",{attrs:{src:t.userInfo.avatar,alt:""}}),n("span",{staticClass:"name"},[t._v(t._s(t.userInfo.nickname))])]),n("div",{staticClass:"right"},[n("div",[n("i"),t._v("\r\n          能量 "+t._s(t.currentEnergy)+"\r\n        ")]),n("div",[n("i"),t._v("\r\n          算力 "+t._s(t.currentPower)+"\r\n        ")])])]),n("div",{staticClass:"map"},[n("div",{staticClass:"energy-block"},[t._l(t.energyBallList,function(e,a){return n("div",{key:a,staticClass:"energy-ball flash infinite animated  ",style:{top:e.y,left:e.x},on:{click:function(n){t.handleClickEnergy(n,e)}}},[n("img",{style:{width:t.formatSize(e.value),height:t.formatSize(e.value)},attrs:{src:t.energyBall,alt:""}}),n("p",[t._v(t._s(e.value))]),n("div",[e.generate?n("span",{style:{color:"#006600"}},[e.generate?n("font",{attrs:{size:"1px"}},[t._v(t._s(e.name))]):t._e()],1):n("span",[n("font",{attrs:{size:"1px"}},[t._v(t._s(e.name))])],1)])])}),t._l(t.walkEnergyBallList,function(e,a){return n("div",{key:a+t.energyBallList.length,staticClass:"energy-ball flash infinite animated  ",style:{top:e.y,left:e.x},on:{click:function(n){t.handleClickWalkEnergy(n,e)}}},[n("img",{style:{width:t.formatSizeWalk(e.value),height:t.formatSizeWalk(e.value)},attrs:{src:t.energyBall,alt:""}}),n("p",[t._v(t._s(e.value))]),n("div",[n("i"),e.generate?n("span",{style:{color:"#006600"}},[e.generate?n("font",{attrs:{size:"1px"}},[t._v(t._s(e.name))]):t._e()],1):n("span",[n("font",{attrs:{size:"1px"}},[t._v(t._s(e.name))])],1)])])})],2),n("img",{staticClass:"attendance",attrs:{src:t.attendance},on:{click:t.handleAttendance}}),n("img",{staticClass:"promote",attrs:{src:t.promote},on:{click:t.handlePromote}}),n("img",{staticClass:"miner",attrs:{src:t.miner},on:{click:t.handleMiner}})]),n("div",{staticClass:"analysis"},[n("div",{staticClass:"title"},[n("h3",[t._v("能量分析")]),n("a",{attrs:{href:"javascript:;"}},[t._v(t._s(t.datetime))])]),n("ul",{staticClass:"progress"},[n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手机")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[0]?n("div",{style:{width:t.formatWalkAnalysis(t.analysis[0].value,t.analysis[0].maxValue)}}):t._e()]),t.analysis[0]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[0].value))]):t._e(),n("span",{staticClass:"count1"},[t._v("积分")])]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("计步")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[1]?n("div",{style:{width:t.formatWalkAnalysis(t.analysis[1].value,t.analysis[1].maxValue)}}):t._e()]),t.analysis[1]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[1].value))]):t._e(),n("span",{staticClass:"count1"},[t._v("步")])]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手表")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[2]?n("div",{style:{width:t.formatWalkAnalysis(t.analysis[2].value,t.analysis[2].maxValue)}}):t._e()]),t.analysis[2]?n("span",{staticClass:"count"},[t._v("（即将上线）")]):t._e()]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("家电")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[3]?n("div",{style:{width:t.formatWalkAnalysis(t.analysis[3].value,t.analysis[3].maxValue)}}):t._e()]),t.analysis[3]?n("span",{staticClass:"count"},[t._v("（即将上线）")]):t._e()])])]),n("div",{staticClass:"consult"},[n("div",{staticClass:"title"},[n("h3",[t._v("OASES资讯")]),n("a",{attrs:{href:"javascript:;"}},[t._v("更多")])]),n("p",{staticClass:"tips"},[t._v("\r\n        关于OAS你可以了解更多\r\n      ")]),n("div",{staticClass:"news-list"},[n("ul",t._l(t.articleList,function(e,a){return n("li",{key:a},[n("a",{attrs:{href:e.newsLink,target:"_blank"}},[n("div",{staticClass:"left"},[n("p",[t._v(t._s(e.title))]),n("p",[t._v(t._s(e.summary))])]),n("img",{attrs:{src:e.imageLink,alt:""}})])])}))])]),t.isShowNewsTip?n("div",{staticClass:"news-tips"},[t._v("加载中...")]):t._e(),n("div"),n("div",{staticClass:"bottom"},[n("img",{attrs:{src:t.bottom,alt:""}})]),t.isShowMask?n("div",{staticClass:"mask"},[n("div",{staticClass:"content"},[n("img",{attrs:{src:t.attendanceSuccess,alt:""}}),n("p",{staticClass:"tips"},[t._v(t._s(t.attendanceMsg.msg))]),t.isShowSuccessMsg?n("p",{staticClass:"info "},[t._v("恭喜您获得"+t._s(t.attendanceMsg.msgall))]):t._e(),n("button",{on:{click:t.handleAttendanceConfirm}},[t._v("确认")])])]):t._e()]),t.isShowToast?n("div",{staticClass:"toast"},[t._v(t._s(t.toastMsg))]):t._e()],1)},o=[],l=(n("6d67"),n("96cf"),n("3040")),c=(n("28a5"),n("8afe")),u=(n("f3e2"),function(t,e){var n=e-t,a=Math.random(),s=t+Math.round(a*n);return s}),g=n("1157"),d=n.n(g),f=n("cf05"),h=n("b6e5"),v=n("6715"),p=n("1eff"),y=n("61ab"),m=n("c0c9"),w=n("cbc6"),A=n("a9fa"),b={name:"index",data:function(){return{avatar:f,attendance:h,attendanceSuccess:y,promote:v,bottom:p,energyBall:m,infoIamge:w,miner:A,width:"80%",isShowMask:!1,isShowSuccessMsg:!1,isShowToast:!1,isShowNewsTip:!1,energyBallList:[],walkEnergyBallList:[],currentEnergy:0,currentPower:0,page:1,newsTotal:1,articleList:[],newsLength:0,datetime:"",analysis:"",tempArr:[],energyMaxValue:0,toastMsg:"提示信息",attendanceMsg:{msg:"签到成功",msgall:""},userInfo:{avatar:"",nickname:""}}},beforeMount:function(){var t=this;function e(){t.tempArr=[],t.energyBallList=[],t.walkEnergyBallList=[],t.getBallAndAnalysis()}setInterval(e,3e5)},created:function(){this.getCurrenttime(),this.getBallAndAnalysis(),this.getCurrentEnergy(),this.getCurrentPower(),this.getArticleList(),this.getUserInfo(),window.skipRefresh=this.skipRefresh,this.location()},filters:{},methods:{getMaxValue:function(){return this.$axios.post("/energyPoint/pointBallMaxValue")},getStep:function(){var t=window.Android.getTodaySteps();return console.log(t),t},getArticleList:function(){var t=this.page;this.loadArticleList(t),this.isShowNewsTip=!0},loadArticleList:function(t){var e=this,n=new FormData;n.append("pageNum",t),n.append("pageSize","3"),this.$axios.post("/energyPoint/inquireNews",n).then(function(t){var n=t.data.data;e.newsTotal=n.data.total,"无更多数据"==n.msg?(e.isShowNewsTip=!1,e.articleList=Object(c["a"])(e.articleList).concat(Object(c["a"])(n.data.rows)),console.log(JSON.stringify(n.data.rows)),e.newsLength=n.data.rows.length):(e.articleList=Object(c["a"])(e.articleList).concat(Object(c["a"])(n.data.rows)),console.log(JSON.stringify(n.data.rows)),e.newsLength=n.data.rows.length)}).catch(function(t){console.log(t)})},infinite:function(t){var e=this;this.page+=1,console.log(this.page);var n=this.page,a=this.newsTotal,s=Math.ceil(a/3);n<=s?(this.isShowNewsTip=!0,this.loadArticleList(n),setTimeout(function(){t()},1e3)):setTimeout(function(){t(!0),e.infinite=void 0},500)},handleAttendance:function(){var t=this;this.$axios.post("/energyPoint/checkin").then(function(e){var n=e.data;console.log(JSON.stringify(n)),0==n.code?(0==n.data.newEnergyPoint?(t.currentPower+=n.data.newPower,t.currentEnergy+=n.data.newEnergyPoint,t.attendanceMsg.msgall=n.data.newPower+"点算力",t.isShowSuccessMsg=!0):0==n.data.newPower?(t.currentPower+=n.data.newPower,t.currentEnergy+=n.data.newEnergyPoint,t.attendanceMsg.msgall=n.data.newEnergyPoint+"点能量",t.isShowSuccessMsg=!0):(t.currentPower+=n.data.newPower,t.currentEnergy+=n.data.newEnergyPoint,t.attendanceMsg.msgall=n.data.newEnergyPoint+"点能量,"+n.data.newPower+"点算力",t.isShowSuccessMsg=!0),t.getEnergyAnalysis()):10012==n.code&&(t.isShowSuccessMsg=!1),t.attendanceMsg.msg=n.message,t.isShowMask=!0})},handleAttendanceConfirm:function(){this.isShowMask=!1},getUserInfo:function(){var t=this;this.$axios.post("/userCenter/inquireUserInfo").then(function(e){var n=e.data.data;console.log("用户信息"+JSON.stringify(n));var a="PNG,GIF,JPG,JPEG,BMP,png,gif,jpg,jpeg,bmp,blob",s=n.profile.split("."),i=n.profile.split("-");-1!=a.indexOf(s[s.length-1])||-1!=a.indexOf(i[i.length-1])?t.userInfo.avatar=n.profile:t.userInfo.avatar=w,t.userInfo.nickname=n.nickname})},handlePromote:function(){console.log("调用安卓"),window.Android.startLiftComputingPower()},handleMiner:function(){console.log("调用安卓"),window.Android.startLiftBuyingMiner()},getBallAndAnalysis:function(){var t=Object(l["a"])(regeneratorRuntime.mark(function t(){var e,n,a,s,i,r,o,l,c=this;return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,e={},n={},a=new Array,s=new Array,t.next=7,this.getMaxValue();case 7:return i=t.sent,console.log("maxvalue"+i.data.data),this.energyMaxValue=i.data.data,t.next=12,this.getEnergyBall();case 12:if(e=t.sent,0==e.data.code)for(a=e.data.data.energyBallList.map(function(t){var e=c.randomPoint();return t.x=e.x/75+"rem",t.y=e.y/75+"rem",t.value<c.energyMaxValue?t.generate=!0:t.generate=!1,t}),r=0;r<a.length;r++)0==a[r].value&&(a.splice(r,1),r--);return t.next=16,this.getWalkEnergyBall();case 16:if(n=t.sent,this.getEnergyAnalysis(),o=P(!0),0==n.data.code)for(s=n.data.data.map(function(t){var e=c.randomPoint();return t.x=e.x/75+"rem",t.y=e.y/75+"rem",t.startDate>=o?t.generate=!0:t.generate=!1,t.value>100&&(console.log(t.value),t.value=parseInt(t.value),console.log(t.value)),t}),l=0;l<s.length;l++)0==s[l].value&&(s.splice(l,1),l--);this.energyBallList=a,console.log(this.energyBallList.length+JSON.stringify(this.energyBallList)),this.walkEnergyBallList=s,console.log("222"+JSON.stringify(this.walkEnergyBallList)),t.next=29;break;case 26:t.prev=26,t.t0=t["catch"](0),console.log(t.t0);case 29:case"end":return t.stop()}},t,this,[[0,26]])}));return function(){return t.apply(this,arguments)}}(),getEnergyBall:function(){return this.$axios.post("/energyPoint/inquireEnergyPointBall")},getWalkEnergyBall:function(){var t=this.getStep(),e={};e["date"]=this.datetime,e["stepNum"]=t;var n=new Array;return n.push(e),this.$axios.post("/walkPoint/inquireWalkPointBall",{quota:n})},getCurrentEnergy:function(){var t=this;this.$axios.post("/energyPoint/inquireEnergyPoint").then(function(e){var n=e.data.data;console.log("能量"+n),t.currentEnergy=n})},getCurrentPower:function(){var t=this;this.$axios.post("/computingPower/inquirePower").then(function(e){var n=e.data.data;t.currentPower=n})},getEnergyAnalysis:function(){var t=this;this.$axios.post("/energyPoint/inquireEnergyPointByCategory").then(function(e){var n=e.data.data;console.log("分析"+JSON.stringify(n)),t.analysis=n})},formatSize:function(t){return t>=100?"1rem":t>=50?64/75+"rem":t>=0?52/75+"rem":void 0},formatSizeWalk:function(t){return t>=1e3?85/75+"rem":t>=500?"1rem":t>=100?64/75+"rem":t>=0?52/75+"rem":void 0},formatWalkAnalysis:function(t,e){return t>e?"100%":t/e*100+"%"},handleClickEnergy:function(t,e){var n=this;console.log(JSON.stringify(e)),console.log(t);var a=e.value;if(a<this.energyMaxValue)this.Toast("能量暂不可收取");else{var s=t.currentTarget;this.$axios.post("/energyPoint/takeEnergyPointBall",{ballId:e.uuid}).then(function(t){var e=t.data;console.log(JSON.stringify(e)),0==e.code&&(s.classList.add("fadeOutUp"),s.classList.remove("flash"),s.classList.remove("infinite"),n.getCurrentEnergy(),n.getCurrentPower(),n.getEnergyAnalysis())})}},handleClickWalkEnergy:function(t,e){var n=this,a=e.startDate,s=P(!0);if(a>=s)this.Toast("能量暂不可收取");else{var i=t.currentTarget;this.$axios.post("/walkPoint/takeWalkPointBall",{ballId:e.uuid}).then(function(t){var e=t.data;console.log(JSON.stringify(e)),0==e.code&&(i.classList.add("fadeOutUp"),i.classList.remove("flash"),i.classList.remove("infinite"),n.getCurrentEnergy(),n.getCurrentPower(),n.getEnergyAnalysis())})}},randomPoint:function(){var t={x:u(20,650),y:u(50,550)};if(0==this.tempArr.length)return this.tempArr.push(t),t;for(var e=this.tempArr.length,n=0;n<e;n++)if(Math.abs(t.x-this.tempArr[n].x)<90&&Math.abs(t.y-this.tempArr[n].y)<90)return this.randomPoint();return this.tempArr.push(t),t},refresh:function(t){this.getCurrenttime(),this.tempArr=[],this.energyBallList=[],this.walkEnergyBallList=[],this.getBallAndAnalysis(),this.getCurrentEnergy(),this.getCurrentPower(),this.getUserInfo(),setTimeout(function(){t()},1e3)},skipRefresh:function(){this.getCurrenttime(),this.tempArr=[],this.energyBallList=[],this.walkEnergyBallList=[],this.getBallAndAnalysis(),this.getCurrentEnergy(),this.getCurrentPower(),this.getUserInfo()},removeclass:function(){d()(".energy-ball").addClass("flash"),d()(".energy-ball").addClass("infinite"),d()(".energy-ball").addClass("animated"),d()(".energy-ball").removeClass("fadeOutUp")},Toast:function(t,e){var n=this;this.toastMsg=t,this.isShowToast=!0,setTimeout(function(){n.isShowToast=!1},e||1500)},location:function(){window.addEventListener("pageshow",function(t){setTimeout(function(){t.persisted&&this.skipRefresh()})})},getCurrenttime:function(){var t=P(!0);this.datetime=t}}};function P(t){var e=new Date,n=e.getFullYear(),a=e.getMonth()+1,s=e.getDate(),i=e.getHours(),r=e.getMinutes(),o=e.getSeconds();if(t){var l=n+"-";return a<10&&(l+="0"),l+=a+"-",s<10&&(l+="0"),l+=s,l}return l=n+"-",a<10&&(l+="0"),l+=a+"-",s<10&&(l+="0"),l+=s+" ",i<10&&(l+="0"),l+=i+":",r<10&&(l+="0"),l+=r+":",o<10&&(l+="0"),l+=o,l}var E=b,S=(n("8d02"),n("2877")),L=Object(S["a"])(E,r,o,!1,null,"6b43748e",null);L.options.__file="Index.vue";var B=L.exports,C={name:"app",components:{Index:B}},k=C,x=(n("034f"),Object(S["a"])(k,s,i,!1,null,null,null));x.options.__file="App.vue";var I=x.exports,j=(n("499a"),n("f5df"),n("4dcb"),n("77ed"),n("bc3a")),O=n.n(j),M=n("1c67"),R=n.n(M);a["a"].use(R.a);var W=null;window.Android&&(W=window.Android.getToken()),O.a.defaults.baseURL="https://oas.cascv.com/api/v1",O.a.interceptors.request.use(function(t){return t.headers["Content-type"]="application/json;charset=UTF-8",t.headers["token"]=W||"99e04e02-fb5c-4f65-a6cf-a93006c351b6",t}),a["a"].prototype.$axios=O.a,a["a"].config.productionTip=!1,new a["a"]({render:function(t){return t(I)}}).$mount("#app")},"61ab":function(t,e,n){t.exports=n.p+"img/attendance.d1a16f79.png"},6715:function(t,e,n){t.exports=n.p+"img/promote_btn.9af813bc.png"},"69c9":function(t,e,n){},"8d02":function(t,e,n){"use strict";var a=n("69c9"),s=n.n(a);s.a},a9fa:function(t,e,n){t.exports=n.p+"img/kuangji@2x.9e9771ac.png"},b6e5:function(t,e,n){t.exports=n.p+"img/attendance_btn.51e9408d.png"},c0c9:function(t,e,n){t.exports=n.p+"img/ball.571c6cc8.png"},c21b:function(t,e,n){},cbc6:function(t,e){t.exports="data:image/png;base64,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"},cf05:function(t,e,n){t.exports=n.p+"img/logo.82b9c7a5.png"}});
//# sourceMappingURL=app.e18c3734.js.map