<template>
<div>
  <scroller :on-refresh="refresh" :on-infinite="infinite">
    <!-- Header部分 Start -->
    <header>
      <div class="left">
        <img :src="userInfo.avatar" alt="">
        <span class="name">{{userInfo.nickname}}</span>
      </div>
      <div class="right">
        <div>
          <i></i>
          能量 {{currentEnergy}}
        </div>
        <div>
          <i></i>
          算力 {{currentPower}}
        </div>
      </div>
    </header>
    <!-- Header部分 End -->
    <!-- 挖矿部分 Start -->
    <div class="map">
      <div class="energy-block">
        <div @click="handleClickEnergy($event,item)"  v-for="(item,index) in energyBallList" :key="index" :style="{top:item.y,left:item.x}" class="energy-ball flash infinite animated  ">
          <!-- flash infinite animated永久性-->
          <img :src="energyBall" alt="" :style="{width: formatSize(item.value),height: formatSize(item.value)}">
          <p>{{item.value}}</p>
         <div>
         <span v-if="item.generate" :style="{color:'#006600'}"><font size="1px" v-if="item.generate">{{item.name}}</font></span>
         <!--使用display:inner-block显示块居中-->
          <span v-else ><font size="1px" >{{item.name}}</font></span>
         </div>
        </div>
        <div @click="handleClickWalkEnergy($event,item)"  v-for="(item,index) in walkEnergyBallList" :key="index+energyBallList.length" :style="{top:item.y,left:item.x}" class="energy-ball flash infinite animated  ">
          <!-- flash infinite animated永久性-->
          <img :src="energyBall" alt="" :style="{width: formatSizeWalk(item.value),height: formatSizeWalk(item.value)}" >
          <p >{{item.value}}</p>
        <div>
          <i></i> 
          <!-- class="ballcolor" -->
          <span v-if="item.generate" :style="{color:'#006600'}"><font size="1px" v-if="item.generate">{{item.name}}</font></span>
          <span v-else ><font size="1px" >{{item.name}}</font></span>
         </div>
          </div>
      
      </div>
      
      <img @click="handleAttendance" :src="attendance" class="attendance" />
      <img @click="handlePromote" :src="promote" class="promote" />
      <img @click="handleMiner" :src="miner" class="miner" />
    </div>
    <!-- 挖矿部分 End -->
    
    <!-- 能量分析部分 Start -->
    <div class="analysis">
      <div class="title">
        <h3>能量分析</h3>
        <a href="javascript:;">{{datetime}}</a>
      </div>
      <ul class="progress">
        <li>
          <i></i>
          <span class="equipment">手机</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[0]" :style="{width: formatWalkAnalysis(analysis[0].value,analysis[0].maxValue)}"></div>
          </div>
          <span  v-if="analysis[0]" class="count">{{analysis[0].value}}</span><span class="count1">积分</span>
        </li>
         <li>
          <i></i>
          <span class="equipment">计步</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[1]" :style="{width: formatWalkAnalysis(analysis[1].value,analysis[1].maxValue)}"></div>
          </div>
          <span  v-if="analysis[1]" class="count">{{analysis[1].value}}</span><span class="count1">步</span>
        </li>
        <li>
          <i></i>
          <span class="equipment">手表</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[2]" :style="{width: formatWalkAnalysis(analysis[2].value,analysis[2].maxValue)}"></div>
          </div>
          <span v-if="analysis[2]" class="count">（即将上线）</span>
        </li>
        <li>
          <i></i>
          <span class="equipment">家电</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[3]" :style="{width: formatWalkAnalysis(analysis[3].value,analysis[3].maxValue)}"></div>
          </div>
          <span v-if="analysis[3]" class="count">（即将上线）</span>
        </li>
      </ul>
    </div>
    <!-- 能量分析部分 End -->

    <!-- OASES咨询 Start-->
    <div class="consult">
      <div class="title">
        <h3>OASES资讯</h3>
        <a href="javascript:;">更多</a>
      </div>
      <p class="tips">
        关于OAS你可以了解更多
      </p>
      <div class="news-list">
        <ul>
          <li :key="index" v-for="(item, index) in articleList">
            <a :href='item.newsLink' target="_blank">
            <div class="left">
              <p>{{item.title}}</p>
              <p>{{item.summary}}</p>
            </div>
            <img :src="item.imageLink" alt="">
            </a>
          </li>
        </ul>
      </div>
    </div>
    <div v-if="isShowNewsTip" class="news-tips">加载中...</div>
    <div >
      <!-- <input  id="SERVER_TIME" type="hidden" v-model="input1" /> -->
       <!-- <input  id="SERVER_TIME"  v-model="input2" /> -->
       </div>
    <!-- OASES咨询 End -->
    <!-- 底部 Start -->
    <div class="bottom">
      <img :src="bottom" alt="">
    </div>
    <!-- 底部 End -->
    <!-- 签到弹框 Start -->
    <div v-show="isShowMask" class="mask">
      <div class="content">
        <img :src="attendanceSuccess" alt="">
        <p class="tips">{{attendanceMsg.msg}}</p>
        <p v-if="isShowSuccessMsg" class="info " >恭喜您获得{{attendanceMsg.msgall}}</p>
        <button @click="handleAttendanceConfirm">确认</button>
      </div>
    </div>
    <!-- 签到弹框 End -->
  </scroller>
   <div class="toast" v-if="isShowToast">{{toastMsg}}</div>
  </div>
</template>

<script>
const avatar = require("@/assets/logo.png");
const attendance = require("@/assets/images/attendance_btn.png");
const promote = require("@/assets/images/promote_btn.png");
const bottom = require("@/assets/images/bottom_logo@2x.png");
const attendanceSuccess = require("@/assets/images/attendance.png");
const energyBall = require("@/assets/images/ball.png");
const infoIamge = require("@/assets/images/icon_default_user.png");
const miner = require("@/assets/images/kuangji@2x.png");
import { randomNum } from '@/utils/utils.js'
import $ from 'jquery'
export default {
  //
  //inject:['reload'],
  name: "index",
  data() {
    return {
      avatar: avatar,
      attendance: attendance,
      attendanceSuccess: attendanceSuccess,
      promote: promote,
      bottom: bottom,
      energyBall: energyBall,
      infoIamge: infoIamge,
      miner:miner,
      width: "80%",
      isShowMask: false,
      isShowSuccessMsg: false,
      //isShowEnergyMsg:false,
      //isShowCommaMsg:false,
     // isShowPowerMsg:false,
      isShowToast: false,
      isShowNewsTip: false,
      energyBallList:[],
      walkEnergyBallList:[],
      currentEnergy:0,
      currentPower:0,
        page:1,
      newsTotal:1,
      articleList:[],
      newsLength:0,
      datetime:'',
      analysis:'',
      //analysisCount:0,
      tempArr:[],
     // tempArrWalk:[],
      //input1:'',
      energyMaxValue:0,
      toastMsg:'提示信息',
      attendanceMsg:{
        msg:'签到成功',
        //energy:0,
        //power:0
        msgall:''
      },
      userInfo:{
        avatar:'',
        nickname: ''
      }
      
    }
  },
  beforeMount() {
         //设置定时器，每3秒刷新一次
         var self = this;
         setInterval(getTotelNumber,300000)
         function getTotelNumber() {
          self.tempArr = [] // 刷新清空这个临时数组 防止栈溢出
          self.energyBallList=[]
          self.walkEnergyBallList=[]
          //self.getMaxValue()
          self.getBallAndAnalysis()
             //await self.getWalkEnergyBall() 
            //console.log(JSON.stringify(list))
            //self.getEnergyAnalysis()
           
         }
         //getTotelNumber();      
    },
  created() {
    
    this.getCurrenttime()
    //this.getEnergyBall()
    //this.getWalkEnergyBall() 
    this.getBallAndAnalysis()
    
    this.getCurrentEnergy()
    this.getCurrentPower()
    
   // this.getEnergyAnalysis()
    this.getArticleList()
    this.getUserInfo()
    
    //this.synchronizeBall()
    window.skipRefresh= this.skipRefresh
     //this.location()
    
    
  },
  filters: {
  },
  methods: {
    getMaxValue(){
    return this.$axios.post('/energyPoint/pointBallMaxValue')
    // .then(({data}) => {
    //   if(data.code==0){
    //     this.maxValue=data.data
    //     console.log(this.maxValue)
    //   }
    // })
    },
   getStep(){
    //let todayStep=100
      let todayStep=window.Android.getTodaySteps()
      console.log(todayStep)
      return todayStep
   },
    //预先加载3条新闻
    getArticleList () {
      var page=this.page; 
      this.loadArticleList(page);
      this.isShowNewsTip=true;    
    },
    // 获取新闻文章列表
    loadArticleList (page) {
      var formData = new FormData();
      formData.append("pageNum", page);
      formData.append("pageSize", "3");
      this.$axios.post('/energyPoint/inquireNews',formData)
      .then(({data:{data}}) =>{
        //console.log(data);
        this.newsTotal=data.data.total;
        //console.log(this.newsTotal);
        if(data.msg=="无更多数据"){
          this.isShowNewsTip=false;  
          this.articleList=[...this.articleList,...data.data.rows];
          console.log(JSON.stringify(data.data.rows))
          this.newsLength=data.data.rows.length
        } 
        else
        {
          this.articleList=[...this.articleList,...data.data.rows]; 
          console.log(JSON.stringify(data.data.rows))
          this.newsLength=data.data.rows.length
        }   
      })
      .catch(function (err) {
        console.log(err);
      })
    },   

   //上拉加载新闻
   infinite (done) { 
      this.page+=1
     /*if(this.newsLength!=3){
      this.page-=1
      let page2=this.newsTotal%3
      console.log("page"+this.page)
      console.log("page2"+page2)
      //for(let i=0;i<page2;i++)
     // {
        this.articleList.splice((this.page-1)*3,page2)
      //i--
      //}
      console.log("this.length"+this.articleList.length)
     }*/
     console.log(this.page)
      var page=this.page
      var newsTotal=this.newsTotal     
      var pageTotal=Math.ceil(newsTotal/3)
      //alert(pageTotal)
      if(page<=pageTotal){
        this.isShowNewsTip=true                     
        this.loadArticleList(page)  
        setTimeout(() => {
          done()
        },1000)
      }
      else{
        setTimeout(() => {
            done(true)
            this.infinite = undefined
          }, 500)
          return;
      }   
    },

    // 签到按钮点击事件
    handleAttendance () {
     
      this.$axios.post('/energyPoint/checkin').then(({data}) => {
        console.log(JSON.stringify(data))
        if (data.code == 0) {
          if(data.data.newEnergyPoint==0){
          this.currentPower += data.data.newPower
          this.currentEnergy += data.data.newEnergyPoint
          
          this.attendanceMsg.msgall = data.data.newPower+"点算力"
          
          this.isShowSuccessMsg = true
         
          }else if(data.data.newPower==0){
          this.currentPower += data.data.newPower
          this.currentEnergy += data.data.newEnergyPoint
          //this.attendanceMsg.energy = data.data.newEnergyPoint
          //this.attendanceMsg.power = data.data.newPower
          this.attendanceMsg.msgall = data.data.newEnergyPoint+"点能量"
          this.isShowSuccessMsg = true
         
          }else{
          this.currentPower += data.data.newPower
          this.currentEnergy += data.data.newEnergyPoint
          //this.attendanceMsg.energy = data.data.newEnergyPoint
          //this.attendanceMsg.power = data.data.newPower
           this.attendanceMsg.msgall = data.data.newEnergyPoint+"点能量"+","+data.data.newPower+"点算力"
          this.isShowSuccessMsg = true
          }
          this.getEnergyAnalysis()
        }
        else if(data.code==10012)
        { this.isShowSuccessMsg = false
          }
        this.attendanceMsg.msg = data.message
        this.isShowMask = true
      })
      
    },
    // 签到弹窗确认按钮
    handleAttendanceConfirm () {
      this.isShowMask = false
    },
    // 获取用户信息
    getUserInfo () {
      this.$axios.post('/userCenter/inquireUserInfo').then(({data:{data}}) => {
        console.log("用户信息"+JSON.stringify(data));
        let image='PNG,GIF,JPG,JPEG,BMP,png,gif,jpg,jpeg,bmp,blob'
          let profile=data.profile.split('.')
          let profile2=data.profile.split('-')
           if(image.indexOf(profile[profile.length-1])!=-1||image.indexOf(profile2[profile2.length-1])!=-1){
            this.userInfo.avatar=data.profile
            }else{	
              this.userInfo.avatar=infoIamge
            }
        this.userInfo.nickname = data.nickname
        
      })
    },
    handlePromote(){
      console.log("调用安卓")
    window.Android.startLiftComputingPower()
    
    },
    handleMiner(){
      console.log("调用安卓")
    window.Android.startLiftBuyingMiner()
    },
    async getBallAndAnalysis() {
            try {
                let dataBall={}
                let dataWalkBall={}
                let energyBallListBackup=new Array()
                let walkEnergyBallListtBackup=new Array()
                
                let list = await this.getMaxValue()
                //console.log(list)
                console.log("maxvalue"+list.data.data)
                this.energyMaxValue=list.data.data
                dataBall=await this.getEnergyBall();
                //console.log(JSON.stringify(dataBall))
                if(dataBall.data.code==0)
                {
                
                  energyBallListBackup = dataBall.data.data.energyBallList.map(el => {
                    let p = this.randomPoint()
                    // pArr.splice(randomIdx,1)
                    el.x = p.x / 75 + 'rem'
                    el.y = p.y / 75 + 'rem'
                    if(el.value<this.energyMaxValue)
                    { el.generate=true}
                    else{el.generate=false}
                    return el
                  }) 
                   for(let i=0;i<energyBallListBackup.length;i++)  
                  { 
                  if(energyBallListBackup[i].value==0)
                    { 
                    energyBallListBackup.splice(i, 1)
                    i--}
                  }
                }
                dataWalkBall=await this.getWalkEnergyBall();
               this.getEnergyAnalysis()
                let time=currentTime(true)
                if(dataWalkBall.data.code==0)
              { walkEnergyBallListtBackup= dataWalkBall.data.data.map(el => {
                
                  let p = this.randomPoint()
                
                  el.x = p.x / 75 + 'rem'
                  el.y = p.y / 75 + 'rem'
                  
                    if(el.startDate>=time)
                  { 
                    el.generate=true}
                  else{el.generate=false}
                   if(el.value>100)
                    { console.log(el.value)
                      el.value=parseInt(el.value)
                      console.log(el.value)
                   } 
                  return el
                }) 
                for(let i=0;i<walkEnergyBallListtBackup.length;i++)  
              { 
              if(walkEnergyBallListtBackup[i].value==0)
                { 
                walkEnergyBallListtBackup.splice(i, 1)
                i--}
              }
              }
              

              this.energyBallList=energyBallListBackup
              console.log(this.energyBallList.length+JSON.stringify(this.energyBallList))
              this.walkEnergyBallList=walkEnergyBallListtBackup
              console.log("222"+JSON.stringify(this.walkEnergyBallList))
              
              } 
              catch(err) {
                    console.log(err);
                }
            },
    // 获取悬浮能量球数据
    getEnergyBall () {
     
       return this.$axios.post('/energyPoint/inquireEnergyPointBall')
      //  .then(({data:{data}})=> {
      //   // let pArr = createPositionArr()
      //   console.log("能量球"+data)
      //  // let i=0
        
      //   this.energyBallListBackup = data.energyBallList.map(el => {
      //     // let randomIdx = randomNum(0,pArr.length - 1)
      //     let p = this.randomPoint()
      //     // pArr.splice(randomIdx,1)
      //     el.x = p.x / 75 + 'rem'
      //     el.y = p.y / 75 + 'rem'
      //     if(el.value<50)
      //    { el.generate=true}
      //    else{el.generate=false}
      //    // console.log("{"+i+"}"+JSON.stringify(el))
      //     //i++
      //     return el
      //   }) 
      //   return this.energyBallListBackup
      // }
      
      // ) 
       
       
                 
    },
     getWalkEnergyBall() {
    let stepNum=this.getStep()
     //let time="2018-10-15"
    
     var data={}
     data['date']=this.datetime
     data['stepNum']=stepNum
     let params = new Array();
     params.push(data)  
     return this.$axios.post('/walkPoint/inquireWalkPointBall',{quota:params})
    //  .then(({data:{data}}) => {
    //   //this.input2=data.startDate
    //     console.log("data"+JSON.stringify(data))
    //    let walkEnergyBallList=new Array();
    //     walkEnergyBallList= data.map(el => {
    //       // let randomIdx = randomNum(0,pArr.length - 1)
    //       let p = this.randomPoint()
    //       // pArr.splice(randomIdx,1)
    //       el.x = p.x / 75 + 'rem'
    //       el.y = p.y / 75 + 'rem'
         
    //        if(el.startDate>=time)
    //      { 
    //        el.generate=true}
    //      else{el.generate=false}
    //       return el
    //     }) 
    //      for(let i=0;i<walkEnergyBallList.length;i++)  
    //   { 
    //     if(walkEnergyBallList[i].value==0)
    //     { 
    //       walkEnergyBallList.splice(i, 1)
    //       i--}
    //     }
    //     console.log("最初de"+walkEnergyBallList)
    //     this.walkEnergyBallListtBackup=walkEnergyBallList
    //     console.log("函数里"+this.walkEnergyBallListtBackup)
    
    //   })
   
     

                 
    },
   
    // 获取当前能量
    getCurrentEnergy () {
      this.$axios.post('/energyPoint/inquireEnergyPoint').then(({data:{data}}) => {
        console.log("能量"+data)
        this.currentEnergy = data
      })
    },
    // 获取当前算力
    getCurrentPower () {
      this.$axios.post('/computingPower/inquirePower').then(({data:{data}}) => {
        this.currentPower = data
      })
    },
    // 获取能量分析
    getEnergyAnalysis () {
      //this.analysisCount=0
      this.$axios.post('/energyPoint/inquireEnergyPointByCategory').then(({data:{data}}) => {
        console.log("分析"+JSON.stringify(data))
        this.analysis = data
        //this.analysisCount=data.maxValue
        // data.forEach(el => {
        // //forEach() 方法用于调用数组的每个元素，并将元素传递给回调函数。注意: forEach() 对于空数组是不会执行回调函数的。
        // //不能终止循环，除非抛出异常，map必须返回，返回一个数组
        //   this.analysisCount += el.value
        // })
      })
    },
    // 获取新闻文章列表
    // getArticleList () {
    //   this.$axios.post('/energyPoint/inquireNews').then(({data:{data}}) =>{
    //     this.articleList = data.rows
    //   })
    // },
    // 根据能量数格式化能量球大小
    formatSize: function (value) {
      /*if (value > 9999) {
        return 75 / 75 + 'rem'
      }*/
      if (value >= 100) {
        return 75 / 75 + 'rem'
      }
      if (value >= 50) {
        return 64 / 75 + 'rem'
      }
      if (value >= 0) {
        return 52 / 75 + 'rem'
      }
    },
    formatSizeWalk: function (value) {
      /*if (value > 9999) {
        return 75 / 75 + 'rem'
      }*/
     if (value >= 1000) {
        return 85 / 75 + 'rem'
      }
      if (value >= 500) {
        return 75 / 75 + 'rem'
      }
      if (value >= 100) {
        return 64 / 75 + 'rem'
      }
      if (value >= 0) {
        return 52 / 75 + 'rem'
      }
    },
   formatWalkAnalysis: function (value,maxValue) {
     if(value>maxValue)
     {return 100+'%'}
     else{
      return value /maxValue * 100 + '%'
     }
    
   },
    // 点击悬浮能量小球事件
    handleClickEnergy (event, data) {
      console.log(JSON.stringify(data));
      console.log(event)
      /*let currentTime = new Date().getTime()
      let endTime = new Date(data.endDate).getTime()
      if (currentTime < endTime) {
        this.Toast('能量暂不可收取')
        return
      }*/
      let value = data.value
      if (value <this.energyMaxValue) {
        this.Toast('能量暂不可收取')
        return
      }
      let ele = event.currentTarget
      this.$axios.post('/energyPoint/takeEnergyPointBall',{ballId: data.uuid}).then(({data}) => {
        console.log(JSON.stringify(data))
        if (data.code != 0) {
          //this.Toast(data.message)
          return
        }
      ele.classList.add('fadeOutUp')
      ele.classList.remove('flash')
      ele.classList.remove('infinite')
      this.getCurrentEnergy()
      this.getCurrentPower()
      this.getEnergyAnalysis()
      })
      
    },
    handleClickWalkEnergy(event, data){
      let datatime = data.startDate
      let time=currentTime(true)
      if (datatime>=time) {
        this.Toast('能量暂不可收取')
        return
      }
      let ele = event.currentTarget
      this.$axios.post('/walkPoint/takeWalkPointBall',{ballId: data.uuid}).then(({data}) => {
        console.log(JSON.stringify(data))
        if (data.code != 0) {
          //this.Toast(data.message)
          return
        }
      ele.classList.add('fadeOutUp')
      ele.classList.remove('flash')
      ele.classList.remove('infinite')
      this.getCurrentEnergy()
      this.getCurrentPower()
      this.getEnergyAnalysis()
      })
    },
    // 随机生成不重复坐标点方法
    randomPoint() {
      let p = {x:randomNum(20,650),y: randomNum(50, 550)}
      if(this.tempArr.length == 0) {
        this.tempArr.push(p)
        return p
      }
      let len = this.tempArr.length
     
      for (let i = 0; i < len; i++){
        if(Math.abs(p.x - this.tempArr[i].x) < 90 && Math.abs(p.y - this.tempArr[i].y) < 90) {
          return this.randomPoint()
        }
      }
      this.tempArr.push(p)
      return p
    },
    // 下拉刷新
    refresh (done) {
      //this.getMaxValue()
      this.getCurrenttime()
      
      this.tempArr = [] // 刷新清空这个临时数组 防止栈溢出
      this.energyBallList=[]
      this.walkEnergyBallList=[]

      this.getBallAndAnalysis()
     // this.getWalkEnergyBall()
      //this.getEnergyBall()
      
      this.getCurrentEnergy()
      this.getCurrentPower()
      //this.getEnergyAnalysis()
      this.getUserInfo()
     
     //location.reload()
     //this.reload()
      setTimeout(() => {
        done()
      },1000)
      
    /*var aaa =  this.$el.childNodes[0].childNodes[0].childNodes[3].childNodes[0].childNodes
    for(var i = 0;i<aaa.length;i++){
       aaa[i].classList.remove("fadeOutUp")
      aaa[i].classList.add("infinite")
       aaa[i].classList.add("flash")
      // aaa[i].classList.add("animated") 
    }*/
    },
   
 skipRefresh() {
      //this.getStep()
      //this.getMaxValue()
      this.getCurrenttime()
      this.tempArr = [] // 刷新清空这个临时数组 防止栈溢出
      this.energyBallList=[]
      this.walkEnergyBallList=[]
      
      this.getBallAndAnalysis()
     // this.getEnergyBall()
      //this.getWalkEnergyBall()
      this.getCurrentEnergy()
      this.getCurrentPower()
      //this.getEnergyAnalysis()
      this.getUserInfo()
  
    },


    removeclass(){
       $(".energy-ball").addClass("flash")
      $(".energy-ball").addClass("infinite")
      $(".energy-ball").addClass( "animated")
      $(".energy-ball").removeClass("fadeOutUp")
    },   
    // 提示信息
    Toast (msg, delay) {
      this.toastMsg = msg
      this.isShowToast = true
      setTimeout(() => {
        this.isShowToast = false
      },delay || 1500)
    },
    location(){
    window.addEventListener('pageshow', function(evt){
    setTimeout(function(){
        if(evt.persisted){
           // location.reload(true);
           this.skipRefresh()
        }
    });
});    
},
getCurrenttime(){
    //var SERVER_TIME = document.getElementById("SERVER_TIME");
var time=currentTime(true)
//this.$refs.input1.value=time
this.datetime=time
//console.log("year"+this.$refs.input1.value);
},
  }
   
};
/*$(function(){
    var SERVER_TIME = document.getElementById("SERVER_TIME");
    //var mytime= CurentTime();
    
     // $("#SERVER_TIME").val(mytime)    
    
console.log("111"+$("#SERVER_TIME").val());
    var REMOTE_VER = SERVER_TIME && SERVER_TIME.value;
    console.log(REMOTE_VER)
    if(REMOTE_VER){   
       var LOCAL_VER = sessionStorage && sessionStorage.PAGEVERSION;  
         //if(LOCAL_VER && parseInt(LOCAL_VER) >= parseInt(REMOTE_VER)){
           if(LOCAL_VER && LOCAL_VER >= REMOTE_VER){ 
           console.log(LOCAL_VER) 
            console.log( parseInt(LOCAL_VER))
             console.log( parseInt(REMOTE_VER))
                //说明html是从本地缓存中读取的       
                 //location.reload(true);
                 window.skipRefresh()    
                 }else{        
                    console.log("222"+LOCAL_VER)
                   //说明html是从server端重新生成的，更新LOCAL_VER      
                     sessionStorage.PAGEVERSION = REMOTE_VER;    }}
    
});*/
/*$(function(){
 if(window.name != "bencalie"){
    window.skipRefresh();
     // location.reload();
    console.log("123")
    window.name = "bencalie";
}else{
    window.name = "";
}
});*/
function currentTime(flag)
    { 
        var now = new Date();
       
        var year = now.getFullYear();       //年
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
       
        var hh = now.getHours();            //时
        var mm = now.getMinutes();          //分
        var ss=now.getSeconds();
        if(flag){
         //var clock = JSON.stringify(year);
        var clock = year + "-";
          
              if(month < 10)
                clock += "0";
          
            clock += month + "-";
          
              if(day < 10)
                clock += "0";
              
            clock += day;
                  return (clock)
                }
        else{
          clock = year + "-";
       
        if(month < 10)
            clock += "0";
       
        clock += month + "-";
       
        if(day < 10)
            clock += "0";
           
        clock += day + " ";
       
        if(hh < 10)
            clock += "0";
           
        clock += hh + ":";
        if (mm < 10) clock += '0'; 
        clock += mm+":"; 
         if (ss < 10) clock += '0'; 
        clock += ss; 
        return(clock); 
        }
    } 

</script>

<style lang="scss" scoped>
header {
  box-sizing: border-box;
  padding: 8px 40px;
  height: 88px;
  background-color: #ecedf1;
}
.left {
  box-sizing: border-box;
  // height: 100%;
  float: left;
  font-size: 28px;
  color: #000;
  display: flex;
  align-items: center;
  img {
    border-radius: 50%;
    width: 72px;
    height: 72px;
  }
}
.right {
  float: right;
  // height: 100%;
  padding: 12px 18px;
  border-radius: 36px;
  background-color: #fff;
  line-height: 48px;
  div {
    float: left;
    display: flex;
    align-items: center;
    i {
      display: inline-block;
      width: 48px;
      height: 48px;
      background-image: url("../assets/images/energy@2x.png");
      background-size: 48px 48px;
    }
    &:last-child {
      margin-left: 16px;
      i {
        background-image: url("../assets/images/power@2x.png");
      }
    }
  }
}

.map {
  position: relative;
  width: 100%;
  height: 768px;
  background-color: #ecedf1;
  .energy-block {
    position: relative;
    width: 100%;
    height: 642px;
    //624px;
    background-image: url("../assets/images/background@2x.png");
    background-repeat: no-repeat;
    background-size: 100% 100%;
    .energy-ball {
      // animation: 1.5s twinkling infinite;
      width: 100px;
      position: absolute;
      top: 0;
      left: 0;
      text-align: center;
      line-height: 40px;
      img {
        width: 100%;
        height: 100%;
        // margin-bottom: 8px;
      }
      p {
        position: absolute;
        margin-top: -20px;
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        color: #000;
      }
      div {
        
      //float: right;
      display: flex;
      justify-content: center;
      text-align: center;
      i{
     //position: absolute;
      position: relative;
      display: inline-block;
      right: 0.1rem;
      width: 32px;
      height: 32px;
      background-image: url("../assets/images/run@2x.png");
      background-size: 32px 32px;
    }
    
    .ballcolor{
      color:#006600;
    }
   
     .blackIamge{
      position: absolute;
      display: inline-block;
      right: 70%;
      width: 32px;
      height: 32px;
      background-image: url("../assets/images/watch@2x.png");
      background-size: 32px 32px;
    }
    
      }

    }

  
  }
  
  
  .attendance,
  .promote,
  .miner{
    width: 112px;
    height: 112px;
    position: absolute;
    bottom: 18px;
    left: 25px;//36
      
    &:nth-child(2){
        left: 25px;}
    &:nth-child(3) {
       left: 159px;//180
    }
    &:last-child {
      width: 100px;
      height: 100px;
      bottom:28px;
      left: 299px;
    }
  }

  // .attendance{
  //    width: 112px;
  //   height: 112px;
  //   position: absolute;
  //   bottom: 18px;
  //   left: 25px;
  // }
  // .promote{
  //    width: 112px;
  //   height: 112px;
  //   position: absolute;
  //   bottom: 18px;
  //   left: 25px;
  //    left: 160px;
  // }
  // .miner{
  //    width: 100px;
  //   height: 100px;
  //   position: absolute;
  //   bottom:28px;
  //   left: 25px;
  //    left: 299px;
  // }

}

.title {
  position: relative;
  font-size: 36px;
  line-height: 50px;
  a {
    position: absolute;
    right: 0;
    top: 0;
  }
}
.analysis,
.consult {
  margin-top: 40px;
  padding: 30px;//40px
  
}
.progress {
  li {
    //display: -webkit-flex;
    display: flex;
    height: 48px;
    align-items: center;
    font-size: 28px;
    margin-top: 32px;
    i {
      width: 48px;
      height: 48px;
    }
    &:first-child i {
      background-image: url("../assets/images/phone@2x.png");
      background-size: 48px 48px;
    }
    &:nth-child(2) i {
      background-image: url("../assets/images/run@2x.png");
      background-size: 48px 48px;
    }
    &:nth-child(3) i {
      background-image: url("../assets/images/watch@2x.png");
      background-size: 48px 48px;
    }
    &:nth-child(4) i {
      background-image: url("../assets/images/other@2x.png");
      background-size: 48px 48px;
    }
  }
  .equipment {
    width: 102px;//112px
    text-align: center;
  }
  .bar {
    position: relative;
    width: 360px;//400px;
    height:12px; //12px;
    div {
      position: absolute;
      top: 0px;
      left: 0px;
      width: 360px;
      height: 12px;
      //第一个div全长灰色
      &:first-child {
        background-color: #d8d8d8;
      }
      //第二个divvalue的值
      &:last-child {
        background: linear-gradient(
          -90deg,
          rgba(87, 176, 70, 1),
          rgba(0, 148, 74, 1)
        );
      }
    }
  }
  .count {
    flex: 1;
    text-align: center;
  }
  .count1 {
    flex: 1;
    text-align: center;
    margin-right: -30px;
    margin-left: -35px;
  }
}

.consult {
  .tips {
    margin-top: 10px;
  }
  li {    
    a {
        display: flex;
        //justify-content: space-between;
        height: 280px;
        padding: 40px 0;
        border-bottom: 1px solid #ddd;    
        word-wrap:break-word;
        word-break:break-all;
      img {
        width: 248px;
        height: 156px;
        margin-left: 26px;
      }
      .left {
        flex: 1;
        display: flex;
        flex-direction: column;
        //justify-content: space-between;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;  //1行
        -webkit-box-orient: vertical;
          p:first-child {
            font-size: 28px;
            line-height: 36px;
            //height: 72px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;  //2行
            -webkit-box-orient: vertical;            
          }
          p:last-child {
            flex: 1;
            font-size: 24px;
            line-height: 34px;
            margin-top: 30px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 3;  //3行
            -webkit-box-orient: vertical;
          }
      }
    }
  }
}
.news-tips {
  //position: relative;
  font-size: 25px;
  line-height: 50px;
  text-align: center; 
}
.bottom {
  position: relative;
  display: block;
  width: 88px;
  height: 22px;
  margin-bottom: 40px;
  margin-top: 20px;
  margin-left: auto;
  margin-right: auto;
  &::before,
  &::after {
    content: "";
    position: absolute;
    width: 96px;
    height: 4px;
    background-color: #ddd;
    top: 50%;
    margin-top: -2px;
  }
  &::before {
    left: -120px;
  }
  &::after {
    right: -120px;
  }
  img {
    width: 100%;
    height: 100%;
  }
}

.mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.3);
  .content {
    width: 448px;
    height: 496px;
    background-color: #fff;
    border-radius: 20px;
    text-align: center;
    margin-top: 400px;
    margin-left: auto;
    margin-right: auto;
    img {
      width: 218px;
      height: 152px;
      margin-top: 48px;
    }
    .tips {
      font-size: 36px;
      margin-top: 20px;
    }
    .info {
      color: #8e8e93;
      margin-top: 8px;
    }
    button {
      width: 224px;
      height: 104px;
      border-radius: 52px;
      margin-top: 36px;
      color: #fff;
      font-size: 36px;
      line-height: 104px;
      background-image: url("../assets/images/button_sign.png");
      background-size: 224px 104px;
    }
  }
}

.toast {
    position: fixed;
    left:50%;
    top: 50%;
    transform:translate(-50%,-50%) scale(1);
    word-wrap:break-word;
    line-height: 50px;
    padding:10px 20px;
    text-align: center;
    z-index:9999;
    max-width:80%;
    color: #fff;
    border-radius: 5px;
    background: rgba(0,0,0,0.7);
    overflow: hidden;

}

.flash {
  animation-duration: 5s;//完成动画时间
}

@keyframes twinkling {
  from {
    opacity: 1
  }
  50% {
    opacity: 0.5
  }
  to {
    opacity: 1
  }
}
</style>


