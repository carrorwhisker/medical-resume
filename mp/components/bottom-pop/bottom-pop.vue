<template>
	<view class="select_picker" v-if="show_box"  @click.stop="handleCancel" @touchmove.stop.prevent="handleTouchMove">
		<view class="select_picker_content" :class="showContent?'show':'hide'" @click.stop="()=>{}">
			<view :style="{minHeight:minHeight}">
				<slot></slot>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			show: {
				type: Boolean,
				default: false,
			},
			minHeight: {
				type: String,
				default: '400rpx',
			},
		},
		data() {
			return {
				showContent: true,
				show_box: this.show,
			}
		},
		mounted() {
			this.showContent = true;
		},
		methods: {
			handleTouchMove(e) {
			      // 空函数即可，仅用于阻断事件
		    },
			handleCancel() {
				this.showContent = false;
				setTimeout(() => {
					this.$emit('update:show', false)
					this.showContent = true;
					this.show_box = false;
				}, 300)
			},
			handleConfirm() {
				this.handleCancel()
			},
		},
		watch: {
			show(val) {
				if (val) {
					this.show_box = this.show;
				} else {
					this.handleCancel()
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.select_picker {
		width: 750rpx;
		height: 100vh;
		position: fixed;
		top: 0;
		left: 0;
		background-color: rgba(0, 0, 0, .5);
		z-index: 999;

		.close_box {
			width: 48rpx;
			height: 48rpx;
			position: absolute;
			right: 30rpx;
			top: 32rpx;
			z-index: 5;
		}

		&_content {
			width: 750rpx;
			overflow: hidden;
			// min-height: 579rpx;
			background: #FFFFFF;
			border-radius: 30rpx 30rpx 0px 0px;
			padding: 39rpx 50rpx;
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
					color: #FF693B;
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