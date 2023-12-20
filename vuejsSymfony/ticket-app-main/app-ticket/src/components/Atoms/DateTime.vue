<template>
  <p>{{ formatDate(date) }}</p>
</template>

<script lang="ts">
import { defineComponent } from 'vue';

export default defineComponent ({
  name: 'DateTime',
  props: {
    date: Date,
    timeOnly: Boolean,
    dateOnly: Boolean,
  },
  methods: {
    formatDate(date:Date|undefined) {
      if (date==undefined) {
        return 'N.A.';
      }
      if (this.timeOnly) {
        return new Date(date).toLocaleTimeString('en-NL', { timeStyle: 'short', });
      } else if (this.dateOnly) {
        return new Date(date).toLocaleDateString('en-NL', { month: 'long', day: 'numeric', year: 'numeric', });
      } else {
        return new Date(date).toLocaleDateString('en-NL', { month: 'long', day: 'numeric', year: 'numeric', hour: 'numeric', minute: 'numeric', hour12: false, });
      }
    },
  },
});
</script>
