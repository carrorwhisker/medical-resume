<template>
	<view class="select_picker" v-if="show_box" @click="handleCancel">
		<view class="select_picker_content" :class="showContent?'show':'hide'" @click.stop="()=>{}">
			<view class="top">
				<view class="close" @click.stop="handleCancel">取消</view>
				<view class="title">{{title}}</view>
				<view class="com" @click.stop="handleConfirm">确定</view>
			</view>
			<!-- <view class="top" v-else>
				<view class="title">{{title}}</view>
			</view> -->
			<view class="content">
				<picker-view class="picker" v-if="listType=='list'" :indicator-style="indicatorStyle" immediate-change
					:value="value" @change="bindChange">
					<picker-view-column>
						<view class="item" :class="actIndex==index?'act':''" v-for="(item,index) in list" :key="index">
							{{item[keyName]}}
						</view>
					</picker-view-column>
				</picker-view>
				<view v-else style="height: 600rpx; display: flex;flex-direction: column;padding-bottom: 60rpx;">
					<scroll-view scroll-y class="content_list_box">
						<view class="content_list">
							<view v-for="(item,index) of list" :key="index" :class="actIndex==index?'active':''"
								@click.stop="handelSelect(index)">{{item[keyName]}}</view>
						</view>
					</scroll-view>
					<!-- <view>
						<view class="bnt_big" @click="handleConfirm">确 定</view>
						<view class="bnt_big_" style="margin-top: 30rpx;" @click="handleCancel">取 消</view>
					</view> -->
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			list: {
				type: Array,
				default: [],
			},
			title: {
				type: String,
				default: '',
			},
			show: {
				type: Boolean,
				default: false,
			},
			keyName: {
				type: String,
				default: 'label',
			},
			index: {
				default: 0,
				type: Number,
			},
			listType: {
				type: String,
				default: 'list' // 
			}
		},
		data() {
			return {
				show_box: this.show,
				showContent: true,
				value: [this.index],
				result: this.list[this.index],
				indicatorStyle: "border-radius:10rpx;height:80rpx;border:none;",
				actIndex: this.index,
			}
		},
		mounted() {
			this.showContent = true;
			if (this.show) {
				this.show_box = true;
			}
		},
		methods: {
			handleCancel() {
				this.showContent = false;
				setTimeout(() => {
					this.$emit('update:show', false)
					this.show_box = false;
					this.showContent = true;
				}, 300)
			},
			handleConfirm() {
				this.$emit("confirm", {
					index: this.actIndex,
					value: this.list[this.actIndex]
				});
				this.handleCancel()
			},
			handelSelect(i) {
				this.value = [i];
				this.actIndex = i;
			},
			bindChange(e) {
				const {
					value
				} = e.detail;
				console.log(e);
				this.actIndex = value[0];
				this.value = [value[0]]
			}
		},
		watch: {
			show(val) {
				if (val) {
					this.show_box = this.show;
				} else {
					this.handleCancel();
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.content_list_box {
		flex: 1;
		overflow: hidden;
		margin-bottom: 40rpx;
	}

	.content_list {
		// max-height: 50vh;
		// overflow: hidden;
		// overflow-y: scroll;
		display: flex;
		flex-wrap: wrap;

		&>view {
			padding: 16rpx 30rpx;
			border-radius: 20rpx;
			font-size: 32rpx;
			color: #333;
			background: #F6F6F6;
			line-height: 42rpx;
			margin: 0rpx 10rpx 10rpx 0rpx;
		}

		.active {
			background: #358FFF;
			color: #FFF;
		}
	}

	.select_picker {
		width: 750rpx;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
		background-color: rgba(0, 0, 0, .5);
		z-index: 9999;

		&_content {
			width: 750rpx;
			min-height: 500rpx;
			max-height: 60vh;
			background: #FFFFFF;
			border-radius: 30rpx 30rpx 0px 0px;
			padding: 40rpx 50rpx;
			position: absolute;
			bottom: 0;
			left: 0;

			.top {
				display: flex;
				justify-content: space-between;
				align-items: center;

				.close {
					font-size: 30rpx;
					color: #797979;
				}

				.title {
					font-size: 34rpx;

					color: #2A2A2A;
				}

				.com {
					font-size: 30rpx;
					color: #4275FD;
				}
			}

			.content {
				width: 650rpx;
				min-height: 500rpx;
				max-height: calc(60vh - 80rpx);
				padding-top: 50rpx;

				.picker {
					width: 100%;
					height: 400rpx;

					::v-deep .uni-picker-view-indicator {
						&::after {
							display: none !important;
						}

						&::before {
							display: none !important;
						}
					}
				}

				.item {
					width: 650rpx;
					height: 80rpx;
					text-align: center;
					line-height: 80rpx;
					font-size: 30rpx;
				}

				.act {
					background: #F9F9F9;
					border-radius: 10rpx;
				}
			}
		}
	}

	.show {
		animation: show .3s linear 1;
		transform: translateY(0);
	}

	.hide {
		animation: hide .3s linear 1;
		transform: translateY(100%);
	}

	@keyframes show {
		0% {
			transform: translateY(100%);
		}

		100% {
			transform: translateY(0);
		}
	}

	@keyframes hide {
		0% {
			transform: translateY(0);
		}

		100% {
			transform: translateY(100%);
		}
	}
</style>