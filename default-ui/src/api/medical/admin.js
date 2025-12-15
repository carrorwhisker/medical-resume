import request from '@/utils/medical-request'

// 查询管理员列表
export function listAdmin(data) {
  return request({
    url: '/Admin/Admin/getAdminList',
    method: 'post',
    data: data
  })
}

// 新增管理员
export function addAdmin(data) {
  return request({
    url: '/Admin/Admin/addAdmin',
    method: 'post',
    data: data
  })
}

// 修改管理员
export function updateAdmin(data) {
  return request({
    url: '/Admin/Admin/editAdmin',
    method: 'post',
    data: data
  })
}

// 删除管理员
export function delAdmin(data) {
  return request({
    url: '/Admin/Admin/delAdmin',
    method: 'post',
    data: data
  })
}

// 管理员登录
export function adminLogin(data) {
  return request({
    url: '/Admin/Admin/adminLogin',
    method: 'post',
    data: data
  })
}

