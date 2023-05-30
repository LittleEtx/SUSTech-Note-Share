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
        <el-button v-if="$store.state.userInfo.userID === note.groupOwnerID" type="success" @click.stop="edit(note)">编辑</el-button>
<!--        <el-button v-if="$store.state.userInfo.userID === note.groupOwnerID" type="danger" @click.stop="disband(note.groupID)">解散</el-button>-->
<!--        <el-button v-else type="danger" @click.stop="remove(note.groupID)">退出</el-button>-->
        <el-popconfirm
            v-if="$store.state.userInfo.userID === note.groupOwnerID"
            confirm-button-text="是"
            cancel-button-text="否"
            icon-color="#ff0000"
            :icon="BellFilled"
            title="是否解散该群组?"
            @confirm.stop="disband(note.groupID)"
            @cancel.stop="cancelEvent"
        >
          <template #reference>
            <el-button type="danger">解散</el-button>
          </template>
        </el-popconfirm>
        <el-popconfirm
            v-else
            confirm-button-text="是"
            cancel-button-text="否"
            icon-color="#ff0000"
            :icon="BellFilled"
            title="是否退出该群组?"
            @confirm.stop="remove(note.groupID)"
            @cancel.stop="cancelEvent"
        >
          <template #reference>
            <el-button type="danger">退出</el-button>
          </template>
        </el-popconfirm>
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
          <el-button @click="resetForm1('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { BellFilled, Notebook, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// 打开页面时从后端读取存在的群组并做过滤，然后保存到groupList中；
// 读取当前用户加入的群组(群组名称、描述信息、创建日期)
export default {
  components: { Notebook, Plus },
  mounted () {
    this.nowUser = this.$store.state.userInfo.userID
    this.getData()
  },
  data () {
    return {
      nowUser: '',
      group: [
        {
          groupID: 1,
          groupName: '',
          createTime: '',
          groupDescription: '',
          groupOwnerID: 1,
          groupOwnerName: ''
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
        groupOwnerID: 1,
        groupOwnerName: ''
      },
      rules: {
        groupName: [
          { required: true, message: '群组名称不能为空', trigger: 'blur' }
        ]
      },
      groupList: [
        { groupID: '', groupName: '', groupDescription: '' }],
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
    selectedGroup () {
      return this.groupList.find(group => group.groupID === this.ruleForm.groupJoin)
    },
    BellFilled () {
      return BellFilled
    }
  },
  methods: {
    getData () {
      axios.get('/api/group/loadJoinedGroup').then(res => {
        this.group = res.data
      })
      axios.get('/api/group/loadEnjoinedGroup').then(res => {
        this.groupList = res.data
      })
    },
    cancelEvent () {
      console.log('operation cancel!')
    },
    createNote (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const d = new Date()
          this.form.createTime = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate()
          axios.post('/api/group/createGroup', {
            groupName: this.form.groupName,
            groupDescription: this.form.groupDescription,
            createTime: this.form.createTime
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success'
            })
            this.getData()
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
          axios.post('/api/group/updateGroup', {
            groupID: this.form.groupID,
            groupName: this.form.groupName,
            groupDescription: this.form.groupDescription
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success'
            })
            this.getData()
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
          axios.post('/api/group/joinGroup', {
            groupID: this.ruleForm.groupJoin
          }).then(res => {
            ElMessage({
              message: res.data,
              type: 'success'
            })
            this.getData()
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
    resetForm1 (formName) {
      this.$refs[formName].resetFields()
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
      axios.post('/api/group/quitGroup', {
        groupID: id
      }).then(res => {
        ElMessage.error(res.data)
        this.getData()
      })
    },
    // 解散群组
    disband (id) {
      axios.post('/api/group/deleteGroup', {
        groupID: id
      }).then(res => {
        ElMessage.error(res.data)
        this.getData()
      })
    },
    handleClickNoteCard (note, event) {
      console.log(event.target.tagName)
      if (event.target.tagName === 'BUTTON') {
        return
      }
      if (event.target.tagName === 'SPAN') {
        return
      }
      this.$router.push('/showTest/' + note.groupID)
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
