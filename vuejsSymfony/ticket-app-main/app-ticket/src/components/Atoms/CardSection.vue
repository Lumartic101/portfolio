<template>
    <div class="card-section">
      <slot></slot>
    </div>
</template>

<script lang="ts">
export default {
  name: 'Card-Section',
};
</script>

<style scoped>
.card-section {
  background-color: #f5f6f7;
  border-radius: 10px;
  padding: 15px;
}
</style>
