<template>
  <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="100" height="100">
    <circle cx="12" cy="12" r="10" fill="#3DDC97"/>
    <path d="M9.7 13.5l-2.4-2.4-1.4 1.4 3.8 3.8 8.6-8.6-1.4-1.4z" fill="#fff"/>
  </svg>
</template>

<script>
export default {
  name: 'Icon-component',
  props: {
    name: {
      type: String,
      required: true,
    },
    size: {
      type: Number,
      default: 24,
    },
  },
};
</script>

<style scoped>
/* Add any custom styles for your SVG icons here */
</style>
