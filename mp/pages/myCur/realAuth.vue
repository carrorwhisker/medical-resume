<template>
	<view class="real_auth">
		<r-top></r-top>
		<view class="back_box">
			<image src="/static/icon/back.png" mode="widthFix" style="width: 32rpx;height: 32rpx;" @click="$back()">
			</image>
		</view>
		<view class="title">实名认证</view>
		<view class="lab">基本信息</view>
		<view class="content">
			<view class="row">
				<view>姓名</view>
				<input type="text" v-model.trim="form.name" placeholder="请输入姓名" placeholder-class="ph" />
			</view>
			<view class="row">
				<view>身份证号</view>
				<input type="text" v-model.trim="form.num" placeholder="请输入身份证号" placeholder-class="ph" />
			</view>
		</view>
		<view class="lab">请拍摄身份证原件清晰照片</view>
		<view class="title1">请拍摄本人的身份证照片</view>
		<view class="img_box">
			<view @click="handleUploadImg(1)">
				<image :src="form.side?form.side:'/static/img/idCard2.png'" mode="aspectFill" class="card"></image>
				<text>人像面</text>
			</view>
			<view @click="handleUploadImg(2)">
				<image :src="form.straight?form.straight:'/static/img/idCard1.png'" mode="aspectFill" class="card">
				</image>
				<text>国徽面</text>
			</view>
		</view>
		<view class="tis">
			<view class="dian"></view>
			<text>上传证件照片要求</text>
		</view>
		<image src="/static/img/tis.png" mode="widthFix"
			style="width: 693rpx;height: 142rpx;margin: 26rpx auto;display: block;"></image>
		<view class="bottom_box">
			<view>
				<view class="bnt_big" :class="canSubmit?'':'ban'" style="width: 623rpx;" @click="handleSubmit">确认</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import {
		checkStr
	} from "@/util/utils.js"
	export default {
		data() {
			return {
				form: {
					name: "",
					num: "",
					straight: "",
					side: ""
				},
			}
		},
		computed: {
			canSubmit() {
				return this.form.name && this.form.num && this.form.side && this.form.straight && true
			}
		},
		onLoad(options) {

		},
		methods: {
			async handleSubmit() {
				if (!this.canSubmit) return;
				let reg = /^[\u4E00-\u9FA5A-Za-z·\s]+$/
				if (!reg.test(this.form.name)) {
					uni.showToast({
						title: "请输入有效姓名"
					})
					return;
				}
				if (!checkStr(this.form.num, 'card')) {
					uni.showToast({
						title: "请输入有效身份证号"
					})
					return;
				}
				uni.showLoading({
					title: "请稍等..."
				})
				const res = await this.$api({
					url: "saveAuthentication",
					data: this.form
				})
				if (res.code == 200) {
					this.$msg("认证成功！")
					this.$store.dispatch("getCardList")
					setTimeout(() => {
						this.$back()
					}, 500)
				}
			},
			async handleUploadImg(type) {
				const res = await this.$uploadImage();
				if (res.code == 200) {
					if (type == 1) {
						this.form.side = res.data.url
					} else {
						this.form.straight = res.data.url
					}
				}
			}
		}
	}
</script>
<style>
	page {
		background: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.real_auth {
		.back_box {
			height: 88rpx;
			display: flex;
			align-items: center;
			padding-left: 20rpx;
		}

		.title {
			font-weight: bold;
			font-size: 56rpx;
			color: #0F0606;
			margin-top: 60rpx;
			padding-left: 27rpx;
		}

		.lab {
			height: 70rpx;
			background: linear-gradient(90deg, #F9F9F9, #FFFFFF);
			padding-left: 27rpx;
			font-size: 34rpx;
			color: #000000;
			line-height: 70rpx;
			margin-top: 50rpx;
		}

		.title1 {
			font-weight: 500;
			font-size: 24rpx;
			color: #8C8C8C;
			line-height: 32rpx;
			margin-top: 26rpx;
			padding-left: 27rpx;
		}

		.img_box {
			margin-top: 36rpx;
			padding: 0rpx 28rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			&>view {
				width: 332rpx;
				font-weight: 400;
				font-size: 26rpx;
				color: #000000;
				line-height: 32rpx;
				text-align: center;

				.card {
					width: 332rpx;
					height: 215rpx;
					overflow: hidden;
					display: block;
					margin-bottom: 16rpx;
				}
			}
		}

		.content {
			margin-top: 20rpx;
			padding: 0rpx 46rpx;
			font-weight: 400;
			font-size: 30rpx;
			color: #000000;
			line-height: 32rpx;

			.row {
				height: 88rpx;
				display: flex;
				align-items: center;

				&>view:first-child {
					width: 140rpx;
				}

				&>input {
					flex: 1;
					text-align: right;
				}

				border-bottom: 2rpx solid #F2F2F2;
			}

		}

		.tis {
			margin-top: 40rpx;
			padding-left: 28rpx;
			font-weight: 500;
			font-size: 22rpx;
			color: #0B6CFC;
			line-height: 32rpx;
			display: flex;
			align-items: center;

			.dian {
				width: 19rpx;
				height: 19rpx;
				background: #0267FC;
				border-radius: 50%;
			}
		}
	}
</style>