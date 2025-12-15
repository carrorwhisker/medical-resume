<template>
	<!-- #ifdef APP -->
	<view :style="{height:systemInfo.safeAreaInsets.top+'px',background:background}"></view>
	<!-- #endif -->
	<!-- #ifdef MP -->
	<view :style="{height:mpInfo.top+'px',background:background}"></view>
	<!-- #endif -->
</template>

<script>
	export default {
		props: {
			background: {
				type: String,
				default: "transparent"
			}
		},
		data() {
			return {
				mpInfo: {},
				systemInfo: uni.getSystemInfoSync(),
			}
		},
		created() {
			// #ifdef MP
			this.mpInfo = uni.getMenuButtonBoundingClientRect();
			// #endif
		}
	}
</script>

<style lang="scss" scoped>

</style>