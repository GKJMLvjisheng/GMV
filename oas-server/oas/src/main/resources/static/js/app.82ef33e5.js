(function(t){function e(e){for(var a,i,o=e[0],l=e[1],c=e[2],v=0,h=[];v<o.length;v++)i=o[v],r[i]&&h.push(r[i][0]),r[i]=0;for(a in l)Object.prototype.hasOwnProperty.call(l,a)&&(t[a]=l[a]);u&&u(e);while(h.length)h.shift()();return s.push.apply(s,c||[]),n()}function n(){for(var t,e=0;e<s.length;e++){for(var n=s[e],a=!0,o=1;o<n.length;o++){var l=n[o];0!==r[l]&&(a=!1)}a&&(s.splice(e--,1),t=i(i.s=n[0]))}return t}var a={},r={app:0},s=[];function i(e){if(a[e])return a[e].exports;var n=a[e]={i:e,l:!1,exports:{}};return t[e].call(n.exports,n,n.exports,i),n.l=!0,n.exports}i.m=t,i.c=a,i.d=function(t,e,n){i.o(t,e)||Object.defineProperty(t,e,{enumerable:!0,get:n})},i.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},i.t=function(t,e){if(1&e&&(t=i(t)),8&e)return t;if(4&e&&"object"===typeof t&&t&&t.__esModule)return t;var n=Object.create(null);if(i.r(n),Object.defineProperty(n,"default",{enumerable:!0,value:t}),2&e&&"string"!=typeof t)for(var a in t)i.d(n,a,function(e){return t[e]}.bind(null,a));return n},i.n=function(t){var e=t&&t.__esModule?function(){return t["default"]}:function(){return t};return i.d(e,"a",e),e},i.o=function(t,e){return Object.prototype.hasOwnProperty.call(t,e)},i.p="/";var o=window["webpackJsonp"]=window["webpackJsonp"]||[],l=o.push.bind(o);o.push=e,o=o.slice();for(var c=0;c<o.length;c++)e(o[c]);var u=l;s.push([0,"chunk-vendors"]),n()})({0:function(t,e,n){t.exports=n("56d7")},"034f":function(t,e,n){"use strict";var a=n("c21b"),r=n.n(a);r.a},1633:function(t,e,n){},"1eff":function(t,e){t.exports="data:image/png;base64,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"},"4dcb":function(t,e,n){},"56d7":function(t,e,n){"use strict";n.r(e);n("cadf"),n("551c"),n("097d");var a=n("2b0e"),r=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("index")],1)},s=[],i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"hello"},[n("h1",[t._v(t._s(t.msg))]),t._m(0),n("h3",[t._v("Installed CLI Plugins")]),t._m(1),n("h3",[t._v("Essential Links")]),t._m(2),n("h3",[t._v("Ecosystem")]),t._m(3)])},o=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("p",[t._v("\n    For guide and recipes on how to configure / customize this project,"),n("br"),t._v("\n    check out the\n    "),n("a",{attrs:{href:"https://cli.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("vue-cli documentation")]),t._v(".\n  ")])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",[n("li",[n("a",{attrs:{href:"https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-babel",target:"_blank",rel:"noopener"}},[t._v("babel")])]),n("li",[n("a",{attrs:{href:"https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-eslint",target:"_blank",rel:"noopener"}},[t._v("eslint")])])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",[n("li",[n("a",{attrs:{href:"https://vuejs.org",target:"_blank",rel:"noopener"}},[t._v("Core Docs")])]),n("li",[n("a",{attrs:{href:"https://forum.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("Forum")])]),n("li",[n("a",{attrs:{href:"https://chat.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("Community Chat")])]),n("li",[n("a",{attrs:{href:"https://twitter.com/vuejs",target:"_blank",rel:"noopener"}},[t._v("Twitter")])]),n("li",[n("a",{attrs:{href:"https://news.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("News")])])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("ul",[n("li",[n("a",{attrs:{href:"https://router.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("vue-router")])]),n("li",[n("a",{attrs:{href:"https://vuex.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("vuex")])]),n("li",[n("a",{attrs:{href:"https://github.com/vuejs/vue-devtools#vue-devtools",target:"_blank",rel:"noopener"}},[t._v("vue-devtools")])]),n("li",[n("a",{attrs:{href:"https://vue-loader.vuejs.org",target:"_blank",rel:"noopener"}},[t._v("vue-loader")])]),n("li",[n("a",{attrs:{href:"https://github.com/vuejs/awesome-vue",target:"_blank",rel:"noopener"}},[t._v("awesome-vue")])])])}],l={name:"HelloWorld",props:{msg:String}},c=l,u=(n("8c9a"),n("2877")),v=Object(u["a"])(c,i,o,!1,null,"e2b2ad2e",null);v.options.__file="HelloWorld.vue";var h=v.exports,p=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",[n("header",[n("div",{staticClass:"left"},[n("img",{attrs:{src:t.avatar,alt:""}}),n("span",{staticClass:"name"},[t._v("伍鑫")])]),n("div",{staticClass:"right"},[n("div",[n("i"),t._v("\n        能量 "+t._s(t.currentEnergy)+"\n      ")]),n("div",[n("i"),t._v("\n        算力 "+t._s(t.currentPower)+"\n      ")])])]),n("div",{staticClass:"map"},[n("div",{staticClass:"energy-block"},t._l(t.energyBallList,function(e,a){return n("div",{key:a,staticClass:"energy-ball flash infinite  animated",style:{top:e.y,left:e.x,width:t.formatSize(e.value),height:t.formatSize(e.value)},on:{click:function(n){t.handleClickEnergy(n,e)}}},[n("img",{attrs:{src:t.energyBall,alt:""}}),n("p",[t._v(t._s(e.value))])])})),n("img",{staticClass:"attendance",attrs:{src:t.attendance},on:{click:t.handleAttendance}}),n("img",{staticClass:"promote",attrs:{src:t.promote}})]),n("div",{staticClass:"analysis"},[t._m(0),n("ul",{staticClass:"progress"},[n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手机")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[0]?n("div",{style:{width:t.analysis[0].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[0]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[0].value))]):t._e()]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("手表")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[1]?n("div",{style:{width:t.analysis[1].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[1]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[1].value))]):t._e()]),n("li",[n("i"),n("span",{staticClass:"equipment"},[t._v("家电")]),n("div",{staticClass:"bar"},[n("div"),t.analysis[2]?n("div",{style:{width:t.analysis[2].value/t.analysisCount*100+"%"}}):t._e()]),t.analysis[2]?n("span",{staticClass:"count"},[t._v(t._s(t.analysis[2].value))]):t._e()])])]),n("div",{staticClass:"consult"},[t._m(1),n("p",{staticClass:"tips"},[t._v("\n      关于OAS你可以了解更多\n    ")]),n("div",{staticClass:"news-list"},[n("ul",t._l(t.articleList,function(e,a){return n("li",{key:a},[n("div",{staticClass:"left"},[n("p",[t._v(t._s(e.title))]),n("p",[t._v(t._s(e.summary))])]),n("img",{attrs:{src:t.avatar,alt:""}})])}))])]),n("div",{staticClass:"bottom"},[n("img",{attrs:{src:t.bottom,alt:""}})]),t.isShowMask?n("div",{staticClass:"mask"},[n("div",{staticClass:"content"},[n("img",{attrs:{src:t.attendanceSuccess,alt:""}}),n("p",{staticClass:"tips"},[t._v("签到成功")]),n("p",{staticClass:"info"},[t._v("恭喜您获得20点能量")]),n("button",{on:{click:t.handleAttendanceConfirm}},[t._v("确认")])])]):t._e()])},f=[function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"title"},[n("h3",[t._v("能量分析")]),n("a",{attrs:{href:"javascript:;"}},[t._v("更多")])])},function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{staticClass:"title"},[n("h3",[t._v("OASES咨询")]),n("a",{attrs:{href:"javascript:;"}},[t._v("更多")])])}],d=(n("ac6a"),n("f3e2"),n("6d67"),function(t,e){var n=e-t,a=Math.random(),r=t+Math.round(a*n);return r}),g=n("cf05"),m=n("b6e5"),A=n("6715"),y=n("1eff"),b=n("61ab"),_=n("c0c9"),C={name:"index",data:function(){return{avatar:g,attendance:m,attendanceSuccess:b,promote:A,bottom:y,energyBall:_,width:"80%",isShowMask:!1,energyBallList:[],currentEnergy:0,currentPower:0,articleList:[],analysis:"",analysisCount:0,tempArr:[],count:0}},created:function(){this.getEnergyBall(),this.getCurrentEnergy(),this.getCurrentPower(),this.getEnergyAnalysis(),this.getArticleList()},filters:{},methods:{handleAttendance:function(){this.isShowMask=!0},handleAttendanceConfirm:function(){this.isShowMask=!1},getEnergyBall:function(){var t=this;this.$axios.post("/inquireEnergyBall").then(function(e){var n=e.data.data;t.energyBallList=n.energyBallList.map(function(e){var n=t.randomPoint();return console.log(t.count),e.x=n.x/75+"rem",e.y=n.y/75+"rem",e}),console.log(t.tempArr)})},getCurrentEnergy:function(){var t=this;this.$axios.post("inquireEnergyPoint").then(function(e){var n=e.data.data;t.currentEnergy=n})},getCurrentPower:function(){var t=this;this.$axios.post("inquirePower").then(function(e){var n=e.data.data;t.currentPower=n})},getEnergyAnalysis:function(){var t=this;this.$axios.post("inquireEnergyPointByCategory").then(function(e){var n=e.data.data;t.analysis=n,console.log(t.analysis),n.forEach(function(e){t.analysisCount+=e.value})})},getArticleList:function(){var t=this;this.$axios.post("inquireNews").then(function(e){var n=e.data.data;t.articleList=n.rows})},formatSize:function(t){return t>9999?"1rem":t>999?64/75+"rem":t>0?52/75+"rem":void 0},handleClickEnergy:function(t,e){var n=t.currentTarget;this.$axios.post("takeEnergyBall").then(function(t){var e=t.data.data;console.log(e)}),n.classList.add("fadeOutUp"),n.classList.remove("flash"),n.classList.remove("infinite"),this.currentEnergy+=e.value},randomPoint:function(){this.count++;var t={x:d(50,700),y:d(50,550)};if(0==this.tempArr.length)return this.tempArr.push(t),t;for(var e=this.tempArr.length,n=0;n<e;n++)if(Math.abs(t.x-this.tempArr[n].x)<75&&Math.abs(t.y-this.tempArr[n].y)<75)return this.randomPoint();return this.tempArr.push(t),t}}},w=C,E=(n("7765"),Object(u["a"])(w,p,f,!1,null,"734eaa57",null));E.options.__file="Index.vue";var x=E.exports,B={name:"app",components:{HelloWorld:h,Index:x}},k=B,L=(n("034f"),Object(u["a"])(k,r,s,!1,null,null,null));L.options.__file="App.vue";var I=L.exports,P=(n("499a"),n("f5df"),n("4dcb"),n("77ed"),n("bc3a")),j=n.n(P),Y=null;window.Android&&(Y=window.Android.getToken(),alert(Y)),console.log(Y),j.a.defaults.baseURL="http://18.219.19.160:8080/api/v1/energyPoint",j.a.interceptors.request.use(function(t){return t.headers={"Content-Type":"application/x-www-form-urlencoded;charset=utf-8",token:Y||"fd59685a-8a8b-474e-9990-551da0d68272"},t}),a["a"].prototype.$axios=j.a,a["a"].config.productionTip=!1,new a["a"]({render:function(t){return t(I)}}).$mount("#app")},"61ab":function(t,e,n){t.exports=n.p+"img/attendance.d1a16f79.png"},6715:function(t,e,n){t.exports=n.p+"img/promote_btn.9af813bc.png"},7765:function(t,e,n){"use strict";var a=n("97c5"),r=n.n(a);r.a},"8c9a":function(t,e,n){"use strict";var a=n("1633"),r=n.n(a);r.a},"97c5":function(t,e,n){},b6e5:function(t,e,n){t.exports=n.p+"img/attendance_btn.51e9408d.png"},c0c9:function(t,e,n){t.exports=n.p+"img/ball.571c6cc8.png"},c21b:function(t,e,n){},cf05:function(t,e,n){t.exports=n.p+"img/logo.82b9c7a5.png"}});
//# sourceMappingURL=app.82ef33e5.js.map