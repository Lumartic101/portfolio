<script setup>
import ManagePersonalizeCard from '@/components/Organisms/ManagePersonalizeCard.vue';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';
import ManageSmallCard from '@/components/Organisms/ManageSmallCard.vue';
import BasicModal from '@/components/Molecules/BasicModal.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';
import {ref, onMounted, computed} from 'vue';
import fetchTickets from '../api/api.js'; // Update the import path

const tickets = ref([]);
const personalizedTickets = ref([]);
const notPersonalizedTickets = ref([]);
const totalPrice = ref(0);
const email = 'user@gmail.com';

onMounted(async () => {
  try {
    const data = await fetchTickets(email); // Parse the response as JSON
    tickets.value = data;
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
  <div class="container">
    <div class="top">
      <div class="back" @click="$router.push({ name: 'ticket-menu' })"
      >
        <IconComponent
            name="back"
            size="61"
        />
      </div>
      <BasicModal :isModalOpen="isModalOpen" :closeModal="closeModal"/>
      <BasicCard
          class="basic_card"
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
          :icon-size="'41'"
      />
    </div>

    <div class="header_text">
      <h1>Manage personalization</h1>
      <h2>Tickets that are not yet claimed.</h2>
    </div>
    <div v-for="ticket in notPersonalizedTickets" :key="ticket.id" class="manage_card">
      <ManagePersonalizeCard :header="ticket.orderId" :price="ticket.price" :subheader="'Weekender'"
                             :input-value="updateValue"/>
    </div>
    <div class="button">
      <BasicButton :label="'Send invitation'" :size="'wide'" :background-color="'#0074e8'" class="sendInvitation"
                   @click="openModal"/>
    </div>
    <span class="bottom_text">Already personalized</span>
    <div class="card_list">
      <ul>
        <li v-for="ticket in personalizedTickets" :key="ticket.id">
          <ManageSmallCard
              :icon-name="'ticket'"
              :heading="ticket.holderName"
              :subheading="'Weekender'"
          />
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  components: {
    BasicModal,
  },
  data() {
    return {
      email: '',
      isModalOpen: false
    };
  },
  methods: {
    updateValue(event) {
      this.email = event;
    },
    openModal() {
      const emailRegex = /\S+@\S+\.\S+/;
      if (emailRegex.test(this.email)) {
        axios
          .post('http://127.0.0.1:8000/api/send-invitation-email/' + this.email)
          .then(() => {
            this.isModalOpen = true;
          })
          .catch((error) => {
            // Handle the error or rejection here
            console.error('An error occurred:', error);
          });

      } else {
        alert('Please provide a valid email');
      }
    },
    closeModal() {
      this.isModalOpen = false;
    },
  },
};

</script>

<style scoped>
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
  cursor: pointer;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.basic_card {
  margin-left: 32px;
}

.back {
  margin-top: 50px;
}

.top {
  display: flex;
  width: 350px;
}

ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
  display: flex;
  align-items: center;
  flex-direction: column;
}

.container {
  margin: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
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

.button {
  margin-top: 9px;
  margin-bottom: 40px;
}

.bottom_text {
  font-size: 14px;
  color: #424c59;
  margin-bottom: 10px;
}
</style>
