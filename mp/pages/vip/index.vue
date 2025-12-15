<template>
	<view class="vip">
		<!-- 用户信息 -->
		<view class="user_info">
			<image :src="userInfo.img?userInfo.img:'/static/my/def.png'" mode="aspectFill" class="avt"></image>
			<view class="user_desc">
				<view class="user_name">
					<text>{{userInfo.username}}</text>
					<image src="/static/vip/vip.png" mode="aspectFill" v-if="userInfo.is_vip==2"></image>
				</view>
				<view class="vip_time">
					会员总时长：<text style="color: #F2CF9F;">{{userInfo.vip_time}}</text>到期
				</view>
			</view>
		</view>
		<!-- 权益 -->
		<view class="rights">
			<view class="rights_title">VIP会员升级后 <text style="color: #F2CF9F;">可获得如下奖励</text></view>
			<view class="rights_list">
				<view>
					<view class="rights_icon">
						<image src="/static/vip/icon_1.png" mode="widthFix"></image>
					</view>
					<view>保存头像</view>
				</view>
				<view>
					<view class="rights_icon">
						<image src="/static/vip/icon_2.png" mode="widthFix"></image>
					</view>
					<view>复制简历</view>
				</view>
				<view>
					<view class="rights_icon">
						<image src="/static/vip/icon_3.png" mode="widthFix"></image>
					</view>
					<view>转发简历</view>
				</view>
				<view>
					<view class="rights_icon">
						<image src="/static/vip/icon_4.png" mode="widthFix"></image>
					</view>
					<view>发布会议</view>
				</view>
			</view>
		</view>

		<view class="list">
			<view class="title">成为会员</view>
			<view class="item" :class="actIndex==i?'active':''" v-for="(k,i) of list" :key="k.id"
				@click="handleSelect(i)">
				<!-- <view class="tag">推荐</view> -->
				<view class="time">{{k.desc}}</view>
				<view class="money">
					<image src="/static/vip/rmb.png" mode="widthFix"></image>
					<!-- <text>¥</text> -->
					<text>{{k.price.toFixed(0)}}</text>
				</view>
				<view class="tiem_title">您将享受如下特权</view>
				<view class="item_lab">保存头像</view>
				<view class="item_lab">复制简历</view>
				<view class="item_lab">转发简历</view>
				<view class="item_lab" v-if="i>0">发布会议</view>
				<view class="item_lab" v-if="i>0">导出报名数据</view>
				<view class="item_lab" v-if="i>1">微官网发布制作1次</view>
			</view>
		</view>
		<!-- <view class="desc">
			<view class="title">尊享VIP特权 | 开启专属精彩体验</view>
			<view class="text">海量资源免费解锁：独家课程、高清影音、精品文档，VIP专享库无限访问</view>
			<view class="text">极速体验：超大带宽加速，下载/播放速度提升300%，告别等待</view>
			<view class="text">优先服务：24小时专属客服，问题优先处理，需求快速响应</view>
			<view class="text">会员标识：金色认证徽章，社区发言置顶，彰显独特身份</view>
			<view class="text">定制功能：个性化界面、专属主题、生日礼包等10+项定制服务</view>
		</view> -->
		<view style="height: 160rpx;"></view>
		<view class="bottom">
			<view>
				<view>
					<view class="total">
						<!-- <text>实付</text> -->
						<!-- <text style="margin-left: 6rpx;">¥</text> -->
						<image src="/static/vip/rmb.png" mode="widthFix"></image>
						<text>{{actInfo.price?actInfo.price.toFixed(0):''}}</text>
					</view>
					<view class="vip_rlu" @click="isAgChange">
						<image :src="`/static/select${isAg?'ed':''}.png`" mode="widthFix"
							style="width: 28rpx;height: 28rpx;margin-right: 4rpx;"></image> 已阅读并同意
						<text @click.stop="toArticle(3)" style="color:#358FFF;">《会员协议》</text>
					</view>
				</view>
				<view class="btn" @click="payVip">立即购买</view>
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
				list: [],
				actIndex: 0,
				isAg: false,
			}
		},
		computed: {
			...mapState(['userInfo']),
			actInfo() {
				return this.list[this.actIndex] || {}
			},
		},
		onLoad(options) {
			this.loadList()
		},
		methods: {
			isAgChange() {
				this.isAg = !this.isAg;
			},
			toArticle(type) {
				uni.navigateTo({
					url: "/pages/article/index?type=" + type
				})
			},
			handleSelect(i) {
				this.actIndex = i
			},
			async loadList() {
				const res = await this.$api({
					url: "getVipList"
				})
				if (res.code == 200) {
					this.list = res.data
				}
			},
			async payVip() {
				if (!this.isAg) {
					this.$msg("请先阅读会员协议")
					return;
				}
				const res = await this.$api({
					url: "rechargePay",
					data: {
						type: this.actInfo.id
					}
				})
				if (res.code == 200) {
					const {
						app_response
					} = res.data;
					console.log(app_response);
					wx.requestPayment({
						...app_response,
						success: () => {
							uni.showToast({
								title: "支付成功",
								icon: "success"
							})
							this.$store.dispatch("getUserInfo")
						},
						complete: (info) => {
							console.log(info);
						}
					})
				}
			}
		}
	}
</script>
<style>
	page {
		/* background: #FFF; */
		background: #252531;

	}
</style>
<style lang="scss" scoped>
	.user_info {
		padding: 30rpx;
		display: flex;
		align-items: center;
		color: #FFF;

		.avt {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
		}

		.user_desc {
			flex: 1;
			padding-left: 20rpx;

			.user_name {
				display: flex;
				align-items: center;
				font-size: 32rpx;
				font-weight: bold;

				&>image {
					width: 68rpx;
					height: 68rpx;
					margin-left: 10rpx;
				}
			}

			.vip_time {
				color: #999DA6;
				margin-top: 10rpx;
				font-size: 24rpx;
			}
		}
	}

	.rights {
		width: 690rpx;
		border-radius: 20rpx;
		padding: 30rpx 50rpx 50rpx 50rpx;
		background: linear-gradient(to right, #303145, #44495D);
		color: #FFFFFF;
		margin: 0 auto;

		&_title {
			font-size: 30rpx;
			line-height: 46rpx;
			margin-bottom: 30rpx;
		}

		.rights_list {
			display: flex;
			justify-content: space-between;
			text-align: center;
			font-size: 30rpx;

			.rights_icon {
				width: 110rpx;
				height: 110rpx;
				border-radius: 50%;
				background-color: #45475C;
				display: block;
				margin-bottom: 20rpx;

				&>image {
					width: 100%;
					height: 100%;
				}
			}
		}
	}

	.vip {
		padding-top: 40rpx;

		.list {
			background-color: #252531;
			border: 2rpx solid #534B48;
			width: 690rpx;
			border-radius: 20rpx;
			padding: 80rpx 20rpx 20rpx 20rpx;
			margin: 40rpx auto;
			margin-top: 100rpx;
			position: relative;
			display: flex;
			justify-content: space-between;

			.title {
				background-color: #252531;
				color: #F3CF9E;
				font-size: 36rpx;
				line-height: 60rpx;
				padding: 0rpx 30rpx;
				position: absolute;
				left: 50%;
				top: -30rpx;
				transform: translateX(-50%);
				z-index: 1;
			}

			.item {
				width: 32%;
				border-radius: 10rpx;
				position: relative;
				margin-bottom: 20rpx;
				background: #F2D9C0;
				text-align: center;
				padding: 20rpx 10rpx;

				.tag {
					width: 77rpx;
					height: 40rpx;
					background: #FA0D49;
					border-radius: 8rpx;
					position: absolute;
					top: -22rpx;
					left: 12rpx;

					font-weight: 600;
					font-size: 23rpx;
					color: #FFFFFF;
					text-align: center;
					line-height: 40rpx;
				}

				.time {
					font-weight: 600;
					font-size: 27rpx;
					color: #000000;
					margin-bottom: 20rpx;
				}

				.money {
					font-weight: 800;
					font-size: 60rpx;
					display: flex;
					align-items: flex-end;
					color: transparent;
					background: linear-gradient(45deg, #7A482D, #C87B3D);
					background-clip: text;
					-webkit-background-clip: text;

					&>image {
						width: 48rpx;
						height: 48rpx;
						position: relative;
						top: -8rpx;
					}
				}

				.tiem_title {
					font-size: 24rpx;
					line-height: 36rpx;
					margin-bottom: 20rpx;
				}

				.item_lab {
					font-size: 22rpx;
					line-height: 30rpx;
					margin-bottom: 10rpx;
					text-align: left;
				}
			}

			.active {
				position: relative;

				&::after {
					content: "";
					display: block;
					width: 100%;
					height: 100%;
					position: absolute;
					top: 0;
					left: 0;
					border: 2rpx solid #EF6820;
					border-radius: 10rpx;
				}
			}
		}

		.desc {
			width: 690rpx;
			margin: 0 auto;
			margin-top: 20rpx;
			padding: 20rpx;
			// border: 2rpx solid #358FFF;
			border-radius: 20rpx;
			background: #D7E9FF;
			color: #666666;

			.title {
				font-size: 34rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 20rpx;
			}

			.text {
				font-size: 28rpx;
				line-height: 40rpx;
			}
		}


		.bottom {
			width: 750rpx;
			background: #4A4C73;
			// border-top: 2rpx solid #F6F6F6;
			
			position: fixed;
			bottom: 0;
			left: 0;
			padding: 0rpx 28rpx;
			padding-bottom: constant(safe-area-inset-bottom);
			/* 兼容 iOS 设备 */
			padding-bottom: env(safe-area-inset-bottom);
			/* 兼容 iPhone X 及以上设备 */

			&>view {
				height: 152rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.total {
					display: flex;
					align-items: flex-end;
					color: transparent;
					background: linear-gradient(45deg, #7A482D, #C87B3D);
					background-clip: text;
					-webkit-background-clip: text;
					// &>text:nth-child(1) {
					// 	font-weight: 400;
					// 	font-size: 26rpx;
					// 	color: #000000;
					// 	position: relative;
					// 	top: -8rpx;
					// }

					// &>text:nth-child(2) {
					// 	font-weight: 600;
					// 	font-size: 26rpx;
					// 	color: #FE4324;
					// 	position: relative;
					// 	top: -8rpx;
					// }
					&>image {
						width: 40rpx;
						height: 40rpx;
						position: relative;
						top: -12rpx;
					}

					&>text {
						font-weight: 600;
						font-size: 60rpx;
						// color: #FE4324;
					}
				}

				.vip_rlu {
					font-size: 20rpx;
					display: flex;
					align-items: center;
				}

				.btn {
					// width: 300rpx;
					height: 90rpx;
					padding: 0rpx 40rpx;
					background: linear-gradient(to right, #EAB87B, #F8D9AD);
					border-radius: 45rpx;

					font-weight: 400;
					font-size: 38rpx;
					color: #2C2831;
					text-align: center;
					line-height: 90rpx;
				}
			}
		}
	}
</style>