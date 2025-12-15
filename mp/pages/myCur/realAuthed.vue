<template>
	<view class="real_auth">
		<r-top></r-top>
		<view class="back_box">
			<image src="/static/icon/back.png" mode="widthFix" style="width: 32rpx;height: 32rpx;" @click="$back()">
			</image>
		</view>
		<image src="/static/img/real.png" mode="widthFix" class="logo"></image>
		<view class="tis">{{isOther?"已通过实名认证":"您已通过实名认证"}}</view>
		<view class="content" v-if="!isOther">
			<view class="row">
				<view>姓名</view>
				<view>{{info.name}}</view>
			</view>
			<view class="row">
				<view>身份证号</view>
				<view>{{info.num}}</view>
			</view>
			<!-- <view class="row">
				<view>地址</view>
				<view>北京市*************</view>
			</view>
			<view class="row">
				<view>时间</view>
				<view>2009 -7-1</view>
			</view> -->
			<view style="margin-top: 40rpx;">身份信息</view>
			<view class="img_box">
				<image :src="info.authentication_side" mode="widthFix"></image>
				<image :src="info.authentication_straight" mode="widthFix"></image>
			</view>
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
				info: {},
				isOther: false,
			}
		},
		onLoad(options) {
			const info = uni.getStorageSync("realAuthInfo");
			if (info) {
				this.isOther = true;
			} else {
				this.loadInfo();
			}
		},
		onUnload() {
			uni.removeStorageSync("realAuthInfo")
		},
		methods: {
			async loadInfo() {
				const res = await this.$api({
					url: "getAuthentication"
				})
				if (res.code == 200) {
					this.info = res.data
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

		.logo {
			width: 202rpx;
			height: 202rpx;
			display: block;
			margin: 0 auto;
			margin-top: 60rpx;
		}

		.tis {
			font-weight: 400;
			font-size: 30rpx;
			color: #4274FD;
			line-height: 40rpx;
			text-align: center;
			margin-top: 30rpx;
		}

		.content {
			margin-top: 120rpx;
			padding: 0rpx 46rpx;
			font-weight: 400;
			font-size: 30rpx;
			color: #A8A8A8;
			line-height: 32rpx;

			.row {
				height: 88rpx;
				display: flex;
				align-items: center;

				&>view {
					width: 50%;
				}

				border-bottom: 2rpx solid #F2F2F2;
				;
			}

			.img_box {
				margin-top: 36rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				&>image {
					width: 310rpx;
					height: 215rpx;
					display: block;
					margin-bottom: 16rpx;
				}

			}
		}

	}
</style>