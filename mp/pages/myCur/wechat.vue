<template>
	<view class="wechat">

		<view class="box" style="padding: 30rpx;">
			<view class="row">
				<view class="r_l">微信号</view>
				<view class="r_v">
					<input type="text" v-model.trim="wechat" placeholder="请输入微信号" placeholder-class="ph" />
				</view>
			</view>
			<view class="row">
				<view class="r_l" style="width: 100%;">微信二维码图像</view>
			</view>
			<view class="img_box">
				<image src="/static/icon/add_.png" mode="widthFix" class="add"></image>
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
		</view>


		<view class="bottom_box">
			<view>
				<view class="bnt_big" style="width: 694rpx;" @click="handleSave">保存</view>
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
				wechat: "",
			}
		},
		computed: {
			...mapState(['cardInfo', 'userInfo']),
		},
		onLoad(options) {
			this.wechat = this.cardInfo.wechat || '';
		},
		methods: {
			async handleSave() {
				if (!this.wechat) {
					this.$msg("请输入微信号")
					return;
				}
				const res = await this.$store.dispatch("saveCardInfo", {
					...this.cardInfo,
					wechat:this.wechat,
				})
				if (res) {
					uni.showToast({
						title: "保存成功"
					})
					setTimeout(() => {
						this.$back()
					}, 500)
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.wechat {
		padding-top: 2rpx;

		.img_box {
			height: 257rpx;
			background: #F7F7F7;
			border-radius: 25rpx;
			position: relative;

			.add {
				width: 43rpx;
				height: 43rpx;
				position: absolute;
				top: 50%;
				left: 50%;
				transform: translate(-50%, -50%);
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

		.row {
			min-height: 100rpx;
			display: flex;
			align-items: center;

			.r_l {
				width: 146rpx;
				font-weight: 400;
				font-size: 32rpx;
				color: #000000;
			}

			.r_v {
				flex: 1;
				display: flex;
				align-items: center;

				&>input {
					flex: 1;
				}
			}
		}
	}
</style>