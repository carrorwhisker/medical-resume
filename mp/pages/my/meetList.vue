<template>
	<view class="meet_list">
		<view class="tabs">
			<view :class="type==i?'active':''" v-for="(k,i) of tabs" :key="i" @click="handleClickTab(i)">{{k}}</view>
		</view>
		<scroll-view class="scroll" scroll-y :refresher-triggered="triggered" refresher-enabled
			@refresherrefresh="refresherrefresh">
			<view style="height: 8rpx;"></view>
			<my-metting v-for="item in list" :key="item.id" :item="item"></my-metting>
			<!-- <view class="item" v-for="item in list" :key="item.id" @click="toDetails(item)">
				<view class="item_top">
					<text>{{item.start_time}}</text>
					<text style="color: #FE4324;">已提交</text>
				</view>
				<view class="line"></view>
				<view class="item_info">
					<image :src="item.meeting_img" mode="aspectFill" class="img"></image>
					<view class="title">{{item.meeting_name}}</view>
				</view>
				<view class="line"></view>
				<view class="item_addres">
					<view>地址：</view>
					<view>{{item.qy_name}}</view>
				</view>
				<view class="btn_box">
					<view class="btn">查看详情</view>
				</view>
			</view> -->
			<empty v-if="!list.length && !isLoading"></empty>
			<view style="height: 20rpx;"></view>
		</scroll-view>
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
				type: 0,
				tabs: ["全部", "待审核", "待参加", "已拒绝"],
				triggered: false,
				page: 1,
				list: [],
				isMore: true,
				isLoading: false,
			}
		},
		onLoad(options) {
			this.type = options.type;
			this.loadlist(true)
		},
		methods: {
			async refresherrefresh() {
				this.triggered = true;
				await this.loadlist(true);
				this.triggered = false;
			},
			handleClickTab(i) {
				if (this.type == i) return;
				this.type = i;
				this.loadlist(true);
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
					url: "getUserListMeeting",
					data: {
						page: this.page,
						size,
						status: this.type,
					}
				})
				this.isLoading = false;
				if (res.code == 200) {
					const rows = res.rows || (res.data && res.data.rows) || []
					const total = res.total || (res.data && res.data.total) || 0
					rows.forEach(k => {
						// 兼容 meeting 嵌套字段 & 驼峰
						const m = k.meeting || {}
						m.meeting_img = m.meeting_img || m.meetingImg
						m.meeting_name = m.meeting_name || m.meetingName
						m.qy_name = m.qy_name || m.qyName
						m.start_time = m.start_time || m.startTime
						m.end_time = m.end_time || m.endTime
						m.meeting_address = m.meeting_address || m.meetingAddress
						// 时间戳处理（秒）
						const fmt = (v) => {
							if (typeof v === 'number') return v;
							if (typeof v === 'string' && /^\d+$/.test(v)) return Number(v);
							return v;
						}
						const toDate = (sec) => {
							const d = new Date(sec * 1000)
							return `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`
						}
						m.start_time = fmt(m.start_time)
						m.end_time = fmt(m.end_time)
						if (typeof m.start_time === 'number') {
							m.start_time = toDate(m.start_time)
						}
						if (typeof m.end_time === 'number') {
							m.end_time = toDate(m.end_time)
						}
						k.create_time = fmt(k.create_time)
						if (typeof k.create_time === 'number') {
							k.create_time = toDate(k.create_time)
						}
						k.meeting = m
						// 平铺到根，便于组件直接取值
						k.active_id = k.id || k.active_id
						k.meeting_img = k.meeting_img || m.meeting_img
						k.meeting_name = k.meeting_name || m.meeting_name
						k.qy_name = k.qy_name || m.qy_name
						// 平铺后的 start/end 也确保是日期字符串
						const flatStart = fmt(k.start_time != null ? k.start_time : m.start_time)
						const flatEnd = fmt(k.end_time != null ? k.end_time : m.end_time)
						k.start_time = typeof flatStart === 'number' ? toDate(flatStart) : flatStart
						k.end_time = typeof flatEnd === 'number' ? toDate(flatEnd) : flatEnd
						k.meeting_address = k.meeting_address || m.meeting_address
						k.is_price_status = k.is_price_status || m.is_price_status
						k.price = k.price || m.price
						// 状态文案
						const map = {1:'待审核',2:'待参加',3:'已拒绝'}
						k.status_text = k.status_text || map[k.status] || ''
					})
					if (rest) {
						this.list = rows;
					} else {
						this.list = this.list.concat(rows);
					}
					this.isMore = rows.length === size && this.list.length < total;
				}
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
	.meet_list {
		height: 100%;
		display: flex;
		flex-direction: column;

		.tabs {
			width: 750rpx;
			height: 100rpx;
			padding: 0rpx 40rpx;
			display: flex;
			justify-content: space-between;
			margin-bottom: 20rpx;

			&>view {
				height: 100rpx;
				font-weight: 400;
				font-size: 30rpx;
				color: #8C8C8C;
				line-height: 100rpx;
			}

			.active {
				color: #000000;
				position: relative;

				&:before {
					content: "";
					display: block;
					width: 100%;
					height: 6rpx;
					background-color: #447DFE;
					border-radius: 3rpx;
					position: absolute;
					bottom: 0;
					left: 0;
				}
			}
		}

		// .item {
		// 	width: 693rpx;
		// 	background: #FFFFFF;
		// 	border-radius: 25rpx;
		// 	margin: 0 auto;
		// 	margin-bottom: 24rpx;

		// 	.line {
		// 		margin: 0;
		// 	}

		// 	.item_top {
		// 		height: 80rpx;
		// 		padding: 0 30rpx;
		// 		display: flex;
		// 		justify-content: space-between;
		// 		align-items: center;

		// 		font-weight: 400;
		// 		font-size: 30rpx;
		// 		color: #8C8C8C;
		// 	}

		// 	.item_info {
		// 		display: flex;
		// 		padding: 28rpx 30rpx;

		// 		.img {
		// 			width: 202rpx;
		// 			height: 114rpx;
		// 			border-radius: 20rpx;
		// 		}

		// 		.title {
		// 			flex: 1;
		// 			padding-left: 26rpx;
		// 			font-weight: 400;
		// 			font-size: 30rpx;
		// 			color: #000000;
		// 		}
		// 	}

		// 	.item_addres {
		// 		display: flex;
		// 		align-items: flex-start;
		// 		margin-top: 30rpx;
		// 		padding: 0rpx 30rpx;
		// 		font-weight: 400;
		// 		font-size: 32rpx;
		// 		color: #000000;
		// 		line-height: 40rpx;

		// 		&>view:nth-child(2) {
		// 			flex: 1;
		// 			font-weight: bold;
		// 		}
		// 	}

		// 	.btn_box {
		// 		height: 114rpx;
		// 		display: flex;
		// 		justify-content: flex-end;
		// 		padding-top: 20rpx;
		// 		padding-right: 16rpx;

		// 		.btn {
		// 			width: 184rpx;
		// 			height: 60rpx;
		// 			background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
		// 			border-radius: 30rpx;

		// 			font-weight: 400;
		// 			font-size: 26rpx;
		// 			color: #FFFFFF;
		// 			text-align: center;
		// 			line-height: 60rpx;
		// 		}
		// 	}
		// }
	}
</style>