<template>
  <div class="input-field">
    <input :type="type" :value="value" @input="$emit('inputValue', $event.target.value)" :placeholder="placeholder" :disabled="disabled">
  </div>
</template>

<script>
export default {
  name: 'InputField',
  props: {
    value: {
      type: String,
      default: '',
    },
    type: {
      type: String,
      default: '',
    },
    placeholder: {
      type: String,
      default: '',
    },
    disabled: {
      type: Boolean,
      default: false,
    },
  },
};
</script>

<style scoped>
.input-field {
  display: inline-block;
  overflow: hidden;
}

input {
  width: 223px;
  height: 47px;
  border-radius: 100px;
  background-color: #ffffff;
  background-size: cover;
  font-size: 14px;
  color: #121212;
  text-decoration: none solid rgb(18, 18, 18);
  box-shadow: inset 0px 1px 2px rgba(0,0,0,0.2);
}
</style>
