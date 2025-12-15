<template>
	<view class="personal-desc">
		<view class="content">
			<view class="textarea-container">
				<textarea 
					class="desc-textarea" 
					v-model="content" 
					placeholder="请输入个人介绍内容" 
					placeholder-class="placeholder"
					maxlength="3000"
					:cursor-spacing="50"
					:adjust-position="true"
				></textarea>
			</view>
			<view class="char-count">{{ content.length }}/3000</view>
		</view>
		
		<view class="bottom-btn">
			<view class="save-btn" @click="handleSave">保存</view>
		</view>
	</view>
</template>

<script>
import { mapState } from "vuex"

export default {
	data() {
		return {
			content: ""
		}
	},
	computed: {
		...mapState(['cardInfo'])
	},
	onLoad(options) {
		// 获取传递过来的内容进行回显
		if (options.content) {
			this.content = decodeURIComponent(options.content);
		} else {
			// 如果没有传递内容，使用store中的数据
			this.content = this.cardInfo.content || '';
		}
	},
	methods: {
		// 返回上一页
		handleBack() {
			uni.navigateBack();
		},
		async handleSave() {
			if (!this.content.trim()) {
				uni.showToast({
					title: "请输入个人介绍内容",
					icon: "none"
				});
				return;
			}
			
			try {
				// 保存到store
				const params = {
					...this.cardInfo,
					content: this.content.trim()
				};
				
				const res = await this.$store.dispatch("saveCardInfo", params);
				if (res) {
					uni.showToast({
						title: "保存成功",
						icon: "success"
					});
					
					// 延迟返回上一页，让用户看到成功提示
					setTimeout(() => {
						uni.navigateBack();
					}, 1000);
				}
			} catch (error) {
				uni.showToast({
					title: "保存失败，请重试",
					icon: "none"
				});
			}
		}
	}
}
</script>

<style lang="scss" scoped>
.personal-desc {
	min-height: 100vh;
	background-color: #f5f5f5;
	display: flex;
	flex-direction: column;
}

/* 导航栏样式 */
.nav-bar {
	height: 88rpx;
	background: #fff;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 30rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.nav-left {
		width: 80rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		
		.back-icon {
			width: 32rpx;
			height: 32rpx;
		}
	}
	
	.nav-title {
		font-size: 36rpx;
		font-weight: 600;
		color: #333;
	}
	
	.nav-right {
		width: 80rpx;
	}
}

.content {
	flex: 1;
	padding: 40rpx 30rpx;
	
	.textarea-container {
		background: #fff;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		height: 500rpx; /* 固定高度 */
	}
	
	.desc-textarea {
		width: 100%;
		height: 100%; /* 填满容器 */
		font-size: 28rpx;
		line-height: 1.6;
		color: #333;
		background: transparent;
		border: none;
		outline: none;
		resize: none;
	}
	
	.placeholder {
		color: #999;
	}
	
	.char-count {
		text-align: right;
		font-size: 24rpx;
		color: #999;
		margin-top: 20rpx;
		padding-right: 30rpx;
	}
}

.bottom-btn {
	padding: 30rpx;
	background: #fff;
	
	.save-btn {
		width: 100%;
		height: 88rpx;
		background: linear-gradient(90deg, #358FFF, #496EFD);
		border-radius: 44rpx;
		color: #fff;
		font-size: 32rpx;
		font-weight: 600;
		text-align: center;
		line-height: 88rpx;
	}
}
</style>
