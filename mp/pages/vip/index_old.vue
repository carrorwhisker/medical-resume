<template>
	<view class="vip">
		<view class="list">
			<view class="item" :class="actIndex==i?'active':''" v-for="(k,i) of list" :key="k.id"
				@click="handleSelect(i)">
				<!-- <view class="tag">推荐</view> -->
				<view class="time">{{k.desc}}</view>
				<view class="money">
					<text>¥</text>
					<text style="font-size: 50rpx;">{{k.price.toFixed(2)}}</text>
				</view>
			</view>
		</view>
		<view class="desc">
			<view class="title">尊享VIP特权 | 开启专属精彩体验</view>
			<view class="text">海量资源免费解锁：独家课程、高清影音、精品文档，VIP专享库无限访问</view>
			<view class="text">极速体验：超大带宽加速，下载/播放速度提升300%，告别等待</view>
			<view class="text">优先服务：24小时专属客服，问题优先处理，需求快速响应</view>
			<view class="text">会员标识：金色认证徽章，社区发言置顶，彰显独特身份</view>
			<view class="text">定制功能：个性化界面、专属主题、生日礼包等10+项定制服务</view>
		</view>
		<view style="height: 160rpx;"></view>
		<view class="bottom">
			<view class="total">
				<text>实付</text>
				<text style="margin-left: 6rpx;">¥</text>
				<text>{{actInfo.price?actInfo.price.toFixed(2):''}}</text>
			</view>
			<view class="btn" @click="payVip">开通会员</view>
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
			}
		},
		computed: {
			actInfo() {
				return this.list[this.actIndex] || {}
			},
		},
		onLoad(options) {
			this.loadList()
		},
		methods: {
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
				const res = await this.$api({
					url: "rechargePay",
					data: {
						code: this.actInfo.id
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
		background: #FFF;
	}
</style>
<style lang="scss" scoped>
	.vip {
		.list {
			display: flex;
			flex-wrap: wrap;
			justify-content: space-between;
			padding: 40rpx 50rpx;

			.item {
				width: 310rpx;
				height: 185rpx;
				background: #F4F6F8;
				border-radius: 25rpx;
				border: 4px solid #E4E6E7;
				padding: 30rpx 0rpx 0rpx 37rpx;
				position: relative;
				margin-bottom: 20rpx;

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
					font-size: 25rpx;
					color: #000000;
					display: flex;
					align-items: flex-end;

					&>text:nth-child(1) {
						position: relative;
						top: -8rpx;
					}
				}
			}

			.active {
				border-color: #516FFD;

				.money {
					color: #5D74FE;
				}
			}
		}
		
		.desc{
			width: 690rpx;
			margin: 0 auto;
			margin-top: 20rpx;
			padding: 20rpx;
			// border: 2rpx solid #358FFF;
			border-radius: 20rpx;
			background: #D7E9FF;
			color: #666666;
			.title{
				font-size: 34rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 20rpx;
			}
			.text{
				font-size: 28rpx;
				line-height: 40rpx;
			}
		}
		

		.bottom {
			width: 750rpx;
			height: 152rpx;
			background: #FFFFFF;
			border-top: 2rpx solid #F6F6F6;
			position: fixed;
			bottom: 0;
			left: 0;
			padding: 0rpx 28rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.total {
				display: flex;
				align-items: flex-end;

				&>text:nth-child(1) {
					font-weight: 400;
					font-size: 26rpx;
					color: #000000;
					position: relative;
					top: -8rpx;
				}

				&>text:nth-child(2) {
					font-weight: 600;
					font-size: 26rpx;
					color: #FE4324;
					position: relative;
					top: -8rpx;
				}

				&>text:nth-child(3) {
					font-weight: 600;
					font-size: 48rpx;
					color: #FE4324;
				}
			}

			.btn {
				width: 300rpx;
				height: 90rpx;
				background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
				border-radius: 45rpx;

				font-weight: 400;
				font-size: 36rpx;
				color: #FFFEFE;
				text-align: center;
				line-height: 90rpx;
			}
		}
	}
</style>