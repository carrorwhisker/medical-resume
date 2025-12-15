<template>
	<view class="article">
		<view class="content">
			<rich-text :nodes="html"></rich-text>
			<!-- <view class="t1">为保护您的个人信息安全，在您与我方交换名片时，请知悉并同意以下隐私条款:</view>
			<view class="t2">信息收集目的</view>
			<view class="t3">您提供的名片信息(包括但不限于姓名、职务、联系方式、企业名称等)仅用于双方商务沟通与合作之目的。</view>
			<view class="t2">信息使用范围</view>
			<view class="t3">信息将严格保密，仅限我方相关部门及直接关联人员必要使用时访问;</view>
			<view class="t3">未经您明确授权，不会向第三方披露或用于营销等无关用途。</view>
			<view class="t2">信息存储与保护</view>
			<view class="t3">电子名片数据将通过加密技术存储于安全服务器；</view>
			<view class="t2">您的权利</view>
			<view class="t3">可随时要求查阅、更正或删除您的名片信息;</view>
			<view class="t2">例外情况</view>
			<view class="t3">根据法律法规要求或司法程序需要披露信息时，可能无需另行征得您同意。</view>
			<view class="t2">协议更新</view>
			<view class="t3">我方保留修订本协议的权利，更新内容将通过[通知渠道]公示。</view>
			<view class="t3">提示:递交名片即视为您已阅读并同意本协议。如不同意，请勿提供名片信息。</view> -->
		</view>
	</view>
</template>

<script>
	import {
		YS,
		YH,
		VIP
	} from "@/common/article.js"
	import {
		mapState,
		mapGetters
	} from "vuex"
	export default {
		data() {
			return {
				type: 1, //1隐私协议  2用户协议  3VIP协议
			}
		},
		computed: {
			html() {
				if (this.type == 1) {
					return YS;
				} else if (this.type == 2) {
					return YH;
				}
				return VIP;
			},
		},
		onLoad(options) {
			this.type = options.type
			if (this.type != 1) {
				uni.setNavigationBarTitle({
					title: this.type==2?"用户协议":"会员协议"
				})
			}
		},
		methods: {

		}
	}
</script>

<style lang="scss" scoped>
	.article {
		padding-top: 20rpx;

		.content {
			background: #FFF;
			width: 702rpx;
			margin: 0 auto;
			padding: 30rpx 20rpx;
			border-radius: 20rpx;
			color: #333;

			.t1 {
				font-size: 36rpx;
				line-height: 50rpx;
				font-weight: bold;
			}

			.t2 {
				font-size: 32rpx;
				line-height: 48rpx;
				margin-top: 20rpx;
				margin-bottom: 10rpx;
				color: #464646;
				font-weight: bold;
			}

			.t3 {
				font-size: 28rpx;
				line-height: 40rpx;
				margin-bottom: 10rpx;
				color: #666;
			}
		}
	}
</style>