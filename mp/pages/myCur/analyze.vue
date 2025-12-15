<template>
	<view class="analyze">
		<view class="top__box">
			<view>
				<view>{{total}}</view>
				<view>简历访问量(次)</view>
			</view>
			<view>
				<view>{{count}}</view>
				<view>交换简历数(次)</view>
			</view>
		</view>
		<view class="content">
			<view class="title">交换请求</view>
			<view class="btns">
				<view :class="status===''?'active':''" @click="statusChange('')">全部</view>
				<view :class="status===1?'active':''" @click="statusChange(1)">待处理</view>
				<view :class="status===2?'active':''" @click="statusChange(2)">已同意</view>
			</view>
			<view class="time">
				<!-- 2025.05.08 -->
			</view>
			<!-- <cur-item :type="1" v-for="k of exchangeList" :item="k" :key="k.id"></cur-item> -->
			<cur-item :type="1" v-for="k of list" :item="k" :key="k.id" @exchange="handleExchange"></cur-item>
			<empty v-if="!list.length && !isLoading"></empty>
		</view>
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
				status: '',
				info: {},
				exchangeList: [],
				list: [],
				page: 1,
				isLoading: false,
				isMore: true,
				count: 0,
				total: 0,
			}
		},
		onLoad(options) {
			this.count = options.count;
			this.total = options.total;
			this.loadlist(true)
		},
		async onPullDownRefresh() {
			await this.loadlist(true);
			uni.stopPullDownRefresh()
		},
		onReachBottom() {
			this.loadlist();
		},
		methods: {
			async handleExchange(id) {
				uni.showModal({
					title: "提示",
					content: "确认同意该交换请求吗？",
					success: async ({
						confirm
					}) => {
						if (confirm) {
							const res = await this.$api({
								url: "departmentCardSave",
								data: {
									id: id,
									type: 2
								}
							})
							if (res.code == 200) {
								uni.showToast({
									title: "操作成功",
									icon: "success"
								})
								this.loadlist(true);
							}
						}
					}
				})
			},
			statusChange(val) {
				if (this.status === val) return;
				this.status = val;
				this.loadlist(true);
			},
			async loadlist(rest) {
				if (rest) {
					this.isMore = true;
					this.page = 1;
					this.list = [];
				}
				if (!this.isMore) {
					this.$msg("没有更多了！")
					return;
				}
				if (this.isLoading) return;
				this.isLoading = true;
				const res = await this.$api({
					url: "getUserCardList",
					data: {
						page: this.page,
						size: 20,
						status: this.status
					}
				})
				this.isLoading = false;
				if (res.code == 200) {
					if (rest) {
						this.list = res.data.list;
					} else {
						this.list = this.list.concat(res.data.list);
					}
					this.isMore = res.data.list.length == 20
				}
			}
		}
	}
</script>
<style>
	page {
		background: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.analyze {
		background: linear-gradient(55deg, #4275FD, #4275FD);
		background-size: 750rpx 432rpx;
		background-repeat: no-repeat;
		padding-top: 20rpx;

		.top__box {
			padding: 42rpx;
			display: flex;
			color: #FFFFFF;

			&>view {
				flex: 1;

				&>view:nth-child(1) {
					font-weight: 600;
					font-size: 45rpx;
				}

				&>view:nth-child(2) {
					margin-top: 48rpx;
					font-weight: 400;
					font-size: 36rpx;
				}
			}
		}

		.content {
			background: #FFFFFF;
			border-radius: 40rpx 40rpx 0rpx 0rpx;
			padding: 28rpx;

			.title {
				font-weight: 400;
				font-size: 32rpx;
				color: #000000;
				line-height: 55rpx;
			}

			.btns {
				margin-top: 20rpx;
				display: flex;

				&>view {
					height: 60rpx;
					background: #F1F1F1;
					border-radius: 30rpx;
					padding: 0rpx 34rpx;

					font-weight: 400;
					font-size: 26rpx;
					color: #000000;
					line-height: 60rpx;
				}

				&>view:not(:first-child) {
					margin-left: 20rpx;
				}

				.active {
					background: #DAE5FF;
					color: #447DFE;
				}
			}

			.time {
				font-weight: 400;
				font-size: 28rpx;
				color: #737373;
				line-height: 55rpx;
				margin-top: 20rpx;
			}
		}


	}
</style>