import Cookies from 'js-cookie'

/**
 * 下载文件
 * @param {String} url 下载地址
 * @param {String} filename 文件名
 */
export function downloadFile(url, filename) {
  const token = Cookies.get('token')
  const baseURL = process.env.VUE_APP_MEDICAL_API || 'https://card.xixinyiban.com/Main/action'
  
  // 构建完整URL
  const fullUrl = url.startsWith('http') ? url : `${baseURL}/${url}`
  
  // 创建隐藏的a标签进行下载
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = fullUrl + (fullUrl.includes('?') ? '&' : '?') + 'token=' + token
  link.setAttribute('download', filename || '下载文件')
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

