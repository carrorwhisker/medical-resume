<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="mb10">
      <el-form :inline="true">
        <el-form-item label="姓名">
          <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table v-loading="loading" stripe border :data="userList">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column label="头像" align="center">
          <template slot-scope="scope">
            <el-avatar :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名" align="center" />
        <el-table-column prop="phone" label="手机号" align="center" />
        <el-table-column label="VIP状态" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.is_vip == 2 ? 'success' : 'info'">
              {{ scope.row.is_vip == 2 ? 'VIP' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="vip_time" label="VIP到期" align="center" width="160" />
        <el-table-column prop="create_time" label="注册时间" align="center" width="160" />
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.page"
        :limit.sync="queryParams.size"
        @pagination="getList"
      />
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      title="编辑用户"
      :visible.sync="dialogVisible"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="VIP状态" prop="is_vip">
          <el-radio-group v-model="form.is_vip">
            <el-radio :label="1">普通用户</el-radio>
            <el-radio :label="2">VIP</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, saveUser, delUser } from '@/api/medical/user'

export default {
  name: 'MedicalUserList',
  data() {
    return {
      loading: false,
      userList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10,
        name: '',
        phone: ''
      },
      dialogVisible: false,
      form: {},
      rules: {
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        const rows = response.rows || response.data?.list || []
        const total = response.total || response.data?.count || 0
        this.userList = rows.map(item => {
          return {
            ...item,
            name: item.name || item.username,
            phone: item.phone,
            avatar: item.avatar || item.img,
            is_vip: item.is_vip ?? item.isVip,
            vip_time: this.formatTs(item.vip_time || item.vipTime),
            create_time: this.formatTs(item.create_time || item.createTime || item.createTimeStamp)
          }
        })
        this.total = total
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 时间戳格式化
    formatTs(val) {
      if (!val) return ''
      const ts = (String(val).length === 10 ? Number(val) * 1000 : Number(val))
      if (Number.isNaN(ts)) return ''
      const d = new Date(ts)
      const pad = n => (n < 10 ? '0' + n : n)
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        page: 1,
        size: 10,
        name: '',
        phone: ''
      }
      this.getList()
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset()
      this.form = { ...row }
      this.dialogVisible = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          saveUser(this.form).then(() => {
            this.$modal.msgSuccess('保存成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除用户"' + row.name + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delUser({ id: row.id })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        name: '',
        phone: '',
        email: '',
        is_vip: 1
      }
      this.resetForm('form')
    }
  }
}
</script>

<style scoped>
.mb10 {
  margin-bottom: 10px;
}
</style>

