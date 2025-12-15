<template>
	<view class="item">
		<view class="item_top">
			<text>{{item.start_time}}</text>
			<text class="status">{{item.status_text || item.status}}</text>
		</view>
		<view class="line"></view>
		<view class="item_info">
			<image :src="item.meeting_img" mode="aspectFill" class="img"></image>
			<view class="title">{{item.meeting_name}}</view>
		</view>
		<view class="line"></view>
		<view class="item_addres">
			<view>地址：</view>
			<view>{{item.qy_name}}</view>
		</view>
		<view class="btn_box">
			<view class="btn" @click="toDetails">查看详情</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: ["item"],
		data() {
			return {

			}
		},
		mounted() {

		},

		methods: {
			toDetails() {
				uni.navigateTo({
					url: "/pages/meeting/applyResult?id=" + this.item.active_id
				})
			}
		},
	}
</script>

<style lang="scss" scoped>
	.item {
		width: 693rpx;
		background: #FFFFFF;
		border-radius: 25rpx;
		margin: 0 auto;
		margin-bottom: 24rpx;

		.line {
			margin: 0;
		}

		.item_top {
			height: 80rpx;
			padding: 0 30rpx;
			display: flex;
			justify-content: space-between;
			align-items: center;

			font-weight: 400;
			font-size: 30rpx;
			color: #8C8C8C;

			.status {
				color: #FE4324;
			}
		}

		.item_info {
			display: flex;
			padding: 28rpx 30rpx;

			.img {
				width: 202rpx;
				height: 114rpx;
				border-radius: 20rpx;
			}

			.title {
				flex: 1;
				padding-left: 26rpx;
				font-weight: 400;
				font-size: 30rpx;
				color: #000000;
			}
		}

		.item_addres {
			display: flex;
			align-items: flex-start;
			margin-top: 30rpx;
			padding: 0rpx 30rpx;
			font-weight: 400;
			font-size: 32rpx;
			color: #000000;
			line-height: 40rpx;

			&>view:nth-child(2) {
				flex: 1;
				font-weight: bold;
			}
		}

		.btn_box {
			height: 114rpx;
			display: flex;
			justify-content: flex-end;
			padding-top: 20rpx;
			padding-right: 16rpx;

			.btn {
				width: 184rpx;
				height: 60rpx;
				background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
				border-radius: 30rpx;

				font-weight: 400;
				font-size: 26rpx;
				color: #FFFFFF;
				text-align: center;
				line-height: 60rpx;
			}
		}
	}
</style>