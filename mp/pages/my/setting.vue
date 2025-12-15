<template>
	<view class="setting">
		<view class="box list">
			<!-- <view class="row" @click="toUpdate">
				<text>修改基本信息</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view> -->
			<!-- <view class="row" @click="toSetPwd">
				<text>修改密码</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view> -->
		</view>
		<view class="logout" @click="handleLogout" v-if="hasLogin">退出登录</view>
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
		computed: {
			...mapGetters(["hasLogin"]),
		},
		onLoad(options) {

		},
		methods: {
			toSetPwd() {
				uni.navigateTo({
					url: "/pages/my/setPwd"
				})
			},
			toUpdate(){
				uni.navigateTo({
					url: "/pages/my/updateInfo"
				})
			},
			handleLogout() {
				this.$store.commit("logout")
			}
		}
	}
</script>

<style lang="scss" scoped>
	.setting {
		padding-top: 2rpx;
		
		.logout {
			width: 623rpx;
			height: 88rpx;
			margin: 0 auto;
			margin-top: 80rpx;
			border-radius: 44rpx;
			background: #FF4A46;
		
			font-weight: 400;
			font-size: 32rpx;
			color: #FFFFFF;
			text-align: center;
			line-height: 88rpx;
		}

		.list {
			padding: 0rpx 30rpx;

			.row {
				height: 114rpx;
				display: flex;
				align-items: center;
				position: relative;

				&>image {
					width: 74rpx;
					height: 74rpx;
					margin-right: 24rpx;
				}

				font-weight: 400;
				font-size: 32rpx;
				color: #000000;

				.r_cion {
					width: 11rpx;
					height: 20rpx;
					position: absolute;
					right: 0;
					top: 50%;
					transform: translateY(-50%);
				}
			}
		}
	}
</style>