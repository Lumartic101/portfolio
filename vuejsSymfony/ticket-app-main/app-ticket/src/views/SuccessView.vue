<script setup lang="ts">
import BackgroundHeaderComponent from '@/components/Atoms/BackgroundHeaderComponent.vue';
import SuccessIcon from '@/components/Atoms/SuccessIconComponent.vue';
import {onMounted} from 'vue';
import router from '@/router';

onMounted(() => {
  setTimeout(() => {
    router.push('/ticket-menu');
  }, 5 * 1000);
});
</script>

<template>
  <section>
    <header>
      <BackgroundHeaderComponent/>
    </header>
    <div class="wrapper">
      <div class="success-icon">
        <SuccessIcon :name="'success'"/>
      </div>
      <div class="approved">
        <p>
          <span class="approved-text">Approved</span><span class="approved-dot">.</span>
        </p>
      </div>
      <div class="closing-text">
        <p>
          You may now close this screen and continue on the other one.
        </p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.wrapper {
  text-align: center;
}

.success-icon {
  margin-top:3rem;
}

.approved {
  font-size: 40px;
  font-weight: bold;
  font-family: "Gotham Rounded", sans-serif;
}

.approved-text, .closing-text {
  color: black;
}

.approved-dot {
  color: blue;
}

.closing-text {
  margin-left: 1rem;
  margin-right: 1rem;
  font-size: 16px;
  text-align: center;
}
</style>
