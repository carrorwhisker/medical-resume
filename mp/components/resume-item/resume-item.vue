<template>
	<view class="resume_item" @click="handleToDetails">
		<view class="sort" v-if="!isCollect">
			<!-- <image :src="`/static/icon/res_${sort}.png`" mode="widthFix" class="sort_img" v-if="sort<4"></image> -->
			<!-- v-else -->
			<text>{{sort}}</text>
		</view>
		<image :src="item.img" mode="aspectFill" class="avt"></image>
		<view class="res_content">
			<view class="name">
				<text>{{item.name}}</text>
				<image src="/static/icon/tag.png" mode="widthFix"></image>
				<image src="/static/img/vip.png" mode="widthFix" style="height: 30rpx;width: 50rpx;"
					v-if="item.is_vip==2"></image>
			</view>
			<view class="text">{{item.company}}丨{{item.department}}</view>
			<view class="text">{{item.position}}</view>
			<view class="bottom">
				<view class="tag_box">
					<view class="tag">{{(item.attachment||item.is_attachment==1) ?'有简历':'暂无简历'}}</view>
					<view class="tag_text" v-if="item.attachment || item.is_attachment==1">{{item.attachment_name}}</view>
				</view>
				<image src="/static/icon/phone.png" mode="widthFix"></image>
				<image src="/static/icon/wx.png" mode="widthFix"></image>
				<image src="/static/icon/email.png" mode="widthFix"></image>
			</view>
		</view>
		<image :src="`/static/icon/col${(item.is_collect==1||isCollect) ?'ed':''}.png`" mode="widthFix" class="col_img">
		</image>
	</view>
</template>

<script>
	export default {
		name: "resumeItem",
		props: {
			item: {
				type: Object,
				default: {}
			},
			sort: {
				type: Number,
				default: 1,
			},
			isCollect: {
				type: Boolean,
				default: false,
			}
		},
		data() {
			return {

			}
		},
		mounted() {

		},

		methods: {
			handleToDetails() {
				uni.navigateTo({
					url: "/pages/resume/details?id=" + this.item.id
				})
			},
		},
	}
</script>

<style lang="scss" scoped>
	.resume_item {
		width: 695rpx;
		// height: 242rpx;
		background: #FFFFFF;
		border-radius: 40rpx;
		margin: 24rpx auto;
		padding: 30rpx 24rpx;
		display: flex;
		align-items: flex-start;

		position: relative;

		.col_img {
			position: absolute;
			top: 37rpx;
			right: 41rpx;
			width: 37rpx;
			height: 31rpx;
		}

		.sort {
			width: 50rpx;
			position: relative;
			top: 30rpx;

			font-weight: 400;
			font-size: 28rpx;
			color: #737373;
			line-height: 42rpx;

			.sort_img {
				width: 32rpx;
				height: 42rpx;
			}
		}

		.avt {
			width: 105rpx;
			height: 105rpx;
			border-radius: 50%;
			position: relative;
			top: 10rpx;
		}

		.res_content {
			flex: 1;
			padding-left: 26rpx;

			.name {
				font-weight: 400;
				font-size: 28rpx;
				color: #000000;
				display: flex;
				align-items: center;
				margin-bottom: 12rpx;

				&>image {
					width: 24rpx;
					height: 24rpx;
					margin-left: 6rpx;
				}
			}

			.text {
				margin-bottom: 8rpx;
				font-weight: 400;
				font-size: 22rpx;
				color: #737373;
			}

			.bottom {
				margin-top: 16rpx;
				display: flex;
				align-items: center;

				.tag_box {
					width: 232rpx;
					display: flex;

					.tag {
						width: 85rpx;
						height: 32rpx;
						background: #FFEED4;
						border-radius: 16rpx;

						font-weight: 400;
						font-size: 20rpx;
						color: #F49E67;
						text-align: center;
						line-height: 32rpx;
					}

					.tag_text {
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

				&>image {
					width: 52rpx;
					height: 52rpx;
					margin-left: 18rpx;
				}
			}
		}
	}
</style>