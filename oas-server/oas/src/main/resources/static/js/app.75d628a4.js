(function(t){function e(e){for(var a,r,o=e[0],c=e[1],l=e[2],h=0,d=[];h<o.length;h++)r=o[h],s[r]&&d.push(s[r][0]),s[r]=0;for(a in c)Object.prototype.hasOwnProperty.call(c,a)&&(t[a]=c[a]);u&&u(e);while(d.length)d.shift()();return i.push.apply(i,l||[]),n()}function n(){for(var t,e=0;e<i.length;e++){for(var n=i[e],a=!0,o=1;o<n.length;o++){var c=n[o];0!==s[c]&&(a=!1)}a&&(i.splice(e--,1),t=r(r.s=n[0]))}return t}var a={},s={app:0},i=[];function r(e){if(a[e])return a[e].exports;var n=a[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,r),n.l=!0,n.exports}r.m=t,r.c=a,r.d=function(t,e,n){r.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,e){if(1&e&&(t=r(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(r.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)r.d(n,a,function(e){return t[e]}.bind(null,a));return n},r.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(e,"a",e),e},r.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},r.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],c=o.push.bind(o);o.push=e,o=o.slice();for(var l=0;l<o.length;l++)e(o[l]);var u=c;i.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var a=n("c21b"),s=n.n(a);s.a},"1eff":function(t,e){t.exports="data:image/png;base64,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"},"4dcb":function(t,e,n){},"56d7":function(t,e,n){"use strict";n.r(e);n("cadf"),n("551c"),n("097d");var a=n("2b0e"),s=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("index")],1)},i=[],r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("scroller",{attrs:{"on-refresh":t.refresh,"on-infinite":t.infinite}},[n("header",[n("div",{staticClass:"left"},[n("img",{attrs:{src:t.avatar,alt:""}}),n("span",{staticClass:"name"},[t._v(t._s(t.userInfo.nickname))])]),n("div",{staticClass:"right"},[n("div",[n("i"),t._v("\r\n          能量 "+t._s(t.currentEnergy)+"\r\n        ")]),n("div",[n("i"),t._v("\r\n          算力 "+t._s(t.currentPower)+"\r\n        ")])])]),n("div",{staticClass:"map"},[n("div",{staticClass:"energy-block"},t._l(t.energyBallList,function(e,a){return n("div",{key:a,staticClass:"energy-ball flash infinite  animated",style:{top:e.y,left:e.x,width:t.formatSize(e.value),height:t.formatSize(e.value)},on:{click:function(n){t.handleClickEnergy(n,e)}}},[n("img",{attrs:{src:t.energyBall,alt:""}}),n("p",[t._v(t._s(e.value))])])})),n("img",{staticClass:"attendance",attrs:{src:t.attendance},on:{click:t.handleAttendance}}),n("img",{staticClass:"promote",attrs:{src:t.promote},on:{click:t.handlePromote}})]),n("div",{staticClass:"analysis"},[n("div",{staticClass:"title"},[n("h3",[t._v("能量分析")]),n("a",{attrs:{href:"javascript:;"}},[t._v("更多")])]),n("ul",{staticClass:"progress"},[n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手机")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[0]?n("div",{style:{width:t.analysis[0].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[0]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[0].value))]):t._e()]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手表")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[1]?n("div",{style:{width:t.analysis[1].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[1]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[1].value))]):t._e()]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("家电")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[2]?n("div",{style:{width:t.analysis[2].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[2]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[2].value))]):t._e()])])]),n("div",{staticClass:"consult"},[n("div",{staticClass:"title"},[n("h3",[t._v("OASES咨询")]),n("a",{attrs:{href:"javascript:;"}},[t._v("更多")])]),n("p",{staticClass:"tips"},[t._v("\r\n        关于OAS你可以了解更多\r\n      ")]),n("div",{staticClass:"news-list"},[n("ul",t._l(t.articleList,function(e,a){return n("li",{key:a},[n("a",{attrs:{href:e.newsLink,target:"_blank"}},[n("div",{staticClass:"left"},[n("p",[t._v(t._s(e.title))]),n("p",[t._v(t._s(e.summary))])]),n("img",{attrs:{src:e.imageLink,alt:""}})])])}))])]),t.isShowNewsTip?n("div",{staticClass:"news-tips"},[t._v("Loading...")]):t._e(),t.isShowNoNews?n("div",{staticClass:"news-tips"},[t._v("No more message")]):t._e(),n("div",{staticClass:"bottom"},[n("img",{attrs:{src:t.bottom,alt:""}})]),t.isShowMask?n("div",{staticClass:"mask"},[n("div",{staticClass:"content"},[n("img",{attrs:{src:t.attendanceSuccess,alt:""}}),n("p",{staticClass:"tips"},[t._v(t._s(t.attendanceMsg.msg))]),t.isShowSuccessMsg?n("p",{staticClass:"info"},[t._v("恭喜您获得"+t._s(t.attendanceMsg.energy)+"点能量,"+t._s(t.attendanceMsg.power)+"点算力")]):t._e(),n("button",{on:{click:t.handleAttendanceConfirm}},[t._v("确认")])])]):t._e()]),t.isShowToast?n("div",{staticClass:"toast"},[t._v(t._s(t.toastMsg))]):t._e()],1)},o=[],c=(n("ac6a"),n("f3e2"),n("6d67"),n("8afe")),l=function(t,e){var n=e-t,a=Math.random(),s=t+Math.round(a*n);return s},u=n("cf05"),h=n("b6e5"),d=n("6715"),f=n("1eff"),p=n("61ab"),g=n("c0c9"),v={name:"index",data:function(){return{avatar:u,attendance:h,attendanceSuccess:p,promote:d,bottom:f,energyBall:g,width:"80%",isShowMask:!1,isShowSuccessMsg:!1,isShowToast:!1,isShowNewsTip:!1,isShowNoNews:!1,energyBallList:[],currentEnergy:0,currentPower:0,page:1,newsTotal:1,articleList:[],analysis:"",analysisCount:0,tempArr:[],toastMsg:"提示信息",attendanceMsg:{msg:"签到成功",energy:0,power:0},userInfo:{avatar:"",nickname:""}}},created:function(){this.getEnergyBall(),this.getCurrentEnergy(),this.getCurrentPower(),this.getEnergyAnalysis(),this.getArticleList(),this.getUserInfo()},filters:{},methods:{getArticleList:function(){var t=this.page;this.loadArticleList(t),this.isShowNewsTip=!0},loadArticleList:function(t){var e=this,n=new FormData;n.append("pageNum",t),n.append("pageSize","3"),this.$axios.post("/energyPoint/inquireNews",n).then(function(t){var n=t.data.data;e.newsTotal=n.data.total,"无更多数据"==n.msg?(e.isShowNewsTip=!1,e.isShowNoNews=!0,e.articleList=Object(c["a"])(e.articleList).concat(Object(c["a"])(n.data.rows))):e.articleList=Object(c["a"])(e.articleList).concat(Object(c["a"])(n.data.rows))}).catch(function(t){console.log(t)})},infinite:function(t){this.page+=1;var e=this.page,n=this.newsTotal,a=Math.ceil(n/3);e<=a&&(this.isShowNewsTip=!0,this.loadArticleList(e),setTimeout(function(){t()},1e3))},handleAttendance:function(){var t=this;this.$axios.post("/energyPoint/checkin").then(function(e){var n=e.data;console.log(n),0==n.code?(t.currentEnergy+=n.data.newEnergyPoint,t.currentPower+=n.data.newPower,t.attendanceMsg.energy=n.data.newEnergyPoint,t.attendanceMsg.power=n.data.newPower,t.isShowSuccessMsg=!0):n.code=10012,t.isShowSuccessMsg=!1,t.attendanceMsg.msg=n.message,t.isShowMask=!0})},handleAttendanceConfirm:function(){this.isShowMask=!1},getUserInfo:function(){var t=this;this.$axios.post("/userCenter/inquireUserInfo").then(function(e){var n=e.data.data;t.userInfo.nickname=n.nickname})},handlePromote:function(){console.log("调用安卓"),window.Android.startLiftComputingPower()},getEnergyBall:function(){var t=this;this.$axios.post("/energyPoint/inquireEnergyBall").then(function(e){var n=e.data.data;console.log(n.energyBallList),t.energyBallList=n.energyBallList.map(function(e){var n=t.randomPoint();return e.x=n.x/75+"rem",e.y=n.y/75+"rem",e})})},getCurrentEnergy:function(){var t=this;this.$axios.post("/energyPoint/inquireEnergyPoint").then(function(e){var n=e.data.data;t.currentEnergy=n})},getCurrentPower:function(){var t=this;this.$axios.post("/computingPower/inquirePower").then(function(e){var n=e.data.data;t.currentPower=n})},getEnergyAnalysis:function(){var t=this;this.$axios.post("/energyPoint/inquireEnergyPointByCategory").then(function(e){var n=e.data.data;t.analysis=n,n.forEach(function(e){t.analysisCount+=e.value})})},formatSize:function(t){return t>=100?"1rem":t>=50?64/75+"rem":t>=0?52/75+"rem":void 0},handleClickEnergy:function(t,e){var n=this;console.log(e),console.log(t);var a=e.value;if(a<50)this.Toast("能量暂不可收取");else{var s=t.currentTarget;this.$axios.post("/energyPoint/takeEnergyBall",{ballId:e.uuid}).then(function(t){var e=t.data;console.log(e),0==e.code?(s.classList.add("fadeOutUp"),s.classList.remove("flash"),s.classList.remove("infinite"),n.getCurrentEnergy(),n.getCurrentPower()):n.Toast(e.message)})}},randomPoint:function(){var t={x:l(20,650),y:l(50,550)};if(0==this.tempArr.length)return this.tempArr.push(t),t;for(var e=this.tempArr.length,n=0;n<e;n++)if(Math.abs(t.x-this.tempArr[n].x)<75&&Math.abs(t.y-this.tempArr[n].y)<75)return this.randomPoint();return this.tempArr.push(t),t},refresh:function(t){this.tempArr=[],this.getEnergyBall(),this.getCurrentEnergy(),this.getCurrentPower(),this.getEnergyAnalysis(),this.getUserInfo(),setTimeout(function(){t()},1e3)},Toast:function(t,e){var n=this;this.toastMsg=t,this.isShowToast=!0,setTimeout(function(){n.isShowToast=!1},e||1500)}}},m=v,y=(n("c464"),n("2877")),w=Object(y["a"])(m,r,o,!1,null,"760c14c4",null);w.options.__file="Index.vue";var A=w.exports,C={name:"app",components:{Index:A}},b=C,E=(n("034f"),Object(y["a"])(b,s,i,!1,null,null,null));E.options.__file="App.vue";var P=E.exports,L=(n("499a"),n("f5df"),n("4dcb"),n("77ed"),n("bc3a")),x=n.n(L),B=n("1c67"),S=n.n(B);a["a"].use(S.a);var I=null;window.Android&&(I=window.Android.getToken()),x.a.defaults.baseURL="http://18.219.19.160:8080/api/v1",x.a.interceptors.request.use(function(t){return t.headers["Content-type"]="application/json;charset=UTF-8",t.headers["token"]=I||"99e04e02-fb5c-4f65-a6cf-a93006c351b6",t}),a["a"].prototype.$axios=x.a,a["a"].config.productionTip=!1,new a["a"]({render:function(t){return t(P)}}).$mount("#app")},"61ab":function(t,e,n){t.exports=n.p+"img/attendance.d1a16f79.png"},6715:function(t,e,n){t.exports=n.p+"img/promote_btn.9af813bc.png"},"6ac9":function(t,e,n){},b6e5:function(t,e,n){t.exports=n.p+"img/attendance_btn.51e9408d.png"},c0c9:function(t,e,n){t.exports=n.p+"img/ball.571c6cc8.png"},c21b:function(t,e,n){},c464:function(t,e,n){"use strict";var a=n("6ac9"),s=n.n(a);s.a},cf05:function(t,e,n){t.exports=n.p+"img/logo.82b9c7a5.png"}});
//# sourceMappingURL=app.75d628a4.js.map