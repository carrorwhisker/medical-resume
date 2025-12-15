<template>
	<view class="login">
		<r-top></r-top>
		<view class="back_box">
			<!-- <image src="/static/icon/back.png" mode="widthFix" style="width: 32rpx;height: 32rpx;" @click="$back()">
			</image> -->
		</view>
		<!-- <view class="top_login">
			<text @click="handleToPhone">快速登录</text>
		</view> -->
		<view class="logo">
			<image src="/static/logo.png" mode="heightFix"></image>
		</view>
		<view class="login_title">欢迎来到{{appInfo.appName}}</view>
		<view class="content">
			<!-- <view class="input_box" style="margin-bottom: 40rpx;">
				<input type="text" v-model.trim="form.name" placeholder="邮箱/手机号" placeholder-class="ph" />
			</view>
			<view class="input_box" style="margin-bottom: 30rpx;">
				<input type="safe-password" v-model.trim="form.pwd" placeholder="请输入登录密码" placeholder-class="ph" />
			</view>
			<view class="rest">
				<text @click="toSetPwd">忘记密码</text>
			</view> -->
			<button class="bnt_big" open-type="getPhoneNumber" @getphonenumber="getphonenumber">手机一键登录</button>
			<!-- <view class="other_login">
				第三方登录
			</view>
			<view class="login_box">
				<image src="/static/img/wx.png" mode="widthFix"></image>
			</view> -->
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import {loginBack} from "@/util/utils.js"
	export default {
		data() {
			return {
				form: {
					name: "",
					pwd: ""
				},
				code: "",
				appInfo: uni.getSystemInfoSync(),
			}
		},
		onLoad(options) {
			uni.login({
				success: (res) => {
					this.code = res.code
				}
			})
		},
		methods: {
			toSetPwd() {
				uni.navigateTo({
					url: "/pages/my/setPwd"
				})
			},
			handleToPhone() {
				uni.navigateTo({
					url: "/pages/login/phoneLogin"
				})
			},
			async getphonenumber(e) {
				const {
					code,
					encryptedData,
					iv
				} = e.detail;
				uni.showLoading({
					title: "登录中..."
				})
				const res = await this.$api({
					url: "login",
					data: {
						jscode: this.code,
						encryptedData: encryptedData,
						iv: iv
					}
				})
				if (res.code == 200) {
					uni.setStorageSync("token", res.data)
					uni.hideLoading()
					uni.showToast({
						title: "登录成功",
						icon: "success"
					})
					// this.$store.commit("setToken",111)
					this.$store.dispatch("initData")
					// this.$back()
					loginBack()
					// uni.switchTab({
					// 	url: "/pages/home/index"
					// })
				}
				console.log(e);
			},
			handleLogin() {
				uni.showLoading({
					title: "登录中..."
				})
				uni.setStorageSync("token", 111)
				setTimeout(() => {
					uni.hideLoading()
					uni.showToast({
						title: "登录成功",
						icon: "success"
					})
					// this.$store.commit("setToken",111)
					this.$store.dispatch("initData")
					// this.$back()
					uni.switchTab({
						url: "/pages/home/index"
					})
				}, 500)
			},
		}
	}
</script>
<style>
	page {
		background: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.login {
		padding-top: 20rpx;

		.back_box {
			height: 88rpx;
			display: flex;
			align-items: center;
			padding-left: 20rpx;
		}

		.top_login {
			font-weight: 800;
			font-size: 33rpx;
			color: #000000;
			line-height: 36rpx;
			text-align: right;
			padding-right: 54rpx;
		}

		.logo {
			// width: 172rpx;
			height: 172rpx;
			// background: #516FFD;
			// border: 2px solid #516FFD;
			margin: 0 auto;
			margin-top: 46rpx;

			&>image {
				height: 172rpx;
				display: block;
				margin: 0 auto;
			}
		}

		.login_title {
			font-weight: 800;
			font-size: 33rpx;
			color: #000000;
			line-height: 36rpx;
			text-align: center;
			margin-top: 46rpx;
		}

		.content {
			width: 625rpx;
			margin: 0 auto;
			margin-top: 106rpx;

			.rest {
				font-weight: 400;
				font-size: 27rpx;
				color: #464646;
				line-height: 31rpx;
				text-align: right;
				margin-bottom: 44rpx;
			}

			.other_login {
				font-weight: 400;
				font-size: 27rpx;
				color: #464646;
				line-height: 31rpx;
				text-align: center;
				margin-top: 82rpx;
			}

			.login_box {
				margin-top: 42rpx;
				display: flex;
				align-items: center;
				justify-content: center;

				&>image {
					width: 84rpx;
					height: 84rpx;
					border-radius: 50%;
				}
			}
		}
	}
</style>