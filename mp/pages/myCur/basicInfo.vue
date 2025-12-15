<template>
	<view class="basicInfo">
		<view class="box card">
			<view class="avt">
				<image :src="cardInfo.img?cardInfo.img:'/static/def.png'" mode="aspectFill"></image>
				<!-- <view class="avt_btn">复制头像</view> -->
			</view>
			<view class="desc">
				<view class="name">{{cardInfo.name||"******"}}
					<image src="/static/icon/vip.png" mode="widthFix" v-if="userInfo.is_vip==2"></image>
				</view>
				<view class="text1">{{cardInfo.company || '******'}}</view>
				<view class="text1">{{cardInfo.department_name || '******'}}</view>
				<view class="text2">
					<image src="/static/icon/min_1.png" mode="widthFix"></image>
					<text>{{cardInfo.phone||'******'}}</text>
				</view>
				<view class="text2">
					<image src="/static/icon/min_2.png" mode="widthFix"></image>
					<text>{{cardInfo.email||'******'}}</text>
				</view>
				<view class="text2">
					<image src="/static/icon/min_3.png" mode="widthFix"></image>
					<text>{{cardInfo.address||'******'}}</text>
				</view>
			</view>
		</view>

		<view class="box basic">
			<view class="title">基本信息</view>
			<view class="line" style="margin: 20rpx 0rpx;"></view>
			<view class="row">
				<view class="r_l">
					<text>头像</text>
				</view>
				<view class="r_v">
					<image :src="form.img?form.img:'/static/img/vat.png'" mode="widthFix"
						style="width: 82rpx;height: 82rpx;margin-right: 22rpx;"></image>
					<text style="color: #4472FD;font-size: 30rpx;" @click="handleUploadImg">点击上传头像</text>
				</view>
			</view>

			<view class="row">
				<view class="r_l">
					<text class="must">姓名</text>
				</view>
				<view class="r_v">
					<input type="text" v-model="form.name" placeholder="请输入真实姓名" placeholder-class="ph" />
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">单位</text>
				</view>
				<view class="r_v">
					<input type="text" v-model="form.company" placeholder="请输入单位名称" placeholder-class="ph" />
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">职位</text>
				</view>
				<view class="r_v">
					<input type="text" v-model="form.position" placeholder="请输入职位或职称" placeholder-class="ph" />
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">科室</text>
				</view>
				<view class="r_v" @click="showPicker=true">
					<view @click="showPicker=true" style="flex: 1;">
						<text class="ph" v-if="!departmentName">请选择科室</text>
						<text v-else>{{departmentName}}</text>
					</view>
					<image src="/static/icon/right_.png" mode="widthFix" style="width: 16rpx;height: 28rpx;"></image>
				</view>
			</view>
			<view class="row">
				<view class="r_l">
					<text class="must">地址</text>
				</view>
				<view class="r_v" @click="handleShowCity">
					<view style="flex: 1;">
						<text class="ph" v-if="!form.address">请选择：XX省XX市XX区</text>
						<text v-else>{{form.address}}</text>
					</view>
					<image src="/static/icon/right_.png" mode="widthFix" style="width: 16rpx;height: 28rpx;"></image>
				</view>
			</view>
		</view>

		<view class="bottom_box">
			<view>
				<view class="bnt_big" style="width: 694rpx;" @click="handleSave">保存</view>
			</view>
		</view>

		<!-- 选择科室 -->
		<!-- <select-picker :show.sync="showPicker" :list="offices" keyName="name"
			@confirm="handleOfficesConfirm"></select-picker> -->


		<!-- 选择地址 -->
		<!-- <cwiyc-citypicker :show="showCity" @confirmArea="handleCityConfirm" :closeOnClickOverlay="true"
			@cancel="showCity=false" /> -->

		<bottom-pop :show.sync="showPicker">
			<view class="pos_box">
				<view class="pos_title">选择科室</view>
				<scroll-view scroll-y class="pos_list_box">
					<view class="pos_list">
						<view v-for="(k,i) of offices" :key="i" :class="position_==k.code?'active':''"
							@click="handleSelect(k)">{{k.name}}</view>
					</view>
				</scroll-view>
				<view class="pos_bottom">
					<view class="bnt_big" @click="handlePosConf">确 定</view>
					<view class="bnt_big_" style="margin-top: 30rpx;" @click="showPicker=false">取 消</view>
				</view>
			</view>
		</bottom-pop>

	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	import {
		checkStr
	} from "@/util/utils.js"
	export default {
		data() {
			return {
				offices: [],
				showPicker: false,
				departmentName: "",
				form: {
					img: "",
					name: "",
					company: "",
					position: "",
					department: "",
					address: "",
				},
				showCity: false,
				position_: "",
			}
		},
		computed: {
			...mapState(['cardInfo', 'userInfo']),
		},
		onLoad(options) {
			this.form.img = this.cardInfo.img || '';
			this.form.name = this.cardInfo.name || '';
			this.form.company = this.cardInfo.company || '';
			this.form.position = this.cardInfo.position || '';
			this.form.department = this.cardInfo.department || '';
			this.position_ = this.cardInfo.department || '';
			this.form.address = this.cardInfo.address || '';
			this.loadOffices();
		},
		methods: {

			handleSelect({
				code,
				name
			}) {
				if (this.position_ == code) {
					this.position_ = "";
				} else {
					this.position_ = code;
				}
			},
			handlePosConf() {
				this.form.department = this.position_;
				this.departmentName = "";
				this.offices.forEach(k => {
					if (k.code == this.position_) {
						this.departmentName = k.name;
					}
				})
				this.showPicker = false;
			},
			handleShowCity() {
				// this.$refs.cityRef.open();
				// console.log(111);
				// this.showCity = true;
				uni.$emit("selectCity", '')
				uni.$once("selectCity", (data) => {
					if (data) {
						this.form.city = data[0];
						this.form.address = data.join("\/");
					}
				})
				uni.navigateTo({
					url: "/pages/city/index?address=" + this.form.address
				})
			},
			handleCityConfirm(e) {
				console.log(e);
				this.form.city = e[0];
				this.form.address = e.join("\/");
				this.showCity = false;
			},
			async handleUploadImg() {
				// const res = await this.$uploadImage()
				// if (res.code == 200) {
				// 	this.form.img = res.data.url
				// }
				uni.$emit("uploadAvt", "");
				uni.$once("uploadAvt", data => {
					if (data) {
						this.form.img = data.img;
						this.form.img_yt = data.img_yt;
					}
				})
				uni.navigateTo({
					url: "/pages/myCur/uploadAvt"
				})
			},
			handleOfficesConfirm(e) {
				this.form.department = e.value.code;
				this.departmentName = e.value.name;
			},
			async handleSave() {
				if (!checkStr(this.form.name, "nickName")) {
					this.$msg("请输入有效姓名")
					return;
				}
				if (!this.form.company) {
					this.$msg("请输入单位名称")
					return;
				}
				if (!this.form.position) {
					this.$msg("请输入职位")
					return;
				}
				if (!this.form.department) {
					this.$msg("请选择科室")
					return;
				}
				if (!this.form.address) {
					this.$msg("请选择地址")
					return;
				}
				const res = await this.$store.dispatch("saveCardInfo", {
					...this.cardInfo,
					...this.form
				})
				if (res) {
					uni.showToast({
						title: "保存成功"
					})
					setTimeout(() => {
						this.$back()
					}, 500)
				}
			},
			async loadOffices() {
				const res = await this.$api({
					url: "departmentList"
				})
				if (res.code == 200) {
					this.offices = res.data;
					if (this.form.department) {
						res.data.forEach((k, i) => {
							if (k.code == this.form.department) {
								this.position_ = k.code;
								this.departmentName = k.name;
							}
						})
					}
				}
			},
			handleChange(e) {
				const {
					value
				} = e.detail;
				this.index = value;
				this.form.department = this.offices[value].code;
			},
		}
	}
</script>

<style lang="scss" scoped>
	.pos_box {
		.pos_title {
			margin-bottom: 40rpx;
			font-size: 36rpx;
			font-weight: bold;
			color: #333;
			line-height: 50rpx;
			text-align: center;
		}

		.pos_list_box {
			width: 100%;
			height: 400rpx;
			overflow: hidden;
			margin-bottom: 40rpx;
		}

		.pos_list {
			// max-height: 50vh;
			// overflow: hidden;
			// overflow-y: scroll;
			display: flex;
			flex-wrap: wrap;
			// margin-bottom: 40rpx;

			&>view {
				padding: 10rpx 30rpx;
				border-radius: 10rpx;
				font-size: 30rpx;
				color: #333;
				background: #F6F6F6;
				line-height: 40rpx;
				margin: 0rpx 20rpx 20rpx 0rpx;
			}

			.active {
				background: #358FFF;
				color: #FFF;
			}
		}

	}

	.basicInfo {
		padding-top: 2rpx;

		.basic {
			padding: 30rpx;

			.title {
				font-weight: 600;
				font-size: 34rpx;
				color: #000000;
			}

			.row {
				min-height: 100rpx;
				display: flex;
				align-items: center;

				.r_l {
					width: 146rpx;
					font-weight: 400;
					font-size: 32rpx;
					color: #000000;
				}

				.r_v {
					flex: 1;
					display: flex;
					align-items: center;

					&>input {
						flex: 1;
					}
				}
			}
		}

	}
</style>