<template>
	<!--  -->
	<view>
		<canvas type="2d" id="met_content" canvas-id="met_content"
			style="width: 400px;height: 400px;position: fixed; left: -500px"></canvas>
		<canvas type="2d" id="met_avtImg" canvas-id="met_avtImg"
			style="width: 300px;height: 148px;position: fixed; left: -500px"></canvas>
	</view>

</template>

<script>
	const card_bg = require("@/static/met_bg.png")
	const icon_1 = require("@/static/meeting/date.png")
	const icon_2 = require("@/static/meeting/address.png")
	export default {
		props: ["item"],
		data() {
			return {

			}
		},
		methods: {
			getAvtImg(url) {
				const that = this;
				return new Promise((resolve, reject) => {
					wx.getImageInfo({
						src: url,
						success: ({
							width,
							height,
							path
						}) => {
							uni.createSelectorQuery().in(this).select('#met_avtImg').fields({
								node: true,
								size: true
							}).exec(async res => {
								const canvas = res[0].node;
								canvas.width = 300;
								canvas.height = 148;
								let ctx = canvas.getContext('2d');
								ctx.fillStyle = '#FFFFFF';
								ctx.fillRect(0, 0, 300, 148);
								let x = 0,
									y = 0;
								let ratio = 300 / 148;
								let zoom = 300 / width;
								if (width / height > ratio) { //横向截取
									x = -(((width - height * ratio) / 2) * zoom)
								} else { //竖向截取
									y = -(((height - width / ratio) / 2) * zoom)
								}

								await that.drawImage(ctx, path, x, y, width * zoom,
									height * zoom, canvas)
								setTimeout(() => {
									uni.canvasToTempFilePath({
										canvasId: "met_avtImg",
										canvas: canvas,
										destWidth: 300,
										destHeight: 148,
										quality: 1,
										success: (imgfile) => {
											resolve(imgfile
												.tempFilePath)
										},
										fail: (err) => {
											console.log(err);
											resolve("")
										},
										complete: (e) => {
											// console.log(e);
										}
									}, this)
								}, 500)
							});
						}
					})
				})
			},
			drawImage(ctx, url, x, y, w, h, canvas) {
				return new Promise((resolve, reject) => {
					let IMG = canvas.createImage();
					IMG.src = url;
					IMG.onload = () => {
						ctx.drawImage(IMG, x, y, w, h);
						resolve();
					}
				})
			},
			getImg(data) {
				const {
					meeting_name,
					start_time,
					end_time,
					meeting_address,
					meeting_img,
				} = data;
				const that = this;
				uni.showLoading({
					title:'加载中...'
				})
				return new Promise((resolve, reject) => {
					uni.createSelectorQuery().in(this).select('#met_content').fields({
						node: true,
						size: true
					}).exec(async res => {
						const canvas = res[0].node;
						canvas.width = 400;
						canvas.height = 400;
						let ctx = canvas.getContext('2d');
						ctx.fillStyle = '#FFFFFF';
						ctx.fillRect(0, 0, 400, 400);
						// await that.drawImage(ctx, card_bg, 0, 0, 400, 400, canvas)
						if (meeting_img) {
							//剪切图片
							let imgUrl = await this.getAvtImg(meeting_img);
							await that.drawImage(ctx, imgUrl, 0, 0, 400, 196, canvas)
						}
						await that.drawImage(ctx, icon_1, 60, 256, 18, 15, canvas)
						await that.drawImage(ctx, icon_2, 60, 325, 12, 15, canvas)
						//价格
						ctx.fillStyle = '#333333';
						ctx.font ="normal bold " +18 + 'px sans-serif';
						let nameCount=meeting_name.length;
						ctx.fillText(meeting_name.substring(0,20)+(nameCount>20?"...":""), 20, 224)
						ctx.fillText("时间", 20, 270)
						ctx.fillText("地点", 20, 340)
						
						ctx.fillStyle = '#8C8C8C';
						ctx.font = 14 + 'px sans-serif';
						ctx.fillText(start_time+' 至 '+end_time, 20, 294)
						ctx.fillText(meeting_address, 20, 364,320)
						ctx.strokeStyle='#e8e8e8'; 
						ctx.moveTo(20, 240)
						ctx.lineTo(360, 240)
						ctx.stroke()
						ctx.moveTo(20, 308)
						ctx.lineTo(360, 308)
						ctx.stroke()
						setTimeout(() => {
							uni.canvasToTempFilePath({
								canvasId: "met_content",
								canvas: canvas,
								destWidth: 750,
								destHeight: 650,
								quality: 1,
								success: (imgfile) => {
									resolve(imgfile.tempFilePath)
								},
								fail: (err) => {
									console.log(err);
									resolve("")
								},
								complete: (e) => {
									// console.log(e);
									uni.hideLoading()
								}
							}, this)
						}, 500)
					});
				})
			},
		},
	}
</script>

<style lang="scss" scoped>
	.card-dome {
		position: fixed;
		top: -1000px;
		left: 0px;
	}
</style>