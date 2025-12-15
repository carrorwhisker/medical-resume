<template>
	<view class="set_pwd">
		<r-top></r-top>
		<view class="back_box">
			<image src="/static/icon/back.png" mode="widthFix" style="width: 32rpx;height: 32rpx;" @click="$back()">
			</image>
		</view>
		<view class="title">设置登录密码</view>

		<view class="content">
			<view class="input_box" style="margin-bottom: 40rpx;">
				<input type="text" placeholder="请输入手机号" placeholder-class="ph" />
			</view>
			<view style="display: flex;align-items: center; margin-bottom:40rpx;padding-right: 50rpx;">
				<view class="input_box" style="flex: 1;">
					<input type="text" placeholder="请输入验证码" placeholder-class="ph" />
				</view>
				<view class="code_btn" style="margin-left: 28rpx;">获取验证码</view>
			</view>
			<view class="input_box" style="margin-bottom: 40rpx;">
				<input type="text" placeholder="请设置登录密码" placeholder-class="ph" />
			</view>
			<view class="input_box" style="margin-bottom: 100rpx;">
				<input type="text" placeholder="请确认登录密码" placeholder-class="ph" />
			</view>
			<view class="bnt_big">确认</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	export default {
		data() {
			return {

			}
		},
		onLoad(options) {

		},
		methods: {

		}
	}
</script>
<style>
	page {
		background: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.set_pwd {
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
			margin-top: 20rpx;
			padding-left: 56rpx;
		}

		.rule {
			padding-left: 60rpx;
			display: flex;
			align-items: center;
			// justify-content: center;
			margin-top: 60rpx;
			font-weight: 400;
			font-size: 22rpx;
			color: #9A9A9A;
		}

		.content {
			width: 625rpx;
			margin: 0 auto;
			margin-top: 106rpx;

			.code_btn {
				width: 156rpx;
				height: 60rpx;
				border-radius: 29rpx;
				border: 2rpx solid #516FFD;

				font-weight: 400;
				font-size: 24rpx;
				color: #516FFD;
				text-align: center;
				line-height: 56rpx;
			}

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