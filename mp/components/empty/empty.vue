<template>
	<view class="empty_box">
		<view class="empty">
			<view class="img_box">
				<image :src="`/static/empty.png`"></image>
				<view class="desc">{{title}}</view>
			</view>
			<view class="btn" v-if="btnText" @click="handleClickBtn">{{btnText}}</view>
			<view class="btn_" v-else></view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			title: {
				type: String,
				default: "暂无数据",
			},
			imgUrl: {
				type: String,
				default: "empty_order",
			},
			btnText: {
				type: String,
				default: "",
			}
		},
		data() {
			return {

			}
		},
		mounted() {

		},
		methods: {
			handleClickBtn() {
				this.$emit("ClickBtn")
			}
		},
		computed: {

		}
	}
</script>

<style lang="scss" scoped>
	.empty_box{
		// width: 300rpx;
		height: 560rpx;
		position: relative;
	}
	.empty {
		position: absolute;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		text-align: center;

		.img_box {
			width: 368rpx;
			height: 300rpx;

			&>image {
				width: 368rpx;
				height: 199rpx;
				display: block;
			}

			.desc {
				margin-top: 27rpx;
				text-align: center;
				font-size: 30rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #9A9C9C;
			}
		}


		.text {
			font-size: 28rpx;
			color: #999999;
			text-align: center
		}

		.btn {
			width: 255rpx;
			height: 64rpx;
			background: linear-gradient(135deg, #41B7B5 0%, #28ADA9 100%);
			border-radius: 32rpx;
			margin: 0 auto;
			margin-top: 90rpx;
			text-align: center;
			line-height: 64rpx;
			font-size: 30rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}

		.btn_ {
			width: 255rpx;
			height: 64rpx;
		}
	}
</style>