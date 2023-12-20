<template>
  <div class="card">
    <div class="card-content">
      <div class="icon">
        <img :src="path" :alt="name">
      </div>
      <div class="text">
        <h1>{{ heading }}</h1>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
defineProps<{
  name: string,
  path: string,
  heading: string,
}>();
</script>

<style scoped>
.card {
  width: 264px;
  height: 73px;
  background-color: #ffffff;
  border-radius: 6px;
  box-shadow: 0px 20px 20px rgba(0,0,0,0.02), 0px 10px 10px rgba(0,0,0,0.05), 0px 5px 5px rgba(0,0,0,0.1);
  margin-bottom: 20px;
  overflow: hidden;
}

.card-content {
  display: flex;
  align-items: center;
}

.icon {
  padding: 20px;
}

img {
  width: 34px;
  height: 34px;
}

h1 {
  margin: 0;
  font-size: 14px;
  font-weight: bold;
  font-family: Gotham Rounded,sans-serif;
  color: #030303;
  text-decoration: none solid rgb(3, 3, 3);
}
</style>
