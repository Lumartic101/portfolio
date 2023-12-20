<template>
    <div class="body">
      <slot></slot>
    </div>
</template>

<script lang="ts">
export default {
  name: 'BasicBody',
};
</script>

<style scoped>
.body {
  padding: 30px;
}

.body > * {
    margin-top: 20px;
}

.body h2 {
  margin: 0;
}

.body p {
  margin: 0;
  margin-top: 5px;
}
</style>
