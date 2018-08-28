<template>
  <div>
    <!-- Header部分 Start -->
    <header>
      <div class="left">
        <img :src="avatar" alt="">
        <span class="name">伍鑫</span>
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
      <img :src="promote" class="promote" />
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
            <div class="left">
              <p>{{item.title}}</p>
              <p>{{item.summary}}</p>
            </div>
            <img :src="avatar" alt="">
          </li>
        </ul>
      </div>
    </div>
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
        <p class="tips">签到成功</p>
        <p class="info">恭喜您获得20点能量</p>
        <button @click="handleAttendanceConfirm">确认</button>
      </div>
    </div>
    <!-- 签到弹框 End -->
  </div>
</template>

<script>
const avatar = require("@/assets/logo.png");
const attendance = require("@/assets/images/attendance_btn.png");
const promote = require("@/assets/images/promote_btn.png");
const bottom = require("@/assets/images/bottom_logo@2x.png");
const attendanceSuccess = require("@/assets/images/attendance.png");
const energyBall = require("@/assets/images/ball.png");

import { randomNum, createPositionArr, getArrItems } from '@/utils/utils.js'
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
      energyBallList:[],
      currentEnergy:0,
      currentPower:0,
      articleList:[],
      analysis:'',
      analysisCount:0,
    };
  },
  created() {
    this.getEnergyBall()
    this.getCurrentEnergy()
    this.getCurrentPower()
    this.getEnergyAnalysis()
    this.getArticleList()
  },
  filters: {
  },
  methods: {
    handleAttendance () {
      this.$axios.post('takeEnergyBall').then(({data}) => {
        console.log(data)
      })
      this.isShowMask = true
    },
    handleAttendanceConfirm () {
      this.isShowMask = false
    },
    getEnergyBall () {
      this.$axios.post('/inquireEnergyBall').then(({data:{data}}) => {
        let pArr = createPositionArr()
        this.energyBallList = data.energyBallList.map(el => {
          let randomIdx = randomNum(0,pArr.length - 1)
          let p = pArr[randomIdx]
          pArr.splice(randomIdx,1)
          el.x = p.x / 75 + 'rem'
          el.y = p.y / 75 + 'rem'
          return el
        })
      })
    },
    getCurrentEnergy () {
      this.$axios.post('inquireEnergyPoint').then(({data:{data}}) => {
        this.currentEnergy = data
      })
    },
    getCurrentPower () {
      this.$axios.post('inquirePower').then(({data:{data}}) => {
        this.currentPower = data
      })
    },
    getEnergyAnalysis () {
      this.$axios.post('inquireEnergyPointByCategory').then(({data:{data}}) => {
        this.analysis = data
        console.log(this.analysis)
        data.forEach(el => {
          this.analysisCount += el.value
        })
      })
    },
    getArticleList () {
      this.$axios.post('inquireNews').then(({data:{data}}) =>{
        this.articleList = data.rows
      })
    },
    formatSize: function (value) {
      if (value > 9999) {
        return 75 / 75 + 'rem'
      }
      if (value > 999) {
        return 64 / 75 + 'rem'
      }
      if (value > 0) {
        return 52 / 75 + 'rem'
      }
    },
    handleClickEnergy (event, data) {
      let ele = event.currentTarget
      this.$axios.post('takeEnergyBall').then(({data:{data}}) => {
        console.log(data)
      })
      ele.classList.add('fadeOutUp')
      ele.classList.remove('flash')
      ele.classList.remove('infinite')
      this.currentEnergy += data.value
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
    background-size: 750px 624px;
    .energy-ball {
      // animation: 1.5s twinkling infinite;
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
    display: flex;
    justify-content: space-between;
    height: 352px;
    padding: 40px 0;
    border-bottom: 1px solid #ddd;
    img {
      width: 448px;
      height: 256px;
      margin-left: 26px;
    }
    .left {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      p:first-child {
        font-size: 28px;
        line-height: 36px;
        height: 72px;
        overflow: hidden;
      }
      p:last-child {
        flex: 1;
        font-size: 24px;
        line-height: 34px;
        margin-top: 40px;
        overflow: hidden;
      }
    }
  }
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

.flash {
  animation-duration: 2.5s;
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


