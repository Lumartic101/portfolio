<template>
    <ul>
      <slot></slot>
    </ul>
</template>

<script lang="ts">
export default {
  name: 'ItemList',
};
</script>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}
</style>
