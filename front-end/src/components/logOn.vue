<template>
  <div class="background" :style="{ backgroundImage: `url(${backgroundUrl})`}">
    <!--  mask to blur background and click  -->
    <transition name="mask-fade">
      <div v-show="isShowBox" class="background-mask"></div>
    </transition>
    <div style="height: 100%; width: 100%; position: absolute;" @click="changeShow"></div>
    <!--  main container for login  -->
    <transition name="basic-fade">
      <div v-show="isShowBox" class="login-container">
        <div class="header">
          <img src="../assets/icon/icon_with_words_shadow.svg" style="height: 100px">
        </div>
        <div class="login-box">
          <Log v-show="!isPush" :isPush="isPush" @choseItem="choseItem"></Log>
          <re-pwd v-show="isPush"></re-pwd>
        </div>
      </div>
    </transition>
    <!--  buttons for download and return  -->
    <transition name="basic-fade">
      <div class="buttons" v-show="!isShowBox">
        <el-button style="margin-right: 10px" type="info" icon="el-icon-s-home" circle
                   @click="isShowBox = !isShowBox" :disabled="!isShowBox"></el-button>
        <el-button style type="primary" icon="el-icon-download" circle @click="downloadBackground"></el-button>
      </div>
    </transition>
  </div>
</template>

<script>
import Log from './Log'
import rePwd from './rePwd'
export default {
  name: 'logOn',
  data () {
    return {
      isPush: false,
      isShowBox: true
    }
  },
  methods: {
    choseItem () {
      this.isPush = true
    },
    changeShow () {
      this.isShowBox = !this.isShowBox
    },
    downloadBackground () {
      const a = document.createElement('a') // 生成一个a元素
      const event = new MouseEvent('click') // 创建一个单击事件
      a.download = 'background.png' // 设置图片名称
      a.href = this.backgroundUrl // 将生成的URL设置为a.href属性
      a.dispatchEvent(event) // 触发a的单击事件
    }
  },
  components: {Log, rePwd},
  computed: {
    backgroundUrl: function () {
      const date = new Date()
      const year = date.getFullYear() //  返回的是年份
      let month = date.getMonth() + 1 //  返回的月份上个月的月份，记得+1才是当月
      if (month < 10) { month = '0' + month } // 格式化
      return require('../assets/login-page/background-img/' + year + '.' + month + '.png')
    }
  }
}
</script>

<style scoped>
.background {
  position: absolute;
  background-position: center;
  background-size: cover;
  width: 100%;
  height: 100%;
}

.background-mask {
  height: 100%;
  width: 100%;
  position: absolute;
  backdrop-filter: blur(3px);
}
.mask-fade-enter-active, .mask-fade-leave-active {
  transition: backdrop-filter .5s;
}

.mask-fade-enter, .mask-fade-leave-to {
  backdrop-filter: blur(0px);
}

.buttons {
  position: absolute;
  top: 30px;
  right: 30px;
  z-index: 100;
}

.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.basic-fade-enter-active, .basic-fade-leave-active {
  transition: opacity .3s;
}
.basic-fade-enter, .basic-fade-leave-to {
  opacity: 0;
}

.header{
  scale: 125%;
  margin-bottom: 10px;
}
.login-box {
  width: 600px;
  height: 370px;
  background-color: white;
  border: 1px solid #2c3e50;
  box-shadow: #2c3e50;
  border-radius: 5px;
}
.login-box {
  backdrop-filter: blur(15px);
  box-shadow: 0 0 5px #fff;
}
</style>
