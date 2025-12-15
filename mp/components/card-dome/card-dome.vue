<template>
	<!--  -->
	<view>
		<canvas type="2d" id="content" canvas-id="content"
			style="width: 375px;height: 325px;position: fixed; left: -400px"></canvas>
		<canvas type="2d" id="avtImg" canvas-id="avtImg"
			style="width: 100px;height: 140px;position: fixed; left: -400px"></canvas>
	</view>

</template>

<script>
	const bg_img = require("@/static/card_bg.png")
	const card_bg = require("@/static/bg/card_bg.png")
	const icon_1 = require("@/static/icon/min_1.png")
	const icon_2 = require("@/static/icon/min_2.png")
	const icon_3 = require("@/static/icon/min_3.png")
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
							uni.createSelectorQuery().in(this).select('#avtImg').fields({
								node: true,
								size: true
							}).exec(async res => {
								const canvas = res[0].node;
								canvas.width = 100;
								canvas.height = 140;
								let ctx = canvas.getContext('2d');
								ctx.fillStyle = '#FFFFFF';
								ctx.fillRect(0, 0, 100, 140);
								let x = 0,
									y = 0;
								let ratio = 100 / 140;
								let zoom = 140 / height;
								if (width / height > ratio) { //横向截取
									x = -(((width - height * ratio) / 2) * zoom)
								} else { //竖向截取
									y = -(((height - width / ratio) / 2) * zoom)
								}

								await that.drawImage(ctx, path, x, y, width * zoom,
									height * zoom, canvas)
								setTimeout(() => {
									uni.canvasToTempFilePath({
										canvasId: "avtImg",
										canvas: canvas,
										destWidth: 200,
										destHeight: 280,
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
					name = "测试", company = "测试公司", position = "测试职位", phone = "12345678963", img, email, address,department_name,department
				} = data;
				const that = this;
				uni.showLoading({
					title:"加载中..."
				})
				return new Promise((resolve, reject) => {
					uni.createSelectorQuery().in(this).select('#content').fields({
						node: true,
						size: true
					}).exec(async res => {
						const canvas = res[0].node;
						canvas.width = 375;
						canvas.height = 325;
						let ctx = canvas.getContext('2d');
						ctx.fillStyle = '#FFFFFF';
						ctx.fillRect(0, 0, 375, 325);
						await that.drawImage(ctx, card_bg, 0, 0, 375, 230, canvas)
						await that.drawImage(ctx, bg_img, 0, 0, 375, 325, canvas)
						if (img) {
							//剪切图片
							let imgUrl = await this.getAvtImg(img);
							await that.drawImage(ctx, imgUrl, 30, 30, 100, 140, canvas)
						}
						await that.drawImage(ctx, icon_1, 150, 125, 18, 18, canvas)
						await that.drawImage(ctx, icon_2, 150, 147, 18, 18, canvas)
						await that.drawImage(ctx, icon_3, 150, 169, 18, 18, canvas)
						//价格
						ctx.fillStyle = '#FFFFFF';
						ctx.font = 16 + 'px sans-serif';
						ctx.fillText(company, 150, 80)
						ctx.font = 24 + 'px sans-serif';
						ctx.fillText(name, 150, 50)
						ctx.font = 16 + 'px sans-serif';
						// ctx.fillText(position, 150, 105)
						ctx.fillText(department_name || department, 150, 105)
						ctx.font = 14 + 'px sans-serif';
						// ctx.fillText(phone, 170, 140)
						// ctx.fillText(email, 170, 162)
						// ctx.fillText(address, 170, 184)
						ctx.fillText("电话：******", 170, 140)
						ctx.fillText("邮箱：******", 170, 162)
						ctx.fillText("地址：******", 170, 184)
						setTimeout(() => {
							uni.canvasToTempFilePath({
								canvasId: "content",
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