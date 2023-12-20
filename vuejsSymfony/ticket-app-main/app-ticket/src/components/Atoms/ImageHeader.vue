<template>
  <div class="header">
    <img :src="imageSrc" alt="Header Image">
  </div>
</template>

<script lang="ts">
export default {
  name: 'Image-Header',
  props: {
    /**
     * Image of the header
     */
    imageSrc: {
      type: String,
      required: false,
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  flex-direction: column;
  height: 230px;
  width: 100%;
  overflow: hidden;
}

.header img {
  height: 100%;
  width: 100%;
  object-fit: cover;
}
</style>
