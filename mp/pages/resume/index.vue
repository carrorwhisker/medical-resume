<template>
	<view class="resume">
		<view class="resume_top">
			<view class="select" @click="showCity=true">
				<text>{{cityName?cityName:"区域"}}</text>
				<image src="/static/icon/down.png" mode="widthFix"></image>
			</view>
			<view class="select" @click="showPosition=true">
				<text>{{positionName}}</text>
				<image src="/static/icon/down.png" mode="widthFix"></image>
			</view>

			<view class="search_box">
				<image src="/static/icon/search.png" mode="widthFix" @click="loadlist(true)"></image>
				<input type="text" v-model="title" placeholder="名称/地址" confirm-type="search" @confirm="loadlist(true)"
					placeholder-class="ph" />
			</view>
		</view>
		<scroll-view class="scroll" scroll-y :refresher-triggered="triggered" refresher-enabled
			@refresherrefresh="refresherrefresh">
			<resume-item :sort="i+1" :item="k" v-for="(k,i) of list" :key="i"></resume-item>
			<empty v-if="!list.length && !isLoading"></empty>
			<view class="no_more" v-if="!isMore && list.length"></view>
			<view style="height: 20rpx;"></view>
		</scroll-view>

		<select-picker :show.sync="showCity" :list="cityList" keyName="name" listType="tag"  title="选择区域"
			@confirm="handleCityConfirm"></select-picker>

		<select-picker :show.sync="showPosition" :list="offices" keyName="name" listType="tag"  title="选择科室"
			@confirm="handlePositionConfirm"></select-picker>

		<!-- <bottom-pop :show.sync="showPosition">
			<view class="pos_box">
				<view class="pos_title">选择科室</view>
				<scroll-view scroll-y class="pos_list_box">
					<view class="pos_list">
						<view v-for="(k,i) of offices" :key="i"
							:class="((k.code==''&&position==''&&position_=='')||k.code==position_)?'active':''"
							@click="handleSelect(k.code)">{{k.name}}</view>
					</view>
				</scroll-view>
				<view class="pos_bottom">
					<view class="bnt_big" @click="handlePosConf">确 定</view>
					<view class="bnt_big_" style="margin-top: 30rpx;" @click="handlePosCel">取 消</view>
				</view>
			</view>
		</bottom-pop> -->

		<login-check></login-check>
	</view>
</template>

<script>
	import {
		mapState,
		mapGetters
	} from "vuex"
	export default {
		data() {
			return {
				page: 1,
				position: "",
				position_: "",
				city: "",
				cityName: "",
				title: "",
				list: [],
				isLoading: false,
				isMore: true,
				needRest: false,
				showCity: false,
				showPosition: false,
				triggered: false,
				offices: [],
			}
		},
		computed: {
			...mapState(["cityList", "positionList"]),
			positionName() {
				if (!this.position || !this.offices.length) return "科室";
				let obj = this.offices.filter(k => k.code == this.position)[0];
				return obj ? obj.name : "科室"
			},
		},
		onLoad(options) {
			this.loadOffices();
			this.loadlist(true);
			uni.$on("handleNeedRest", () => {
				this.needRest = true;
			})
		},
		onShow() {
			if (this.needRest) {
				this.loadlist(true);
				this.needRest = false;
			}
		},
		methods: {
			async loadOffices() {
				const res = await this.$api({
					url: "departmentList"
				})
				if (res.code == 200) {
					this.offices = [{
						name: "全部",
						code: ''
					}, ...res.data];
				}
			},
			handleSelect(val) {
				this.position_ = this.position_ == val ? "" : val;
			},
			handlePosCel() {
				this.showPosition = false;
				this.position_ = this.position;
			},
			handlePosConf() {
				this.showPosition = false;
				this.position = this.position_;
				console.log(this.position_);
				this.loadlist(true)
			},
			async refresherrefresh() {
				this.triggered = true;
				await this.loadlist(true);
				this.triggered = false;
			},
			handleCityConfirm(e) {
				this.city = e.value.code;
				this.cityName = e.value.code ? e.value.name : '';
				this.loadlist(true)
			},
			handlePositionConfirm(e) {
				this.position = e.value.code;
				this.loadlist(true)
			},
			async loadlist(rest) {
				if (rest) {
					this.isMore = true;
					this.page = 1;
				}
				if (!this.isMore) {
					this.$msg("没有更多了！")
					return;
				}
				if (this.isLoading) return;
				this.isLoading = true;
				const size = 20
				const res = await this.$api({
					url: "indexList",
					data: {
						page: this.page,
						size,
						position: this.position == "科室" ? "" : this.position,
						city: this.city,
						title: this.title,
					}
				})
				this.isLoading = false;
				if (res.code == 200) {
					const rows = res.rows || (res.data && res.data.rows) || []
					const total = res.total || (res.data && res.data.total) || 0
					if (rest) {
						this.list = rows;
					} else {
						this.list = this.list.concat(rows);
					}
					this.isMore = rows.length === size && this.list.length < total
				}
			}
		}
	}
</script>
<style>
	page,
	uni-page-body {
		height: 100%;
	}
</style>
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

	.resume {
		height: 100%;
		display: flex;
		flex-direction: column;
		overflow: hidden;
		padding-top: 2rpx;

		.resume_top {
			height: 80rpx;
			display: flex;
			align-items: center;
			padding: 0rpx 30rpx;
			background: #F6F6F6;
			margin-bottom: 20rpx;

			.select {
				font-weight: 400;
				font-size: 26rpx;
				color: #8C8C8C;
				display: flex;
				align-items: center;
				margin-right: 48rpx;

				&>image {
					width: 24rpx;
					height: 13rpx;
					margin-left: 10rpx;
				}
			}

		}
	}
</style>