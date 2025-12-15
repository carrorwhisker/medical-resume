<script>
	export default {
		onLaunch: function() {
			this.checkUpdateVersion();
			console.log('App Launch')
			this.$store.dispatch("initData")
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
			// 版本更新
			checkUpdateVersion() {
				const updateManager = wx.getUpdateManager()
				updateManager.onCheckForUpdate(function(res) {
					// 请求完新版本信息的回调
					if (res.hasUpdate) {
						// 新版本下载成功
						updateManager.onUpdateReady(function() {
							wx.showModal({
								title: '更新提示',
								content: '新版本已经准备好，请您重启应用，以确保正常使用。',
								success: function(res) {
									if (res.confirm) {
										// 新的版本已经下载好，调用 applyUpdate 应用新版本并重启
										updateManager.applyUpdate()
									}
								}
							})
						})
						// 新版本下载失败
						updateManager.onUpdateFailed(function() {
							wx.showModal({
								title: '更新提示',
								content: '请您删除当前小程序，到微信 “发现-小程序” 页，重新搜索打开呦~'
							})
						})
					}
				})
			},
		}
	}
</script>
<style>
	view {
		box-sizing: border-box;
		margin: 0;
		padding: 0;
		line-height: calc(1em+"8rpx");
	}

	::-webkit-scrollbar {
		display: none !important;
	}

	page,
	uni-page-body {
		width: 750rpx;
		overflow-x: hidden;
		box-sizing: border-box;
	}

	page {
		background: #F6F6F6;
	}
</style>
<style lang="scss">
	/*每个页面公共css */
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";

	.bnt_big {
		height: 100rpx;
		background: linear-gradient(90deg, #358FFF, #496EFD, #697CFF);
		border-radius: 50rpx;

		font-weight: 400;
		font-size: 36rpx;
		color: #FFFFFF !important;
		line-height: 100rpx;
		text-align: center;
		margin: 0 auto;
	}

	.bnt_big_ {
		height: 100rpx;
		background: #FFF;
		border-radius: 50rpx;
		border: 2rpx solid #358FFF;
		font-weight: 400;
		font-size: 36rpx;
		color: #358FFF;
		line-height: 96rpx;
		text-align: center;
		margin: 0 auto;
	}

	.box {
		width: 694rpx;
		background: #FFFFFF;
		border-radius: 25rpx;
		margin: 24rpx auto;
	}

	.bottom_box {
		width: 750rpx;
		height: 144rpx;

		&>view {
			width: 750rpx;
			height: 144rpx;
			background: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			position: fixed;
			left: 0;
			bottom: 0;
			z-index: 10;
		}
	}

	.flex1 {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.line {
		height: 2rpx;
		background: #F5F5F5;
		margin: 0 auto;
		margin-top: 20rpx;
	}

	.must {
		position: relative;
		padding-right: 18rpx;

		&::after {
			content: "*";
			color: #FF1F1F;
			font-size: 32rpx;
			display: block;
			position: absolute;
			right: 0rpx;
			top: 0rpx;
		}
	}

	.ph {
		font-weight: 400;
		font-size: 30rpx;
		color: #999999;
	}

	.input_box {
		height: 96rpx;
		background: #F7F7F7;
		border-radius: 48rpx;
		display: flex;
		align-items: center;
		padding: 0rpx 50rpx;

		&>input {
			flex: 1;
		}
	}

	.input_box_ {
		height: 300rpx;
		background: #F7F7F7;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		padding: 20rpx 30rpx;

		&>textarea {
			flex: 1;
			height: 100%;
		}
	}

	.ban {
		background: #E5E5E5 !important;
	}

	.search_box {
		flex: 1;
		height: 64rpx;
		background: #F6F6F6;
		border-radius: 32rpx;
		border: 2rpx solid #AEAEAE;
		padding: 0rpx 20rpx;
		display: flex;
		align-items: center;

		&>image {
			width: 30rpx;
			height: 31rpx;
			margin-right: 20rpx;
		}

		&>input {
			flex: 1;
		}
	}

	.scroll {
		flex: 1;
		overflow: hidden;
		padding: 20rpx 0rpx;
	}

	.no_more {
		&:before {
			content: "没有更多了哟~";
			font-weight: 400;
			font-size: 26rpx;
			color: #8C8C8C;
			line-height: 36rpx;
		}

		margin-top: 20rpx;
		text-align: center;
	}

	.acc_item {
		display: flex;
		align-items: center;

		.acc_avt {
			width: 202rpx;
			height: 114rpx;
			border-radius: 20rpx;
		}

		.acc_content {
			padding: 0rpx 20rpx;
			flex: 1;

			.acc_name {
				width: 240rpx;
				font-weight: 400;
				font-size: 30rpx;
				color: #000000;
				margin-bottom: 24rpx;
				overflow: hidden;
				text-overflow: ellipsis;
			}

			.acc_time {
				font-weight: 400;
				font-size: 22rpx;
				color: #A9A9A9;
			}
		}

		.acc_icon {
			width: 48rpx;
			height: 48rpx;
		}

		.acc_btn {
			box-sizing: border-box;
			height: 54rpx;
			padding: 0rpx 20rpx;
			font-weight: 400;
			font-size: 24rpx;
			line-height: 50rpx;
			white-space: nowrap;
		}
	}

	.ban {
		filter: grayscale(100%);
	}

	.btn_tb {
		background: linear-gradient(90deg, #EFD297, #DFB76F);
		border-radius: 10rpx;
		color: #694103;
	}


	.card {
		min-height: 350rpx;
		background-image: url("@/static/bg/card_bg.png");
		background-size: 100% 100%;
		padding: 40rpx 30rpx 20rpx 30rpx;
		display: flex;
		color: #FFFFFF;
		margin: 0 auto;

		.avt {
			width: 186rpx;
			height: 262rpx;
			border-radius: 20rpx;
			position: relative;

			&>image {
				width: 186rpx;
				height: 262rpx;
				border-radius: 20rpx;
			}

			.avt_btn {
				position: absolute;
				left: 16rpx;
				bottom: 6rpx;
				width: 152rpx;
				height: 44rpx;
				border-radius: 20rpx;
				border: 2px solid #FFFFFF;
				background: rgba(255, 255, 255, .7);
				border-radius: 20rpx;


				font-weight: 400;
				font-size: 22rpx;
				color: #000000;
				line-height: 40rpx;
				text-align: center;
			}
		}

		.desc {
			flex: 1;
			margin-left: 24rpx;

			.name {
				font-weight: 400;
				font-size: 36rpx;
				margin-bottom: 20rpx;
				display: flex;
				align-items: center;

				&>image {
					width: 27rpx;
					height: 25rpx;
					margin-left: 11rpx;
				}
			}

			.text1 {
				font-weight: 400;
				font-size: 22rpx;
				margin-bottom: 16rpx;
			}

			.text2 {
				display: flex;
				align-items: center;
				flex-shrink: 0;
				margin-bottom: 12rpx;

				&>image {
					width: 20rpx;
					height: 20rpx;
					margin-right: 6rpx;
				}

				&>text {
					flex: 1;
					font-weight: 400;
					font-size: 21rpx;
				}
			}
		}
	}
</style>