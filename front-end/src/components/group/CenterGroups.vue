<template>
  <div class="note-container">
    <el-card class="note-card create-card" shadow="hover" @click="handleClickCreateCard">
      <div class="create-card-icon"><el-icon><Plus /></el-icon></div>
    </el-card>
    <el-card v-for="note in group" :key="note.groupID" class="note-card" shadow="hover" @click="handleClickNoteCard(note, $event)">
      <template #header>
        <div class="note-title note-header">
          <el-icon><Notebook /></el-icon>
          <span style="margin-left: 5px">{{ note.groupName }}</span></div>
        <div class="note-date">{{ note.createTime }}</div>
      </template>
      <div class="note-content">描述信息：{{ note.groupDescription }}</div>
      <div class="note-creator">群主：{{ note.groupOwnerName }}</div>
      <div class="note-footer">
        <el-button v-if="nowUser === note.groupOwnerID" type="success" @click.stop="edit(note)">编辑</el-button>
        <el-button v-if="nowUser === note.groupOwnerID" type="danger" @click.stop="disband(note.groupID)">解散</el-button>
        <el-button v-else type="danger" @click.stop="remove(note.groupID)">退出</el-button>
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
            <el-form-item label="组名" prop="groupName">
              <el-input v-model="form.groupName"
                        placeholder="请输入群组名字"
                        onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
            </el-form-item>
            <el-form-item label="描述信息" prop="groupDescription">
              <el-input v-model="form.groupDescription" placeholder="请输入描述信息"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="createNote('form')">创建</el-button>
              <el-button @click="resetForm()">重置</el-button>
            </el-form-item>
          </el-form>
        </template>
        <template v-else>
          <!--   加入群组表单     -->
          <el-form ref="ruleForm" :model="ruleForm" label-width="120px" :rules="join_rules">
            <el-form-item label="选择加入的群组" prop="groupJoin">
              <el-select  v-model="ruleForm.groupJoin" placeholder="请选择群组">
                <el-option v-for="(item, index) in groupList"  :key="index" :label="item.groupName" :value="item.groupID" />
              </el-select>
            </el-form-item>
            <div v-if="selectedGroup" style="margin-top: 10px; color: #999">描述信息：{{ selectedGroup.groupDescription }}</div>
            <el-form-item>
              <el-button style="margin-top: 15px" type="primary" @click="joinGroup('ruleForm')">加入</el-button>
              <el-button style="margin-top: 15px" @click="cancelForm()">取消</el-button>
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
        <el-form-item label="组名" prop="groupName">
          <el-input v-model="form.groupName"
                    placeholder="请输入群组名字"
                    onkeyup="this.value=this.value.replace(/\s+/g,'')"></el-input>
        </el-form-item>
        <el-form-item label="描述信息" prop="groupDescription">
          <el-input v-model="form.groupDescription" placeholder="请输入描述信息"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="editNote('form')">编辑</el-button>
          <el-button @click="resetForm()">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { Notebook, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'
// 打开页面时从后端读取存在的群组并做过滤，然后保存到groupList中；
// 读取当前用户加入的群组(群组名称、描述信息、创建日期)
export default {
  components: { Notebook, Plus },
  mounted () {
    this.getData()
    setInterval(() => {
      this.getData()
    }, 100)
  },
  data () {
    return {
      nowUser: 12112628,
      group: [
        {
          groupID: 1,
          groupName: '高数',
          createTime: '2022-05-07',
          groupDescription: '这是该群组的描述',
          groupOwnerID: 12112628,
          groupOwnerName: '黄宇海'
        },
        {
          groupID: 2,
          groupName: '大物',
          createTime: '2022-05-06',
          groupDescription: '这是该群组的描述',
          groupOwnerID: 12112628,
          groupOwnerName: '杨子德'
        },
        {
          groupID: 3,
          groupName: '计网',
          createTime: '2022-05-05',
          groupDescription: '',
          groupOwnerID: 12112628,
          groupOwnerName: '陈孙兵'
        },
        {
          groupID: 4,
          groupName: '计网',
          createTime: '2022-05-05',
          groupDescription: '这是该群组的描述',
          groupOwnerID: 12112628,
          groupOwnerName: '陈孙兵'
        }
      ],
      activeName: 'creat',
      createFormVisible: false,
      editFormVisible: false,
      form: {
        groupID: 1,
        groupName: '',
        createTime: '',
        groupDescription: '',
        groupOwnerID: 12112628,
        groupOwnerName: '李田'
      },
      rules: {
        groupName: [
          { required: true, message: '群组名称不能为空', trigger: 'blur' }
        ]
      },
      groupList: [
        {groupID: '', groupName: '高数', groupDescription: '这是高数的描述' },
        {groupID: '', groupName: '大物', groupDescription: '这是大物的描述' },
        {groupID: '', groupName: '计网', groupDescription: '这是计网的描述' },
        {groupID: '', groupName: '离散', groupDescription: '这是离散的描述' }],
      ruleForm: {
        groupJoin: ''
      },
      join_rules: {
        groupJoin: [
          { required: true, message: '请选择群组', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    selectedGroup() {
      return this.groupList.find(group => group.groupID === this.ruleForm.groupJoin)
    }
  },
  methods: {
    getData(){
      axios.get("http://10.32.58.153:8088/api/group/loadJoinedGroup").then(res => {
        this.group = res.data;
      });
      axios.get("http://10.32.58.153:8088/api/group/loadEnjoinedGroup").then(res => {
        this.groupList = res.data;
      });
    },
    createNote (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const d = new Date()
          this.form.createTime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          axios.post("http://10.32.58.153:8088/api/group/createGroup",{
            groupName: this.form.groupName,
            groupDescription: this.form.groupDescription,
            createTime: this.form.createTime
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          })
          this.createFormVisible = false
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    editNote (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const d = new Date()
          this.form.createTime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          axios.post("http://10.32.58.153:8088/api/group/updateGroup",{
            groupID: this.form.groupID,
            groupName: this.form.groupName,
            groupDescription: this.form.groupDescription
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          })
          this.editFormVisible = false
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    joinGroup (formName) {
      // 直接加入群组
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post("http://10.32.58.153:8088/api/group/joinGroup",{
            groupID: this.ruleForm.groupJoin
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success',
            })
          })
          this.createFormVisible = false
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm () {
      this.form.groupName = ''
      this.form.groupDescription = ''
    },
    cancelForm () {
      this.form.groupName = ''
      this.form.groupDescription = ''
      this.createFormVisible = false
    },
    edit (note) {
      this.form = Object.assign({}, note)
      this.editFormVisible = true
    },
    // 退出群组
    remove (id) {
      axios.post("http://10.32.58.153:8088/api/group/quitGroup",{
        groupID: id
      }).then(res => {
        ElMessage.error(res.data)
      })
    },
    //解散群组
    disband(id) {
      axios.post("http://10.32.58.153:8088/api/group/deleteGroup",{
        groupID: id
      }).then(res => {
        ElMessage.error(res.data)
      })
    },
    handleClickNoteCard (note, event) {
      if (event.target.tagName === 'BUTTON') {
        return
      }
      this.$router.push('/showTest')
    },
    handleClickCreateCard () {
      this.form.groupName = ''
      this.form.groupDescription = ''
      this.ruleForm.groupJoin = ''
      this.createFormVisible = true
    }
  }
}
</script>

<style scoped>
.note-container {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.note-card {
  width: 270px;
  height: 250px;
  position: relative;
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
