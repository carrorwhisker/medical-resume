<template>
	<view>
		<u-picker :show="show&&cshow" ref="uPicker" :columns="columns" @confirm="confirm" @close="close"
			@change="changeHandler" :closeOnClickOverlay="closeOnClickOverlay" @cancel="cancel"></u-picker>
	</view>
</template>

<script>
	import {
		province,
		city,
		county,
		arrrr
	} from './city.js'
	export default {
		name: "CwiycCitypicker",
		props: ['show', 'confirmArea', 'closeOnClickOverlay'],
		data() {
			return {
				loading: false,
				cshow: true,
				columns: [
					province,
					city[0],
					county[0][0]
				],
				secondColumnData: city,
				thirdColumnData: county,
			}
		},
		methods: {
			compute(index) {
				let sum = 0
				for (let i = 0; i < index; i++) {
					sum += arrrr[i]
				}
				return sum
			},
			secondCompute(index) {

			},
			changeHandler(e) {
				const {
					columnIndex,
					value,
					values, // values为当前变化列的数组内容
					index,
					indexs,
					// 微信小程序无法将picker实例传出来，只能通过ref操作
					picker = this.$refs.uPicker
				} = e
				// console.log(e)
				// 当第一列值发生变化时，变化第二列(后一列)对应的选项
				if (columnIndex === 0) {
					// picker为选择器this实例，变化第二列对应的选项
					this.first = this.compute(index)
					picker.setColumnValues(1, this.secondColumnData[index])
					picker.setColumnValues(2, (this.thirdColumnData[index])[0])
				}
				if (columnIndex === 1) {
					// picker为选择器this实例，变化第二列对应的选项
					// console.log(this.first)
					picker.setColumnValues(2, this.thirdColumnData[indexs[0]][index])
				}
			},
			// 回调参数为包含columnIndex、value、values
			confirm(e) {
				this.$emit("confirmArea", e.value);
			},
			close() {
				this.cshow = false
			},
			cancel(){
				this.$emit("cancel");
			},
		}
	}
</script>

<style>

</style>
