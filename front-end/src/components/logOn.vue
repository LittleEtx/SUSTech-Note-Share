<template>
  <div class="login_container">
    <div class="header">
      <img src="../assets/icon/icon_with_words_shadow.svg" style="height: 100px">
    </div>
  <div class="login_box">
    <Log v-show="!isPush" :isPush="isPush" @choseItem="choseItem"></Log>
    <re-pwd v-show="isPush"></re-pwd>
  </div>
  </div>
</template>

<script>
import Log from './Log'
import rePwd from './rePwd'
export default {
  name: 'logOn',
  data () {
    return {
      isPush: false
    }
  },
  methods: {
    choseItem () {
      this.isPush = true
    }
  },
  components: {Log, rePwd}
}
</script>

<style scoped>
.login_container {
  width: 100%;
  height: 100%;
  background: url(../assets/R-C.png);
  position: fixed;
  background-size: 100% 100%;
}
.header{
  height: 100px;
  width: 100px;
  position: relative;
  top: 15%;
  left: 42%;
}
.login_box {
  width: 600px;
  height: 370px;
  background-color: white;
  border: 1px solid #2c3e50;
  box-shadow: #2c3e50;
  border-radius: 5px;
  position: relative;
  left: 50%;
  top: 40%;
  transform: translate(-50%, -50%);
}
.login_box {
  backdrop-filter: blur(15px);
  box-shadow: 0 0 5px #fff;
}
</style>
