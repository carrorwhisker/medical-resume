import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'

import uView from 'uview-ui'
Vue.use(uView)
// 如此配置即可
// uni.$u.config.unit = 'rpx'

// #ifdef MP
import share from '@/common/share.js'	// 导入并挂载全局的分享方法
Vue.mixin(share)
// #endif

import store from './store'
import {
	api
} from "common/api.js"

import {
	uploadFlie,
	uploadImage
} from "common/filehandle.js"

const toast = s => {
	uni.showToast({
		title: s,
		icon: 'none'
	})
}
Vue.prototype.$msg = toast;
Vue.prototype.$api = api;
Vue.prototype.$store = store;
Vue.prototype.$uploadFlie = uploadFlie;
Vue.prototype.$uploadImage = uploadImage;
Vue.prototype.$back = (delta = 1, url="") => {
	uni.navigateBack({
		delta: delta,
		fail: () => {
			uni.switchTab({
				url: url ? url : "/pages/home/index"
			})
		}
	})
};
Vue.prototype.$copy = function(s) {
	if (s) {
		uni.setClipboardData({
			data: s
		})
	}
}

Vue.prototype.$isVipTip = (isTis = true) => {
	if (store.state.userInfo.is_vip != 2) { //不是VIP
		if (isTis) {
			uni.showModal({
				title: "温馨提示",
				content: "您还未成为VIP用户，当前功能暂无法使用，是否前往开通VIP。",
				confirmText: "去开通",
				cancelText: "取消",
				confirmColor: "#358FFF",
				success: ({
					confirm
				}) => {
					if (confirm) {
						uni.navigateTo({
							url: "/pages/vip/index"
						})
					}
				}
			})
		}
		return false;
	}
	return true;
}


Vue.prototype.$getFileName = s => {
	if (!s) return "";
	return s.substring(s.lastIndexOf("/") + 1)
}


const app = new Vue({
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif