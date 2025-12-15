<template>
  <view class="itinerary-container">
    <view class="header">
      <text class="title">我的行程</text>
    </view>

    <view v-if="itineraryList.length === 0" class="empty">
      <image src="/static/empty.png" class="empty-img" mode="aspectFit"></image>
      <text class="empty-text">暂无行程信息</text>
    </view>

    <view v-else class="itinerary-list">
      <view v-for="(item, index) in itineraryList" :key="index" class="itinerary-item">
        <view class="meeting-info">
          <image :src="item.meeting_img" class="meeting-img" mode="aspectFill"></image>
          <view class="meeting-detail">
            <text class="meeting-name">{{ item.meeting_name }}</text>
            <text class="meeting-time">{{ item.start_time_str }} 至 {{ item.end_time_str }}</text>
            <text class="meeting-address">{{ item.meeting_address }}</text>
          </view>
        </view>

        <view class="reception-info">
          <view class="info-row">
            <text class="label">接待地点：</text>
            <text class="value">{{ item.reception_location || '待安排' }}</text>
          </view>
          <view class="info-row">
            <text class="label">接待专家：</text>
            <text class="value">{{ item.reception_expert || '待安排' }}</text>
          </view>
          <view v-if="item.reception_expert_phone" class="info-row">
            <text class="label">专家电话：</text>
            <text class="value phone" @click="callPhone(item.reception_expert_phone)">{{ item.reception_expert_phone }}</text>
          </view>
          <view v-if="item.notes" class="info-row">
            <text class="label">行程备注：</text>
            <text class="value">{{ item.notes }}</text>
          </view>
        </view>

        <view v-if="item.transportations && item.transportations.length > 0" class="transport-section">
          <view class="section-title">交通信息</view>
          <view v-for="(trans, tIndex) in item.transportations" :key="tIndex" class="transport-item">
            <view class="transport-header">
              <text class="transport-type">{{ trans.transport_type_name }}</text>
              <text v-if="trans.transport_number" class="transport-number">{{ trans.transport_number }}</text>
            </view>
            <view class="transport-route">
              <view class="route-point">
                <view class="point-dot start"></view>
                <view class="point-info">
                  <text class="point-location">{{ trans.departure_location }}</text>
                  <text class="point-time">{{ trans.departure_time_str }}</text>
                </view>
              </view>
              <view class="route-line"></view>
              <view class="route-point">
                <view class="point-dot end"></view>
                <view class="point-info">
                  <text class="point-location">{{ trans.arrival_location }}</text>
                  <text class="point-time">{{ trans.arrival_time_str }}</text>
                </view>
              </view>
            </view>
            <view v-if="trans.seat_info" class="transport-detail">
              <text class="detail-label">座位：</text>
              <text class="detail-value">{{ trans.seat_info }}</text>
            </view>
            <view v-if="trans.ticket_image" class="ticket-image">
              <image :src="trans.ticket_image" mode="aspectFit" @click="previewImage(trans.ticket_image)"></image>
            </view>
          </view>
        </view>

        <view class="action-buttons">
          <button class="btn btn-calendar" @click="addToCalendar(item)">
            <text class="btn-icon">📅</text>
            <text>添加到日历</text>
          </button>
          <button class="btn btn-detail" @click="viewDetail(item)">
            <text>查看详情</text>
          </button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { BAES_URL } from '@/common/config.js'

export default {
  data() {
    return {
      itineraryList: []
    }
  },
  onLoad() {
    this.getItineraryList()
  },
  onShow() {
    // 每次显示页面时刷新数据
    this.getItineraryList()
  },
  methods: {
    // 获取行程列表
    getItineraryList() {
      uni.showLoading({
        title: '加载中...'
      })
      
      uni.request({
        url: BAES_URL + 'Itinerary/Itinerary/getMyItineraryList',
        method: 'POST',
        header: {
          'Token': uni.getStorageSync('token')
        },
        success: (res) => {
          uni.hideLoading()
          if (res.data.code === 200) {
            this.itineraryList = res.data.data.list || []
          } else if (res.data.code === 201) {
            uni.showToast({
              title: '请先登录',
              icon: 'none'
            })
            setTimeout(() => {
              uni.navigateTo({
                url: '/pages/login/index'
              })
            }, 1500)
          } else {
            uni.showToast({
              title: res.data.msg || '获取失败',
              icon: 'none'
            })
          }
        },
        fail: () => {
          uni.hideLoading()
          uni.showToast({
            title: '网络错误',
            icon: 'none'
          })
        }
      })
    },
    
    // 拨打电话
    callPhone(phone) {
      uni.showModal({
        content: `是否拨打 ${phone}？`,
        success: (res) => {
          if (res.confirm) {
            uni.makePhoneCall({
              phoneNumber: phone
            })
          }
        }
      })
    },
    
    // 预览图片
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: url
      })
    },
    
    // 添加到日历
    addToCalendar(item) {
      // 检查是否有会议时间
      if (!item.start_time || !item.end_time) {
        uni.showToast({
          title: '会议时间未确定',
          icon: 'none'
        })
        return
      }
      
      // 将时间戳转换为日期对象
      const startDate = new Date(item.start_time * 1000)
      const endDate = new Date(item.end_time * 1000)
      
      // 构建日历事件描述
      let description = `会议：${item.meeting_name}\n`
      if (item.meeting_address) {
        description += `地点：${item.meeting_address}\n`
      }
      if (item.reception_expert) {
        description += `接待专家：${item.reception_expert}\n`
      }
      if (item.reception_location) {
        description += `接待地点：${item.reception_location}\n`
      }
      
      // 调用微信小程序API添加到日历
      // #ifdef MP-WEIXIN
      wx.addPhoneCalendar({
        title: item.meeting_name,
        startTime: Math.floor(startDate.getTime() / 1000),
        endTime: Math.floor(endDate.getTime() / 1000),
        description: description,
        location: item.meeting_address || item.reception_location || '',
        success: () => {
          uni.showToast({
            title: '已添加到日历',
            icon: 'success'
          })
        },
        fail: (err) => {
          console.error('添加日历失败', err)
          uni.showToast({
            title: '添加失败，请手动添加',
            icon: 'none'
          })
        }
      })
      // #endif
      
      // #ifndef MP-WEIXIN
      uni.showToast({
        title: '该功能仅支持微信小程序',
        icon: 'none'
      })
      // #endif
    },
    
    // 查看会议详情
    viewDetail(item) {
      uni.navigateTo({
        url: `/pages/meeting/details?id=${item.meeting_id}`
      })
    }
  }
}
</script>

<style scoped>
.itinerary-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 20rpx;
}

.header {
  background-color: #fff;
  padding: 30rpx;
  text-align: center;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
}

.empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-img {
  width: 300rpx;
  height: 300rpx;
  margin-bottom: 30rpx;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.itinerary-list {
  padding: 20rpx;
}

.itinerary-item {
  background-color: #fff;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
  overflow: hidden;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
}

.meeting-info {
  display: flex;
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.meeting-img {
  width: 160rpx;
  height: 160rpx;
  border-radius: 10rpx;
  margin-right: 20rpx;
  flex-shrink: 0;
}

.meeting-detail {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.meeting-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 10rpx;
}

.meeting-time {
  font-size: 24rpx;
  color: #666;
  margin-bottom: 5rpx;
}

.meeting-address {
  font-size: 24rpx;
  color: #999;
}

.reception-info {
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.info-row {
  display: flex;
  margin-bottom: 15rpx;
  font-size: 28rpx;
}

.info-row:last-child {
  margin-bottom: 0;
}

.label {
  color: #666;
  flex-shrink: 0;
  width: 180rpx;
}

.value {
  color: #333;
  flex: 1;
}

.phone {
  color: #1890ff;
  text-decoration: underline;
}

.transport-section {
  padding: 30rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 20rpx;
}

.transport-item {
  background-color: #f9f9f9;
  border-radius: 10rpx;
  padding: 20rpx;
  margin-bottom: 15rpx;
}

.transport-item:last-child {
  margin-bottom: 0;
}

.transport-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.transport-type {
  font-size: 28rpx;
  font-weight: bold;
  color: #1890ff;
}

.transport-number {
  font-size: 24rpx;
  color: #666;
}

.transport-route {
  position: relative;
  padding-left: 40rpx;
}

.route-point {
  display: flex;
  align-items: flex-start;
  position: relative;
}

.point-dot {
  width: 20rpx;
  height: 20rpx;
  border-radius: 50%;
  position: absolute;
  left: -30rpx;
  top: 5rpx;
}

.point-dot.start {
  background-color: #52c41a;
}

.point-dot.end {
  background-color: #f5222d;
}

.route-line {
  position: absolute;
  left: -21rpx;
  top: 25rpx;
  width: 2rpx;
  height: 60rpx;
  background-color: #d9d9d9;
}

.point-info {
  display: flex;
  flex-direction: column;
  margin-bottom: 20rpx;
}

.point-location {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 5rpx;
}

.point-time {
  font-size: 24rpx;
  color: #999;
}

.transport-detail {
  margin-top: 15rpx;
  font-size: 26rpx;
}

.detail-label {
  color: #666;
}

.detail-value {
  color: #333;
}

.ticket-image {
  margin-top: 15rpx;
}

.ticket-image image {
  width: 100%;
  height: 300rpx;
  border-radius: 10rpx;
}

.action-buttons {
  display: flex;
  padding: 20rpx 30rpx;
}

.btn {
  flex: 1;
  height: 70rpx;
  line-height: 70rpx;
  border-radius: 35rpx;
  font-size: 28rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
}

.btn-calendar {
  background-color: #fff;
  color: #1890ff;
  border: 1rpx solid #1890ff;
  margin-right: 20rpx;
}

.btn-icon {
  margin-right: 10rpx;
}

.btn-detail {
  background-color: #1890ff;
  color: #fff;
}
</style>

