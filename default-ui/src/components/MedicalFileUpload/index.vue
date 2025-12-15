<template>
  <div class="medical-upload-file">
    <el-upload
      :action="uploadUrl"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      ref="fileUpload"
      :on-remove="handleDelete"
      :show-file-list="true"
      :file-list="fileList"
      :class="{ hide: this.fileList.length >= this.limit }"
      :http-request="handleUploadFile"
    >
      <el-button size="small" type="primary">点击上传</el-button>
      <div class="el-upload__tip" slot="tip" v-if="showTip">
        请上传
        <template v-if="fileSize">
          大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
        </template>
        <template v-if="fileType">
          格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
        </template>
        的文件
      </div>
    </el-upload>
  </div>
</template>

<script>
import { uploadFile } from '@/api/medical/upload'

export default {
  name: 'MedicalFileUpload',
  props: {
    value: [String, Object, Array],
    limit: {
      type: Number,
      default: 1
    },
    fileSize: {
      type: Number,
      default: 10
    },
    fileType: {
      type: Array,
      default: () => ['pptx', 'ppt', 'pdf', 'word', 'excel', 'doc', 'docx']
    },
    isShowTip: {
      type: Boolean,
      default: true
    },
    updateName: {
      type: Boolean,
      default: false
    },
    fileName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      number: 0,
      uploadList: [],
      fileList: [],
      loading: null,
      uploadUrl: ''
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          const list = Array.isArray(val) ? val : this.value.split(',')
          this.fileList = list.map(item => {
            if (typeof item === 'string') {
              item = { name: item, url: item }
            }
            return item
          })
        } else {
          this.fileList = []
        }
      },
      deep: true,
      immediate: true
    }
  },
  computed: {
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize)
    }
  },
  methods: {
    handleUploadFile({ file }) {
      const formData = new FormData()
      formData.append('file', file)
      uploadFile(formData)
        .then(res => {
          this.handleUploadSuccess(res, file)
          if (this.updateName) {
            this.$emit('update:fileName', file.name)
          }
        })
        .catch(() => {
          this.handleUploadError()
        })
    },
    handleBeforeUpload(file) {
      let isFile = false
      if (this.fileType.length) {
        let fileExtension = ''
        if (file.name.lastIndexOf('.') > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
        }
        isFile = this.fileType.some(type => {
          if (file.type.indexOf(type) > -1) return true
          if (fileExtension && fileExtension.indexOf(type) > -1) return true
          return false
        })
      }

      if (!isFile) {
        this.$message.error(
          `文件格式不正确, 请上传${this.fileType.join('/')}格式文件!`
        )
        return false
      }
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`)
          return false
        }
      }
      this.loading = this.$loading({
        lock: true,
        text: '正在上传...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      this.number++
    },
    handleExceed() {
      this.$message.error(`上传文件数量不能超过 ${this.limit} 个!`)
    },
    handleUploadSuccess(res, file) {
      if (res.code === 200) {
        this.uploadList.push({
          name: res.originalFilename || res.fileName,
          url: res.url
        })
        this.uploadedSuccessfully()
      } else {
        this.number--
        if (this.loading) {
          this.loading.close()
          this.loading = null
        }
        this.$message.error(res.msg || '上传失败')
        this.$refs.fileUpload.handleRemove(file)
        this.uploadedSuccessfully()
      }
    },
    handleDelete(file) {
      const findex = this.fileList.map(f => f.name).indexOf(file.name)
      if (findex > -1) {
        this.fileList.splice(findex, 1)
        this.$emit('input', this.listToString(this.fileList))
      }
    },
    handleUploadError() {
      this.$message.error('上传失败，请重试')
      if (this.loading) {
        this.loading.close()
        this.loading = null
      }
    },
    uploadedSuccessfully() {
      if (this.number > 0 && this.uploadList.length === this.number) {
        this.fileList = this.fileList.concat(this.uploadList)
        this.uploadList = []
        this.number = 0
        this.$emit('input', this.listToString(this.fileList))
        if (this.loading) {
          this.loading.close()
          this.loading = null
        }
      }
    },
    listToString(list, separator) {
      let strs = ''
      separator = separator || ','
      for (let i in list) {
        if (list[i].url) {
          strs += list[i].url + separator
        }
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : ''
    }
  }
}
</script>

<style scoped>
::v-deep.hide .el-upload {
  display: none;
}
</style>

