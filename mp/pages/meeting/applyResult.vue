<template>
	<view class="apply_result" v-if="info.id">
		<view class="box">
			<view style="height: 179rpx;">
				<view class="box state_box" style="height: 262rpx;"
					:style="{backgroundColor:status==0?'#FFB033':status==1?'#2BC67B':'#FF4A46'}">
					<view class="state">
						<image src="/static/meeting/ok.png" mode="widthFix" v-if="status!=2"></image>
						<image src="/static/meeting/war.png" mode="widthFix" v-else></image>
						<text>{{status==0?'提交成功':status==1?'报名成功':'报名失败'}}</text>
					</view>
					<view class="state_text">{{status==0?'请耐心等待主办方审核~':status==1?'请填写备注及住宿信息~':info.remark}}
					</view>
				</view>
			</view>
			<view class="box content">
				<view class="title">{{meeting_info.meeting_name}}</view>
				<view class="title" style="margin-top: 40rpx;">
					<text>时间</text>
					<image src="/static/meeting/date_.png" mode="widthFix" style="width: 35rpx;height: 31rpx;"></image>
				</view>
				<view class="text">{{meeting_info.start_time}} 至 {{meeting_info.end_time}}</view>
				<view class="title" style="margin-top: 40rpx;">
					<text>地点</text>
					<image src="/static/meeting/address_.png" mode="widthFix" style="width: 35rpx;height: 31rpx;">
					</image>
				</view>
				<view class="text">{{meeting_info.meeting_address}}</view>
				<view v-if="info.remark">
					<view class="title" style="color: #000;font-size: 34rpx;margin-top: 40rpx;">
						<text>管理员备注</text>
					</view>
					<view class="text">{{info.remark}}</view>
				</view>
				<view v-if="status==1">
					<view class="line" style="margin-top: 48rpx;"></view>
					<view class="title" style="color: #000;font-size: 34rpx;margin-top: 40rpx;">
						<text class="must">备注</text>
					</view>
					<view class="textarea_box">
						<textarea v-model.trim="trip_content" placeholder="请输入备注及住宿信息" placeholder-class="ph"
							:disabled="isTrip"></textarea>
					</view>
				</view>

				<view class="bnt_big" v-if="status==0" style="margin-top:80rpx;" @click="toHome">
					返回首页
				</view>
				<view class="bnt_big" v-if="status==1 && !isTrip" style="margin-top:80rpx;" @click="submit">
					提交
				</view>
				<view class="bnt_big" v-if="status==2" style="margin-top:80rpx;" @click="reRegister">
					再次报名
				</view>

				<view class="cancel_btn" v-if="status!=2" @click="showCancel=true;">取消会议</view>
			</view>
		</view>

		<pop-box :show.sync="showCancel">
			<view class="pop_cancel" @click.stop="()=>{}">
				<image src="/static/icon/close.png" mode="widthFix" class="close" @click="showCancel=false"></image>
				<view v-if="isPrice">如需取消订单，请点击确认</view>
				<view v-if="isPrice">并跳转在线客服页退款~</view>
				<view v-if="!isPrice">您确定要取消会议?</view>
				<view class="pop_bottom">
					<view @click="showCancel=false">不取消</view>
					<view @click="handleCancel">
						确认
						<button open-type="contact" v-if="isPrice" class="contact"></button>
					</view>
				</view>
			</view>
		</pop-box>

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
				// state: 1, //0审核中  1成功  2失败
				showCancel: false,
				info: {},
				meeting_info: {
					price: 0,
				},
				id: "",
				trip_content: "",
				type:1,//1结果   2//查看详情
			}
		},
		computed: {
			status() { //0审核中  1成功  2失败
				if (this.info.status == 1) {
					return 0;
				} else if (this.info.status == 2) {
					return 1;
				} else if (this.info.status == 3) {
					return 2;
				}
			},
			isTrip() { //是否已提交行程
				return !!this.info.trip_content;
			},
			isPrice() { //是否为付费
				return this.meeting_info.price * 1 != 0;
			},
		},
		onLoad(options) {
			this.id = options.id || options.active_id || '';
		},
		onShow() {
			this.loadInfo()
		},
		methods: {
			async handleCancel() {
				if (this.isPrice) return;
				uni.showLoading({
					title: "请稍等.."
				})
				const res = await this.$api({
					url: "cancelMeetingData",
					data: {
						active_id: this.id,
					}
				})
				uni.hideLoading()
				if (res.code == 200) {
					uni.showToast({
						title: "操作成功",
						icon: "success"
					})
					setTimeout(() => {
						this.$back()
					}, 500)
				}
			},
			reRegister() {
				uni.navigateTo({
					url: `/pages/meeting/apply?id=${this.id}`
				})
			},
			toHome() {
				uni.switchTab({
					url: "/pages/home/index"
				})
			},
			async loadInfo() {
				const res = await this.$api({
					url: "getMeetingSignUpInfo",
					data: {
						active_id: this.id
					}
				})
				if (res.code == 200) {
					this.info = res.data.sign_up_info;
					this.meeting_info = res.data.meeting_info;
					// 时间戳（秒）转日期
					const m = this.meeting_info || {};
					if (typeof m.start_time === 'number') {
						const d = new Date(m.start_time * 1000);
						m.start_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`;
					}
					if (typeof m.end_time === 'number') {
						const d = new Date(m.end_time * 1000);
						m.end_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`;
					}
					this.meeting_info = m;
					if (res.data.sign_up_info && res.data.sign_up_info.trip_content) {
						this.trip_content = res.data.sign_up_info.trip_content || '';
					}
				}
			},
			// 提交行程
			async submit() {
				if (!this.trip_content) {
					this.$msg("请输入行程内容！")
					return
				};
				const res = await this.$api({
					url: "getMeetingSignUpTrip",
					data: {
						trip_content: this.trip_content,
						active_id: this.id,
					}
				})
				if (res.code == 200) {
					uni.showToast({
						title: "提交成功",
						icon: "success"
					})
					this.loadInfo();
				}
			}
		}
	}
</script>
<style>
	page {
		background: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.pop_cancel {
		width: 567rpx;
		height: 250rpx;
		background: #FFFFFF;
		border-radius: 25rpx;
		overflow: hidden;
		position: relative;
		padding-top: 40rpx;
		font-weight: 400;
		font-size: 26rpx;
		color: #000000;
		line-height: 40rpx;
		text-align: center;

		.close {
			width: 30rpx;
			height: 30rpx;
			position: absolute;
			top: 20rpx;
			right: 20rpx;
		}

		.pop_bottom {
			height: 82rpx;
			display: flex;
			position: absolute;
			bottom: 0;
			left: 0;
			width: 567rpx;

			&>view {
				width: 50%;
				text-align: center;
				line-height: 82rpx;
				font-weight: 400;
				font-size: 32rpx;
			}

			&>view:nth-child(1) {
				color: #000;
				background: #F6F6F6;
			}

			&>view:nth-child(2) {
				color: #FFF;
				background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
				position: relative;

				.contact {
					width: 100%;
					height: 100%;
					position: absolute;
					top: 0;
					left: 0;
					opacity: 0;
					z-index: 1;
				}
			}
		}
	}

	.apply_result {

		.cancel_btn {
			width: 244rpx;
			height: 70rpx;
			border-radius: 35rpx;
			border: 2rpx solid #959595;

			font-weight: 400;
			font-size: 32rpx;
			color: #8C8C8C;
			text-align: center;
			line-height: 66rpx;
			margin: 0 auto;
			margin-top: 80rpx;
		}

		.state_box {
			padding-top: 48rpx;

			.state {
				display: flex;
				align-items: center;
				justify-content: center;
				font-weight: 600;
				font-size: 36rpx;
				color: #FFFFFF;

				&>image {
					width: 36rpx;
					height: 36rpx;
					margin-right: 13rpx;
				}
			}

			.state_text {
				margin-top: 20rpx;
				text-align: center;
				font-weight: 400;
				font-size: 28rpx;
				color: #FFFFFF;
			}
		}


		.content {
			position: relative;
			z-index: 2;
			padding: 50rpx 40rpx;

			.title {
				font-weight: 400;
				font-size: 32rpx;
				color: #8C8C8C;
				line-height: 55rpx;
				display: flex;
				align-items: center;

				&>image {
					margin-left: 20rpx;
				}
			}

			.text {
				font-size: 30rpx;
				color: #8C8C8C;
				line-height: 36rpx;
				margin-top: 10rpx;
			}

			.textarea_box {
				width: 601rpx;
				height: 208rpx;
				background: #F6F6F6;
				border-radius: 25rpx;
				margin: 0 auto;
				margin-top: 23rpx;

				&>textarea {
					width: 100%;
					height: 100%;
					padding: 30rpx 34rpx;
				}
			}
		}
	}
</style>