<template>
  <div class="card">
    <div class="card-content">
      <div class="icon">
        <IconComponent
          :name="iconName"
          size="35"
        />
      </div>
      <div class="text">
        <h1>{{ heading }}</h1>
        <h2>{{ subheading }}</h2>
      </div>
      <div class="button">
        <BasicButton :label="'Reset'" :background-color="'#0074e8'" :size="'medium'"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import IconComponent from '@/components/Atoms/IconComponent.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';

defineProps<{
  iconName: string,
  heading: string,
  subheading: string,
}>();
</script>

<style scoped>
.card {
  width: 348px;
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

h1 {
  margin: 0;
  font-size: 14px;
  font-weight: bold;
  font-family: Gotham Rounded,sans-serif;
  color: #030303;
  text-decoration: none solid rgb(3, 3, 3);
}

h2 {
  margin: 0;
  font-size: 14px;
  color: #030303;
  text-decoration: none solid rgb(3, 3, 3);
}

.button {
  margin-left: auto;
  margin-right: 15px;
}
</style>
