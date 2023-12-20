<template>
  <div class="background" />
  <main class="main">
    <TicketDetail
        v-if="idTicket"
        :barcode-src="'https://cdn-dfhjh.nitrocdn.com/BzQnABYFnLkAUVnIDRwDtFjmHEaLtdtL/assets/images/optimized/rev-c133d21/wp-content/uploads/2015/02/barcode-13.png'"
        :image-src="'https://upload.wikimedia.org/wikipedia/commons/e/e6/Bvf_haarlem_11_hq.jpeg'"
        :num-people="'2 Adults'"
        :country="idTicket?.event.country"
        :title="idTicket?.event.eventName"
        :start-date="new Date(idTicket?.event.startDate)"
        :end-date="new Date(idTicket?.event.endDate)"
        :ticket-holder="idTicket?.holderName"
        :ticket-type="'VIP'"
        :order-id="idTicket?.orderId"
    />
  </main>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import getTicketById from '@/api/getTicketById';
import TicketDetail from '@/components/Pages/TicketDetail.vue';

interface Ticket {
  event: {
    country: string;
    eventName: string;
    endDate: string;
    startDate: string;
  };
  holderName: string;
  orderId: number;
}

export default defineComponent({
  components: {
    TicketDetail,
  },
  setup() {
    const idTicket = ref<Ticket | null>(null);
    const router = useRouter();

    onMounted(async () => {
      try {
        const id = Array.isArray(router.currentRoute.value.params.id)
          ? router.currentRoute.value.params.id[0]
          : router.currentRoute.value.params.id;
        const parsedId = parseInt(id);
        idTicket.value = await getTicketById(parsedId);
      } catch (error) {
        console.error(error);
      }
    });

    return {
      idTicket,
    };
  },
});
</script>

<style scoped>
.main {
  padding-top: 3%;
  width: min(22rem, 100% - 2rem);
  margin-inline: auto;
  z-index: 10;
}

.background {
  position: absolute;
  width: 100%;
  height: 110%;
  background-color: #000;
  opacity: 79%;
  z-index: 9;
}
</style>
