<template>
  <div class="app-container">
    <el-card>
      <div class="mb20">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </div>

      <el-table v-loading="loading" stripe border :data="cardList">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="姓名" align="center" />
        <el-table-column prop="phone" label="手机号" align="center" />
        <el-table-column prop="company" label="单位" align="center" />
        <el-table-column prop="position" label="职位" align="center" />
        <el-table-column prop="create_time" label="创建时间" align="center" width="160" />
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

    <!-- 添加或修改对话框 -->
    <el-dialog
      :title="dialogType == 1 ? '新增' : '编辑'"
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
        <el-form-item label="单位" prop="company">
          <el-input v-model="form.company" placeholder="请输入单位" />
        </el-form-item>
        <el-form-item label="职位" prop="position">
          <el-input v-model="form.position" placeholder="请输入职位" />
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
import { listUserCard, saveUserCard } from '@/api/medical/user'

export default {
  name: 'MedicalUserCard',
  data() {
    return {
      loading: false,
      cardList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10
      },
      dialogVisible: false,
      dialogType: 1, // 1:新增 2:编辑
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
      listUserCard(this.queryParams).then(response => {
        this.cardList = response.data.list || []
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
          saveUserCard(this.form).then(() => {
            this.$modal.msgSuccess('保存成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用删除接口
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        name: '',
        phone: '',
        company: '',
        position: ''
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
</style>

