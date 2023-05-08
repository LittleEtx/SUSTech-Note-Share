<template>
  <div
    class="background"
    :style="{ backgroundImage: `url(${backgroundUrl})`}"
  >
    <!--  mask to blur background and click  -->
    <transition name="mask-fade">
      <div v-show="isShowBox" class="background-mask"></div>
    </transition>
    <div style="height: 100%; width: 100%; position: absolute;" @click="changeShow"></div>
    <!--  main container for login  -->
    <transition name="basic-fade">
      <div v-show="isShowBox" class="login-container">
        <div class="icon-header">
          <img :src="SUSTechNoteIcon" style="height: 100px" alt="">
        </div>
        <div class="login-box">
          <login-pane></login-pane>
        </div>
      </div>
    </transition>
    <!--  buttons for download and return  -->
    <transition name="basic-fade">
      <div class="buttons" v-show="!isShowBox">
        <!--suppress JSValidateTypes -->
        <el-button style="margin-right: 10px" type="info" :icon="HomeFilled" circle
                   @click="isShowBox = !isShowBox"
        ></el-button>
        <!--suppress JSValidateTypes -->
        <el-button style type="primary" :icon="Download" circle @click="downloadBackground"></el-button>
      </div>
    </transition>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import { HomeFilled, Download } from '@element-plus/icons-vue'
import LoginPane from '@/components/login_page/LoginPane.vue'
import { router } from '@/router'
import SUSTechNoteIcon from '@/assets/icon/icon_with_word.svg'

export default {
  components: { LoginPane },
  data () {
    return {
      isShowBox: true
    }
  },
  computed: {
    Download () {
      return Download
    },
    HomeFilled () {
      return HomeFilled
    },
    SUSTechNoteIcon () {
      return SUSTechNoteIcon
    },
    backgroundUrl: function () {
      const date = new Date()
      const year = date.getFullYear() //  返回的是年份
      let month = date.getMonth() + 1 //  返回的月份上个月的月份，记得+1才是当月
      if (month < 10) { month = '0' + month } // 格式化
      return new URL('../assets/login-page/background-img/' + year + '.' + month + '.png', import.meta.url).href
    }
  },
  beforeMount () {
    // check whether the user has logged in
    if (Cookies.get('satoken')) {
      // if token not acceptable, will jump back to this page
      router.push('home')
    }
  },
  methods: {
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

/*noinspection CssUnusedSymbol*/
.mask-fade-enter-active, .mask-fade-leave-active {
  transition: backdrop-filter .5s;
}
/*noinspection CssUnusedSymbol*/
.mask-fade-enter-from, .mask-fade-leave-to {
  backdrop-filter: blur(0px);
}
/*noinspection CssUnusedSymbol*/
.basic-fade-enter-active, .basic-fade-leave-active {
  transition: opacity .3s;
}
/*noinspection CssUnusedSymbol*/
.basic-fade-enter-from, .basic-fade-leave-to {
  opacity: 0;
}

.icon-header{
  scale: 125%;
  margin-bottom: 10px;
}
.login-box {
  width: 600px;
  height: 370px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 100;
  position: relative;
}

</style>
