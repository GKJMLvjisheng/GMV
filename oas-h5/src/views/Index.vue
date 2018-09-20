<template>
<div>
  <scroller :on-refresh="refresh" :on-infinite="infinite">
    <!-- Header部分 Start -->
    <header>
      <div class="left">
        <img :src="avatar" alt="">
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
        <div @click="handleClickEnergy($event,item)"  v-for="(item,index) in energyBallList" :key="index" :style="{top:item.y,left:item.x,width: formatSize(item.value),height: formatSize(item.value)}" class="energy-ball flash infinite  animated">
          <img :src="energyBall" alt="">
          <p>{{item.value}}</p>
        </div>
      </div>
      <img @click="handleAttendance" :src="attendance" class="attendance" />
      <img @click="handlePromote" :src="promote" class="promote" />
    </div>
    <!-- 挖矿部分 End -->
    
    <!-- 能量分析部分 Start -->
    <div class="analysis">
      <div class="title">
        <h3>能量分析</h3>
        <a href="javascript:;">更多</a>
      </div>
      <ul class="progress">
        <li>
          <i></i>
          <span class="equipment">手机</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[0]" :style="{width: analysis[0].value / analysisCount * 100 + '%'}"></div>
          </div>
          <span  v-if="analysis[0]" class="count">{{analysis[0].value}}</span>
        </li>
        <li>
          <i></i>
          <span class="equipment">手表</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[1]" :style="{width: analysis[1].value / analysisCount * 100 + '%'}"></div>
          </div>
          <span v-if="analysis[1]" class="count">{{analysis[1].value}}</span>
        </li>
        <li>
          <i></i>
          <span class="equipment">家电</span>
          <div class="bar">
            <div></div>
            <div v-if="analysis[2]" :style="{width: analysis[2].value / analysisCount * 100 + '%'}"></div>
          </div>
          <span v-if="analysis[2]" class="count">{{analysis[2].value}}</span>
        </li>
      </ul>
    </div>
    <!-- 能量分析部分 End -->

    <!-- OASES咨询 Start-->
    <div class="consult">
      <div class="title">
        <h3>OASES咨询</h3>
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
    <!-- OASES咨询 End -->
    <!-- 底部 Start -->
    <div class="bottom">
      <img :src="bottom" alt="">
    </div>
    <!-- 底部 End -->
    <!-- 签到弹框 Start -->
    <div v-if="isShowMask" class="mask">
      <div class="content">
        <img :src="attendanceSuccess" alt="">
        <p class="tips">{{attendanceMsg.msg}}</p>
        <p v-if="isShowSuccessMsg" class="info">恭喜您获得{{attendanceMsg.energy}}点能量,{{attendanceMsg.power}}点算力</p>
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

import { randomNum } from '@/utils/utils.js'
export default {
  name: "index",
  data() {
    return {
      avatar: avatar,
      attendance: attendance,
      attendanceSuccess: attendanceSuccess,
      promote: promote,
      bottom: bottom,
      energyBall: energyBall,
      width: "80%",
      isShowMask: false,
      isShowSuccessMsg: false,
      isShowToast: false,
      isShowNewsTip: false,
      energyBallList:[],
      currentEnergy:0,
      currentPower:0,
      page:1,
      newsTotal:1,
      articleList:[],
      analysis:'',
      analysisCount:0,
      tempArr:[],
      toastMsg:'提示信息',
      attendanceMsg:{
        msg:'签到成功',
        energy:0,
        power:0
      },
      userInfo:{
        avatar:'',
        nickname: ''
      }
    }
  },
  created() {
    this.getEnergyBall()
    this.getCurrentEnergy()
    this.getCurrentPower()
    this.getEnergyAnalysis()
    this.getArticleList()
    this.getUserInfo()
  },
  filters: {
  },
  methods: {

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
        if(data.msg=="无更多数据"){
          this.isShowNewsTip=false;  
          this.articleList=[...this.articleList,...data.data.rows];
        } 
        else
        {
          this.articleList=[...this.articleList,...data.data.rows]; 
        }   
      })
      .catch(function (err) {
        console.log(err);
      })
    },   

   //上拉加载新闻
   infinite (done) { 
      this.page+=1
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
        console.log(data)
        if (data.code == 0) {
         
          this.currentEnergy += data.data.newEnergyPoint
          this.currentPower += data.data.newPower
          this.attendanceMsg.energy = data.data.newEnergyPoint
          this.attendanceMsg.power = data.data.newPower
          this.isShowSuccessMsg = true
        }
        else(data.code=10012)
        {this.isShowSuccessMsg = false}
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
        this.userInfo.nickname = data.nickname
      })
    },
    handlePromote(){
      console.log("调用安卓")
    window.Android.startLiftComputingPower()
    
    },
    // 获取悬浮能量球数据
    getEnergyBall () {
      this.$axios.post('/energyPoint/inquireEnergyBall').then(({data:{data}}) => {
        // let pArr = createPositionArr()
        console.log(data.energyBallList)
        this.energyBallList = data.energyBallList.map(el => {
          // let randomIdx = randomNum(0,pArr.length - 1)
          let p = this.randomPoint()
          // pArr.splice(randomIdx,1)
          el.x = p.x / 75 + 'rem'
          el.y = p.y / 75 + 'rem'
          return el
        })
        
      })              
    },
    // 获取当前能量
    getCurrentEnergy () {
      this.$axios.post('/energyPoint/inquireEnergyPoint').then(({data:{data}}) => {
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
      this.$axios.post('/energyPoint/inquireEnergyPointByCategory').then(({data:{data}}) => {
        this.analysis = data
        data.forEach(el => {
          this.analysisCount += el.value
        })
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
      }
      if (value > 999) {
        return 64 / 75 + 'rem'
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
    // 点击悬浮能量小球事件
    handleClickEnergy (event, data) {
      console.log(data);
      console.log(event)
      /*let currentTime = new Date().getTime()
      let endTime = new Date(data.endDate).getTime()
      if (currentTime < endTime) {
        this.Toast('能量暂不可收取')
        return
      }*/
      let value = data.value
      if (value <50) {
        this.Toast('能量暂不可收取')
        return
      }
      let ele = event.currentTarget
      this.$axios.post('/energyPoint/takeEnergyBall',{ballId: data.uuid}).then(({data}) => {
        console.log(data)
        if (data.code != 0) {
          this.Toast(data.message)
          return
        }
      ele.classList.add('fadeOutUp')
      ele.classList.remove('flash')
      ele.classList.remove('infinite')
      this.getCurrentEnergy()
      this.getCurrentPower()
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
        if(Math.abs(p.x - this.tempArr[i].x) < 75 && Math.abs(p.y - this.tempArr[i].y) < 75) {
          return this.randomPoint()
        }
      }
      this.tempArr.push(p)
      return p
    },
    // 下拉刷新
    refresh (done) {
      this.tempArr = [] // 刷新清空这个临时数组 防止栈溢出
      this.getEnergyBall()
      this.getCurrentEnergy()
      this.getCurrentPower()
      this.getEnergyAnalysis()
      this.getUserInfo()
      setTimeout(() => {
        done()
      },1000)
    },   
    // 提示信息
    Toast (msg, delay) {
      this.toastMsg = msg
      this.isShowToast = true
      setTimeout(() => {
        this.isShowToast = false
      },delay || 1500)
    }
  }
};
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
    height: 624px;
    background-image: url("../assets/images/background@2x.png");
    background-repeat: no-repeat;
    background-size: 100% 100%;
    .energy-ball {
      // animation: 1.5s twinkling infinite;
      width: 75px;
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
        top: 50%;
        left: 50%;
        transform: translate(-50%,-50%);
        color: #000;
      }
    }
  }
  .attendance,
  .promote{
    width: 112px;
    height: 112px;
    position: absolute;
    bottom: 36px;
    left: 36px;
    &:last-child {
      left: 180px;
    }
  }
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
  padding: 40px;
}
.progress {
  li {
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
      background-image: url("../assets/images/watch@2x.png");
      background-size: 48px 48px;
    }
    &:nth-child(3) i {
      background-image: url("../assets/images/other@2x.png");
      background-size: 48px 48px;
    }
  }
  .equipment {
    width: 112px;
    text-align: center;
  }
  .bar {
    position: relative;
    width: 400px;
    height: 12px;
    div {
      position: absolute;
      top: 0;
      left: 0;
      width: 400px;
      height: 12px;
      &:first-child {
        background-color: #d8d8d8;
      }
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
}

.consult {
  .tips {
    margin-top: 10px;
  }
  li {    
    a {
        display: flex;
        justify-content: space-between;
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
        justify-content: space-between;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 1;  //1行
        -webkit-box-orient: vertical;
          p:first-child {
            font-size: 28px;
            line-height: 36px;
            height: 72px;
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
  animation-duration: 5s;
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


