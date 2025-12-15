<template>
	<view v-if="!hasLogin || !isPrivacy">
		<button v-if="!hasLogin" class="login_check" open-type="getPhoneNumber"
			@getphonenumber="getphonenumber">手机一键登录</button>
		<view class="privacy" v-if="!isPrivacy" @click.stop="show_privacy=true"></view>
		<pop-box :show.sync="show_privacy">
			<view class="pop_box" @click.stop="()=>{}">
				<view class="title">温馨提示</view>
				<view class="desc">
					感谢您的信任并使用{{appInfo.appName}}!
					我们依据最新的法律法规及监管政策要求更新了 <text style="color: #63ADE7;" @click="toArticle(1)">《隐私协议》</text>、<text style="color: #63ADE7;" @click="toArticle(2)">《用户协议》</text>
					您推送此说明，以便继续为您服务。请仔细阅读《隐私协议》和《用户协议》(也可称为“服务条款”)并理解相关条款内容，在确认充分理解并同意后使用{{appInfo.appName}}相关产品或服务。
				</view>
				<!-- <button class="bnt_big" open-type="getPhoneNumber"
					@getphonenumber="getphonenumber" v-if="!hasLogin">同意并继续</button> -->
				<view class="bnt_big"  @click="clickPrivacy">
					同意并继续
				</view>
				<view class="bnt_big" style="background: #FFFFFF;margin-top: 30rpx;" @click="show_privacy=false">
					<text style="color:#9E9E9E">取消</text>
				</view>
			</view>
		</pop-box>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from 'vuex'
	export default {
		props: ["item"],
		data() {
			return {
				code: "",
				show_privacy: false,
				// show_privacyed: false,
				appInfo: uni.getSystemInfoSync(),
			}
		},
		computed: {
			...mapGetters(['hasLogin']),
			...mapState(["isPrivacy"])
		},
		mounted() {
			this.initCode();
		},
		methods: {
			toArticle(type){
				uni.navigateTo({
					url:"/pages/article/index?type="+type
				})
			},
			initCode() {
				uni.login({
					success: (res) => {
						this.code = res.code
					}
				})
			},
			clickPrivacy(){
				this.show_privacy = false;
				uni.setStorageSync('PRIVACY', true);
				this.$store.commit("setState",{
					key:"isPrivacy",
					val:true,
				})
			},
			async getphonenumber(e) {
				this.show_privacy = false;
				uni.setStorageSync('PRIVACY', true);
				this.$store.commit("setState",{
					key:"isPrivacy",
					val:true,
				})
				const {
					code,
					encryptedData,
					iv,
					errMsg
				} = e.detail;
				if (errMsg.indexOf("fail") != -1) {
					return;
				}
				uni.showLoading({
					title: "登录中..."
				})
				const res = await this.$api({
					url: "login",
					data: {
						jscode: this.code,
						encryptedData: encryptedData,
						iv: iv
					}
				})
				if (res.code == 200) {
					uni.setStorageSync("token", res.data)
					uni.hideLoading()
					uni.showToast({
						title: "登录成功",
						icon: "success"
					})
					this.$store.dispatch("initData")
				}
			},
		},
	}
</script>

<style lang="scss" scoped>
	.pop_box {
		width: 690rpx;
		padding: 30rpx;
		background: #FFF;
		border-radius: 30rpx;

		.title {
			font-size: 34rpx;
			font-weight: bold;
			margin-bottom: 30rpx;
			color: #333;
		}

		.desc {
			color: #656565;
			font-size: 28rpx;
			line-height: 36rpx;
			margin-bottom: 40rpx;
		}
	}

	.privacy {
		width: 750rpx;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 12;
		opacity: 0;
	}

	.login_check {
		width: 750rpx;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
		z-index: 10;
		opacity: 0;
	}
</style>