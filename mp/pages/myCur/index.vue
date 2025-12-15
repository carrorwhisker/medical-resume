<template>
	<view class="myCur">
		<view class="box card">
			<view class="avt">
				<image :src="cardInfo.img ? cardInfo.img : '/static/def.png'" mode="aspectFill"></image>
				<!-- <view class="avt_btn">复制头像</view> -->
			</view>
			<view class="desc">
				<view class="name">{{ cardInfo.name || "******" }}
					<image src="/static/icon/vip.png" mode="widthFix" v-if="userInfo.is_vip == 2"></image>
				</view>
				<view class="text1">{{ cardInfo.company || '******' }}</view>
				<view class="text1">{{ cardInfo.department_name || '******' }}</view>
				<view class="text2">
					<image src="/static/icon/min_1.png" mode="widthFix"></image>
					<text>{{ cardInfo.phone || '******' }}</text>
				</view>
				<view class="text2">
					<image src="/static/icon/min_2.png" mode="widthFix"></image>
					<text>{{ cardInfo.email || '******' }}</text>
				</view>
				<view class="text2">
					<image src="/static/icon/min_3.png" mode="widthFix"></image>
					<text>{{ cardInfo.address || '******' }}</text>
				</view>
			</view>
		</view>
		<view class="box authentication" @click="handleRealAuth">
			<text class="title">我的认证</text>
			<view style="display: flex;align-items: center;">
				<image :src="cardInfo.is_authentication == 1 ? '/static/icon/smrzd.png' : '/static/icon/smrz.png'"
					mode="widthFix" style="width: 132rpx;height: 45rpx;margin-right: 37rpx;"></image>
				<image src="/static/icon/right_.png" mode="widthFix" style="width:16rpx ;height: 28rpx; "></image>
			</view>
		</view>

		<view class="box tabs">
			<view @click="showPhone = true">
				<image src="/static/tabs/tab_1.png" mode="widthFix"></image>
				<text>电话</text>
			</view>
			<view @click="showEmail = true">
				<image src="/static/tabs/tab_2.png" mode="widthFix"></image>
				<text>邮箱</text>
			</view>
			<view @click="showAddress = true">
				<image src="/static/tabs/tab_3.png" mode="widthFix"></image>
				<text>地址</text>
			</view>
			<view @click="showWx = true">
				<image src="/static/tabs/tab_4.png" mode="widthFix"></image>
				<text>微信</text>
			</view>
		</view>

		<view class="box schedule">
			<view class="lab">
				<text class="title" style="margin-right: 22rpx;">简历完成度</text>
				<view class="pro">
					<view class="bar">
						<view :style="{ width: cardPlan }"></view>
					</view>
					<view class="pro_lab">已完成{{ cardPlan }}</view>
				</view>
			</view>
			<view class="flex1" style="margin-top: 26rpx;">
				<view class="desc_">补充头像姓名等基本信息</view>
				<view class="btn_r" @click="toEdit">去完善</view>
			</view>
		</view>

		<view class="box schedule">
			<view class="lab flex1">
				<text class="title" style="margin-right: 22rpx;">个人介绍</text>
				<!-- <view class="btn_r">复制</view> -->
			</view>
			<view style="margin-top:30rpx;" @click="handleGoToDescPage">
				<view class="desc_" style="white-space: pre-wrap;">{{ cardInfo.content ? cardInfo.content : '请简单介绍一下自己' }}
				</view>
			</view>
		</view>

		<view class="box attachments">
			<view class="title">简历附件</view>
			<view class="line" style="margin-top: 34rpx;"></view>
			<view class="add" @click="handleUploadPDF" v-if="!attachment">
				<view>+ 上传PDP、PPT、Word文件</view>
				<view>可添加对外个人简介，用于会议中的串场PPT，仅支持单个文件上传，最大10M</view>
			</view>
			<view class="acc_item" style="margin-top: 40rpx;" v-else>
				<image src="/static/img/att.png" mode="aspectFill" class="acc_avt"></image>
				<view class="acc_content">
					<view class="acc_name">{{ attachment_name }}</view>
					<view class="acc_time" @click="handleUploadPDF">点击上传附件</view>
				</view>
				<image src="/static/icon/share.png" mode="widthFix" class="acc_icon" @click.stop="showMenu = true">
				</image>
			</view>
		</view>

		<view class="bottom_box">
			<view>
				<view class="bnt_big" style="width: 694rpx;" @click="handleSaveJL">保存简历</view>
				<!-- <view class="bnt_big" style="width: 694rpx;" v-if="!cardInfo.name" @click="setShareInfo">保存简历</view>
				<button type="default" class="bnt_big" style="width: 694rpx;" open-type="share" v-else>保存简历</button> -->
			</view>
		</view>
		<!-- 手机号 -->
		<bottom-pop :show.sync="showPhone">
			<view class="pop_box">
				<view class="pop_title">
					<text class="must">电话</text>
				</view>
				<view class="input_box">
					<input type="text" :cursor-spacing="20" v-model="phone" placeholder="请输入电话"
						placeholder-class="ph" />
				</view>
				<view class="tags">
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 手机
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 座机
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 传真
					</view>
				</view>
				<view class="bnt_big" style="margin-top: 120rpx;" @click="saveCardInfo('phone')">保存</view>
			</view>
		</bottom-pop>
		<!-- 邮箱 -->
		<bottom-pop :show.sync="showEmail">
			<view class="pop_box">
				<view class="pop_title">
					<text>邮箱</text>
				</view>
				<view class="input_box">
					<input type="text" :cursor-spacing="20" v-model="email" placeholder="请输入邮箱"
						placeholder-class="ph" />
				</view>
				<view class="tags">
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 添加
					</view>
				</view>
				<view class="bnt_big" style="margin-top: 120rpx;" @click="saveCardInfo('email')">保存</view>
			</view>
		</bottom-pop>
		<!-- 地址 -->
		<bottom-pop :show.sync="showAddress">
			<view class="pop_box">
				<view class="pop_title">
					<text>地址</text>
				</view>
				<view class="input_box" @click="handleShowCity">
					<input type="text" :cursor-spacing="20" v-model.trim="address" placeholder="XX省XX市XX区"
						placeholder-class="ph" disabled @click.stop.prevent="handleShowCity" />
				</view>
				<view class="tags">
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 添加
					</view>
				</view>
				<view class="bnt_big" style="margin-top: 120rpx;" @click="saveCardInfo('address')">保存</view>
			</view>
		</bottom-pop>

		<!-- 微信号 -->
		<bottom-pop :show.sync="showWx">
			<view class="pop_box">
				<view class="pop_title">
					<text>微信号</text>
				</view>
				<view class="input_box">
					<input type="text" :cursor-spacing="20" v-model.trim="wechat" placeholder="请输入微信号"
						placeholder-class="ph" />
				</view>
				<view class="tags">
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 微信
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> QQ
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 微博
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> 个人主页
					</view>
					<view>
						<image src="/static/icon/add.png" mode="widthFix"></image> Linkedin
					</view>
				</view>
				<view class="bnt_big" style="margin-top: 120rpx;" @click="saveCardInfo('wechat')">保存</view>
			</view>
		</bottom-pop>

		<bottom-pop :show.sync="showRename">
			<view class="pop_box">
				<view class="pop_title">
					<text>简历附件重命名</text>
				</view>
				<view class="input_box">
					<input type="text" :cursor-spacing="20" v-model.trim="attachment_name" placeholder="请输入附件名"
						placeholder-class="ph" />
				</view>
				<view class="bnt_big" style="margin-top: 120rpx;" @click="saveCardInfo('attachment_name')">保存</view>
			</view>
		</bottom-pop>

		<bottom-pop :show.sync="showMenu">
			<view class="menu">
				<view class="menu_item" @click="handleshareFile">分享好友</view>
				<view class="menu_item" @click="handleRename">重命名</view>
				<view class="menu_item" @click="handleUploadPDF">更新文档</view>
				<view class="menu_item" style="color: #F56C6C;" @click="handleDelFile">删除</view>
				<view class="menu_item" style="margin-top: 20rpx;" @click="showMenu = false">取消</view>
			</view>
		</bottom-pop>
	</view>
</template>

<script>
import {
	mapState,
	mapGetters
} from "vuex"
import {
	checkStr
} from "@/util/utils.js"
export default {
	data() {
		return {
			showPhone: false,
			showEmail: false,
			showAddress: false,
			showWx: false,
			showMenu: false,
			phone: "",
			email: "",
			address: "",
			city: "",
			wechat: "",
			attachment: "", //附件
			attachment_name: "",
			shareInfo: {
				path: "",
				title: "",
			},
			showRename: false,
			isFirstFocus: true, // 10.13 zty新增 标记是否为第一次聚焦，避免抖动
			focusTimer: null, // 10.13 zty新增 聚焦处理定时器
		}
	},
	computed: {
		...mapState(['cardInfo', 'userInfo']),
		cardPlan() {
			if (!this.cardInfo.id) {
				return '0%'
			};
			let sum = 0;
			let total = 6;
			let arr = ['img', 'name', 'company', 'position', 'department', 'address'];
			arr.forEach(k => {
				if (this.cardInfo[k]) {
					sum++
				}
			})
			// for (let key in this.cardInfo) {
			// 	total++;
			// 	if (this.cardInfo[key]) {
			// 		sum++
			// 	}
			// }
			return (sum / total).toFixed(2) * 100 + "%"
		},
	},
		onLoad(options) {
			this.phone = this.cardInfo.phone || '';
			this.email = this.cardInfo.email || '';
			this.address = this.cardInfo.address || '';
			this.wechat = this.cardInfo.wechat || '';
			this.attachment = this.cardInfo.attachment || '';
			this.attachment_name = this.cardInfo.attachment_name || "";
			if (this.cardInfo.name) {
				this.setShareInfo();
			}
		},
		// 页面显示时刷新数据
		onShow() {
			// 从个人介绍页面返回时，重新获取最新数据
			this.$store.dispatch("getCardList");
		},
	onShareAppMessage() {
		return this.shareInfo
	},
	onShareTimeline() {

	},
	methods: {
		// 10.13 zty新增 聚焦处理，避免键盘问题
		handleFocusSimple(e) {
			if (this.isFirstFocus) {
				this.isFirstFocus = false;
				return;
			}
		},
		// 10.13 zty新增 处理输入框失焦事件
		handleBlur(e) {
			// 清除聚焦定时器
			if (this.focusTimer) {
				clearTimeout(this.focusTimer);
				this.focusTimer = null;
			}
		},
		// 10.13 zty新增 检查并在必要时调整视图，避免过度干预系统调整
		checkAndAdjustIfNeeded() {
			const query = uni.createSelectorQuery().in(this);
			query.select('.textarea-scroll-area').boundingClientRect();
			query.exec((res) => {
				if (res[0]) {
					uni.getSystemInfo({
						success: (sysInfo) => {
							const windowHeight = sysInfo.windowHeight;
							const inputBottom = res[0].bottom;
							if (inputBottom > windowHeight - 50) {
								const adjustDistance = inputBottom - windowHeight + 80; 
								if (adjustDistance > 0) {
									uni.pageScrollTo({
										scrollTop: adjustDistance,
										duration: 100 
									});
								}
							}
						}
					});
				}
			});
		},
		// 跳转到个人介绍页面
		handleGoToDescPage() {
			const content = this.cardInfo.content || '';
			uni.navigateTo({
				url: `/pages/myCur/personalDesc?content=${encodeURIComponent(content)}`
			});
		},
		//选择地址
		handleShowCity() {
			// this.$refs.cityRef.open();
			// console.log(111);
			// this.showCity = true;
			uni.$emit("selectCity", '')
			uni.$once("selectCity", (data) => {
				if (data) {
					this.city = data[0];
					this.address = data.join("\/");
				}
			})
			uni.navigateTo({
				url: "/pages/city/index?address=" + this.address
			})
		},
		//删除文件
		handleDelFile() {
			uni.showModal({
				title: "温馨提示",
				content: "确认要删除简历附件吗？",
				success: async ({
					confirm
				}) => {
					if (confirm) {
						this.showMenu = false;
						const res = await this.$api({
							url: "delAttachment",
							data: {
								id: this.cardInfo.id
							}
						})
						if (res.code == 200) {
							this.$msg("操作成功");
							this.$store.dispatch("getCardList");
							this.attachment = '';
							this.attachment_name = "";
						}
					}
				}
			})
		},
		//重命名
		handleRename() {
			this.showMenu = false;
			this.showRename = true;
		},
		handleSaveJL() {
			uni.showToast({
				title: "保存成功！",
				icon: "success"
			})
			setTimeout(() => {
				this.$back()
			}, 500)
		},
		handleshareFile() {
			uni.showLoading({
				title: "请稍等..."
			})
			uni.downloadFile({
				//此处url为需要分享的文件的url
				url: this.cardInfo.attachment,
				success: (res) => {
					this.showMenu = false;
					wx.shareFileMessage({
						filePath: res.tempFilePath,
						fileName: this.cardInfo.attachment_name || '',
						success() { },
						fail: console.error,
					})
				},
				complete: () => {
					uni.hideLoading()
				}
			});
		},
		setShareInfo() {
			if (this.cardInfo.name) {
				this.shareInfo = {
					title: this.cardInfo.name + "的简历",
					path: "/pages/resume/details?id=" + this.cardInfo.id
				}
			} else {
				this.$msg("简历不完整！")
			}
		},
		async handleUploadPDF() {
			this.showMenu = false;
			const res = await this.$uploadFlie()
			if (res.code == 200) {
				this.attachment = res.data.url;
				this.attachment_name = res.data.name;
				this.saveCardInfo("attachment");
			}
		},
		async saveCardInfo(key) {
			let val = this[key];
			if (!val) {
				uni.showToast({
					title: "请输入内容"
				})
				return;
			}
			if (key == "phone") {
				let reg = /^1[3-9][0-9]{9}$/;
				if (!reg.test(val)) {
					uni.showToast({
						title: "请输入有效手机号"
					})
					return;
				}
			};
			if (key == "email") {
				let reg =
					/[\w!#$%&'*+/=?^_`{|}~-]+(?:\.[\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\w](?:[\w-]*[\w])?\.)+[\w](?:[\w-]*[\w])?/;
				if (!reg.test(val)) {
					uni.showToast({
						title: "请输入有效邮箱"
					})
					return;
				}
			};
			let parmas = {
				...this.cardInfo,
				city: this.city || '',
			}
			if (key == "attachment") {
				parmas.attachment = this.attachment;
				parmas.attachment_name = this.attachment_name;
			} else {
				parmas[key] = this[key]
			}
			const res = await this.$store.dispatch("saveCardInfo", parmas)
			if (res) {
				uni.showToast({
					title: "保存成功",
					icon: "success"
				})
				switch (key) {
					case "phone":
						this.showPhone = false;
						break;
					case "email":
						this.showEmail = false;
						break;
					case "address":
						this.showAddress = false;
						break;
					case "wechat":
						this.showWx = false;
						break;
					case "attachment_name":
						this.showRename = false;
					default:
						break;
				}
			}
		},
		toEdit() {
			uni.navigateTo({
				url: "/pages/myCur/basicInfo"
			})
		},
		addWx() {
			uni.navigateTo({
				url: "/pages/myCur/wechat"
			})
		},
		handleRealAuth() {
			if (this.cardInfo.is_authentication == 1) {
				uni.navigateTo({
					url: "/pages/myCur/realAuthed"
				})
			} else {
				uni.navigateTo({
					url: "/pages/myCur/realAuth"
				})
			}

		}
	}
}
</script>

<style lang="scss" scoped>
.menu {
	.menu_item {
		height: 100rpx;
		text-align: center;
		font-weight: 400;
		font-size: 32rpx;
		color: #000000;
		line-height: 100rpx;

	}
}

.pop_box {
	.pop_title {
		font-weight: 600;
		font-size: 30rpx;
		color: #010101;
		text-align: center;
	}

	.input_box {
		margin-top: 40rpx;
	}

	.input_box_ {
		margin-top: 40rpx;

		&>textarea.vertical-only {
			resize: vertical;
		}
	}

	.tags {
		margin-top: 28rpx;
		display: flex;
		flex-wrap: wrap;

		&>view {
			border-radius: 25rpx;
			border: 2rpx solid #D7D7D7;
			display: flex;
			align-items: center;
			padding: 0rpx 24rpx;
			margin: 4rpx;

			&>image {
				width: 18rpx;
				height: 18rpx;
				margin-right: 8rpx;
			}

			font-weight: 400;
			font-size: 28rpx;
			color: #989898;
		}
	}

}

.myCur {
	width: 750rpx;
	padding: 20rpx;
	overflow-x: hidden;


	.title {
		font-weight: 600;
		font-size: 34rpx;
		color: #000000;
	}

	.desc_ {
		font-weight: 400;
		font-size: 26rpx;
		color: #999999;
		// overflow: hidden;
	}

	.btn_r {
		width: 135rpx;
		height: 54rpx;
		background: #358FFF;
		border-radius: 25rpx;
		// border: 2rpx solid #5B73FE;
		// background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
		font-weight: 400;
		font-size: 24rpx;
		color: #FFFFFF;
		text-align: center;
		line-height: 50rpx;
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

	.schedule {
		min-height: 186rpx;
		padding: 30rpx;

		.lab {
			display: flex;
			align-items: center;
		}

		.pro {
			flex: 1;
			display: flex;
			align-items: center;

			.bar {
				flex: 1;
				height: 10rpx;
				background: #BEDDFE;
				border-radius: 5rpx;
				position: relative;

				&>view {
					height: 10rpx;
					background: #5B73FE;
					border-radius: 5rpx;
					position: absolute;
					top: 0;
					left: 0;
				}
			}

			.pro_lab {
				margin-left: 22rpx;
				font-weight: 400;
				font-size: 24rpx;
				color: #272727;
			}
		}


	}

	.attachments {
		height: 352rpx;
		padding: 38rpx 32rpx;

		.add {
			font-weight: 400;
			font-size: 28rpx;
			color: #5B73FE;
			text-align: center;
			margin-top: 90rpx;

			&>view:nth-child(2) {
				width: 500rpx;
				margin: 0 auto;
				font-size: 24rpx;
				margin-top: 10rpx;
				color: #888888;
			}
		}
	}

}
</style>