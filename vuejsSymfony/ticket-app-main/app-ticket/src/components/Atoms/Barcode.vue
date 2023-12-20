<template>
  <div class="barcode">
    <img :src="barcodeSrc" alt="Barcode">
  </div>
</template>

<script lang="ts">
export default {
  name: 'BarcodeImg',
  props: {
    /**
     * Image of the barcode
     */
    barcodeSrc: {
      type: String,
      required: false,
    },
  },
};
</script>

<style scoped>
.barcode {
  align-self: center;
  flex-grow: 1;
  margin: 0;
}

.barcode img {
  height: 100px;
  width: 100%;
  object-fit: contain;
}
</style>
