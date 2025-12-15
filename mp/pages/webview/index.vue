<template>
	<web-view :src="src">加载中...</web-view>
</template>
<!-- zd新增：打开外部 H5页面  -->
<script>
	export default {
		data() {
			return {
				src: '',
				title: '',
				shareInfo: {}
			}
		},
		onLoad(options) {
			this.src = options.url ? decodeURIComponent(options.url) : '';
			var title = options.title ? decodeURIComponent(options.title) : '';
      //07.24 修改为 JSON.parse(decodeURIComponent(options.shareInfo))，并赋值给 this.shareInfo
			var shareInfo = options.shareInfo ? JSON.parse(decodeURIComponent(options.shareInfo)) : '';
      this.shareInfo = shareInfo;
			uni.setNavigationBarTitle({
				title: title
			})
		},
		onShareAppMessage() {
			return this.shareInfo
		},
		onShareTimeline() {

		},
		methods: {
			
		}
	}
</script>

<style>

</style>
