import request from '@/utils/medical-request'

// 查询会议列表
export function listMeeting(data) {
  return request({
    url: '/Admin/AdminMeeting/getAdminMeetingList',
    method: 'post',
    data: data
  })
}

// 删除会议
export function delMeeting(data) {
  return request({
    url: '/Admin/AdminMeeting/delMeetingAdmin',
    method: 'post',
    data: data
  })
}

// 新增会议
export function addMeeting(data) {
  return request({
    url: '/Admin/AdminMeeting/addAdminMeeting',
    method: 'post',
    data: data
  })
}

// 修改会议
export function updateMeeting(data) {
  return request({
    url: '/Admin/AdminMeeting/editAdminMeeting',
    method: 'post',
    data: data
  })
}

// 获取区域列表
export function getAreaList() {
  return request({
    url: '/Card/Card/filterList',
    method: 'post'
  })
}

