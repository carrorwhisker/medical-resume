import request from '@/utils/medical-request'

// 查询用户列表
export function listUser(params) {
  return request({
    url: '/Home/Home/getUserList',
    method: 'get',
    params: params
  })
}

// 保存用户信息
export function saveUser(params) {
  return request({
    url: '/Home/Home/userListSave',
    method: 'get',
    params: params
  })
}

// 删除用户
export function delUser(params) {
  return request({
    url: '/Home/Home/delUserList',
    method: 'get',
    params: params
  })
}

// 查询用户卡片列表
export function listUserCard(data) {
  return request({
    url: '/Admin/Admin/getCustomerList',
    method: 'post',
    data: data
  })
}

// 保存用户卡片
export function saveUserCard(data) {
  return request({
    url: '/Admin/Admin/saveCustomer',
    method: 'post',
    data: data
  })
}

