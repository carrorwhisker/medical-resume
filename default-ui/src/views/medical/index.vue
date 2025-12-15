<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card" shadow="hover">
          <div class="user">
            <el-avatar
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              :size="130"
              class="user_img"
            >
              <img
                src="https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
              />
            </el-avatar>
            <div class="user_info">
              <p class="name">{{ userName }}</p>
              <p class="role">{{ isRoot == 0 ? "超级管理员" : "管理员" }}</p>
            </div>
          </div>
          <div class="login_info">
            <p>
              当前IP：<span>{{ ip }}</span>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card shadow="hover">
          <div slot="header">
            <span>系统信息</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="系统名称">医简历管理系统</el-descriptions-item>
            <el-descriptions-item label="系统版本">2.0</el-descriptions-item>
            <el-descriptions-item label="当前用户">{{ userName }}</el-descriptions-item>
            <el-descriptions-item label="用户类型">{{ isRoot == 0 ? "超级管理员" : "管理员" }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Cookies from 'js-cookie'
import axios from 'axios'

export default {
  name: 'MedicalHome',
  data() {
    return {
      userName: '',
      ip: '',
      isRoot: 1
    }
  },
  async mounted() {
    this.userName = Cookies.get('userName') || '管理员'
    this.isRoot = Cookies.get('is_root') || 1
    this.ip = await this.getIP()
  },
  methods: {
    async getIP() {
      try {
        const response = await axios.get('https://ipv4.icanhazip.com/')
        return response.data.replace(/\n/g, '')
      } catch (error) {
        return '获取失败'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.user {
  padding-bottom: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  display: flex;
  align-items: center;
  .user_img {
    margin-right: 40px;
  }
  .user_info {
    .name {
      font-size: 32px;
      margin-bottom: 10px;
    }
    .role {
      color: #999999;
    }
  }
}
.login_info {
  p {
    line-height: 28px;
    font-size: 14px;
    color: #999;

    span {
      color: #666666;
      margin-left: 60px;
    }
  }
}
</style>

