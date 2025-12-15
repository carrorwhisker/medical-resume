import Vue from 'vue'
import Vuex from 'vuex'
import {
	api
} from '@/common/api.js'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		token: '',
		userInfo: {},
		cardList: [{}, {}], //只有两个简历
		cardInfo: {}, //当前简历信息
		activeCard: 0, //当前简历下标
		cityList: [],
		positionList: [],
		isPrivacy:false,
	},
	getters: {
		hasLogin(state) {
			return !!state.token;
		},
	},
	mutations: {
		//更新state数据
		setState(state, {
			key,
			val
		}) {
			state[key] = val;
		},
		//更新Token
		setToken(state, data) {
			state.token = data;
			uni.setStorageSync('token', data);
			this.dispatch('getUserInfo'); //更新用户信息
			this.dispatch("getCardList"); //用户简历
		},
		//退出登录
		logout(state) {
			state.token = "";
			uni.removeStorageSync("token");
			uni.reLaunch({
				url: "/pages/login/index"
			})
		},
		toggleCard(state) {
			if (state.activeCard == 1) {
				state.activeCard = 0;
				state.cardInfo = state.cardList[0] || {}
			} else {
				state.activeCard = 1;
				state.cardInfo = state.cardList[1] || {};
			}
		},
	},
	actions: {
		async getCardList({
			state,
			commit,
			dispatch
		}) {
			const res = await api({
				url: "cardInfoList",
			});
			if (res.code == 200) {
				state.cardList = res.data || [];
				state.cardInfo = state.cardList[state.activeCard] || {};
			}
		},
		async saveCardInfo({
			state,
			commit,
			dispatch
		}, info) {
			const res = await api({
				url: "saveCard",
				data: info,
			});
			if (res.code == 200) {
				dispatch("getCardList")
				return true;
			}
			return false;
		},
		initData({
			state,
			commit,
			dispatch
		}) {
			const TOKEN = uni.getStorageSync("token");
			if (TOKEN) {
				commit("setToken", TOKEN);
				dispatch("getFilterData");
			}
			state.isPrivacy = !!uni.getStorageSync("PRIVACY");
			//  else {
			// 	uni.reLaunch({
			// 		url: "/pages/login/index"
			// 	})
			// }
		},
		async getFilterData({
			state,
			commit,
			dispatch
		}) {
			const res = await api({
				url: "filterList",
			});
			if (res.code == 200) {
				state.cityList = res.data.city || [];
				state.positionList = res.data.position || [];
			}
		},
		//更新用户信息
		async getUserInfo({
			state,
			commit,
			dispatch
		}) {
			const res = await api({
				url: "userInfo",
			});
			if (res.code == 200) {
				const userInfo = res.data[0] || {};
				commit('setState', {
					key: 'userInfo',
					val: userInfo
				})
			}
		},
	}
})


export default store