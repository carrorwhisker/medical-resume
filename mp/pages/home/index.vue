<template>
	<view class="home">
		<!-- 轮播图 -->
		<swiper class="swiper" :autoplay="true" :interval="7000" :duration="1000" circular>
			<swiper-item v-for="(k,i) of banners" :key="i">
				<view class="swiper-item">
					<image :src="k" mode="aspectFill"></image>
				</view>
			</swiper-item>
		</swiper>
		<!-- 名片 -->
		<view class="info">
			<view class="card" @click="toMyCur">
				<image :src="cardInfo.img?cardInfo.img:'/static/def.png'" mode="aspectFill" class="avt"
					@click.stop="handlePreview"></image>
				<view class="desc">
					<view class="name" style="font-weight: bold;">{{cardInfo.name||"******"}}
						<image src="/static/icon/vip.png" mode="widthFix" v-if="userInfo.is_vip==2"></image>
					</view>
					<view class="text1" style="font-size: 26rpx;">{{cardInfo.company || '******'}}</view>
					<view class="text1" style="font-size: 26rpx;">{{cardInfo.department_name || '******'}}</view>
					<view class="text2">
						<image src="/static/icon/min_1.png" mode="widthFix"></image>
						<text>{{cardInfo.phone||'******'}}</text>
					</view>
					<view class="text2">
						<image src="/static/icon/min_2.png" mode="widthFix"></image>
						<text>{{cardInfo.email||'******'}}</text>
					</view>
					<view class="text2">
						<image src="/static/icon/min_3.png" mode="widthFix"></image>
						<text>{{cardInfo.address||'******'}}</text>
					</view>
				</view>
			</view>
			<view class="info_btn">
				<view @click="toMyCur">
					<image src="/static/icon/icon_1.png" mode="widthFix"></image> 编辑资料
				</view>
				<view @click="handleToggleCard">
					<image src="/static/icon/icon_2.png" mode="widthFix"></image>切换简历
				</view>
			</view>
		</view>
		<view class="bnt_big" style="width: 694rpx;" v-if="!cardInfo.name" @click="setShareInfo">发简历</view>
		<button type="default" class="bnt_big" style="width: 694rpx;" open-type="share" v-else>发简历</button>
		<!-- <scroll-view style="width: 750rpx;height: 650px; overflow: hidden;" scroll-x>
			<view style="width: 750px;height: 650px;">
				<card-dome ref="cardDome"></card-dome>
			</view>
		</scroll-view> -->
		<card-dome ref="cardDome"></card-dome>
		<!-- 消息 -->
		<view class="message" v-if="exchangeInfo.id">
			<view class="message_top">
				<image src="/static/icon/icon_3.png" mode="widthFix"></image>
				{{exchangeInfo.name}}想与您交换简历，请问您是否同意
			</view>
			<view class="user">
				<image :src="exchangeInfo.img" mode="aspectFill" class="user_img"></image>
				<view class="user_desc">
					<view>{{exchangeInfo.name}}</view>
					<view>{{exchangeInfo.company}}</view>
					<view>{{exchangeInfo.department}}</view>
					<view>{{exchangeInfo.exchange_time}}向您发起简历交换</view>
				</view>
				<view class="user_btn" @click="handleExchange(1)">
					<image src="/static/icon/no.png" mode="widthFix"></image>
					<view class="btn_text">忽略</view>
				</view>
				<view class="user_btn" style="margin-left: 30rpx;" @click="handleExchange(2)">
					<image src="/static/icon/yes.png" mode="widthFix"></image>
					<view class="btn_text">同意</view>
				</view>
			</view>
		</view>

		<!-- 信息 -->
		<view class="content">
			<view class="lab">
				<view class="title">我的简历数据</view>
				<view class="title_r" @click="handleToAnalyze">
					查看更多简历数据
					<image src="/static/icon/right.png" mode="widthFix"></image>
				</view>
			</view>
			<view class="tabs" @click="handleToAnalyze">
				<view>
					<view class="count">
						{{info.userViewNum}}
						<!-- <view class="tps">+1</view> -->
					</view>
					<view class="tab_text">我的访客</view>
				</view>
				<view>
					<view class="count">{{info.userExchange }}</view>
					<view class="tab_text">交换简历数</view>
				</view>
				<view>
					<view class="count">{{info.userCollect }}</view>
					<view class="tab_text">我的收藏</view>
				</view>
			</view>
			<view class="lab">
				<view class="title">我的收藏</view>
			</view>
			<cur-item v-for="k of list" :key="k.id" :item="k"></cur-item>
			<empty v-if="!list.length && !isLoading"></empty>
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
	export default {
		data() {
			return {
				banners: [],
				info: {},
				exchangeList: [], //最新
				list: [],
				isLoading: false,
				shareInfo: {
					path: "",
					title: "",
				},
				shareCardId: "",
				systemInfo: uni.getSystemInfoSync(),
			}
		},
		computed: {
			...mapState(['cardInfo', "userInfo"]),
			exchangeInfo() {
				return this.exchangeList[0] || {}
			},
		},
		async onLoad(options) {
			await this.loadBanners();
			await this.loadData();
		},
		async onPullDownRefresh() {
			await this.loadBanners();
			await this.loadData();
			uni.stopPullDownRefresh()
		},
		onShareAppMessage() {
			// uni.pageScrollTo({
			// 	scrollTop: 310 / this.systemInfo.devicePixelRatio, // 滚动到页面顶部
			// 	duration: 0 // 立即滚动
			// });
			// const promise= this.getYanChi(this.shareInfo);
			// uni.pageScrollTo({
			// 	scrollTop: 310 / this.systemInfo.devicePixelRatio, // 滚动到页面顶部
			// 	duration: 0 // 立即滚动
			// });
			return this.shareInfo;
			// setTimeout(() => {
			// 	return {
			// 		...this.shareInfo,
			// 		promise
			// 	}
			// }, 50)
		},
		onShareTimeline() {
			uni.pageScrollTo({
				scrollTop: 220, // 滚动到页面顶部
				duration: 0 // 立即滚动
			});
			setTimeout(() => {
				return {

				}
			}, 50)
		},
		methods: {
			handlePreview() {
				let url = this.cardInfo.img || '/static/def.png';
				uni.previewImage({
					urls: [url]
				})
			},
			getYanChi(val) {
				return new Promise((resolve, reject) => {
					setTimeout(() => {
						resolve(val)
					}, 50)
				})
			},
			async handleExchange(type) {
				uni.showModal({
					title: "提示",
					content: type == 1 ? "确认忽略该交换请求吗？" : "确认同意该交换请求吗？",
					success: async ({
						confirm
					}) => {
						if (confirm) {
							const res = await this.$api({
								url: "departmentCardSave",
								data: {
									id: this.exchangeInfo.id,
									type: type
								}
							})
							if (res.code == 200) {
								uni.showToast({
									title: "操作成功",
									icon: "success"
								})
								this.loadData();
							}
						}
					}
				})
			},
			async loadData() {
				this.isLoading = true;
				const res = await this.$api({
					url: "homeCardList"
				})
				this.isLoading = false;
				if (res.code == 200) {
					this.info = res.data.myselfCardNum;
					this.exchangeList = res.data.myselfExchangeList;
					this.list = res.data.myselfNewCard;
				}
			},
			handleToggleCard() {
				this.$store.commit("toggleCard");

			},
			setShareInfo() {
				// if (this.cardInfo.name) {
				// 	this.shareInfo = {
				// 		title: this.cardInfo.name + "的简历",
				// 		path: "/pages/resume/details?id=" + this.cardInfo.id
				// 	}
				// } else {
				this.$msg("暂无简历分享")
				// }
			},
			async loadBanners() {
				const res = await this.$api({
					url: "getBanner"
				})
				if (res.code == 200) {
					this.banners = res.data
				}
			},
			toMyCur() {
				uni.navigateTo({
					url: "/pages/myCur/index"
				})
			},
			handleToAnalyze() {
				uni.navigateTo({
					url: `/pages/myCur/analyze?count=${this.info.userExchange}&total=${this.info.userViewNum}`
				})
			},

		},
		watch: {
			cardInfo: {
				async handler(val) {
					if (val.name) {
						console.log(val.id);
						if (this.shareCardId != val.id) {
							let url = await this.$refs.cardDome.getImg(val)
							this.shareInfo = {
								title: `我是${val.name}，这是我的简历，请惠存`,
								path: "/pages/resume/details?id=" + val.id,
								imageUrl: url
							}
							this.shareCardId = val.id;
						}
					}
				},
				deep: true,
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
	.home {
		.swiper {
			width: 694rpx;
			height: 210rpx;
			border-radius: 25rpx;
			margin: 0 auto;

			.swiper-item {
				width: 694rpx;
				height: 210rpx;
				border-radius: 25rpx;
				overflow: hidden;

				&>image {
					width: 694rpx;
					height: 210rpx;
					border-radius: 25rpx;
				}
			}
		}

		.info {
			width: 694rpx;
			background: #FFFFFF;
			border-radius: 24rpx;
			border: 2rpx solid #C4C4C4;
			margin: 24rpx auto;


			.info_btn {
				display: flex;
				position: relative;

				&:before {
					content: "";
					display: block;
					width: 2rpx;
					height: 30rpx;
					background: #D2D1D1;
					position: absolute;
					left: 50%;
					top: 50%;
					transform: translate(-50%, -50%);
				}

				&>view {
					height: 86rpx;
					width: 50%;
					font-weight: 400;
					font-size: 26rpx;
					color: #727373;
					line-height: 86rpx;
					display: flex;
					align-items: center;
					justify-content: center;

					&>image {
						width: 26rpx;
						height: 26rpx;
						margin-right: 11rpx;
					}
				}
			}

		}

		.message {
			width: 695rpx;
			// height: 246rpx;
			border-radius: 25rpx;
			border: 2px solid #5972FE;
			overflow: hidden;
			margin: 24rpx auto;

			&_top {
				height: 60rpx;
				background: #DEE3FF;
				padding-left: 34rpx;
				line-height: 60rpx;
				font-weight: 400;
				font-size: 22rpx;
				color: #000000;
				display: flex;
				align-items: center;

				&>image {
					width: 22rpx;
					height: 19rpx;
					margin-right: 8rpx;
				}
			}

			.user {
				padding: 20rpx 36rpx 10rpx 34rpx;
				display: flex;
				align-items: center;

				&_img {
					width: 106rpx;
					height: 106rpx;
					border-radius: 30rpx;
					margin-right: 32rpx;
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
						font-weight: 400;
						font-size: 20rpx;
						color: #B8B8B8;
						margin-bottom: 16rpx;
					}
				}

				&_btn {
					&>image {
						width: 68rpx;
						height: 68rpx;
					}

					.btn_text {
						margin-top: 10rpx;
						font-weight: 400;
						font-size: 22rpx;
						color: #999999;
						text-align: center;
					}
				}
			}
		}

		.content {
			width: 694rpx;
			background: linear-gradient(to bottom, #E9F3FF, #FFFFFF);
			background-size: 694rpx 524rpx;
			background-repeat: no-repeat;
			border-radius: 25rpx;
			margin: 0 auto;
			padding: 24rpx;
			margin-top: 20rpx;

			.lab {
				height: 80rpx;
				display: flex;
				align-items: center;
				justify-content: space-between;

				.title {
					font-weight: 600;
					font-size: 36rpx;
					color: #000000;
				}

				.title_r {
					font-weight: 400;
					font-size: 26rpx;
					color: #838383;
					display: flex;
					align-items: center;

					&>image {
						width: 26rpx;
						height: 26rpx;
						margin-left: 10rpx;
					}
				}
			}

			.tabs {
				width: 641rpx;
				height: 207rpx;
				background: #F4F9FF;
				border-radius: 25rpx;
				margin: 20rpx auto;
				display: flex;

				&>view {
					width: 33.333%;
					height: 100%;
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;

					.count {
						font-weight: 600;
						font-size: 45rpx;
						color: #272727;
						position: relative;

						.tps {
							background: url("@/static/icon/msg.png");
							background-size: 100% 100%;
							position: absolute;
							top: 0rpx;
							right: 0rpx;
							transform: translate(100%, -95%);
							height: 26rpx;
							font-weight: 400;
							font-size: 20rpx;
							color: #F4F9FF;
							line-height: 26rpx;
							padding: 0rpx 10rpx;
						}
					}

					.tab_text {
						font-weight: 400;
						font-size: 24rpx;
						color: #999999;
						margin-top: 27rpx;
					}
				}
			}
		}
	}
</style>