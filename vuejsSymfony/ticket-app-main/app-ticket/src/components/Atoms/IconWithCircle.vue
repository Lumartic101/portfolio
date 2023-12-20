<template>
  <div class="icon-container">
    <div class="circle">
      <IconComponent :name="icon"/>
    </div>
  </div>
</template>

<script lang="ts" setup>
import IconComponent from '@/components/Atoms/IconComponent.vue';

defineProps<{
  icon: string,
}>();
</script>

<style scoped>
.icon-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 64px;
  height: 64px;
}

.circle {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 45px;
  height: 45px;
  border-radius: 50%;
  background-color: #456fd1;
}

.circle IconComponent {
  width: 21px;
  height: 21px;
}
</style>
