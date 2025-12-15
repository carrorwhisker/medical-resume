<template>
	<view class="my" :style="bg">
		<view class="my_title">我的</view>
		<view class="user">
			<view class="u_avt" v-if="hasLogin">
				<image :src="userInfo.img?userInfo.img:'/static/my/def.png'" mode="aspectFill"></image>
			</view>
			<view class="u_info">
				<view class="name">
					<text>{{hasLogin?userInfo.username:"欢迎来到 简历~"}}</text>
					<image src="/static/icon/vip.png" mode="widthFix"
						style="width: 31rpx;height: 29rpx;margin-left: 14rpx;" v-if="userInfo.is_vip==2"></image>
				</view>
				<view class="remark">{{hasLogin?userInfo.content:"登录开启**新体验"}}</view>
			</view>
			<view class="login_btn" v-if="!hasLogin" @click="handleToLogin">立即登录</view>
		</view>

		<view class="box tabs">
			<view class="tabs_lab">
				<view class="lab_l">参加活动</view>
				<view class="lab_r" @click="toMeetList(0)">
					<text>查看全部</text>
					<image src="/static/icon/right_.png" mode="widthFix"
						style="width: 11rpx;height: 20rpx;margin-left: 14rpx;"></image>
				</view>
			</view>
			<view class="tabs_list">
				<!-- 2 -->
				<view @click="toMeetList(0)">
					<view class="img_box">
						<image src="/static/my/icon_1.png" mode="widthFix"></image>
					</view>
					<text>待参加</text>
				</view>
				<view @click="toMeetList(1)">
					<view class="img_box">
						<image src="/static/my/icon_2.png" mode="widthFix"></image>
					</view>
					<text>待审核</text>
				</view>
			</view>
		</view>

		<view class="box list">
			<view class="row" @click="toVip">
				<image src="/static/my/icon_3.png" mode="widthFix"></image>
				<text>会员开通</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view>
			<view class="row" @click="toItinerary">
				<image src="/static/my/icon_4.png" mode="widthFix"></image>
				<text>我的行程</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view>
			<view class="row" @click="toCollect">
				<image src="/static/my/icon_4.png" mode="widthFix"></image>
				<text>我的收藏</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view>
			<view class="row" @click="toEdit">
				<image src="/static/my/icon_5.png" mode="widthFix"></image>
				<text>资料修改</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view>
			<view class="row">
				<image src="/static/my/icon_6.png" mode="widthFix"></image>
				<text>联系客服</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
				<button open-type="contact" class="contact"></button>
			</view>
			<view class="row" @click="toSet">
				<image src="/static/my/icon_7.png" mode="widthFix"></image>
				<text>设置</text>
				<image src="/static/icon/right_.png" mode="widthFix" class="r_cion"></image>
			</view>
		</view>
		<bottom-rule></bottom-rule>
		<login-check></login-check>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import {
		myBg
	} from "@/common/img.js"
	export default {
		data() {
			return {
				bg: `background-image: url(${myBg})`
			}
		},
		computed: {
			...mapGetters(["hasLogin"]),
			...mapState(["userInfo"])
		},
		onLoad(options) {

		},
		onShow() {

		},
		methods: {
			
			toEdit() {
				uni.navigateTo({
					url: "/pages/my/updateInfo"
				})
			},
			handleToLogin() {
				uni.navigateTo({
					url: "/pages/login/index"
				})
			},
			toSet() {
				uni.navigateTo({
					url: "/pages/my/setting"
				})
			},
			toCollect() {
				uni.navigateTo({
					url: '/pages/my/collect'
				})
			},
			toItinerary() {
				uni.navigateTo({
					url: '/pages/itinerary/index'
				})
			},
			toMeetList(type) {
				uni.navigateTo({
					url: '/pages/my/meetList?type=' + type
				})
			},
			toVip() {
				uni.navigateTo({
					url: "/pages/vip/index"
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	
	.my {

		background-size: 750rpx 540rpx;
		background-repeat: no-repeat;
		padding-top: 2rpx;
		padding-bottom: 20rpx;



		.my_title {
			font-family: PingFang SC;
			font-weight: 600;
			font-size: 36rpx;
			color: #FFFFFF;
			text-align: center;
			margin-top: 77rpx;
		}

		.user {
			margin-top: 74rpx;
			display: flex;
			align-items: center;
			padding-left: 28rpx;
			padding-right: 36rpx;

			.u_avt {
				width: 102rpx;
				height: 102rpx;
				border: 2rpx solid #FFFFFF;
				border-radius: 50%;
				overflow: hidden;

				&>image {
					width: 100rpx;
					height: 100rpx;
					border-radius: 50%;
				}
			}

			.u_info {
				flex: 1;
				padding-left: 30rpx;

				.name {
					display: flex;
					align-items: center;

					font-weight: 500;
					font-size: 42rpx;
					color: #FFFFFF;
				}

				.remark {
					margin-top: 16rpx;
					font-weight: 400;
					font-size: 28rpx;
					color: #FFFFFF;
				}
			}

			.login_btn {
				width: 240rpx;
				height: 80rpx;
				background: #FFFFFF;
				border-radius: 40rpx;

				font-weight: 400;
				font-size: 28rpx;
				color: #4097FF;
				text-align: center;
				line-height: 80rpx;
			}
		}

		.tabs {
			margin-top: 50rpx;
			background-image: url("@/static/bg/my_tabs.png");
			background-size: 100% 180rpx;
			background-repeat: no-repeat;
			padding: 40rpx;

			.tabs_lab {
				display: flex;
				align-items: center;
				justify-content: space-between;

				.lab_l {
					font-weight: 500;
					font-size: 32rpx;
					color: #000000;
				}

				.lab_r {
					font-weight: 400;
					font-size: 28rpx;
					color: #8C8C8C;
					display: flex;
					align-items: center;
				}
			}

			.tabs_list {
				margin-top: 50rpx;
				display: flex;
				position: relative;

				&:before {
					content: "";
					display: block;
					width: 2rpx;
					height: 86rpx;
					background-color: #EEEEEE;
					position: absolute;
					left: 50%;
					top: 50%;
					transform: translate(-50%, -50%);
				}

				&>view {
					width: 50%;
					height: 100rpx;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;

					font-weight: 400;
					font-size: 24rpx;
					color: #727272;

					.img_box {
						width: 50rpx;
						height: 44rpx;
						position: relative;
						margin-bottom: 26rpx;

						&>image {
							width: 100%;
							height: 100%;
						}

						.tag {
							width: 30rpx;
							height: 30rpx;
							background: #FF1F1F;
							border-radius: 50%;
							position: absolute;
							top: -30rpx;
							right: -30rpx;

							font-weight: 400;
							font-size: 20rpx;
							color: #FFFFFF;
							text-align: center;
							line-height: 30rpx;
						}
					}


				}
			}
		}

		.list {
			padding: 20rpx 30rpx;

			.row {
				height: 114rpx;
				display: flex;
				align-items: center;
				position: relative;
				
				.contact{
					width: 100%;
					height: 100%;
					position: absolute;
					top: 0;
					left: 0;
					opacity: 0;
				}

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