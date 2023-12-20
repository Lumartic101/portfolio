<script setup>
import OverallTicketCard from '@/components/Molecules/OverallTicketCard.vue';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import {ref, onMounted, computed} from 'vue';
import fetchTickets from '../api/api.js'; // Update the import path

const tickets = ref([]);
const personalizedTickets = ref([]);
const notPersonalizedTickets = ref([]);
const totalPrice = ref(0);
const email = 'user@gmail.com';
onMounted(async () => {
  try {
    // Parse the response as JSON
    tickets.value = await fetchTickets(email); // Populate the 'tickets' array
    personalizedTickets.value = tickets.value.filter(ticket => ticket.holderName !== '');
    notPersonalizedTickets.value = tickets.value.filter(ticket => ticket.holderName === '');
    // Calculate the total price
    totalPrice.value = tickets.value.reduce((acc, ticket) => acc + ticket.price, 0).toFixed(2);

  } catch (error) {
    console.error(error);
  }
});

const formatPrice = (price) => {
  return price.toString().replace(/\./g, ',');
};

const formattedStartDate = computed(() => {
  const startDate = tickets.value[0]?.event.startDate;
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

<template>
  <div class="overview_list">
    <div class="big_cardview">
      <BasicCard
          :label="tickets[0]?.event.eventName"
          :secLabel="formattedStartDate"
          :price="formatPrice(totalPrice)"
          :totalTickets="tickets.length"
          :tekstInIcon="true"
          :tekstNextToIcon="true"
          :showPrice="false"
          :buttonClass="false"
          :showPriceBig="true"
          :ticketIcon="true"
          :icon-size="'41'"/>
    </div>
    <div class="header_text">
      <h1 v-if="tickets[0]?.holderName">Hi {{ tickets[0].holderName }}</h1>
      <h2>What would you like to do?</h2>
    </div>
    <ul>
      <li @click="$router.push({ name: 'tickets' })">
        <OverallTicketCard
            :heading="'Show tickets(s)'"
            :subheading="'Display, download or add to wallet'"
            :icon-name="'ticket'"
        />
      </li>
      <li @click="$router.push({ name: 'manage' })">
        <OverallTicketCard
            :icon-name="'manage'"
            :heading="'Manage personalization'"
            :subheading="'Name, adress, phone you name it'"

        />
      </li>
      <li>
        <OverallTicketCard
            :icon-name="'date'"
            :heading="'Change time or date'"
            :subheading="'Want to go on another day?'"
        />
      </li>
      <li>
        <OverallTicketCard
            :icon-name="'sell'"
            :heading="'Sell tickets'"
            :subheading="'Submit to secondary sale platform'"
        />
      </li>
      <li>
        <OverallTicketCard
            :icon-name="'refund'"
            :heading="'Request refund'"
            :subheading="'Get a (part of) your money back'"
        />
      </li>

    </ul>
  </div>
</template>

<style scoped>
ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center;
  flex-direction: column;
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

.overview_list {
  margin: auto;
}

.big_cardview {
  padding-bottom: 8px;
}

.header_text {
  padding-bottom: 20px;
}
</style>
