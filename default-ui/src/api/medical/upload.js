import request from '@/utils/request'

// 上传文件（使用标准request，不带/Main/action前缀）
export function uploadFile(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 上传图片（使用标准request，不带/Main/action前缀）
export function uploadImage(data) {
  return request({
    url: '/common/upload',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

