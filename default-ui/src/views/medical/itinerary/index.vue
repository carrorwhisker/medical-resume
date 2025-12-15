<template>
  <div class="app-container">
    <el-card style="margin-bottom: 20px;">
      <el-form :inline="true">
        <el-form-item label="会议名称">
          <el-input
            v-model="queryParams.meeting_name"
            placeholder="请输入会议名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card>
      <el-table v-loading="loading" :data="itineraryList" border>
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="会议名称" align="center" prop="meeting_name" />
        <el-table-column label="报名人" align="center" prop="sign_up_name" />
        <el-table-column label="联系电话" align="center" prop="sign_up_phone" />
        <el-table-column label="单位" align="center" prop="sign_up_company" show-overflow-tooltip />
        <el-table-column label="接待地点" align="center" prop="reception_location" show-overflow-tooltip />
        <el-table-column label="接待专家" align="center" prop="reception_expert" />
        <el-table-column label="专家电话" align="center" prop="reception_expert_phone" />
        <el-table-column label="创建时间" align="center" prop="create_time" width="180" />
        <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >查看交通</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
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

    <!-- 编辑行程对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="接待地点" prop="reception_location">
          <el-input v-model="form.reception_location" placeholder="请输入接待地点" />
        </el-form-item>
        <el-form-item label="接待专家" prop="reception_expert">
          <el-input v-model="form.reception_expert" placeholder="请输入接待专家姓名" />
        </el-form-item>
        <el-form-item label="专家电话" prop="reception_expert_phone">
          <el-input v-model="form.reception_expert_phone" placeholder="请输入专家联系电话" />
        </el-form-item>
        <el-form-item label="行程备注">
          <el-input
            v-model="form.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入行程备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看交通信息对话框 -->
    <el-dialog title="交通信息" :visible.sync="transportDialogVisible" width="900px" append-to-body>
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        style="margin-bottom: 15px"
        @click="handleAddTransport"
      >添加交通信息</el-button>
      
      <el-table :data="transportList" border>
        <el-table-column label="序号" type="index" width="55" align="center" />
        <el-table-column label="交通类型" align="center" width="100">
          <template slot-scope="scope">
            {{ getTransportTypeName(scope.row.transport_type) }}
          </template>
        </el-table-column>
        <el-table-column label="出发地" align="center" prop="departure_location" />
        <el-table-column label="到达地" align="center" prop="arrival_location" />
        <el-table-column label="出发时间" align="center" prop="departure_time" width="180" />
        <el-table-column label="到达时间" align="center" prop="arrival_time" width="180" />
        <el-table-column label="车次/航班号" align="center" prop="transport_number" />
        <el-table-column label="座位信息" align="center" prop="seat_info" />
        <el-table-column label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEditTransport(scope.row)"
            >编辑</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDeleteTransport(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 添加/编辑交通信息对话框 -->
    <el-dialog :title="transportDialogTitle" :visible.sync="transportFormVisible" width="600px" append-to-body>
      <el-form ref="transportForm" :model="transportForm" :rules="transportRules" label-width="120px">
        <el-form-item label="交通类型" prop="transport_type">
          <el-select v-model="transportForm.transport_type" placeholder="请选择交通类型">
            <el-option label="飞机" :value="1" />
            <el-option label="火车" :value="2" />
            <el-option label="高铁" :value="3" />
            <el-option label="汽车" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="出发地" prop="departure_location">
          <el-input v-model="transportForm.departure_location" placeholder="请输入出发地" />
        </el-form-item>
        <el-form-item label="到达地" prop="arrival_location">
          <el-input v-model="transportForm.arrival_location" placeholder="请输入到达地" />
        </el-form-item>
        <el-form-item label="出发时间" prop="departure_time">
          <el-date-picker
            v-model="transportForm.departure_time"
            type="datetime"
            placeholder="选择出发时间"
            value-format="timestamp"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="到达时间" prop="arrival_time">
          <el-date-picker
            v-model="transportForm.arrival_time"
            type="datetime"
            placeholder="选择到达时间"
            value-format="timestamp"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="车次/航班号">
          <el-input v-model="transportForm.transport_number" placeholder="请输入车次或航班号" />
        </el-form-item>
        <el-form-item label="座位信息">
          <el-input v-model="transportForm.seat_info" placeholder="请输入座位信息" />
        </el-form-item>
        <el-form-item label="票据图片">
          <medical-image-upload v-model="transportForm.ticket_image" :limit="1" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="transportForm.notes"
            type="textarea"
            :rows="2"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="transportFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTransportForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getItineraryList, getItineraryBySignUpId, saveItinerary, deleteItinerary, addTransportation, updateTransportation, deleteTransportation } from '@/api/medical/itinerary'
import MedicalImageUpload from '@/components/MedicalImageUpload'

export default {
  name: 'Itinerary',
  components: {
    MedicalImageUpload
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 行程列表
      itineraryList: [],
      // 查询参数
      queryParams: {
        page: 1,
        size: 10,
        meeting_name: undefined
      },
      // 对话框标题
      dialogTitle: '',
      // 是否显示对话框
      dialogVisible: false,
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        reception_location: [
          { required: true, message: '接待地点不能为空', trigger: 'blur' }
        ],
        reception_expert: [
          { required: true, message: '接待专家不能为空', trigger: 'blur' }
        ]
      },
      // 交通信息对话框
      transportDialogVisible: false,
      transportList: [],
      currentItineraryId: null,
      // 交通信息表单
      transportDialogTitle: '',
      transportFormVisible: false,
      transportForm: {},
      transportRules: {
        transport_type: [
          { required: true, message: '交通类型不能为空', trigger: 'change' }
        ],
        departure_location: [
          { required: true, message: '出发地不能为空', trigger: 'blur' }
        ],
        arrival_location: [
          { required: true, message: '到达地不能为空', trigger: 'blur' }
        ],
        departure_time: [
          { required: true, message: '出发时间不能为空', trigger: 'change' }
        ],
        arrival_time: [
          { required: true, message: '到达时间不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询行程列表 */
    getList() {
      this.loading = true
      getItineraryList(this.queryParams).then(response => {
        const rows = response.rows || response.data?.rows || response.data || []
        const total = response.total || response.data?.total || 0
        this.itineraryList = rows
        this.total = total
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
        meeting_name: undefined
      }
      this.handleQuery()
    },
    /** 编辑按钮操作 */
    handleEdit(row) {
      this.form = {
        id: row.id,
        sign_up_id: row.sign_up_id,
        reception_location: row.reception_location,
        reception_expert: row.reception_expert,
        reception_expert_phone: row.reception_expert_phone,
        notes: row.notes
      }
      this.dialogTitle = '编辑行程'
      this.dialogVisible = true
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          saveItinerary(this.form).then(response => {
            this.$modal.msgSuccess('保存成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除该行程?').then(() => {
        return deleteItinerary({ id: row.id })
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    /** 查看交通信息 */
    handleView(row) {
      this.currentItineraryId = row.id
      this.transportList = row.transportations || []
      this.transportDialogVisible = true
    },
    /** 添加交通信息 */
    handleAddTransport() {
      this.transportForm = {
        itinerary_id: this.currentItineraryId,
        transport_type: undefined,
        departure_location: '',
        arrival_location: '',
        departure_time: undefined,
        arrival_time: undefined,
        transport_number: '',
        seat_info: '',
        ticket_image: '',
        notes: ''
      }
      this.transportDialogTitle = '添加交通信息'
      this.transportFormVisible = true
    },
    /** 编辑交通信息 */
    handleEditTransport(row) {
      this.transportForm = {
        id: row.id,
        itinerary_id: this.currentItineraryId,
        transport_type: row.transport_type,
        departure_location: row.departure_location,
        arrival_location: row.arrival_location,
        departure_time: row.departure_time * 1000, // 转换为毫秒
        arrival_time: row.arrival_time * 1000,
        transport_number: row.transport_number,
        seat_info: row.seat_info,
        ticket_image: row.ticket_image,
        notes: row.notes
      }
      this.transportDialogTitle = '编辑交通信息'
      this.transportFormVisible = true
    },
    /** 提交交通信息 */
    submitTransportForm() {
      this.$refs['transportForm'].validate(valid => {
        if (valid) {
          // 将时间戳转换为秒
          const data = {
            ...this.transportForm,
            departure_time: Math.floor(this.transportForm.departure_time / 1000),
            arrival_time: Math.floor(this.transportForm.arrival_time / 1000)
          }
          
          const action = data.id ? updateTransportation(data) : addTransportation(data)
          action.then(response => {
            this.$modal.msgSuccess('保存成功')
            this.transportFormVisible = false
            this.getList()
            // 刷新交通信息列表
            this.itineraryList.forEach(item => {
              if (item.id === this.currentItineraryId) {
                this.transportList = item.transportations || []
              }
            })
          })
        }
      })
    },
    /** 删除交通信息 */
    handleDeleteTransport(row) {
      this.$modal.confirm('是否确认删除该交通信息?').then(() => {
        return deleteTransportation({ id: row.id })
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
        // 刷新交通信息列表
        this.itineraryList.forEach(item => {
          if (item.id === this.currentItineraryId) {
            this.transportList = item.transportations || []
          }
        })
      }).catch(() => {})
    },
    /** 获取交通类型名称 */
    getTransportTypeName(type) {
      const typeMap = {
        1: '飞机',
        2: '火车',
        3: '高铁',
        4: '汽车',
        5: '其他'
      }
      return typeMap[type] || '其他'
    }
  }
}
</script>

