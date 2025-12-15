import request from '@/utils/medical-request'

// 查询简历列表
export function listResume(data) {
  return request({
    url: '/Admin/AdminCard/getAdminCardList',
    method: 'post',
    data: data
  })
}

// 删除简历
export function delResume(data) {
  return request({
    url: '/Admin/AdminCard/delCardAdmin',
    method: 'post',
    data: data
  })
}

// 审核简历
export function examineResume(data) {
  return request({
    url: '/Admin/AdminCard/toExamineCard',
    method: 'post',
    data: data
  })
}

// 修改简历附件名称
export function updateAttachment(data) {
  return request({
    url: '/Admin/AdminCard/updateAttachment',
    method: 'post',
    data: data
  })
}

// 新增简历
export function createResume(data) {
  return request({
    url: '/Admin/AdminCard/createCardData',
    method: 'post',
    data: data
  })
}

// 修改简历
export function updateResume(data) {
  return request({
    url: '/Admin/AdminCard/updateAdminCardList',
    method: 'post',
    data: data
  })
}

// 获取科室列表
export function getDepartmentList() {
  return request({
    url: '/Card/Card/departmentList',
    method: 'post'
  })
}

