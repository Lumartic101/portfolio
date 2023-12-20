<template>
  <div class="wrapper">
    <div class="myOrderText">
      <p>
        <span class="myOrderTextWordMyOrder">My order</span><span class="myOrderTextWordDot">.</span>
      </p>
    </div>
    <div class="inputField">
      <input class="input" :type="'email'" v-model="value" :placeholder="'Enter Email'"/>
    </div>
    <div class="button">
      <s-button :label="'Send access link'" :primary='true' @click="submitForm"></s-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import axios from 'axios';
import Button from '@/components/Atoms/BasicButton.vue';

export default defineComponent({
  components: {
    's-button': Button,
  },
  data() {
    return {
      value: ''
    };
  },
  methods: {
    submitForm() {
      const emailRegex = /\S+@\S+\.\S+/;
      if (emailRegex.test(this.value)) {
        axios
          .post('http://127.0.0.1:8000/login?email=' + this.value, null, {
            withCredentials: true,
          })
          .then(() => {
            this.$router.push('/success');
          })
          .catch(() => {
            // Handle the error, show error message, etc.
          });
      } else {
        alert('Please provide a valid email');
      }
    },
  },
});
</script>

<style scoped>
.wrapper {
  text-align: center;
}

p {
  color:black;
}

.inputField {
  margin-top: 1rem;
  margin-bottom: 1rem;
  padding: 0;
}

.myOrderText {
  font-size: 40px;
  font-weight: bold;
  font-family: "Assistant", sans-serif;
}

.myOrderTextWordMyOrder {
  color: black;
}

.myOrderTextWordDot {
  color: blue;
}

.button-class {
  font-size: 18px;
}

.inputField {
  display: inline-block;
  border-radius: 100px;
  overflow: hidden;
}

input {
  width: 275px;
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
