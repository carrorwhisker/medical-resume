import store from "@/store/index.js"
import {
	BAES_URL,
	service,
} from "./config.js"
import {
	setLoginUrl
} from "@/util/utils.js"
export const api = options => {
	//suffix 后缀
	const {
		type = "POST", data = {}, url, header = {}, suffix = "", loading = false, needUserId = true
	} = options;

	return new Promise(function(resolve, reject) {
		const Request_URL = BAES_URL + service[url] //正式服
		if (loading) {
			uni.showLoading({
				title: "加载中...",
			})
		}
		const token = uni.getStorageSync('token');
		if (token) {
			header.Token = token;
		}
		uni.request({
			url: suffix ? Request_URL + suffix : Request_URL,
			data: data,
			method: type,
			header: {
				'content-type': 'application/json',
				...header
			},
			success: res => {
				const {
					code,
					msg
				} = res.data;

				switch (code) {
					case 201:
						setLoginUrl()
						store.commit("logout")
						break;
					default:
						if (msg && code != 200 && code != 202) {
							uni.showToast({
								title: msg,
								icon: "none"
							})
						}
						break;
				}
				resolve(res.data)
			},
			fail: err => {
				console.log(err)
			},
			complete() {
				if (loading) {
					uni.hideLoading()
				}
			}
		})
	})
}