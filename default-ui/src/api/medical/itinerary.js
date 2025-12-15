import request from '@/utils/medical-request'

// 查询行程列表
export function getItineraryList(data) {
  return request({
    url: '/Admin/AdminItinerary/getItineraryList',
    method: 'post',
    data: data
  })
}

// 根据报名ID获取行程详情
export function getItineraryBySignUpId(signUpId) {
  return request({
    url: '/Admin/AdminItinerary/getItineraryBySignUpId',
    method: 'get',
    params: { signUpId }
  })
}

// 保存行程(新增或更新)
export function saveItinerary(data) {
  return request({
    url: '/Admin/AdminItinerary/saveItinerary',
    method: 'post',
    data: data
  })
}

// 删除行程
export function deleteItinerary(data) {
  return request({
    url: '/Admin/AdminItinerary/deleteItinerary',
    method: 'post',
    data: data
  })
}

// 添加交通信息
export function addTransportation(data) {
  return request({
    url: '/Admin/AdminItinerary/addTransportation',
    method: 'post',
    data: data
  })
}

// 更新交通信息
export function updateTransportation(data) {
  return request({
    url: '/Admin/AdminItinerary/updateTransportation',
    method: 'post',
    data: data
  })
}

// 删除交通信息
export function deleteTransportation(data) {
  return request({
    url: '/Admin/AdminItinerary/deleteTransportation',
    method: 'post',
    data: data
  })
}

