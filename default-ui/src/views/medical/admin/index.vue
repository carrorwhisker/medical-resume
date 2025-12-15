<template>
  <div class="app-container">
    <el-card>
      <div class="mb20">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >添加管理员</el-button>
      </div>
      
      <el-table v-loading="loading" stripe border :data="adminList">
        <el-table-column
          type="index"
          label="序号"
          width="80"
          align="center"
        />
        <el-table-column
          prop="name"
          label="用户名"
          align="center"
        />
        <el-table-column label="类型" align="center">
          <template slot-scope="scope">
            {{ scope.row.is_root == 1 ? "管理员" : "超级管理员" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          label="创建时间"
          align="center"
        />
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="handleEdit(scope.row)"
            >修改</el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              @click="handleDelete(scope.row)"
            >删除</el-button>
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
    
    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="dialogType == 1 ? '新增管理员' : '修改管理员'"
      :visible.sync="dialogVisible"
      width="600px"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="form.name" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
      </el-form>
      <div class="dialog-tip">
        <p style="color: #f56c6c">注：超级管理员才能添加管理员</p>
      </div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAdmin, addAdmin, updateAdmin, delAdmin } from '@/api/medical/admin'

export default {
  name: 'MedicalAdmin',
  data() {
    return {
      loading: false,
      adminList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10
      },
      dialogVisible: false,
      dialogType: 1, // 1:新增 2:修改
      form: {},
      rules: {
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
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
      listAdmin(this.queryParams).then(response => {
        this.adminList = response.data.list || []
        this.total = response.data.count || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.dialogType = 1
      this.dialogVisible = true
    },
    /** 修改按钮操作 */
    handleEdit(row) {
      this.reset()
      this.form = { ...row }
      this.dialogType = 2
      this.dialogVisible = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.dialogType == 1) {
            addAdmin(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            updateAdmin(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除管理员"' + row.name + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delAdmin({ id: row.id })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        name: null,
        password: null
      }
      this.resetForm('form')
    }
  }
}
</script>

<style scoped>
.mb20 {
  margin-bottom: 20px;
}
.dialog-tip {
  padding: 0 20px;
}
</style>

