<template>
	<view class="apply">
		<view class="box form">
			<view class="title">报名信息</view>
			<view class="line"></view>
			<view class="row">
				<view class="r_l">
					<text class="must">姓名</text>
				</view>
				<view class="r_r">
					<!-- <input type="text" v-model.trim="form.sign_up_name" placeholder="请输入姓名" placeholder-class="ph" /> -->
					<my-input v-model.trim="form.sign_up_name" placeholder="请输入姓名"></my-input>
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">手机</text>
				</view>
				<view class="r_r">
					<!-- <input type="text" v-model.trim="form.sign_up_phone" placeholder="请输入手机号" placeholder-class="ph" /> -->
					<my-input v-model.trim="form.sign_up_phone" placeholder="请输入手机号"></my-input>
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">单位</text>
				</view>
				<view class="r_r">
					<!-- <input type="text" v-model.trim="form.sign_up_company" placeholder="请输入公司" placeholder-class="ph" /> -->
					<my-input v-model.trim="form.sign_up_company" placeholder="请输入单位"></my-input>
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">职位</text>
				</view>
				<view class="r_r">
					<!-- <input type="text" v-model.trim="form.sign_up_position" placeholder="请输职位" placeholder-class="ph" /> -->
					<my-input v-model.trim="form.sign_up_position" placeholder="请输职位"></my-input>
				</view>
			</view>
			<view class="row" v-for="(k,i) of selectList" :key="i">
				<view class="r_l">
					<text>{{k.label}}</text>
				</view>
				<view class="r_r">
					<!-- <input type="text" v-model.trim="form.sign_up_position" placeholder="请输职位" placeholder-class="ph" /> -->
					<my-input v-model.trim="k.value" placeholder="请输入"></my-input>
				</view>
			</view>
			<!-- <view class="row" v-if="selectList.length">
				<view class="r_l">
					<text class="must">多选内容</text>
				</view>
				<view class="r_r">
					<view class="select_list">
						<view v-for="(k,i) of selectList" :key="i" @click="hanleClick(i)">
							<image :src="`/static/meeting/ck${k.select?'ed':''}.png`" mode="scaleToFill"></image>
							<text>{{k.label}}</text>
						</view>
					</view>
				</view>
			</view> -->
			<!-- <view class="row">
				<view class="r_l">
					<text class="must">邀请人</text>
				</view>
				<view class="r_r">
					<input type="text" placeholder="请输邀请人" placeholder-class="ph" />
				</view>
			</view> -->
		</view>
		<view class="bnt_big"
			style="width: 558rpx;margin-top: 150rpx;display: flex;justify-content: center;align-items: center;"
			@click="handleSubmit">
			<text>立即报名</text>
			<view class="price" v-if="is_price_status==1">
				<text>¥</text>
				<text>{{price.toFixed(2)}}</text>
			</view>
		</view>
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
					sign_up_name: "",
					sign_up_phone: "",
					sign_up_company: "",
					sign_up_position: "",
					ext_content: "",
					meeting_id: "",
				},
				price: 0,
				is_price_status: 0, //0 免费  1收费
				selectList: [],
			}
		},
		onLoad(options) {
			this.form.meeting_id = options.id;
			this.loadInfo();
		},
		methods: {
			async loadInfo() {
				const res = await this.$api({
					url: "getMeetingDetail",
					data: {
						id: this.form.meeting_id
					}
				})
				if (res.code == 200) {
					const {is_price_status,price,ext_content}=res.data;
					this.is_price_status=is_price_status||0;
					this.price=price*1 ||0;
					if (ext_content) {
						this.selectList = JSON.parse(ext_content).map(k => {
							return {
								value: "",
								label: k
							}
						})
					}
				}
			},
			hanleClick(index) {
				this.selectList[index].select = !this.selectList[index].select
			},
			async handleSubmit() {
				if (!checkStr(this.form.sign_up_name,'nickName')) {
					this.$msg("请输入有效姓名")
					return;
				}
				let reg = /^1[3-9][0-9]{9}$/;
				if (!reg.test(this.form.sign_up_phone)) {
					this.$msg("请输入有效手机号")
					return;
				}
				if (!this.form.sign_up_company) {
					this.$msg("请输入单位")
					return;
				}
				if (!this.form.sign_up_position) {
					this.$msg("请输入职位")
					return;
				}
				let ext_content = this.selectList.filter(k=>k.value).map(k => k.label+":"+k.value).join(";");
				this.form.ext_content = ext_content;
				const res = await this.$api({
					url: "getMeetingSignUp",
					data: this.form
				})
				if (res.code == 200) {
					if (this.is_price_status==1) {
						const result = await this.$api({
							url: "meetingPay",
							data: {
								id: this.form.meeting_id,
								active_id: res.data
							}
						})
						if (result.code == 200) {
							const {
								app_response
							} = result.data;
							wx.requestPayment({
								...app_response,
								success: () => {
									uni.showToast({
										title: "报名成功,等待审核...",
										icon: "success"
									})
									setTimeout(() => {
										uni.redirectTo({
											url: "/pages/my/meetList?type=1"
										})
									}, 500)
								},
								complete: (info) => {}
							})
						}
					} else {
						uni.showToast({
							title: "报名成功",
							icon: "success"
						})
						setTimeout(() => {
							uni.redirectTo({
								url: "/pages/my/meetList?type=1"
							})
						}, 500)
					}
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.apply {
		padding-top: 10rpx;

		.price {
			margin: 0rpx 30rpx;
			display: flex;
			align-items: flex-end !important;
			font-weight: 600;
			font-size: 48rpx;
			color: #FFF;

			&>text:nth-child(1) {
				font-size: 26rpx;
				position: relative;
				top: 2rpx;
			}
		}

		.title {
			font-weight: 600;
			font-size: 34rpx;
			color: #000000;
			line-height: 55rpx;
		}

		.form {
			padding: 30rpx;

			.row {
				display: flex;
				align-items: center;
				min-height: 100rpx;

				.r_l {
					min-width: 143rpx;
					margin-right: 10rpx;
					font-weight: 400;
					font-size: 32rpx;
					color: #000000;
				}

				.r_r {
					flex: 1;

					&>input {
						width: 100%;
					}
				}
			}

			.select_list {
				display: flex;
				flex-wrap: wrap;

				&>view {
					display: flex;
					align-items: center;
					font-weight: 400;
					font-size: 32rpx;
					color: #000000;
					margin-left: 8rpx;
					margin-bottom: 8rpx;

					&>image {
						width: 30rpx;
						height: 30rpx;
						margin-right: 14rpx;
						display: block;
					}
				}
			}
		}
	}
</style>