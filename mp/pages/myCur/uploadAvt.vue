<template>
	<qf-image-cropper ref="qfImageCropper" :width="500" :height="500" :radius="30" @crop="handleCrop"></qf-image-cropper>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import {
		uploadImgUrl
	} from "@/common/config.js"
	export default {
		data() {
			return {

			}
		},
		onLoad(options) {
			
		},
		mounted() {
			this.$refs.qfImageCropper.chooseImage({
				sourceType: ['album']
			});
		},
		methods: {
			async handleCrop(e) {
				if (!e.tempFilePath) return;
				uni.showLoading({
					title: "上传中..."
				})
				// uni.previewImage({
				// 	urls: [e.tempFilePath],
				// 	current: 0
				// });
				//上传剪切图
				const res=await this.uploadImage(e.tempFilePath);
				//上传原图
				const res1=await this.uploadImage(e.originalImage);
				if(res.code==200 &&res1.code==200){
					uni.navigateBack({
						success: () => {
							uni.$emit("uploadAvt",{
								img:res.data.url,
								img_yt:res1.data.url
							})
						}
					})
				}
				uni.hideLoading()
			},
			async uploadImage(url){
				return new Promise((resolve,reject)=>{
					uni.uploadFile({
						url: uploadImgUrl,
						name: "file",
						filePath: url,
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
						fail: (err) => {
							reject(err)
						}
					})
				})
			}
		}
	}
</script>

<style lang="scss" scoped>

</style>