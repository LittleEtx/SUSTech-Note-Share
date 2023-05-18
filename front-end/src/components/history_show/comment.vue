<template>
  <div>
    <div v-for="(item,i) in comments" :key="i" class="author-title reply-father">
      <el-avatar class="header-img" :size="40" :src="item.headImg"></el-avatar>
      <div class="author-info">
        <span class="author-name">{{item.name}}</span>
        <span class="author-time">{{item.time}}</span>
      </div>
      <div class="icon-btn">
        <span><el-icon><ChatDotSquare /></el-icon>{{item.commentNum}}</span>
        <el-icon><Star /></el-icon>{{item.like}}
      </div>
      <div class="talk-box">
        <p>
          <span class="reply">{{item.comment}}</span>
        </p>
      </div>
      <div class="reply-box">
        <div v-for="(reply,j) in item.reply" :key="j" class="author-title">
          <el-avatar class="header-img" :size="40" :src="reply.fromHeadImg"></el-avatar>
          <div class="author-info">
            <span class="author-name">{{reply.from}}</span>
            <span class="author-time">{{reply.time}}</span>
          </div>
          <div class="icon-btn">
            <span><el-icon><ChatDotSquare /></el-icon>{{reply.commentNum}}</span>
            <span><el-icon><Star /></el-icon>{{reply.like}}</span>
          </div>
          <div class="talk-box">
            <p>
              <span>回复 {{reply.to}}:</span>
              <span class="reply">{{reply.comment}}</span>
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ChatDotSquare, Star} from '@element-plus/icons-vue'

export default {
  components: { ChatDotSquare, Star },
  name:'ArticleComment',
  data(){
    return{
      btnShow: false,
      index:'0',
      replyComment:'',
      myName:'Lana Del Rey',
      myHeader:'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
      myId:19870621,
      to:'',
      toId:-1,
      comments:[
        {
          name:'Lana Del Rey',
          id:19870621,
          headImg:'https://ae01.alicdn.com/kf/Hd60a3f7c06fd47ae85624badd32ce54dv.jpg',
          comment:'我发布一张新专辑Norman Fucking Rockwell,大家快来听啊',
          time:'2019年9月16日 18:43',
          commentNum:2,
          like:15,
          inputShow:false,
          reply:[
            {
              from:'Taylor Swift',
              fromId:19891221,
              fromHeadImg:'https://ae01.alicdn.com/kf/H94c78935ffa64e7e977544d19ecebf06L.jpg',
              to:'Lana Del Rey',
              toId:19870621,
              comment:'我很喜欢你的新专辑！！',
              time:'2019年9月16日 18:43',
              commentNum:1,
              like:15,
              inputShow:false
            },
            {
              from:'Ariana Grande',
              fromId:1123,
              fromHeadImg:'https://ae01.alicdn.com/kf/Hf6c0b4a7428b4edf866a9fbab75568e6U.jpg',
              to:'Lana Del Rey',
              toId:19870621,
              comment:'别忘记宣传我们的合作单曲啊',
              time:'2019年9月16日 18:43',
              commentNum:0,
              like:5,
              inputShow:false

            }
          ]
        }
      ]
    }
  },
  methods: {

  },
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
    @media screen and (max-width:1200px) {
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
  border-bottom: 1px solid rgba(178,186,194,.3)
.author-title
  padding 10px
  .header-img
    display inline-block
    vertical-align top
  .author-info
    display inline-block
    margin-left 5px
    width 60%
    height 40px
    line-height 20px
    >span
      display block
      overflow hidden
      white-space nowrap
      text-overflow ellipsis
    .author-name
      color #000
      font-size 18px
      font-weight bold
    .author-time
      font-size 14px
  .icon-btn
    width 30%
    padding 0 !important
    float right
    @media screen and (max-width : 1200px){
      width 20%
      padding 7px
    }
  .talk-box
    margin 0 50px
    >p
      margin 0
    .reply
      font-size 16px
      color #000
  .reply-box
    margin 10px 0 0 50px
    background-color #efefef
</style>

