<template>
    <div class="footer" :class="classes">
      <slot></slot>
    </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';

const props = withDefaults(defineProps<{
  /**
   * add tearline at top of footer
   */
  tearLine?: boolean,
}>(), { tearLine: false });

const classes = computed(() => ({
  'footer--tearline': props.tearLine,
}));
</script>

<style scoped>
.footer {
  position: relative;
  justify-content: space-between;
  padding: 30px;
  z-index: 1;
}

.footer--tearline {
  background-image: url("data:image/svg+xml,%3csvg width='100%30' height='100%25' xmlns='http://www.w3.org/2000/svg'%3e%3crect width='100%25' height='0.1px' fill='none' stroke='%23DEE2EEFF' stroke-width='3' stroke-dasharray='8%2c 13' stroke-dashoffset='45' stroke-linecap='square'/%3e%3c/svg%3e");
}
</style>
