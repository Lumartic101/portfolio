<template>
  <div class="container">
    <b-button
      class="close button"
      :label="'Close screen'"
      :size="'wide'"
      :primary="false"
      @click="$router.push({ name: 'tickets' })"
    />
    <b-button
      class="download button"
      :label="'Download'"
      :size="'medium'"
      :primary="true"
    />
    <b-button
      class="wallet button"
      :label="'Add to wallet'"
      :size="'medium'"
      :primary="true"
    />
  </div>
  <ticket
    :barcode-src="barcodeSrc"
    :image-src="imageSrc"
    :num-people="numPeople"
    :country="country"
    :title="title"
    :start-date="startDate"
    :end-date="endDate"
    :ticket-holder="ticketHolder"
    :ticket-type="ticketType"
    :order-id="orderId"
  />
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import EventTicket from '../Organisms/EventTicket.vue';
import BasicButton from '../Atoms/BasicButton.vue';

export default defineComponent ({
  components: {
    'ticket': EventTicket,
    'b-button': BasicButton,
  },
  name: 'EventTicket',
  props: {
    imageSrc: String,
    title: String,
    country: String,
    numPeople: String,
    startDate: Date,
    endDate: Date,
    ticketHolder: String,
    ticketType: String,
    orderId: Number,
    barcodeSrc: String,
  },
  methods: {
    handleClick() {
      this.$router.push({ name: 'tickets' });
    },
  }
});
</script>

<style scoped>
.container {
  max-width: 21.875rem;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 0px 0px;
  grid-template-areas:
    "close close close"
    "download wallet wallet";
}

.button {
  height: 3rem;
  width: auto;
  margin: 5px 0;
  font-size: medium;
}
.close {
  grid-area: close;
}
.download {
  grid-area: download;
  margin: 5px 10px 15px 0;
}
.wallet {
  grid-area: wallet;
  margin: 5px 0 15px 10px;
}
</style>
