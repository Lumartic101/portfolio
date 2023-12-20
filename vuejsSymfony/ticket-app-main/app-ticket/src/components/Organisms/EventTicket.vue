<template>
  <div class="ticket">
    <i-header :image-src="imageSrc"></i-header>
    <b-body class="ticket-body">
      <h2 class="title">{{ title }}</h2>
      <p class="country">{{ country }}</p>
      <card-section class="event-info">
        <p class="amount-people">{{ numPeople }}</p>
        <list class="date">
          <li><p style="margin: 0;"><strong>Date</strong></p></li>
          <li><datetime :date="startDate" :date-only="true"></datetime></li>
        </list>
        <list class="time">
          <li><p style="margin: 0;"><strong>Time</strong></p></li>
          <li><from-till :start-date="startDate" :end-date="endDate"></from-till></li>
        </list>
      </card-section>
      <div class="ticket-info">
        <list class="parent">
          <li><p>Name</p></li>
          <li><p style="color: #aeb3bb;">Ticket Type</p></li>
          <li><p>Order ID</p></li>
        </list>
        <list class="detail">
          <li><p style="font-weight: lighter; color: #747474;">{{ ticketHolder }}</p></li>
          <li><p>{{ ticketType }}</p></li>
          <li><p style="color: #464646;">{{ orderId }}</p></li>
        </list>
      </div>
    </b-body>
    <b-footer :tear-line="true" class="ticket-footer">
      <barcode :barcode-src="barcodeSrc"></barcode>
    </b-footer>
  </div>
  <svg>
    <clipPath id="clip-footer" clipPathUnits="objectBoundingBox">
      <path d="M0,1 H1 V0.116 C0.967,0.116,0.946,0.055,0.946,0 H0.054 C0.054,0.067,0.031,0.116,0,0.116 V1" fill="white"/>
    </clipPath>
  </svg>
  <svg>
    <clipPath id="clip-body" clipPathUnits="objectBoundingBox">
      <path d="M0,0 H1 V0.953 C0.967,0.953,0.946,0.978,0.946,1 H0.054 C0.054,0.973,0.031,0.953,0,0.953 V0" fill="white"/>
    </clipPath>
  </svg>
</template>

<script lang="ts">
import { defineComponent } from 'vue';
import ImageHeader from '../Atoms/ImageHeader.vue';
import BasicFooter from '../Atoms/BasicFooter.vue';
import Barcode from '../Atoms/Barcode.vue';
import BasicBody from '../Atoms/BasicBody.vue';
import CardSection from '../Atoms/CardSection.vue';
import DateTime from '../Atoms/DateTime.vue';
import ItemList from '../Atoms/ItemList.vue';
import FromTillTime from '../Molecules/FromTillTime.vue';

export default defineComponent ({
  components: {
    'i-header': ImageHeader,
    'b-footer': BasicFooter,
    'barcode': Barcode,
    'b-body': BasicBody,
    'card-section': CardSection,
    'datetime': DateTime,
    'list': ItemList,
    'from-till': FromTillTime,
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
});
</script>

<style scoped>
.ticket {
  background-color: #ffffff00;
  border-radius: 20px;
  color: #555;
  display: flex;
  flex-direction: column;
  font-family: Arial, sans-serif;
  max-width: 21.875rem;
  overflow: hidden;
}

.title {
  margin-bottom: 0;
  margin-top: 0.5rem;
  font-size: xx-large;
  line-height: 2rem;
}

.event-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 2rem 1fr;
  gap: 0px 0px;
  grid-template-areas:
    "amount-people amount-people"
    "date time";
}
.amount-people {
   grid-area: amount-people;
   margin-top: 0;
}
.date {
  grid-area: date;
  font-size: large;
}
.time {
  grid-area: time;
  font-size: large;
}

.country {
  margin-top: 5px;
  font-size: 16px;
}

.ticket-info {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr;
  gap: 0px 0px;
  grid-template-areas:
    "parent detail";
}

.parent {
  grid-area: parent;
  color: #bababa;
  font-weight: bold;
  font-size: 19px;
}

.detail {
  grid-area: detail;
  color: #303030;
  text-align: right;
  font-weight: bold;
  font-size: 19px;
}

.ticket-footer {
  background-color: #ffffff;
  top: -0.5px;
  clip-path: url(#clip-footer);
}

.ticket-body {
  background-color: #ffffff;
  clip-path: url(#clip-body);
}

svg {
  position: absolute;
  max-width: 0;
  max-height: 0;
}
</style>
