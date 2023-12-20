<template>
  <div class="card">
    <div class="header">
      <div class="header_left">
        <h1>{{ header }}</h1>
        <h2>{{ subheader }}</h2>
      </div>
      <div class="header_right">
        <span class="price">
          €{{formatPrice(price)}}
        </span>
      </div>
    </div>
    <div class="content">
      <div class="left">
        <IconWithCircle
          :icon="'share'"
          @click="$router.push({ name: 'social' })"
         />
        <span>or</span>
      </div>
      <div class="right">
        <InputField class="inputEmail" :placeholder="'Fill in e-mail address'" v-on:inputValue="inputValue"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import IconWithCircle from '@/components/Atoms/IconWithCircle.vue';
import InputField from '@/components/Atoms/InputField.vue';

defineProps<{
  header: string,
  subheader: string,
  price: string,
  inputValue: string,
}>();

const formatPrice = (price) => {
  return price.toString().replace(/\./g, ',');
};
</script>

<style scoped>
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

.card {
  width: 348px;
  height: 144px;
  border-radius: 6px;
  fill: #ffffff;
  box-shadow: 0px 20px 20px rgba(0,0,0,0.02), 0px 10px 10px rgba(0,0,0,0.05), 0px 5px 5px rgba(0,0,0,0.1);
  padding-left: 21px;
  padding-top: 14px;
  padding-right: 21px;
  margin: 16px;
}

.header {
  margin-bottom: 7px;
  display: flex;
  align-items: center;
}

.content {
  display: flex;
  align-items: center;
}

.left {
  display: flex;
  align-items: center;
  margin-right: 16px;
}

.left img {
  width: 24px;
  height: 24px;
  margin-right: 8px;
}

.right {
  flex-grow: 1;
  padding-top: 9px;
}
.input {
  width: 223px;
  padding-top: 7px;
}

.header_right {
  margin-left: auto;
}
</style>
