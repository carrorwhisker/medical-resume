<template>
	<view class="details">
		<view class="box">
			<view class="resume_card">
				<view class="desc">
					<view class="name">{{info.name}}
						<image src="/static/icon/vip.png" mode="widthFix" v-if="info.is_vip==2"></image>
					</view>
					<view class="text1">{{info.company}}</view>
					<view class="text1">{{info.department_name}}</view>
					<view class="text2">
						<image src="/static/icon/min_1.png" mode="widthFix"></image>
						<text>{{showVal(info.phone)}}</text>
					</view>
					<view class="text2">
						<image src="/static/icon/min_2.png" mode="widthFix"></image>
						<text>{{showVal(info.email)}}</text>
					</view>
					<view class="text2">
						<image src="/static/icon/min_3.png" mode="widthFix"></image>
						<text>{{info.address}}</text>
					</view>
				</view>
				<view class="avt">
					<image :src="info.img?info.img:'/static/def.png'" mode="aspectFill"></image>
					<!-- v-if="userInfo.is_vip==2" -->
					<!-- showVipBtn?'VIP':'' -->
					<view class="avt_btn btn_tb" @click="saveAvt">保存照片</view>
				</view>
			</view>
			<view class="see_box">
				<view class="avt_list" v-if="viewCount">
					<view v-for="k in viewCount" :key="k">
						<image src="/static/icon/user_icon.png" mode="aspectFill"></image>
					</view>
				</view>
				<view class="see_text" v-if="viewCount">{{info.view_num}}人看过他</view>
				<image :src="`/static/icon/col${info.is_collect==1?'ed':''}.png`" mode="widthFix" class="col"></image>
			</view>
		</view>

		<view class="box authentication" @click="handleRealAuth">
			<text class="title">他的认证</text>
			<image :src="info.is_authentication==1?'/static/icon/smrzd.png':'/static/icon/smrz.png'" mode="widthFix"
				style="width: 132rpx;height: 45rpx;">
			</image>
		</view>

		<view class="box tabs">
			<view @click="handleShow(1)">
				<image src="/static/tabs/tab_5.png" mode="widthFix"></image>
				<text>电话</text>
			</view>
			<view @click="handleShow(2)">
				<image src="/static/tabs/tab_6.png" mode="widthFix"></image>
				<text>邮箱</text>
			</view>
			<view @click="handleShow(3)">
				<image src="/static/tabs/tab_7.png" mode="widthFix"></image>
				<text>地址</text>
			</view>
			<view @click="handleShow(4)">
				<image src="/static/tabs/tab_8.png" mode="widthFix"></image>
				<text>微信</text>
			</view>
		</view>

		<view class="box introduce">
			<view class="title" style="margin-bottom: 20rpx;">
				<text>个人介绍</text>
				<!-- v-if="userInfo.is_vip==2" -->
				<!-- showVipBtn?'VIP':'' -->
				<view class="btn_r btn_tb" @click="handleCopyContent">复制简历</view>
			</view>
			<view class="int_text">{{info.content}}</view>
		</view>

		<view class="box accessory" v-if="info.attachment">
			<view class="title">
				<view>简历附件</view>
				<!-- <text style="font-size: 30rpx;margin-left: 4rpx;">(VIP转发简历)</text> -->
			</view>
			<view class="line" style="margin: 30rpx 0rpx;"></view>
			<view class="acc_item">
				<image src="/static/img/att.png" mode="aspectFill" class="acc_avt"></image>
				<view class="acc_content">
					<view class="acc_name">{{info.attachment_name}}</view>
					<!-- <view class="acc_time">2023.05.26</view> -->
				</view>
				<!-- <image src="/static/icon/share.png" mode="widthFix" class="acc_icon" @click="handleshareFile"></image> -->
				<!-- showVipBtn?'VIP':'' -->
				<view class="acc_btn btn_tb" @click="handleshareFile">转发简历</view>
			</view>
		</view>

		<view class="bottom_box">
			<view style="padding: 0rpx 28rpx 0rpx 46rpx;">
				<view class="col_box" @click="handleCollect">
					<image :src="`/static/icon/col${info.is_collect==1?'ed':''}.png`" mode="widthFix"></image>
					<text>{{info.is_collect==1?'已收藏':'收藏'}}</text>
				</view>
				<view class="bnt_big" style="width: 495rpx;" @click="handleShowSwap('简历')">交换简历</view>
			</view>
		</view>

		<pop-box :show.sync="showSwap">
			<view class="pop_box">
				<view class="pop_title">确认与{{info.name}}交换{{exchangeType}}</view>
				<view class="bnt_big" @click.stop="handleExchange">确认交换</view>
			</view>
		</pop-box>
		<pop-box :show.sync="showInfo">
			<view class="pop_box">
				<view class="pop_title">{{tabInfo.title}} <text class="open_vip">（交换成功后可查看）</text> </view>
				<view class="pop_content">
					<text>{{showVal(tabInfo.content)}}</text>
					<!-- <view class="copy_btn" v-if="tabInfo.isPhone" @click="$copy(tabInfo.content)">复制</view> -->
				</view>
				<!-- <view class="bnt_big" @click="handleClickTabBtn">{{tabInfo.isPhone?"一键联系":"复制"}}</view> -->
				<view class="bnt_big" @click="handleClickTabBtn">
					{{info.exchangeStatus==2?(tabInfo.isPhone?"一键联系":"复制"):"我要交换"}}
				</view>
			</view>
		</pop-box>

		<card-dome ref="cardDome"></card-dome>
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
				showSwap: false,
				showInfo: false,
				id: "",
				info: {
					view_num: 0
				},
				tabInfo: {
					title: "",
					isPhone: false,
					content: ""
				},
				exchangeType: "简历",
				shareInfo: {},
			}
		},
		computed: {
			...mapState(["cardInfo", "userInfo"]),
			viewCount() {
				if (this.info.view_num > 3) {
					return 3
				} else {
					return this.info.view_num || 0
				}
			},
			showVipBtn() {
				if (this.userInfo.is_vip != 2 && this.info.exchangeStatus == 2) {
					return false
				} else {
					return true
				}
			},
		},
		onLoad(options) {
			this.id = options.id;
		},
		onShow() {
			this.loadInfo();
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
		methods: {
			handleShowSwap(val) {
				this.exchangeType = val;
				this.showSwap = true;
			},
			handleCopyContent() {
				// if (this.info.exchangeStatus != 2) {
				// 	// this.handleExchange();
				// 	this.showSwap = true
				// 	return;
				// }
				if (!this.$isVipTip(this.info.exchangeStatus != 2) && this.info.exchangeStatus != 2) return;
				this.$copy(this.info.content)
			},
			showVal(val) {
				// &&this.userInfo.is_vip==2
				if (this.info.exchangeStatus == 2) {
					return val;
				} else {
					return "******"
				}
			},
			handleshareFile() {
				if (!this.info.attachment) {
					this.$msg("无效链接")
					return;
				}
				if (!this.$isVipTip(this.info.exchangeStatus != 2) && this.info.exchangeStatus != 2) return;
				// if (this.info.exchangeStatus!=2) return;
				uni.showLoading({
					title: "请稍等..."
				})
				uni.downloadFile({
					//此处url为需要分享的文件的url
					url: this.info.attachment,
					success: (res) => {
						wx.shareFileMessage({
							filePath: res.tempFilePath,
							fileName: this.info.attachment_name || '',
							success() {},
							fail: console.error,
						})
					},
					complete() {
						uni.hideLoading()
					}
				});
			},
			saveAvt() {
				if (!this.$isVipTip(this.info.exchangeStatus != 2) && this.info.exchangeStatus != 2) {
					return;
				}
				// if (this.info.exchangeStatus!=2) return;
				// uni.previewImage({
				// 	urls:[this.info.img]
				// })

				uni.getImageInfo({
					src: this.info.img_yt || this.info.img,
					success: (res) => {
						uni.saveImageToPhotosAlbum({
							filePath: res.path,
							success: function() {
								uni.showToast({
									title: "保存成功！",
									icon: "success",
									duration: 1700
								})
							}
						});
					}
				})
			},
			toVip() {
				uni.navigateTo({
					url: "/pages/vip/index"
				})
			},
			async handleExchange() {
				const result = await this.$api({
					url: "exchangeCard",
					data: {
						old_card_id: this.info.id,
						exchange_card_id: this.cardInfo.id
					}
				})
				if (result.code == 200) {
					this.showSwap = false;
					uni.showToast({
						title: "已提交交换申请",
						icon: "success"
					})
				} else {
					this.$msg(result.msg)
				}
			},
			handleClickTabBtn() {
				if (this.info.exchangeStatus != 2) {
					// this.handleExchange();
					this.handleShowSwap(this.tabInfo.title)
					return;
				}
				if (this.tabInfo.isPhone && this.tabInfo.content) {
					uni.makePhoneCall({
						phoneNumber: this.tabInfo.content
					})
				} else {
					this.$copy(this.tabInfo.content)
				}
			},
			async handleCollect() {
				const res = await this.$api({
					url: "cardCollect",
					data: {
						id: this.id,
						type: this.info.is_collect == 1 ? 2 : 1
					}
				})
				if (res.code == 200) {
					uni.$emit("handleNeedRest")
					this.loadInfo()
				}
			},
			async loadInfo() {
				const res = await this.$api({
					url: "cardDetail",
					data: {
						id: this.id
					}
				})
				if (res.code == 200) {
					this.info = res.data;
					if (!this.shareInfo.imageUrl) {
						let url = await this.$refs.cardDome.getImg(res.data);
						this.shareInfo = {
							title: `我是${res.data.name}，这是我的简历，请惠存`,
							path: "/pages/resume/details?id=" + res.data.id,
							imageUrl: url
						}
					}
					uni.setNavigationBarTitle({
						title: this.info.name + "的简历"
					})
				}
			},
			handleRealAuth() {
				if (this.info.is_authentication != 1) return;
				uni.setStorageSync("realAuthInfo", this.info.authentication_content)
				uni.navigateTo({
					url: "/pages/myCur/realAuthed"
				})
			},
			handleShow(type) {
				this.tabInfo.isPhone = false;
				switch (type) {
					case 1: //电话
						this.tabInfo.title = "电话";
						this.tabInfo.isPhone = true;
						this.tabInfo.content = this.info.phone;
						break;
					case 2: //邮箱
						this.tabInfo.title = "邮箱";
						this.tabInfo.content = this.info.email;
						break;
					case 3: //地址
						this.tabInfo.title = "地址";
						this.tabInfo.content = this.info.address
						break;
					case 4: //微信
						this.tabInfo.title = "微信";
						this.tabInfo.content = this.info.wechat
						break;
					default:
						break;
				}
				this.showInfo = true;
			}
		}
	}
</script>

<style lang="scss" scoped>
	.pop_box {
		width: 694rpx;
		background: #FFFFFF;
		border-radius: 25rpx;
		padding: 28rpx;

		.pop_title {
			font-weight: 600;
			font-size: 30rpx;
			color: #010101;
			text-align: center;
			margin-bottom: 36rpx;
			position: relative;

			.open_vip {
				position: absolute;
				right: 0;
				top: 50%;
				transform: translateY(-50%);
				font-weight: 600;
				font-size: 30rpx;
				color: #C7C7C7;
			}
		}

		.pop_content {
			display: flex;
			align-items: center;
			justify-content: center;
			font-weight: 400;
			font-size: 30rpx;
			color: #000000;
			line-height: 31rpx;
			margin-bottom: 30rpx;

			.copy_btn {
				width: 135rpx;
				height: 54rpx;
				background: #FFFFFF;
				border-radius: 25rpx;
				border: 2rpx solid #5B73FE;

				font-weight: 400;
				font-size: 24rpx;
				color: #5B73FE;
				text-align: center;
				line-height: 50rpx;
				margin-left: 24rpx;
			}
		}
	}

	.details {
		padding-top: 10rpx;

		.title {
			font-weight: 600;
			font-size: 34rpx;
			color: #000000;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.btn_r {
				// width: 135rpx;
				height: 54rpx;
				padding: 0rpx 20rpx;
				// background: #FFFFFF;
				// border-radius: 25rpx;
				// border: 2rpx solid #5B73FE;

				font-weight: 400;
				font-size: 24rpx;
				// color: #5B73FE;
				text-align: center;
				line-height: 50rpx;
			}
		}

		.resume_card {
			min-height: 350rpx;
			background-image: url("@/static/bg/card_bg.png");
			background-size: 100% 100%;
			padding: 42rpx 32rpx;
			display: flex;
			color: #FFFFFF;
			margin: 0 auto;

			.avt {
				width: 186rpx;
				height: 262rpx;
				border-radius: 20rpx;
				position: relative;

				&>image {
					width: 186rpx;
					height: 262rpx;
					border-radius: 20rpx;
				}

				.avt_btn {
					position: absolute;
					left: 16rpx;
					bottom: 6rpx;
					width: 152rpx;
					height: 44rpx;
					// border-radius: 20rpx;
					// border: 2px solid #FFFFFF;
					// background: rgba(255, 255, 255, .7);
					// border-radius: 20rpx;


					font-weight: 400;
					font-size: 22rpx;
					// color: #000000;
					line-height: 40rpx;
					text-align: center;
				}
			}

			.desc {
				flex: 1;
				margin-right: 24rpx;

				.name {
					font-weight: 400;
					font-size: 36rpx;
					margin-bottom: 20rpx;
					display: flex;
					align-items: center;

					&>image {
						width: 27rpx;
						height: 25rpx;
						margin-left: 11rpx;
					}
				}

				.text1 {
					font-weight: 400;
					font-size: 22rpx;
					margin-bottom: 16rpx;
				}

				.text2 {
					display: flex;
					align-items: center;
					flex-shrink: 0;
					margin-bottom: 12rpx;

					&>image {
						width: 20rpx;
						height: 20rpx;
						margin-right: 6rpx;
					}

					&>text {
						flex: 1;
						font-weight: 400;
						font-size: 21rpx;
					}
				}
			}
		}

		.see_box {
			padding: 20rpx;
			display: flex;
			align-items: center;
			position: relative;

			.avt_list {
				display: flex;
				align-items: center;

				&>view {
					height: 68rpx;
					width: 50rpx;

					&>image {
						width: 70rpx;
						height: 68rpx;
						border: 2rpx solid #FFFFFF;
						border-radius: 50%;
						overflow: hidden;
					}
				}
			}

			.see_text {
				font-weight: 400;
				font-size: 28rpx;
				color: #999999;
				margin-left: 32rpx;
			}

			.col {
				width: 35rpx;
				height: 30rpx;
				position: absolute;
				right: 38rpx;
				top: 50%;
				transform: translateY(-50%);
			}
		}

		.authentication {
			height: 110rpx;
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding: 0rpx 34rpx;
		}

		.tabs {
			height: 210rpx;
			display: flex;
			align-items: center;

			&>view {
				width: 25%;
				display: flex;
				flex-direction: column;
				align-items: center;

				&>image {
					display: block;
					width: 98rpx;
					height: 98rpx;
					margin-bottom: 21rpx;
				}

				font-weight: 400;
				font-size: 24rpx;
				color: #333;
			}
		}

		.introduce {
			padding: 40rpx 30rpx;

			.int_text {
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;
				line-height: 50rpx;
				white-space: pre-wrap;
			}
		}

		.accessory {
			padding: 34rpx;


		}

		.col_box {
			display: flex;
			align-items: center;
			margin-right: 28rpx;

			&>image {
				width: 35rpx;
				height: 30rpx;
				margin-right: 12rpx;
			}

			font-weight: 400;
			font-size: 36rpx;
			color: #A9A9A9;
		}
	}
</style>