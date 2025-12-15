<template>
	<view class="rule">
		<text @click="toArticle(1)">《隐私协议》</text>和<text @click="toArticle(2)">《用户协议》</text>
	</view>
</template>

<script>
	export default {
		props: ["item"],
		data() {
			return {

			}
		},
		mounted() {

		},

		methods: {
			toArticle(type) {
				uni.navigateTo({
					url: "/pages/article/index?type=" + type
				})
			},
		},
	}
</script>

<style lang="scss" scoped>
	.rule {
		width: 750rpx;
		margin-top: 40rpx;
		text-align: center;
		font-size: 24rpx;
		color: #727272;
		line-height: 36rpx;
		padding-bottom: 40rpx;

		&>text {
			text-decoration-line: underline;
		}
	}
</style>