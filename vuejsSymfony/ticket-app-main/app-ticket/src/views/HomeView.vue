<script setup lang="ts">
import BackgroundHeaderComponent from '@/components/Atoms/BackgroundHeaderComponent.vue';
import MyOrderLinkAccess from '@/components/Molecules/MyOrderLinkAccess.vue';
</script>

<template>
  <section>
    <section>
      <header>
        <BackgroundHeaderComponent/>
      </header>
    </section>
    <div class="order-access">
      <MyOrderLinkAccess/>
    </div>
  </section>
</template>

<style scoped>
.order-access {
  margin-top: 5rem;
}
</style>
