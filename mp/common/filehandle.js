import {
	uploadFileUrl,
	uploadImgUrl
} from "./config.js"
//允许上传的格式
const typeList=['pptx','ppt','pdf','word','excel','doc','docx'];
export const uploadFlie = () => {
	return new Promise((resolve, reject) => {
		wx.chooseMessageFile({
			count: 1,
			type: "file",
			success: (res) => {
				console.log(res);
				const {name,size}=res.tempFiles[0];
				//验证文件格式
				let fileType=name.substring(name.lastIndexOf(".")+1).toLocaleLowerCase();
				if(typeList.indexOf(fileType)==-1){
					uni.showToast({
						title:"文件格式不支持",
						icon:"fail"
					})
					reject(`只支持上传${typeList.join("/")}格式的文件`)
					return;
				}
				if(size>1024*1024*10){
					uni.showToast({
						title:"上传文件不能超过10M",
						icon:"fail"
					})
					reject(`上传文件不能超过10M`)
					return;
				}
                uni.showLoading({
                	title: "文件上传中..."
                })
                uni.uploadFile({
                	url: uploadFileUrl,
                	name: "file",
                	filePath: res.tempFiles[0].path,
					formData:{
						fileName:res.tempFiles[0].name
					},
                	header: {
                		Token: uni.getStorageSync('token'),
						Authorization: 'Bearer ' + uni.getStorageSync('token')
                	},
                	success: result => {
						const data = JSON.parse(result.data)
						// 适配新接口返回格式
						if (data.code === 200) {
							resolve({
								code: 200,
								data: {
									url: data.url || data.data?.url,
									name: data.originalFilename || data.data?.originalFilename
								}
							})
						} else {
							resolve(data)
						}
                	},
                	complete: (res) => {
						let info=JSON.parse(res.data);
						if(info.code!=200){
							uni.showToast({
								title:info.msg,
								icon:"error",
								duration:1500
							})
						}else{
							uni.hideLoading()
						}
                	}
                })
			}
		})
	})
}

export const uploadImage = () => {
	return new Promise((resolve, reject) => {
		uni.chooseImage({
			count: 1,
			// crop:{
			// 	width:400,
			// 	height:400
			// },
			success: res => {
				uni.showLoading({
					title: "图片上传中..."
				})
				uni.uploadFile({
					url: uploadImgUrl,
					name: "file",
					filePath: res.tempFilePaths[0],
					formData:{
						fileName:res.tempFiles[0].name
					},
					header: {
						Token: uni.getStorageSync('token')
					},
					success: result => {
						resolve(JSON.parse(result.data))
					},
					complete: () => {
						uni.hideLoading()
					}
				})
			},
			complete: (info) => {}
		})
	})
}