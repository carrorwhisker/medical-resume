export default {
	data() {
		return {

		}
	},
	// 分享给朋友
	onShareAppMessage(res) {
		// return {
		// 	title: this.share.title,
		// 	path: this.share.path,
		// 	imageUrl: this.share.imageUrl,
		// }
		return {
			// path:"/pages/home/index"
		}
	},
	// 分享到朋友圈
	onShareTimeline(res) {
		// return {
		// 	title: this.share.title,
		// 	path: this.share.path,
		// 	imageUrl: this.share.imageUrl,
		// }
		return {
			// path:"/pages/home/index"
		}
	},
}