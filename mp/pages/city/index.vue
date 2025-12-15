<template>
	<view class="city_page">
		<view class="city">
			<!-- style="background-color: #F5F5F5;" -->
			<scroll-view class="list_box border" scroll-y :scroll-into-view="pid">
				<view :id="'province'+i" class="item" :class="index1==i?'active':''" v-for="(k,i) of province" :key="i"
					@click="handleSelect(i,1)">{{k.name}}</view>
			</scroll-view>
			<!-- style="background-color: #F9F9F9;" -->
			<scroll-view class="list_box border" scroll-y :scroll-into-view="cid">
				<view :id="'city'+i" class="item" :class="index2==i?'active':''" v-for="(k,i) of city" :key="i"
					@click="handleSelect(i,2)">{{k.name}}</view>
			</scroll-view>
			<!-- style="background-color: #FFFFFF;" -->
			<scroll-view class="list_box" scroll-y :scroll-into-view="aid">
				<view :id="'area'+i" class="item" :class="index3==i?'active':''" v-for="(k,i) of area" :key="i"
					@click="handleSelect(i,3)">{{k.name}}</view>
			</scroll-view>
		</view>
		<view class="city_bottom">
			<view class="bnt_big" style="width: 690rpx;" @click="handleSubmit">确 认</view>
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
				province: require("@/common/city.json"),
				index1: 0,
				index2: 0,
				index3: 0,
                pid:"",//滚动到省
				cid:"",//滚动到市
				aid:"",//滚动到区
			}
		},
		computed: {
			city() {
				return this.province[this.index1].children || [];
			},
			area() {
				return this.city[this.index2].children || [];
			},
		},
		onLoad(options) {
			const address = options.address;
			if (address) {
				this.initCity(address)
			}
		},
		methods: {
			initCity(str = "") {
				let arr = str.split("/");
				arr.forEach((k, i) => {
					if (i === 0) { //省
						this.province.forEach((item, index) => {
							if (item.name == k) {
								this.index1 = index;
								this.pid="province"+index;
							}
						})
					}
					if (i === 1) { //市
						this.city.forEach((item, index) => {
							if (item.name == k) {
								this.index2 = index;
								this.cid="city"+index;
							}
						})
					}
					if (i === 2) { //区
						this.area.forEach((item, index) => {
							if (item.name == k) {
								this.index3 = index;
								this.aid="area"+index;
							}
						})
					}
				})
			},
			handleSubmit() {
				let p = this.province[this.index1].name || '';
				let c = this.city[this.index2].name || '';
				let a = this.area[this.index3].name || '';
				let res = [p, c, a];
				uni.navigateBack({
					success: () => {
						uni.$emit("selectCity", res)
					}
				})
			},
			handleSelect(index, type) {
				console.log(index);
				if (type == 1) { //选择省
					this.index1 = index;
					this.index2 = 0;
					this.index3 = 0;
				} else if (type == 2) { //选择市
					this.index2 = index;
					this.index3 = 0;
				} else { //选择区
					this.index3 = index;
				}
			}
		}
	}
</script>
<style>
	page,
	uni-page-body {
		height: 100%;
		background-color: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.city_page {
		height: 100%;
		display: flex;
		flex-direction: column;

		.city_bottom {
			padding: 20rpx;
			padding-bottom: constant(safe-area-inset-bottom);
			/* 兼容 iOS 设备 */
			padding-bottom: env(safe-area-inset-bottom);
			/* 兼容 iPhone X 及以上设备 */
		}
	}

	.city {
		flex: 1;
		display: flex;
		overflow: hidden;

		.border {
			border-right: 2rpx solid #358FFF;
		}

		.list_box {
			width: 33.33%;
			height: 100%;
			overflow: hidden;

			.item {
				height: 80rpx;
				width: 100%;
				padding: 0rpx 20rpx;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				font-size: 36rpx;
				line-height: 80rpx;
				color: #333;
			}

			.active {
				background: #358FFF;
				color: #FFFFFF;
			}
		}
	}
</style>