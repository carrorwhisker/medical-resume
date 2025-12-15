<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="mb10">
      <el-form :inline="true">
        <el-form-item label="名称">
          <el-input v-model="queryParams.title" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <div class="mb20">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增简历</el-button>
        <el-button
          type="success"
          icon="el-icon-download"
          size="mini"
          :disabled="ids.length === 0"
          @click="handleExport"
        >导出</el-button>
      </div>

      <el-table
        v-loading="loading"
        stripe
        border
        :data="resumeList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column label="名称" prop="name" align="center" />
        <el-table-column label="头像" align="center">
          <template slot-scope="scope">
            <el-image
              style="width: 80px; height: 80px"
              :src="scope.row.img"
              :preview-src-list="[scope.row.img]"
            />
          </template>
        </el-table-column>
        <el-table-column label="职位" prop="position" align="center" />
        <el-table-column label="单位" prop="company" align="center" />
        <el-table-column label="地址" prop="address" align="center" />
        <el-table-column label="手机号" prop="phone" align="center" />
        <el-table-column label="邮箱" prop="email" align="center" />
        <el-table-column label="是否审核" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.is_status == 0" type="warning">申请中</el-tag>
            <el-tag v-else-if="scope.row.is_status == 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="create_time" align="center" width="160" />
        <el-table-column label="操作" align="center" fixed="right" width="240">
          <template slot-scope="scope">
            <el-button type="success" size="mini" @click="handleDetail(scope.row)">详情</el-button>
            <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              v-if="scope.row.is_status == 0"
              type="warning"
              size="mini"
              @click="handleAudit(scope.row)"
            >审核</el-button>
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

    <!-- 详情/审核对话框 -->
    <el-dialog
      :title="dialogType == 1 ? '详情' : '审核'"
      :visible.sync="detailVisible"
      width="1000px"
    >
      <el-descriptions title="简历信息" border :column="3">
        <el-descriptions-item label="头像">
          <el-image
            style="width: 80px; height: 80px"
            :src="detailInfo.img"
            :preview-src-list="[detailInfo.img]"
          />
        </el-descriptions-item>
        <el-descriptions-item label="名称">{{ detailInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ detailInfo.company }}</el-descriptions-item>
        <el-descriptions-item label="职位">{{ detailInfo.position }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detailInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ detailInfo.email }}</el-descriptions-item>
        <el-descriptions-item label="地址">{{ detailInfo.address }}</el-descriptions-item>
        <el-descriptions-item label="认证状态">
          {{ detailInfo.is_authentication == 1 ? '已认证' : '未认证' }}
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          {{ detailInfo.is_status == 1 ? '已通过' : '审核中' }}
        </el-descriptions-item>
        <el-descriptions-item label="是否开通vip">
          {{ detailInfo.is_vip == 2 ? '是' : '否' }}
        </el-descriptions-item>
        <el-descriptions-item label="个人介绍" :span="3">
          {{ detailInfo.content }}
        </el-descriptions-item>
        <el-descriptions-item label="附件" v-if="detailInfo.attachment" :span="3">
          {{ detailInfo.attachment_name }}
          <el-button type="primary" size="mini" @click="handleUpdateAttachment">修改附件名称</el-button>
          <el-button type="success" size="mini" @click="handleDownload(detailInfo.attachment)">下载</el-button>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" v-if="dialogType == 2">
        <el-button @click="detailVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAudit">通 过</el-button>
      </div>
    </el-dialog>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="formType == 1 ? '新增简历' : '编辑简历'"
      :visible.sync="formVisible"
      width="1000px"
      @closed="resetForm"
    >
      <el-form
        :model="form"
        :rules="rules"
        ref="form"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="头像" prop="img">
              <medical-image-upload v-model="form.img" :limit="1" />
            </el-form-item>
            <el-form-item label="头像原图" prop="img_yt">
              <medical-image-upload v-model="form.img_yt" :limit="1" />
              <div class="form-tip">用于VIP复制</div>
            </el-form-item>
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
            <el-form-item label="单位" prop="company">
              <el-input v-model="form.company" placeholder="请输入单位" />
            </el-form-item>
            <el-form-item label="职位" prop="position">
              <el-input v-model="form.position" placeholder="请输入职位" />
            </el-form-item>
            <el-form-item label="科室" prop="department">
              <el-select v-model="form.department" placeholder="请选择科室">
                <el-option
                  v-for="item in departmentList"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入手机号" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
            <el-form-item label="微信" prop="wechat">
              <el-input v-model="form.wechat" placeholder="请输入微信" />
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-cascader
                v-model="addressArr"
                placeholder="请选择地址"
                :options="cityOptions"
                :props="{ label: 'name', value: 'name' }"
                @change="handleCityChange"
                clearable
                filterable
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="个人介绍" prop="content">
              <el-input
                v-model="form.content"
                type="textarea"
                :rows="3"
                placeholder="请输入个人介绍"
              />
            </el-form-item>
            <el-form-item label="附件">
              <medical-file-upload
                v-model="form.attachment"
                :updateName="true"
                :fileName.sync="form.attachment_name"
              />
            </el-form-item>
            <el-form-item label="附件名称">
              <el-input v-model="form.attachment_name" placeholder="请输入附件名称" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer">
        <el-button @click="formVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listResume,
  delResume,
  examineResume,
  updateAttachment,
  createResume,
  updateResume,
  getDepartmentList
} from '@/api/medical/resume'
import { downloadFile } from '@/utils/medical-download'
import MedicalImageUpload from '@/components/MedicalImageUpload'
import MedicalFileUpload from '@/components/MedicalFileUpload'
import cityData from '@/utils/city.json'

export default {
  name: 'MedicalResume',
  components: {
    MedicalImageUpload,
    MedicalFileUpload
  },
  data() {
    return {
      loading: false,
      resumeList: [],
      total: 0,
      ids: [],
      queryParams: {
        page: 1,
        size: 10,
        title: ''
      },
      detailVisible: false,
      dialogType: 1, // 1:详情 2:审核
      detailInfo: {},
      formVisible: false,
      formType: 1, // 1:新增 2:编辑
      form: {},
      addressArr: [],
      cityOptions: cityData,
      departmentList: [],
      rules: {
        img: [{ required: true, message: '请上传头像', trigger: 'blur' }],
        img_yt: [{ required: true, message: '请上传头像原图', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        company: [{ required: true, message: '请输入单位', trigger: 'blur' }],
        position: [{ required: true, message: '请输入职位', trigger: 'blur' }],
        department: [{ required: true, message: '请选择科室', trigger: 'change' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        address: [{ required: true, message: '请选择地址', trigger: 'blur' }],
        content: [{ required: true, message: '请输入个人介绍', trigger: 'blur' }],
        wechat: [{ required: true, message: '请输入微信', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getDepartmentList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listResume(this.queryParams).then(response => {
        const rows = response.rows || response.data?.list || []
        const total = response.total || response.data?.count || 0
        // 适配后端字段 & 格式化时间
        this.resumeList = rows.map(item => {
          return {
            ...item,
            create_time: this.formatTs(item.create_time || item.createTime || item.createTimeStamp),
            update_time: this.formatTs(item.update_time || item.updateTime || item.updateTimeStamp),
            // 后端未返回认证状态，前端默认0
            is_authentication: item.is_authentication ?? 0
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
    /** 获取科室列表 */
    getDepartmentList() {
      getDepartmentList().then(response => {
        this.departmentList = response.data || []
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
        title: ''
      }
      this.getList()
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.formType = 1
      this.formVisible = true
    },
    /** 编辑按钮操作 */
    handleEdit(row) {
      this.reset()
      this.form = { ...row }
      if (row.address) {
        this.addressArr = row.address.split('/')
      }
      this.formType = 2
      this.formVisible = true
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      this.dialogType = 1
      this.detailInfo = { ...row }
      this.detailVisible = true
    },
    /** 审核按钮操作 */
    handleAudit(row) {
      this.dialogType = 2
      this.detailInfo = { ...row }
      this.detailVisible = true
    },
    /** 提交审核 */
    submitAudit() {
      examineResume({ id: this.detailInfo.id, is_status: 1 }).then(() => {
        this.$modal.msgSuccess('审核成功')
        this.detailVisible = false
        this.getList()
      })
    },
    /** 修改附件名称 */
    handleUpdateAttachment() {
      this.$prompt('请输入附件名称', '修改附件名称', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        if (!value || !value.trim()) {
          this.$message.error('请输入附件名称')
          return
        }
        updateAttachment({
          id: this.detailInfo.id,
          attachment_name: value
        }).then(() => {
          this.$modal.msgSuccess('修改成功')
          this.detailInfo.attachment_name = value
          this.getList()
        })
      }).catch(() => {})
    },
    /** 下载附件 */
    handleDownload(url) {
      if (url) {
        window.open(url)
      }
    },
    /** 提交表单 */
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.formType == 1) {
            createResume(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.formVisible = false
              this.getList()
            })
          } else {
            updateResume(this.form).then(() => {
              this.$modal.msgSuccess('修改成功')
              this.formVisible = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm('是否确认删除简历"' + row.name + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delResume({ id: row.id })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      if (this.ids.length === 0) {
        this.$message.error('请选择要导出的简历')
        return
      }
      downloadFile(
        'Admin/AdminCard/cardExcel?id=' + this.ids.join(','),
        '简历列表.xlsx'
      )
    },
    /** 城市选择 */
    handleCityChange(value) {
      if (value && value.length) {
        this.form.city = value[0]
        this.form.address = this.addressArr.join('/')
      }
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        img: '',
        img_yt: '',
        name: '',
        company: '',
        position: '',
        department: '',
        phone: '',
        email: '',
        address: '',
        content: '',
        wechat: '',
        attachment: '',
        attachment_name: '简历',
        city: ''
      }
      this.addressArr = []
      this.resetForm('form')
    }
  }
}
</script>

<style scoped>
.mb10 {
  margin-bottom: 10px;
}
.mb20 {
  margin-bottom: 20px;
}
.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>

