<template>
	<view class="meeting">
		<r-top></r-top>
		<view class="img_box">
			<image src="/static/meeting/img_1.png" mode="widthFix" class="img_l"></image>
			<image src="/static/meeting/img_2.png" mode="widthFix" class="img_r"></image>
		</view>
		<view class="meeting_top">
			<view class="select" @click="showCity=true">
				<text>{{cityName?cityName:"区域"}}</text>
				<image src="/static/icon/down.png" mode="widthFix"></image>
			</view>
			<view class="search_box">
				<image src="/static/icon/search.png" mode="widthFix" @click="loadlist(true)"></image>
				<input type="text" v-model="title" confirm-type="search" placeholder="名称搜索" placeholder-class="ph"
					@confirm="loadlist(true)" />
			</view>
		</view>
		<view class="tabs">
			<view :class="order==2?'active':''" @click="handleClickTab(2)">
				<text>综合排序</text>
				<image src="/static/meeting/icon_1.png" mode="widthFix" style="width: 12rpx;height: 21rpx;"></image>
			</view>
			<view :class="order==3?'active':''" @click="handleClickTab(3)">
				<text>最新时间</text>
				<image src="/static/meeting/icon_1.png" mode="widthFix" style="width: 12rpx;height: 21rpx;"></image>
			</view>
			<!-- <view>
				<text>筛选</text>
				<image src="/static/meeting/icon_2.png" mode="widthFix" style="width: 24rpx;height: 22rpx;"></image>
			</view> -->
		</view>
		<scroll-view class="scroll" scroll-y :refresher-triggered="triggered" refresher-enabled
			@refresherrefresh="refresherrefresh">
			<view style="height: 20rpx;"></view>
			<view class="meeting_item" v-for="k of list" :key="k.id" @click="toInfo(k)">
				<image :src="k.meeting_img" mode="aspectFill" class="avt"></image>
				<view class="desc">
					<view class="title">{{k.meeting_name}}</view>
					<view class="time">
						<text>{{k.start_time}}-{{k.end_time}} </text>
						<text>{{k.qy_name}}</text>
					</view>
				</view>
			</view>
			<empty v-if="!list.length && !isLoading"></empty>
			<view class="no_more" v-if="!isMore && list.length"></view>
			<view style="height: 20rpx;"></view>
		</scroll-view>

		<select-picker :show.sync="showCity" :list="cityList" keyName="name" listType="tag"  title="选择区域"
			@confirm="handleCityConfirm"></select-picker>
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
				triggered: false,
				showCity: false,
				order: 1, // 1默认排序  2综合排序  3时间排序
				city: "",
				cityName: "",
				title: "",
				list: [],
				isLoading: false,
				isMore: true,
			}
		},
		computed: {
			...mapState(["cityList", "positionList"]),
		},
		onLoad(options) {
			this.loadlist(true)
		},
		methods: {
			handleClickTab(val) {
				if (this.order == val) {
					this.order = 1
				} else {
					this.order = val
				}
				this.loadlist(true)
			},
			handleCityConfirm(e) {
				if (this.city === e.value.code) return;
				this.city = e.value.code;
				this.cityName = e.value.code ? e.value.name : '';
				this.loadlist(true)
			},
			async refresherrefresh() {
				this.triggered = true;
				await this.loadlist(true);
				this.triggered = false;
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
					url: "getMeetingList",
					data: {
						page: this.page,
						size,
						city: this.city,
						title: this.title,
						order: this.order,
					}
				})
				this.isLoading = false;
				if (res.code == 200) {
					const rows = res.rows || (res.data && res.data.rows) || []
					const total = res.total || (res.data && res.data.total) || 0
					rows.forEach(k => {
						// 兼容后端驼峰字段
						k.meeting_img = k.meeting_img || k.meetingImg
						k.meeting_name = k.meeting_name || k.meetingName
						k.qy_name = k.qy_name || k.qyName
						k.start_time = k.start_time || k.startTime
						k.end_time = k.end_time || k.endTime
						// 时间戳转日期
						if (typeof k.end_time === 'number') {
							const d = new Date(k.end_time * 1000)
							k.end_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`
						}
						if (typeof k.start_time === 'number') {
							const d = new Date(k.start_time * 1000)
							k.start_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`
						}
						if (typeof k.end_time === 'string') {
							k.end_time = k.end_time.split(" ")[0].replace(/-/g, '.');
						}
						if (typeof k.start_time === 'string') {
							k.start_time = k.start_time.split(" ")[0].replace(/-/g, '.');
						}
					})
					if (rest) {
						this.list = rows;
					} else {
						this.list = this.list.concat(rows);
					}
					this.isMore = rows.length === size && this.list.length < total
				}
			},
			toInfo({
				id
			}) {
				uni.navigateTo({
					url: "/pages/meeting/details?id=" + id
				})
			},
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
	.meeting {
		height: 100%;
		display: flex;
		flex-direction: column;
		overflow: hidden;

		.img_box {
			margin-top: 40rpx;
			display: flex;
			align-items: flex-start;
			justify-content: space-between;
			padding: 0rpx 28rpx;
			margin-bottom: 20rpx;

			.img_l {
				width: 194rpx;
				height: 69rpx;
				position: relative;
				top: 40rpx;
			}

			.img_r {
				width: 256rpx;
				height: 136rpx;
			}
		}

		.meeting_top {
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

		.tabs {
			display: flex;
			justify-content: flex-end;
			padding-right: 30rpx;

			&>view {
				margin-left: 40rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				font-weight: 400;
				font-size: 26rpx;
				color: #8C8C8C;

				&>image {
					margin-left: 10rpx;
				}
			}

			.active {
				color: #333333;
			}
		}

		.meeting_item {
			width: 695rpx;
			// background: #FFFFFF;
			border-radius: 40rpx;
			margin: 24rpx auto;
			display: flex;

			.avt {
				width: 326rpx;
				height: 184rpx;
				border-radius: 20rpx;
			}

			.desc {
				flex: 1;
				padding: 12rpx 14rpx 12rpx 18rpx;
				display: flex;
				flex-direction: column;
				justify-content: space-between;

				.title {
					font-weight: 400;
					font-size: 32rpx;
					color: #000000;
					line-height: 45rpx;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					/* 显示两行 */
					overflow: hidden;

				}

				.time {
					font-weight: 400;
					font-size: 22rpx;
					color: #B8B8B8;
					display: flex;
					justify-content: space-between;
				}
			}
		}

	}
</style>