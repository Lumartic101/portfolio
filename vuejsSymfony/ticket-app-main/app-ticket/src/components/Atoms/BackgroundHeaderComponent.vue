<template>
  <header>
    <div class="content-wrapper">
      <div class="wrapper">
        <div class="logo"></div>
        <div class="logo-text">CM.</div>
      </div>
      <div class="blue-line"></div>
    </div>
  </header>
</template>

<script lang="ts" setup>
defineEmits<{
  (event: 'baseHeader'): void;
}>();
</script>

<style scoped>
.content-wrapper {
  border-bottom: 10px solid #1ea7fd;
}

.wrapper {
  height: 200px;
  width: 100%;
  background-color:#303030;
  position:relative;
  text-align: center;
}

.logo {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  border: 3px solid white;
  border-radius: 90%;
  width: 100px;
  height: 100px;
}

.logo-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size:20px;
  color:white;
}
</style>
