<template>
  <div class="time-container">
    <datetime :date="startDate" :time-only="true"></datetime>
    <p class="minus">-</p>
    <datetime :date="endDate" :time-only="true"></datetime>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import DateTime from '../Atoms/DateTime.vue';

export default defineComponent ({
  components: {
    'datetime': DateTime,
  },
  name: 'FromTillTime',
  props: {
    startDate: Date,
    endDate: Date,
  },
});
</script>

<style scoped>
.time-container {
  display: flex;
}

.minus {
  margin: auto 3px;
}
</style>
