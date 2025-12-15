<template>
	<view class="collect">
		<view class="collect_top">
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
				<input type="text" placeholder="名称搜索" v-model="title" placeholder-class="ph" confirm-type="search"
					@confirm="loadlist(true)" />
			</view>
		</view>
		<scroll-view class="scroll" scroll-y>
			<resume-item :isCollect="true" :item="k" v-for="(k,i) of list" :key="i"></resume-item>
			<empty v-if="!list.length && !isLoading"></empty>
			<view class="no_more" v-if="!isMore && list.length"></view>
		</scroll-view>

		<select-picker :show.sync="showCity" :list="cityList" keyName="name"  listType="tag"
			@confirm="handleCityConfirm"></select-picker>

		<select-picker :show.sync="showPosition" :list="offices" keyName="name" listType="tag"
			@confirm="handlePositionConfirm"></select-picker>
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
				city: "",
				cityName: "",
				title: "",
				list: [],
				isLoading: false,
				isMore: true,
				needRest: false,
				showCity: false,
				showPosition: false,
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
			handleCityConfirm(e) {
				this.city = e.value.code;
				this.cityName = e.value.code ? e.value.name : '';
				this.loadlist(true)
			},
			handlePositionConfirm(e) {
				this.position = e.value.code ;
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
				const res = await this.$api({
					url: "getCollectList",
					data: {
						page: this.page,
						size: 20,
						position: this.position,
						city: this.city,
						title: this.title,
					}
				})
				this.isLoading = false;
				if (res.code == 200) {
					// 兼容后端返回的 rows 字段（TableDataInfo 格式）
					const rows = res.rows || (res.data && res.data.rows) || []
					if (rows.length > 0) {
						if (rest) {
							this.list = rows;
						} else {
							this.list = this.list.concat(rows);
						}
						this.isMore = rows.length == 20;
						this.page++;
					} else {
						if (rest) {
							this.list = [];
						}
						this.isMore = false;
					}
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
	.collect {
		height: 100%;
		display: flex;
		flex-direction: column;
		overflow: hidden;
		padding-top: 2rpx;

		.collect_top {
			width: 750rpx;
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