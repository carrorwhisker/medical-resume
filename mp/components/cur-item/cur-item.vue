<template>
	<view class="cur_item" @click="handleToDetails_">
		<view class="cur_info">
			<image :src="item.img" mode="aspectFill" class="cur_info_img"></image>
			<view class="cur_info_desc">
				<view>{{item.name}}</view>
				<view>{{item.company}}</view>
				<view>{{item.department_name}}</view>
				<view v-if="type==0" style="display: flex;align-items: center;">
					<view class="tag">{{item.is_attachment==1?"有简历":"暂无简历"}}</view> <text class="tag_text"
						v-if="item.is_attachment">{{item.attachment_name}}</text>
				</view>
			</view>
			<view class="cur_info_btn">
				<!-- <view class="new" v-if="type==0">简历有更新</view> -->
				<view class="btn" v-if="type==0" @click="handleToDetails">查看简历</view>
				<view class="btn" v-if="type==1 && item.status==1" @click="handleExchange">同意交换</view>
				<view class="status" v-if="type==1 && item.status==2">已同意</view>
			</view>
		</view>
		<view class="msg" v-if="type==1 && item.status==1">
			<view>
				对方发起与 <text style="color: #333333;margin: 0rpx 8rpx;">您</text> 的简历交 换请求
			</view>
			<view class="msg_time">{{item.exchange_time}}向您发起简历交换</view>
		</view>
		<view class="line"></view>
	</view>
</template>

<script>
	export default {
		name: "curItem",
		props: {
			item: {
				type: Object,
				default: {}
			},
			type: {
				type: Number,
				default: 0, //0简历  1交换简历
			},
		},
		data() {
			return {

			}
		},
		mounted() {

		},

		methods: {
			handleExchange() {
				this.$emit("exchange", this.item.id)
			},
			handleToDetails() {
				uni.navigateTo({
					url: "/pages/resume/details?id=" + this.item.id
				})
			},
			handleToDetails_() {
				if (this.type == 1 && this.item.status == 2) {
					uni.navigateTo({
						url: "/pages/resume/details?id=" + this.item.exchange_card_id
					})
				}
			}
		},
	}
</script>

<style lang="scss" scoped>
	.cur_item {
		// width: 640rpx;
		margin: 0 auto;
		padding: 22rpx 10rpx 0rpx 10rpx;

		.cur_info {
			display: flex;
			align-items: center;

			&_img {
				width: 106rpx;
				height: 106rpx;
				border-radius: 30rpx;
				margin-right: 28rpx;
			}

			&_desc {
				flex: 1;

				&>view:nth-child(1) {
					font-weight: 400;
					font-size: 28rpx;
					color: #000000;
					margin-bottom: 10rpx;
				}

				&>view:nth-child(2) {
					font-weight: 400;
					font-size: 22rpx;
					color: #737373;
					margin-bottom: 10rpx;
				}

				&>view:nth-child(3) {
					font-weight: 400;
					font-size: 22rpx;
					color: #737373;
					margin-bottom: 10rpx;
				}

				&>view:nth-child(4) {
					display: flex;
					align-items: center;
					font-weight: 400;
					font-size: 20rpx;
					color: #B8B8B8;
					margin-bottom: 16rpx;

					.tag {
						padding: 0rpx 12rpx;
						height: 32rpx;
						background: #FFEED4;
						border-radius: 16rpx;

						font-weight: 400;
						font-size: 20rpx;
						color: #F49E67;
						line-height: 32rpx;
						margin-right: 8rpx;
					}
					.tag_text{
						margin-left: 8rpx;
						width: 130rpx;
						font-weight: 400;
						font-size: 22rpx;
						color: #BBBBBB;
						overflow: hidden;
						text-overflow: ellipsis;
						white-space: nowrap;
					}
				}
			}

			&_btn {
				.new {
					font-weight: 400;
					font-size: 22rpx;
					color: #999999;
					padding-right: 18rpx;
					position: relative;
					text-align: right;

					&::after {
						content: "";
						display: block;
						width: 8rpx;
						height: 8rpx;
						border-radius: 50%;
						background-color: #FF4A46;
						position: absolute;
						top: 50%;
						right: 0;
						transform: translate(-50%, -50%);
					}
				}

				.status {
					width: 184rpx;
					text-align: center;
					font-weight: 400;
					font-size: 26rpx;
					color: #B8B8B8;
				}

				.btn {
					margin-top: 20rpx;
					width: 184rpx;
					height: 64rpx;
					border-radius: 26rpx;
					border: 2rpx solid #298DFD;

					font-weight: 400;
					font-size: 28rpx;
					color: #298DFD;
					text-align: center;
					line-height: 60rpx;
				}
			}
		}

		.msg {
			margin-top: 10rpx;
			background: #F6F6F6;
			border-radius: 26rpx;
			padding: 26rpx;

			&>view {
				font-weight: 400;
				font-size: 26rpx;
				color: #ABABAB;
				line-height: 36rpx;
			}

			&_time {
				margin-top: 10rpx;
				font-weight: 400;
				font-size: 22rpx;
				color: #ABABAB;
				line-height: 36rpx;
			}
		}

		.line {
			width: 618rpx;
			height: 2rpx;
			background: #F5F5F5;
			margin: 0 auto;
			margin-top: 20rpx;
		}
	}
</style>