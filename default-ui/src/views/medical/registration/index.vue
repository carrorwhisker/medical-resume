<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-card class="mb10">
      <el-form :inline="true">
        <el-form-item label="会议">
          <el-select v-model="queryParams.meeting_id" placeholder="请选择会议" clearable>
            <el-option label="全部" value="" />
            <el-option
              v-for="item in meetingList"
              :key="item.id"
              :label="item.meeting_name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
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
      <el-table v-loading="loading" stripe border :data="registrationList">
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="name" label="姓名" align="center" />
        <el-table-column prop="phone" label="手机号" align="center" />
        <el-table-column prop="company" label="单位" align="center" />
        <el-table-column prop="position" label="职位" align="center" />
        <el-table-column prop="meeting_name" label="会议名称" align="center" />
        <el-table-column label="审核状态" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 1" type="warning">申请中</el-tag>
            <el-tag v-else-if="scope.row.status == 2" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="create_time" label="报名时间" align="center" width="160" />
        <el-table-column label="操作" align="center" width="260">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status == 1"
              type="success"
              size="mini"
              @click="handleExamine(scope.row, 2)"
            >通过</el-button>
            <el-button
              v-if="scope.row.status == 1"
              type="danger"
              size="mini"
              @click="handleExamine(scope.row, 3)"
            >拒绝</el-button>
            <el-button
              v-if="scope.row.status == 2"
              type="primary"
              size="mini"
              @click="openItinerary(scope.row)"
            >设置行程</el-button>
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

    <!-- 行程设置弹窗 -->
    <el-dialog
      title="设置行程"
      :visible.sync="itineraryDialogVisible"
      width="500px"
      append-to-body
    >
      <el-form
        ref="itineraryForm"
        :model="itineraryForm"
        :rules="itineraryRules"
        label-width="120px"
      >
        <el-form-item label="报名人">
          <span>{{ itineraryForm.name }}</span>
        </el-form-item>
        <el-form-item label="会议名称">
          <span>{{ itineraryForm.meeting_name }}</span>
        </el-form-item>
        <el-form-item label="接待地点" prop="reception_location">
          <el-input v-model="itineraryForm.reception_location" placeholder="请输入接待地点" />
        </el-form-item>
        <el-form-item label="接待专家" prop="reception_expert">
          <el-input v-model="itineraryForm.reception_expert" placeholder="请输入接待专家" />
        </el-form-item>
        <el-form-item label="专家电话">
          <el-input v-model="itineraryForm.reception_expert_phone" placeholder="请输入专家电话" />
        </el-form-item>
        <el-form-item label="行程备注">
          <el-input
            type="textarea"
            :rows="3"
            v-model="itineraryForm.notes"
            placeholder="备注（可选）"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="itineraryDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitItinerary">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRegistration, examineRegistration, getMeetingList } from '@/api/medical/registration'
import { saveItinerary } from '@/api/medical/itinerary'

export default {
  name: 'MedicalRegistration',
  data() {
    return {
      loading: false,
      registrationList: [],
      total: 0,
      queryParams: {
        page: 1,
        size: 10,
        meeting_id: '',
        name: ''
      },
      meetingList: [],
      itineraryDialogVisible: false,
      itineraryForm: {},
      itineraryRules: {
        reception_location: [{ required: true, message: '请输入接待地点', trigger: 'blur' }],
        reception_expert: [{ required: true, message: '请输入接待专家', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    this.getMeetingList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listRegistration(this.queryParams).then(response => {
        const rows = response.rows || response.data?.list || []
        const total = response.total || response.data?.count || 0
        this.registrationList = rows.map(item => {
          return {
            ...item,
            name: item.sign_up_name,
            phone: item.sign_up_phone,
            company: item.sign_up_company,
            position: item.sign_up_position,
            meeting_name: item.meeting_name || item.meetingName,
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
    /** 获取会议列表 */
    getMeetingList() {
      getMeetingList().then(response => {
        this.meetingList = response.data || []
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
        meeting_id: '',
        name: ''
      }
      this.getList()
    },
    /** 审核操作 */
    handleExamine(row, status) {
      const text = status == 1 ? '通过' : '拒绝'
      this.$confirm('是否确认' + text + '该报名?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return examineRegistration({ id: row.id, status: status })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('操作成功')
      }).catch(() => {})
    },
    /** 打开行程设置 */
    openItinerary(row) {
      this.itineraryForm = {
        sign_up_id: row.id,
        meeting_name: row.meeting_name,
        name: row.name,
        reception_location: '',
        reception_expert: '',
        reception_expert_phone: '',
        notes: ''
      }
      this.itineraryDialogVisible = true
    },
    /** 保存行程 */
    submitItinerary() {
      this.$refs['itineraryForm']?.validate(valid => {
        if (!valid) return
        const payload = {
          sign_up_id: this.itineraryForm.sign_up_id,
          reception_location: this.itineraryForm.reception_location,
          reception_expert: this.itineraryForm.reception_expert,
          reception_expert_phone: this.itineraryForm.reception_expert_phone,
          notes: this.itineraryForm.notes
        }
        saveItinerary(payload).then(() => {
          this.$modal.msgSuccess('行程已保存')
          this.itineraryDialogVisible = false
          this.getList()
        })
      })
    }
  }
}
</script>

<style scoped>
.mb10 {
  margin-bottom: 10px;
}

