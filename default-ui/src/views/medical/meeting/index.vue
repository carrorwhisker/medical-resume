<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="mb10">
      <el-form :inline="true">
        <el-form-item label="会议名称">
          <el-input
            v-model="queryParams.meeting_name"
            placeholder="请输入会议名称"
            clearable
          />
        </el-form-item>
        <el-form-item label="城市">
          <el-select v-model="queryParams.city" placeholder="请选择城市" clearable>
            <el-option label="全部" value="" />
            <el-option
              v-for="item in areaList"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否付费">
          <el-select v-model="queryParams.is_price_status" placeholder="请选择" clearable>
            <el-option label="全部" value="" />
            <el-option label="免费" value="1" />
            <el-option label="付费" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
        >添加会议</el-button>
        <el-button
          type="success"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </div>

      <el-table v-loading="loading" stripe border :data="meetingList">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="meeting_name" label="标题" align="center" />
        <el-table-column label="封面" align="center">
          <template slot-scope="scope">
            <el-image
              style="width: 80px; height: 80px"
              :src="scope.row.meeting_img"
              :preview-src-list="[scope.row.meeting_img]"
            />
          </template>
        </el-table-column>
        <el-table-column label="是否免费" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.is_price_status == 1 ? 'success' : 'warning'">
              {{ scope.row.is_price_status == 1 ? '免费' : '付费' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" align="center" />
        <el-table-column prop="qy_name" label="城市" align="center" />
        <el-table-column prop="meeting_address" label="地址" align="center" />
        <el-table-column prop="start_time" label="开始时间" align="center" width="160" />
        <el-table-column prop="end_time" label="结束时间" align="center" width="160" />
        <el-table-column prop="create_time" label="创建时间" align="center" width="160" />
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-edit"
              size="mini"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
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
      :title="dialogType == 1 ? '新增会议' : '编辑会议'"
      :visible.sync="dialogVisible"
      width="1000px"
      :close-on-click-modal="false"
    >
      <el-form :model="form" :rules="rules" ref="form" label-width="150px">
        <el-form-item label="会议名称" prop="meeting_name">
          <el-input v-model="form.meeting_name" placeholder="请输入会议名称" />
        </el-form-item>
        <el-form-item label="会议开始时间" prop="start_time">
          <el-date-picker
            v-model="form.start_time"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="会议结束时间" prop="end_time">
          <el-date-picker
            v-model="form.end_time"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="会议地址" prop="meeting_address">
          <el-input v-model="form.meeting_address" placeholder="请输入会议地址" />
        </el-form-item>
        <el-form-item label="会议封面" prop="meeting_img">
          <medical-image-upload v-model="form.meeting_img" :limit="1" />
        </el-form-item>
        <el-form-item label="会议区域" prop="qy_code">
          <el-select v-model="form.qy_code" placeholder="请选择区域">
            <el-option
              v-for="item in areaList"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否付费" prop="is_price_status">
          <el-radio-group v-model="form.is_price_status">
            <el-radio :label="1">免费</el-radio>
            <el-radio :label="2">付费</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="会议价格" prop="price" v-if="form.is_price_status == 2">
          <el-input-number v-model="form.price" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="会议详情" prop="meeting_content">
          <medical-editor v-model="form.meeting_content" :min-height="300" />
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
import { listMeeting, addMeeting, updateMeeting, delMeeting, getAreaList } from '@/api/medical/meeting'
import MedicalImageUpload from '@/components/MedicalImageUpload'
import MedicalEditor from '@/components/MedicalEditor'

export default {
  name: 'MedicalMeeting',
  components: {
    MedicalImageUpload,
    MedicalEditor
  },
  data() {
    return {
      loading: false,
      meetingList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10,
        meeting_name: '',
        city: '',
        is_price_status: ''
      },
      dialogVisible: false,
      dialogType: 1, // 1:新增 2:编辑
      form: {},
      areaList: [],
      rules: {
        meeting_name: [
          { required: true, message: '请输入会议名称', trigger: 'blur' }
        ],
        start_time: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        end_time: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ],
        meeting_address: [
          { required: true, message: '请输入会议地址', trigger: 'blur' }
        ],
        meeting_img: [
          { required: true, message: '请上传会议封面', trigger: 'blur' }
        ],
        qy_code: [
          { required: true, message: '请选择区域', trigger: 'change' }
        ],
        is_price_status: [
          { required: true, message: '请选择是否付费', trigger: 'change' }
        ],
        meeting_content: [
          { required: true, message: '请输入会议详情', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
    this.getAreaList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listMeeting(this.queryParams).then(response => {
        const rows = response.rows || response.data?.list || []
        const total = response.total || response.data?.count || 0
        // 适配后端字段 & 格式化时间
        this.meetingList = rows.map(item => {
          return {
            ...item,
            meeting_name: item.meeting_name || item.meetingName,
            meeting_img: item.meeting_img || item.meetingImg,
            meeting_address: item.meeting_address || item.meetingAddress,
            meeting_content: item.meeting_content || item.meetingContent,
            qy_name: item.qy_name || item.qyName,
            qy_code: item.qy_code || item.qyId,
            is_price_status: item.is_price_status ?? item.isPriceStatus,
            price: item.price,
            start_time: this.formatTs(item.start_time || item.startTime),
            end_time: this.formatTs(item.end_time || item.endTime),
            create_time: this.formatTs(item.create_time || item.createTimeStamp || item.createTime)
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
    /** 获取区域列表 */
    getAreaList() {
      getAreaList().then(response => {
        this.areaList = response.data || []
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
        meeting_name: '',
        city: '',
        is_price_status: ''
      }
      this.getList()
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
            addMeeting(this.form).then(() => {
              this.$modal.msgSuccess('新增成功')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            updateMeeting(this.form).then(() => {
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
      this.$confirm('是否确认删除会议"' + row.meeting_name + '"?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delMeeting({ id: row.id })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$message.info('导出功能开发中')
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        meeting_name: '',
        start_time: '',
        end_time: '',
        meeting_address: '',
        meeting_img: '',
        qy_code: '',
        is_price_status: 1,
        price: 0,
        meeting_content: ''
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
.mb20 {
  margin-bottom: 20px;
}
</style>

