<template>
  <div class="note-container">
    <el-card class="note-card create-card" shadow="always" @click.native="handleClickCreateCard">
      <div class="create-card-icon"><el-icon><Plus /></el-icon></div>
    </el-card>
    <el-card v-for="note in group" :key="note.id" class="note-card" shadow="always" @click="handleClickNoteCard(note, $event)">
      <div slot="header" class="note-header">
        <div class="note-title">
          <el-icon><Notebook /></el-icon>
          <span style="margin-left: 5px">{{ note.title }}</span></div>
        <div class="note-date">{{ note.date }}</div>
      </div>
      <div class="note-content">描述信息：{{ note.content }}</div>
      <div class="note-creator">群主：{{ note.creator }}</div>
      <div class="note-footer">
        <el-button v-if="nowUser === note.creator" type="success" @click.stop="edit(note)">编辑</el-button>
<!--        <el-popconfirm-->
<!--            confirm-button-text="是"-->
<!--            cancel-button-text="否"-->
<!--            icon-color="#626AEF"-->
<!--            title="请确认是否要解散/退出该群组？"-->
<!--            @confirm="remove(note.id)"-->
<!--            @cancel="cancelEvent"-->
<!--        >-->
<!--          <template #reference>-->
<!--            <el-button type="danger" round>删除</el-button>-->
<!--          </template>-->
<!--        </el-popconfirm>-->
        <el-button type="danger" @click.stop="remove(note.id)">删除</el-button>
      </div>
    </el-card>
    <el-dialog
        title="创建/加入群组"
        v-model="createFormVisible"
        width="500px"
    >
      <el-tabs v-model="activeName" class="demo-tabs">
        <el-tab-pane label="创建群组" name="creat"></el-tab-pane>
        <el-tab-pane label="加入群组" name="join"></el-tab-pane>
      </el-tabs>
      <div class="pane-style">
        <template v-if="activeName === 'creat'">
          <!--   创建群组表单     -->
          <el-form ref="form" :model="form" label-width="80px" :rules="rules">
            <el-form-item label="组名" prop="title">
              <el-input v-model="form.title"
                        placeholder="请输入群组名字"
                        onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="描述信息" prop="content">
              <el-input v-model="form.content" placeholder="请输入描述信息"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="createNote('form')">创建</el-button>
              <el-button @click="resetForm('form')">重置</el-button>
            </el-form-item>
          </el-form>
        </template>
        <template v-else>
          <!--   加入群组表单     -->
          <el-form ref="ruleForm" :model="ruleForm" label-width="120px" :rules="join_rules">
            <el-form-item label="选择加入的群组" prop="groupJoin">
              <el-select  v-model="ruleForm.groupJoin" placeholder="请选择群组">
                <el-option v-for="(item, index) in groupList"  :key="index" :label="item.name" :value="item" />
              </el-select>
            </el-form-item>
            <div class="description">
              描述信息：{{ ruleForm.groupJoin.description }}
            </div>
            <el-form-item>
              <el-button style="margin-top: 15px" type="primary" @click="joinGroup('ruleForm')">加入</el-button>
              <el-button style="margin-top: 15px" @click="cancelForm('ruleForm')">取消</el-button>
            </el-form-item>
          </el-form>
        </template>
      </div>
    </el-dialog>
    <el-dialog
        title="编辑群组信息"
        v-model="editFormVisible"
        width="500px"
    >
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="组名" prop="title">
          <el-input v-model="form.title"
                    placeholder="请输入群组名字"
                    onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
        </el-form-item>
        <el-form-item label="描述信息" prop="content">
          <el-input v-model="form.content" placeholder="请输入描述信息"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="createNote('form')">创建</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {useRouter} from "vue-router/dist/vue-router";
// 打开页面时从后端读取存在的群组并做过滤，然后保存到groupList中；
// 读取当前用户加入的群组(群组名称、描述信息、创建日期)
export default {
  data() {
    return {
      nowUser: '陈孙兵',
      group: [
        {
          id: 1,
          title: '高数',
          date: '2022-05-07',
          content: '这是该群组的描述',
          creator: '黄宇海'
        },
        {
          id: 2,
          title: '大物',
          date: '2022-05-06',
          content: '这是该群组的描述',
          creator: '杨子德'
        },
        {
          id: 3,
          title: '计网',
          date: '2022-05-05',
          content: '',
          creator: '陈孙兵'
        },
        {
          id: 4,
          title: '计网',
          date: '2022-05-05',
          content: '这是该群组的描述',
          creator: '陈孙兵'
        },
      ],
      activeName: 'creat',
      createFormVisible: false,
      editFormVisible: false,
      form: {
        id: 1,
        title: '',
        date: '',
        content: '',
        creator: '黄宇海'
      },
      rules: {
        title: [
          { required: true, message: '群组名称不能为空', trigger: 'blur' },
        ]
      },
      groupList: [
        {name:'高数', description: '这是高数的描述'},
        {name:'大物', description: '这是大物的描述'},
        {name:'计网', description: '这是计网的描述'},
        {name:'离散', description: '这是离散的描述'}],
      ruleForm: {
        groupJoin: ''
      },
      join_rules: {
        groupJoin: [
          { required: true, message: '请选择群组', trigger: 'blur' },
        ]
      },
    };
  },
  methods: {
    createNote(formName) {
      // 将当前用户（群主）、群组名称、群组描述、创建日期发给后端
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // alert('submit!');
          const d = new Date();
          const date = d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate();
          alert(date)
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    joinGroup(formName) {
      // 直接加入群组
      this.$refs[formName].validate((valid) => {
        if (valid) {
          alert('submit!');
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.form.title = '';
      this.form.content = '';
    },
    cancelForm(formName) {
      this.form.title = '';
      this.form.content = '';
      this.createFormVisible = false;
    },
    edit(note) {
      // 修改群信息(名称、描述信息)
      this.form = Object.assign({}, note);
      this.editFormVisible = true
    },
    remove(id) {
      // 解散或者退出群组
      this.group = this.group.filter(note => note.id !== id);
    },
    handleClickNoteCard(note, event) {
      // 处理点击卡片事件
      if (event.target.tagName === 'BUTTON') {
        return
      }
      this.$router.push('/login')
    },
    handleClickCreateCard() {
      // 处理创建新卡片的逻辑
      this.createFormVisible = true
    }
  },
};
</script>

<style scoped>
.note-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.note-card {
  width: 320px;
  height: 320px;
  position: relative;
  /*background-image: url('../../assets/icon/icon_with_words_shadow.svg');*/
  /*background-repeat: no-repeat;*/
  /*background-size: cover;*/
  /*background-position: center;*/
  /*opacity: 0.4; !* 控制背景图片的透明度 *!*/
  background-color: #f2f2f2;
}

.note-header {
  display: flex;
  justify-content: space-between;
}

.note-title {
  font-size: 18px;
  font-weight: bold;
}

.note-date {
  font-size: 14px;
  color: #999;
}

.note-content {
  font-size: 13px;
  margin-top: 16px;
}

.note-creator {
  margin-top: 16px;
}

.note-footer {
  position: absolute;
  bottom: 0;
  right: 0;
}
.create-card {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  color: #999999;
  cursor: pointer;
}

.create-card-icon {
  font-size: 100px;
  margin-bottom: 10px;
}

</style>
