import request from '@/utils/medical-request'

// 查询医生反馈列表
export function listDoctor(data) {
  return request({
    url: '/Home/Home/getFeedback',
    method: 'get',
    params: data
  })
}

// 保存医生信息
export function saveDoctor(data) {
  return request({
    url: '/Admin/Admin/saveMedic',
    method: 'post',
    data: data
  })
}

