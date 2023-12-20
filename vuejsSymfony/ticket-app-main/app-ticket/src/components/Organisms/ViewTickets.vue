<template>
  <div class="top">
    <div class="back" @click="$router.push({ name: 'ticket-menu' })">
      <IconComponent
          name="back"
          size="61"
      />
    </div>
    <BasicCard
        class="basic_card"
        :label="ticketList[0]?.event.eventName"
        :secLabel="formattedStartDate"
        :price="formatPrice(totalPrice)"
        :totalTickets="ticketList.length"
        :tekstInIcon="true"
        :tekstNextToIcon="true"
        :showPrice="false"
        :buttonClass="false"
        :showPriceBig="true"
        :ticketIcon="true"
        :icon-size="'41'"
    />
  </div>

  <div class="header_text">
    <h1>Viewing tickets</h1>
    <h2>Select a ticket below to view</h2>
  </div>

  <div v-for="persTicket in personalizedTickets" :key="persTicket.id" class="card-view card-view-prosonalized">
    <BasicCard
        :primary="true"
        :label="persTicket.holderName"
        :secLabel="persTicket.event.eventName"
        :icon="true"
        :price="formatPrice(persTicket.price)"
        :tekstInIcon="false"
        :tekstNextToIcon="true"
        :showPrice="true"
        :buttonClass="false"
        :showPriceBig="false"
        :ticketIcon="true"
        @click="$router.push({ path: 'ticket/' + persTicket.id })"
        :icon-size="'35'"
    />
  </div>

  <div class="displayTable">
    <Button
        :primary="true"
        :label="'Download All'"
    />
  </div>
  <p class="normalLabel">Tickets requiring personalization</p>
  <div v-for="ticket in notPersonalizedTickets" :key="ticket.id" class="card-view card-view-not-personalized">
    <BasicCard
        :primary="true"
        :label="ticket.orderId"
        :secLabel="ticket.event.eventName"
        :icon="true"
        :price="formatPrice(ticket.price)"
        :tekstInIcon="false"
        :tekstNextToIcon="true"
        :showPrice="true"
        :buttonClass="false"
        :showPriceBig="false"
        :ticketIcon="true"
        @click="$router.push({ path: 'ticket/'+ticket.id })"
        :icon-size="'35'"
    />
  </div>
</template>

<script setup>
import '../basicCard.css';
import Button from '../Atoms/BasicButton.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import { ref, onMounted } from 'vue';
import fetchTickets from '../../api/api.js';
import { computed } from 'vue';

const props = defineProps(['tickets']);
const ticketList = ref([]);
const personalizedTickets = ref([]);
const notPersonalizedTickets = ref([]);
const totalPrice = ref(0);

onMounted(async () => {
  try {
    const email = 'user@gmail.com';
    ticketList.value = props.tickets ? props.tickets : await fetchTickets(email); // Updated to 'ticketList' for duplicate key
    personalizedTickets.value = ticketList.value.filter(ticket => ticket.holderName !== '');
    notPersonalizedTickets.value = ticketList.value.filter(ticket => ticket.holderName === '');
    totalPrice.value = ticketList.value.reduce((acc, ticket) => acc + parseFloat(ticket.price), 0).toFixed(2);
  } catch (error) {
    console.error(error);
  }
});

const formatPrice = (price) => {
  return price.toString().replace(/\./g, ',');
};
const formattedStartDate = computed(() => {
  const startDate = ticketList.value[0]?.event.startDate;
  if (startDate) {
    const date = new Date(startDate);
    const options = {
      hour: '2-digit',
      minute: '2-digit',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    };
    return date.toLocaleDateString(undefined, options);
  }
  return '';
});
</script>

<style scoped>
.basic_card {
  margin-left: 32px;
}

.back {
  margin-top: 50px;
}

.top {
  margin: auto;
  display: flex;
  width: 350px;
}

.storybook-button storybook-button--primary {
  margin: auto;
}

.displayTable {
  margin-left: auto;
  margin-right: auto;
  display: table;
  margin-bottom: 10px;
}

.header_text {
  margin-bottom: 19px;
}

.header_text h1 {
  font-size: 22px;
  font-weight: bold;
  color: #303030;
  text-decoration: none solid rgb(48, 48, 48);
  text-align: center;
}

.header_text h2 {
  font-size: 18px;
  color: #007fff;
  text-decoration: none solid rgb(0, 127, 255);
  text-align: center;
}

.normalLabel {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
  font-size: 14px;
  color: rgb(183, 183, 183);
}

.card-view {
  cursor: pointer;
}
</style>
