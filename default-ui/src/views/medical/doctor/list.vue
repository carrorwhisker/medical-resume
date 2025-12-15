<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="mb10">
      <el-form :inline="true">
        <el-form-item label="姓名">
          <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table v-loading="loading" stripe border :data="doctorList">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="姓名" align="center" />
        <el-table-column prop="phone" label="手机号" align="center" />
        <el-table-column prop="company" label="单位" align="center" />
        <el-table-column prop="position" label="职位" align="center" />
        <el-table-column prop="department" label="科室" align="center" />
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

    <!-- 编辑对话框 -->
    <el-dialog
      title="编辑医生"
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
        <el-form-item label="科室" prop="department">
          <el-input v-model="form.department" placeholder="请输入科室" />
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
import { listDoctor, saveDoctor } from '@/api/medical/doctor'

export default {
  name: 'MedicalDoctorList',
  data() {
    return {
      loading: false,
      doctorList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10,
        name: ''
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
      listDoctor(this.queryParams).then(response => {
        this.doctorList = response.data.list || []
        this.total = response.data.count || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
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
        name: ''
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
          saveDoctor(this.form).then(() => {
            this.$modal.msgSuccess('保存成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除医生"' + row.name + '"?', '警告', {
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
        position: '',
        department: ''
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

