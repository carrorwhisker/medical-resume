import request from '@/utils/medical-request'

// 查询报名列表
export function listRegistration(data) {
  return request({
    url: '/Admin/AdminMeeting/getSignUpList',
    method: 'post',
    data: data
  })
}

// 审核报名
export function examineRegistration(data) {
  return request({
    url: '/Admin/AdminMeeting/checkSignUp',
    method: 'post',
    data: data
  })
}

// 获取会议列表（用于导出）
export function getMeetingList() {
  return request({
    url: '/Admin/AdminMeeting/getSignUpExcelMeeting',
    method: 'post'
  })
}

