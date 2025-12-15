<template>
	<view class="details">
		<view class="top_back" :style="{backgroundColor:scrollTop>150?'#FFF':''}">
			<r-top></r-top>
			<view class="back_box">
				<image v-if="scrollTop>150" src="/static/icon/back.png" mode="widthFix"
					style="width: 30rpx;height: 30rpx;" @click="$back(1,'/pages/meeting/index')"></image>
				<image v-else src="/static/icon/back_.png" mode="widthFix" style="width: 17rpx;height: 31rpx;"
					@click="$back(1,'/pages/meeting/index')"></image>
			</view>
		</view>
		<view class="img_box">
			<image :src="info.meeting_img" mode="widthFix"></image>
		</view>
		<view class="content">
			<view class="title">{{info.meeting_name}}</view>
			<view class="line"></view>
			<view class="title" style="margin-top: 40rpx;">
				<text>时间</text>
				<image src="/static/meeting/date.png" mode="widthFix" style="width: 35rpx;height: 31rpx;"></image>
			</view>
			<view class="text">
				{{info.start_time}} 至 {{info.end_time}}
			</view>
			<view class="title" style="margin-top: 40rpx;">
				<text>地点</text>
				<image src="/static/meeting/address.png" mode="widthFix" style="width: 24rpx;height: 29rpx;"></image>
			</view>
			<view class="text">
				{{info.meeting_address}}
			</view>

			<view class="poster_box">
				<image :src="info.gw_banner" mode="aspectFill"></image>
				<!-- <view class="copy_btn" @click="$copy(info.gw_url)">打开</view> -->
         <!-- zd新增：打开外部 H5页面  -->
        <button class="copy_btn" @click="openWebview">打开</button>
			</view>

			<view class="title" style="margin-top: 40rpx;">会议详情</view>
			<!-- v-html="info.meeting_content" -->
			<view class="content_text">
				<!-- <rich-text :nodes="nodes" @itemclick="handleRichTextClick"></rich-text> -->
				<view v-for="(k,i) of nodes" :key="i">
					<rich-text style="margin: 0;" :nodes="k.text" v-if="k.type=='text'"></rich-text>
					<image :src="k.src" v-if="k.type=='node'" mode="widthFix" style="width: 100%;height: auto;" show-menu-by-longpress></image>
				</view>
			</view>
		</view>

		<view class="bottom_box">
			<view style="justify-content: center;padding: 0rpx 30rpx;">
				<view class="share_box" style="margin-right: 30rpx;position: relative;">
					<button open-type="share"
						style="width:100%;height: 100%;opacity: 0;position: absolute;top: 0;left: 0;"></button>
					<image src="/static/icon/share.png" mode="widthFix"></image>
					<text>分享</text>
				</view>
				<view class="price" v-if="info.is_price_status==1"> <text>¥</text> <text>{{info.price}}</text> </view>
				<view class="bnt_big" style="flex:1;margin: 0;" :class="info.is_sign_up==1?'ban':''"
					@click="handleApply">立即报名</view>
			</view>
		</view>
		<view style="width: 750rpx;overflow-x: scroll;">
			<metting-dome ref="cardDome"></metting-dome>
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
				info: {},
				id: "",
				scrollTop: 0,
				shareInfo: {
					path: "",
					title: "",
				},
				nodes: [],
			}
		},
		onLoad(options) {
      this.id = options.id;
       // zd新增：start
      this.info = options ? Object.keys(options).reduce((decoded, key) => {
          try {
            decoded[key] = decodeURIComponent(options[key]);
          } catch (e) {
            console.error(`参数${key}解码失败`, e);
            decoded[key] = options[key];
          }
          return decoded;
        }, {}) : {};
      this.parseRichText(decodeURIComponent(options.meeting_content));
       // zd新增：end
		},
		onShow() {
			this.loadInfo()
		},
		onShareAppMessage() {
      console.log(JSON.stringify(this.shareInfo))
			return this.shareInfo
		},
		onShareTimeline() {
      // zd新增：start
      const queryParam = Object.keys(this.info)
          .map(key => `${key}=${encodeURIComponent(this.info[key])}`)
          .join('&');
      return {
          title: this.info.meeting_name,
          query: queryParam, // 可选
          imageUrl: this.info.meeting_img // 可选
        }
        // zd新增：end
		},
		onPageScroll(e) {
			this.scrollTop = e.scrollTop;
		},
		methods: {
      // zd新增：打开外部 H5页面 ======start
      openWebview() {
        if (this.info.gw_url) {
          //07.24 修改为 encodeURIComponent(JSON.stringify(this.shareInfo))
          uni.navigateTo({
            url: '/pages/webview/index?url='+encodeURIComponent(this.info.gw_url)+
            '&title='+encodeURIComponent(this.info.meeting_name)+
            '&shareInfo='+encodeURIComponent(JSON.stringify(this.shareInfo))+'&r=2025'
          })
        }
      },
       // zd新增：打开外部 H5页面 ======end
			parseRichText(html) {
				// 使用正则匹配所有 img 标签
				const imgReg = /<img[^>]+src=['"]([^'"]+)['"][^>]*>/g;
				let nodes = [];
				let lastIndex = 0;
				html.replace(imgReg, (match, src, index) => {
					// 处理 img 标签前的文本
					if (index > lastIndex) {
						nodes.push({
							type: 'text',
							text: html.slice(lastIndex, index)
						});
					}
					// 添加可交互的图片节点
					nodes.push({
						type: 'node',
						name: 'image',
						src:src,
						attrs: {
							class: 'rich-img',
							src: src,
							'data-src': src,
							mode: 'widthFix',
							style: 'max-width: 100%;height: auto;'
						},
						children: [],
						events: {
							longpress: 'handleLongPressImage' // 绑定长按事件
						}
					});
					lastIndex = index + match.length;
					return match;
				});

				// 处理剩余的文本
				if (lastIndex < html.length) {
					nodes.push({
						type: 'text',
						text: html.slice(lastIndex)
					});
				}
				this.nodes = nodes;
			},
			handleApply() {
				if (this.info.is_sign_up == 1) {
					this.$msg("已提交报名！")
					return;
				}
				uni.navigateTo({
					url: `/pages/meeting/apply?id=${this.info.id}`
				})
			},
			async loadInfo() {
				const res = await this.$api({
					url: "getMeetingDetail",
					data: {
						id: this.id
					}
				})
				if (res.code == 200) {
					// 兼容驼峰/下划线
					const data = res.data || {}
					data.meeting_img = data.meeting_img || data.meetingImg
					data.meeting_name = data.meeting_name || data.meetingName
					data.qy_name = data.qy_name || data.qyName
					data.start_time = data.start_time || data.startTime
					data.end_time = data.end_time || data.endTime
					data.meeting_address = data.meeting_address || data.meetingAddress
					data.gw_banner = data.gw_banner || data.gwBanner
					data.gw_url = data.gw_url || data.gwUrl
					data.meeting_content = data.meeting_content || data.meetingContent || ''

					// 时间戳转日期
					if (typeof data.start_time === 'number') {
						const d = new Date(data.start_time * 1000)
						data.start_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`
					}
					if (typeof data.end_time === 'number') {
						const d = new Date(data.end_time * 1000)
						data.end_time = `${d.getFullYear()}-${('0' + (d.getMonth() + 1)).slice(-2)}-${('0' + d.getDate()).slice(-2)}`
					}
					// 富文本图片样式
					if (data.meeting_content) {
						data.meeting_content = data.meeting_content.replace(/\<img/gi,
							'<img style="max-width:100%;height:auto" ')
					}

					this.info = data
					this.parseRichText(data.meeting_content || '');
					if (!this.shareInfo.imageUrl) {
						let url = await this.$refs.cardDome.getImg(data)
						this.shareInfo = {
							title: data.meeting_name,
							path: "/pages/meeting/details?id=" + data.id,
							imageUrl: url
						}
					}
				}
			},
		}
	}
</script>

<style lang="scss" scoped>
	.details {
		.price {
			margin: 0rpx 30rpx;
			display: flex;
			align-items: flex-end;

			font-weight: 600;
			font-size: 48rpx;
			color: #FE4324;

			&>text:nth-child(1) {
				font-size: 26rpx;
				position: relative;
				top: -8rpx;
			}
		}

		.top_back {
			width: 750rpx;
			position: fixed;
			top: 0;
			left: 0;
			z-index: 10;
		}

		.back_box {
			height: 88rpx;
			display: flex;
			align-items: center;
			padding-left: 20rpx;
		}

		.img_box {
			width: 750rpx;
			height: 370rpx;
			overflow: hidden;

			&>image {
				width: 750rpx;
				height: 400rpx;
			}
		}

		.content {
			width: 750rpx;
			background: #FFFFFF;
			border-radius: 40rpx 40rpx 0rpx 0rpx;
			padding: 45rpx 34rpx;
			position: relative;
			z-index: 2;

			.title {
				font-weight: 600;
				font-size: 36rpx;
				color: #000000;
				line-height: 55rpx;
				display: flex;
				align-items: center;

				&>image {
					margin-left: 16rpx;
				}
			}

			.text {
				font-weight: 400;
				font-size: 30rpx;
				color: #8C8C8C;
				line-height: 36rpx;
				margin-top: 10rpx;
			}

			.content_text {
				word-wrap: break-word;
				white-space: normal;
				font-weight: 400;
				font-size: 34rpx;
				color: #000000;
				line-height: 55rpx;
				margin-top: 30rpx;

				::v-deep img {
					width: 100%;
				}
				
				::v-deep p{
					margin: 0;
				}
			}

			.poster_box {
				margin-top: 50rpx;
				height: 160rpx;
				background: #F6F6F6;
				border-radius: 25rpx;
				display: flex;
				align-items: center;

				&>image {
					width: 480rpx;
					height: 164rpx;
					border-radius: 25rpx;
					margin-right: 28rpx;
				}

				.copy_btn {
					width: 143rpx;
					height: 70rpx;
					background: linear-gradient(90deg, #6FF0D8, #43DFAB, #5FEE91);
					box-shadow: 0rpx 0rpx 13rpx 0rpx #5DED92;
					border-radius: 35rpx;

					font-weight: 400;
					font-size: 30rpx;
					color: #FFFEFE;
					text-align: center;
					line-height: 70rpx;
				}
			}
		}


		.share_box {
			font-weight: 400;
			font-size: 24rpx;
			color: #000000;
			text-align: center;

			&>image {
				width: 30rpx;
				height: 30rpx;
				display: block;
				margin: 0 auto;
				margin-bottom: 14rpx;
			}
		}
	}
</style>