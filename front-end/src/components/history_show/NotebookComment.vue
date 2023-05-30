<template>
  <div>
    <div v-clickoutside="hideReplyBtn" @click="inputFocus" class="my-reply">
      <el-avatar class="header-img" :size="40" :src="myHeader"></el-avatar>
      <div class="reply-info" >
        <div
            tabindex="0"
            contenteditable="true"
            id="replyInput"
            spellcheck="false"
            placeholder="输入评论..."
            class="reply-input"
            @focus="showReplyBtn"
            @input="onDivInput($event)"
        >
        </div>
      </div>
      <div class="reply-btn-box" v-show="btnShow">
        <el-button class="reply-btn" size="default" @click="sendComment" type="primary">发表评论</el-button>
      </div>
    </div>
    <div v-for="(item,i) in comments" :key="i" class="author-title reply-father">
      <el-link
        style="margin-left: -100px"
        :underline="false"
        @click="$router.push({ name: 'user', params: { userID: item.comment.userID }})">
        <el-avatar :size="40" :src="item.comment.headImg" class="header-img"></el-avatar>
        <div class="author-info">
          <span class="author-name">{{ item.comment.name }}</span>
          <span class="author-time">{{ item.comment.commentTime }}</span>
        </div>
      </el-link>
      <div class="icon-btn">
        <span @click="showReplyInput(i,item.comment.name,item.id)"><el-icon><ChatDotSquare/></el-icon>{{
            item.comment.replyNum
          }}  </span>

        <el-button v-if="item.comment.userID===myId" :icon="Delete" text type="danger"
                   @click="deleteComment(item.comment.commentID)"></el-button> <!-- Delete button -->
        <i class="iconfont el-icon-caret-top"></i>{{ item.like }}
      </div>
      <div class="talk-box">
        <p>
          <span class="reply">{{ item.comment.commentContent }}</span>
        </p>
      </div>
      <div class="reply-box">
        <div v-for="(reply,j) in item.comment.replies" :key="j" class="author-title">
          <el-link :underline="false"
                   @click="$router.push({ name: 'user', params: { userID: reply.userID }})">
            <el-avatar :size="40" :src="reply.userAvatar" class="header-img"></el-avatar>
            <div class="author-info">
              <span class="author-name">{{ reply.userName }}</span>
              <span class="author-time">{{ reply.replyTime }}</span>
            </div>
          </el-link>
          <div class="icon-btn">
            <span @click="showReplyInput(i,reply.userName,reply.id)">
              <el-icon><ChatDotSquare/></el-icon></span>
            <i class="iconfont el-icon-caret-top"></i>{{}}
            <el-button v-if="reply.userID===myId" :icon="Delete" text type="danger"
                       @click="deleteReply(item.comment.commentID, reply.replyID)"></el-button> <!-- Delete button -->
          </div>
          <div class="talk-box">
            <p>
              <span>回复 {{ reply.toUserName }}:</span>
              <span class="reply">{{ reply.replyContent }}</span>

            </p>
          </div>

        </div>
      </div>
      <div v-show="_inputShow(i)" class="my-reply my-comment-reply">
        <el-avatar :size="40" :src="myHeader" class="header-img"></el-avatar>
        <div class="reply-info">
          <div class="reply-input reply-comment-input" contenteditable="true" placeholder="输入评论..."
               spellcheck="false"
               tabindex="0" @input="onDivInput($event)"></div>
        </div>
        <div class=" reply-btn-box">
          <el-button class="reply-btn" size="default" type="primary" @click="sendCommentReply(i,j)">发表评论</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ChatDotSquare, Delete } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessageBox } from 'element-plus'
import UserAvatar from '@/components/UserAvatar.vue'
import { apiGetUserInfo } from '@/scripts/API_User'

const clickoutside = {
  // 初始化指令
  bind (el, binding, vnode) {
    function documentHandler (e) {
      // 这里判断点击的元素是否是本身，是本身，则返回
      if (el.contains(e.target)) {
        return false
      }
      // 判断指令中是否绑定了函数
      if (binding.expression) {
        // 如果绑定了函数 则调用那个函数，此处binding.value就是handleClose方法
        binding.value(e)
      }
    }

    // 给当前元素绑定个私有变量，方便在unbind中可以解除事件监听
    el.vueClickOutside = documentHandler
    document.addEventListener('click', documentHandler)
  },
  update () {
  },
  unbind (el, binding) {
    // 解除事件监听
    document.removeEventListener('click', el.vueClickOutside)
    delete el.vueClickOutside
  }
}
export default {
  computed: {
    Delete () {
      return Delete
    }
  },
  props: {
    notebookId: {
      type: String,
      required: true
    }
  },
  mounted () {
    this.getData()
  },
  components: { UserAvatar, ChatDotSquare },
  data () {
    return {
      btnShow: false,
      index: '0',
      replyComment: '',
      myName: 'Lana Del Rey',
      myHeader: 'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
      myId: 19870621,
      to: '',
      toId: -1,
      comments: []
    }
  },
  directives: { clickoutside },
  methods: {
    async deleteComment (index) {
      try {
        await ElMessageBox.confirm('确定删除该评论？评论下所有回复也将被一并删除', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          confirmButtonClass: 'el-button--danger',
          type: 'warning'
        })
      } catch (e) {
        return
      }
      await axios.delete('/api/interact/comments/delete-comment', {
        params: {
          comment: index
        }
      })
      this.getData()
    },
    async deleteReply (commentIndex, replyIndex) {
      try {
        await ElMessageBox.confirm('确认删除该回复？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          confirmButtonClass: 'el-button--danger',
          type: 'warning'
        })
      } catch (e) {
        return
      }
      await axios.delete('/api/interact/comments/delete-reply', {
        params: {
          comment: commentIndex,
          reply: replyIndex
        }
      })
      this.getData()
    },
    getData () {
      axios.get('/api/user/get-info').then(op => {
        this.myName = op.data.userName
        this.myHeader = op.data.avatar
        this.myId = op.data.userID
      })
      axios.get('/api/interact/comments/get-notebook-comments', {
        params: {
          notebook: this.notebookId,
        }
      }).then(res => {
        console.log(res)
        this.comments = res.data
        for (let i = 0; i < res.data.length; i++) {
          this.comments[i].comment.inputShow = false
          apiGetUserInfo(this.comments[i].comment.userID)
              .then(op => {
            this.comments[i].comment.name = op.userName
            this.comments[i].comment.headImg = op.avatar
          })
        }
        console.log(this.comments)
      })
      console.log('刷新成功')
    },
    inputFocus () {
      var replyInput = document.getElementById('replyInput')
      replyInput.style.padding = '8px 8px'
      replyInput.style.border = '2px solid blue'
      replyInput.focus()
    },
    showReplyBtn () {
      this.btnShow = true
    },
    hideReplyBtn () {
      this.btnShow = false
      replyInput.style.padding = '10px'
      replyInput.style.border = 'none'
    },
    showReplyInput (i, name, id) {
      this.comments[this.index].comment.inputShow = false
      this.index = i
      this.comments[i].comment.inputShow = true
      this.to = name
      this.toId = id
    },
    _inputShow (i) {
      return this.comments[i].comment.inputShow
    },
    sendComment () {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      } else {
        let input = document.getElementById('replyInput')
        console.log(this.notebookId)
        console.log(this.replyComment)
        axios.post('/api/interact/comments/comment', {}, {
          params: {
            notebookID: this.notebookId,
            comment: this.replyComment,
          }
        }).then(res => {
        })
        this.replyComment = ''
        input.innerHTML = ''
        setTimeout(() => {
          // 执行你的操作
          this.getData()
        }, 500)
        this.$forceUpdate()
      }
    },
    sendCommentReply (i, j) {
      if (!this.replyComment) {
        this.$message({
          showClose: true,
          type: 'warning',
          message: '评论不能为空'
        })
      } else {
        let a = {}
        let timeNow = new Date().getTime()
        let time = this.dateStr(timeNow)
        axios.post('/api/interact/comments/reply', {
          content: this.replyComment,
          toUserName: this.to,
          commentID: this.comments[i].comment.commentID
        }, {}).then(res => {
        })

        this.replyComment = ''
        document.getElementsByClassName('reply-comment-input')[i].innerHTML = ''
        setTimeout(() => {
          // 执行你的操作
          this.getData()
        }, 1000)
      }
    },
    onDivInput: function (e) {
      this.replyComment = e.target.innerHTML
    },
    dateStr (date) {
      //获取js 时间戳
      var time = new Date().getTime()
      //去掉 js 时间戳后三位，与php 时间戳保持一致
      time = parseInt((time - date) / 1000)
      //存储转换值
      var s
      if (time < 60 * 10) {//十分钟内
        return '刚刚'
      } else if ((time < 60 * 60) && (time >= 60 * 10)) {
        //超过十分钟少于1小时
        s = Math.floor(time / 60)
        return s + '分钟前'
      } else if ((time < 60 * 60 * 24) && (time >= 60 * 60)) {
        //超过1小时少于24小时
        s = Math.floor(time / 60 / 60)
        return s + '小时前'
      } else if ((time < 60 * 60 * 24 * 30) && (time >= 60 * 60 * 24)) {
        //超过1天少于30天内
        s = Math.floor(time / 60 / 60 / 24)
        return s + '天前'
      } else {
        //超过30天ddd
        var date = new Date(parseInt(date))
        return date.getFullYear() + '/' + (date.getMonth() + 1) + '/' + date.getDate()
      }
    }
  }
}
</script>

<style lang="stylus" scoped>
.my-reply
  padding 10px
  background-color #fafbfc

  .header-img
    display inline-block
    vertical-align top

  .reply-info
    display inline-block
    margin-left 5px
    width 90%
    @media screen and (max-width: 1200px) {
      width 80%
    }

    .reply-input
      min-height 20px
      line-height 22px
      padding 10px 10px
      color #ccc
      background-color #fff
      border-radius 5px

      &:empty:before
        content attr(placeholder)

      &:focus:before
        content none

      &:focus
        padding 8px 8px
        border 2px solid blue
        box-shadow none
        outline none

  .reply-btn-box
    height 25px
    margin 10px 0

    .reply-btn
      position relative
      float right
      margin-right 15px

.my-comment-reply
  margin-left 50px

  .reply-input
    width flex

.author-title:not(:last-child)
  border-bottom: 1px solid rgba(178, 186, 194, .3)

.author-title
  padding 5px

  .header-img
    display inline-block
    vertical-align top

  .author-info
    margin-top -20px
    display inline-block
    margin-left 1px
    width 80%
    height 20px
    line-height 20px
    text-align: left

    > span
      display block
      cursor pointer
      overflow hidden
      white-space nowrap
      text-overflow ellipsis

    .author-name
      color #000
      font-size 12px
      font-weight bold

    .author-time
      font-size 10px

  .icon-btn
    width 30%
    padding 0 !important
    float right
    @media screen and (max-width: 1200px) {
      width 20%
      padding 7px
    }

    > span
      cursor pointer

    .iconfont
      margin 0 5px

  .talk-box
    margin 0 50px

    > p
      margin 0

    .reply
      font-size 16px
      color #000

  .reply-box
    margin 10px 0 0 50px
    background-color #efefef
</style>
