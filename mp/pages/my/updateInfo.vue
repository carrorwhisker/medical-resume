<template>
	<view class="update_info">
		<view class="avt_box">
			<image :src="form.img?form.img:'/static/icon/user_icon.png'" mode="aspectFill" class="avt"></image>
			<image src="/static/icon/camera.png" mode="aspectFill" class="camera" @click="handleUploadImg"></image>
		</view>
		<view class="box info">
			<view class="row">
				<view>昵称</view>
				<!-- <input type="text" v-model="form.name" placeholder="请输入昵称" placeholder-class="ph"> -->
				<my-input v-model="form.name" placeholder="请输入昵称"></my-input>
			</view>
			<!-- <view class="row" style="margin-top: 60rpx;">
				<view>联系电话</view>
				<my-input v-model="form.phone" placeholder="请输入电话"></my-input>
			</view> -->
			<view class="row" style="margin-top: 60rpx;">
				<view>个人介绍</view>
			</view>
			<view class="textarea_box">
				<textarea v-model="form.content" placeholder="请输入个人介绍" placeholder-class="ph"></textarea>
			</view>
		</view>
		<view class="bnt_big" style="width: 585rpx;margin-top: 110rpx;" @click="handleSubmit">提交</view>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import { checkStr } from "@/util/utils.js"
	export default {
		data() {
			return {
				form: {
					name: "",
					img: "",
					phone: "",
					content: "",
				}
			}
		},
		computed: {
			...mapState(["userInfo"])
		},
		onLoad(options) {
			this.initForm()
		},
		methods: {
			initForm() {
				this.form = {
					name: this.userInfo.username,
					img: this.userInfo.img,
					// phone: this.userInfo.phone,
					content: this.userInfo.content
				}
			},
			async handleSubmit() {
				// checkStr(this.form.name,'nickName')
				if (!this.form.name) {
					this.$msg("请输入昵称！")
					return;
				}
				if (!this.form.img) {
					this.$msg("请上传头像！")
					return;
				}
				// let reg = /^1[3-9][0-9]{9}$/;
				// if (!reg.test(this.form.phone)) {
				// 	this.$msg("请输入有效手机号！")
				// 	return;
				// }
				const res = await this.$api({
					url: "updateUserInfo",
					data: this.form
				})
				if (res.code == 200) {
					uni.showToast({
						title: "修改成功",
						icon: "success"
					})
					this.$store.dispatch("getUserInfo")
					setTimeout(() => {
						this.$back()
					}, 500)
				} else {
					uni.showToast({
						title: "修改失败",
						icon: "fail"
					})
				}
			},
			async handleUploadImg() {
				const res = await this.$uploadImage()
				if (res.code == 200) {
					this.form.img = res.data.url
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.update_info {
		padding-top: 2rpx;

		.avt_box {
			width: 146rpx;
			height: 146rpx;
			margin: 0 auto;
			margin-top: 35rpx;
			position: relative;

			.avt {
				width: 146rpx;
				height: 146rpx;
				border-radius: 50%;
			}

			.camera {
				width: 60rpx;
				height: 60rpx;
				position: absolute;
				bottom: 0rpx;
				right: -18rpx;
				z-index: 2;
			}
		}

		.info {
			margin-top: 44rpx;
			padding: 50rpx 44rpx 60rpx 44rpx;

			.row {
				display: flex;
				align-items: center;
				font-weight: 400;
				font-size: 32rpx;
				color: #000000;

				&>view:nth-child(1) {
					width: 186rpx;
				}

				&>input {
					flex: 1;
				}
			}

			.textarea_box {
				width: 604rpx;
				height: 208rpx;
				background: #F6F6F6;
				border-radius: 25rpx;
				margin: 0 auto;
				margin-top: 23rpx;

				&>textarea {
					box-sizing: border-box;
					width: 100%;
					height: 100%;
					padding: 30rpx 34rpx;
				}
			}
		}
	}
</style>