<template>
	<input type="text" v-model="inputValue" @compositionstart="handleCompositionStart"
		@compositionend="handleCompositionEnd" @input="handleInput" @paste="handlePaste" :placeholder="placeholder" placeholder-class="ph" />
</template>

<script>
	export default {
		props: {
			value: [String],
			placeholder: {
				type: String,
				default: ""
			},
		},
		data() {
			return {
				inputValue: this.value,
				isComposing: false
			}
		},
		mounted() {

		},
		watch: {
			value: {
				handler(val) {
					this.inputValue = val;
				},
				deep: true,
				immediate: true,
			},
			inputValue: {
				handler(val) {
					this.$emit("input", val)
				},
				deep: true,
			},
		},
		methods: {
			handleCompositionStart() {
				this.isComposing = true;
			},
			handleCompositionEnd(e) {
				this.isComposing = false;
				this.handleInput(e);
			},
			handleInput(e) {
				if (!this.isComposing) {
					this.$nextTick(() => {
						const value = e.detail?.value || e.target?.value || '';
						this.inputValue = this.filterEmoji(value);
					});
				}
			},
			handlePaste(e) {
				e.preventDefault();
				const text = (e.clipboardData || window.clipboardData).getData('text');
				const cleanText = this.filterEmoji(text);
				document.execCommand('insertText', false, cleanText);
			},
			filterEmoji(text) {
				const emojiRegex =
					/[\uD83C[\uDF00-\uDFFF]|\uD83D[\uDC00-\uDE4F]|\uD83D[\uDE80-\uDEFF]|[\u2600-\u26FF]|[\u2700-\u27BF]|\uD83E[\uDD00-\uDDFF]/g;
				return text.replace(emojiRegex, '');
			}
		},
	}
</script>

<style lang="scss" scoped>

</style>