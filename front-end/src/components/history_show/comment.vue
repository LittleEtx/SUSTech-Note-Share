<template>
  <div>
    <h2>评论区</h2>
    <form @submit.prevent="addComment" class="comment-form">
      <!-- 表单内容 -->
      <div class="form-group">
        <label for="author">作者</label>
        <input type="text" id="author" v-model="newComment.author" required>
      </div>
      <div class="form-group">
        <label for="comment">评论</label>
        <textarea id="comment" v-model="newComment.text" required></textarea>
      </div>
      <div class="form-group">
        <label for="avatar">头像</label>
        <input type="text" id="avatar" v-model="newComment.avatar" placeholder="https://example.com/avatar.png">
      </div>
      <button type="submit">提交评论</button>
    </form>

    <ul>
      <li v-for="comment in comments" :key="comment.id" class="comment">

        <div class="comment-header">
          <!-- 评论者头像和名称 -->
          <div class="avatar">
            <img :src="comment.avatar" alt="用户头像">
          </div>
        </div>

        <div class="author">{{ comment.author }}</div>

        <div class="comment-content">
          <!-- 评论内容 -->
            <div class="text">{{ comment.text }}</div>
        </div>

        <div v-if="index < comments.length - 1" class="comment-separator"></div>
      </li>
    </ul>
  </div>
</template>



<script>
export default {
  name: "comment",
  props:{
    notebookId: {
      required: true,
      type: String
    }
  },
  data() {
    return {
      comments: [
        { id: 1, author: '小明', text: '这是第一条评论', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        { id: 2, author: '小红', text: '这是第二条评论', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
      ],
      newComment: {
        author: '',
        text: '',
        avatar: '',
      },
    };
  },
  methods: {
    addComment() {
      const newId = this.comments.length + 1;
      this.comments.push({ ...this.newComment, id: newId });
      this.newComment.author = '';
      this.newComment.text = '';
      this.newComment.avatar = '';
    },
  },
};
</script>

<style scoped>
.comment {
  display: flex;
  margin-bottom: 20px;
}

.comment-separator {
  border: none;
  border-top: 1px dashed #ccc;
  margin: 10px 0;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 10px;
}

.avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.comment-content {
  flex: 1;
}

.author {
  font-weight: bold;
  margin-bottom: 5px;
}

.text {
  white-space: pre-wrap;
}

.comment-form {
  margin-top: 30px;
}

.form-group {
  margin-bottom: 10px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box;
}

button[type="submit"] {
  background-color: #007aff;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  cursor: pointer;
}
</style>
