<script setup>
import BasicButton from '@/components/Atoms/BasicButton.vue';
import IconComponent from '../Atoms/IconComponent.vue';
</script>

<template>
  <div v-if="isModalOpen" class="modal-container">
    <div class="modal">
      <div class="modal-content">
        <!-- Modal content here -->
        <h1 class="h1-succes-modal">
          <IconComponent
            :size="'29'"
            :name="'check'"
          />
          Success!
        </h1>
        <p class="textSuccesInvitation">They have been invited to claim this ticket.</p>
        <BasicButton
          class="closeModalButton"
          :primary="true"
          :size="'wide'"
          :background-color="'rgb(3, 107, 210)'"
          :label="'Close'"
          @click="closeModalInternal"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BasicModal',
  props: {
    isModalOpen: {
      type: Boolean,
      required: true,
    },
    closeModal: {
      type: Function,
      required: true,
    },
  },
  setup() {
    const closeModalMain = () => {
      this.$emit('closeModal');
    };
    return {
      closeModalMain
    };
  },
  methods: {
    closeModalInternal() {
      this.closeModal();
    },
  },
};
</script>

<style scoped>
.modal-container {
  display: flex;
  align-items: center;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.4); /* Update the background color and opacity */
  height: 100%;
  z-index: 9999; /* Set a high z-index to ensure the modal is on top */
}

.modal {
  display: block;
  width: 348px;
  height: 213px;
  border-radius: 6px;
  fill: #ffffff;
  box-shadow: 0px 20px 20px rgba(0, 0, 0, 0.02), 0px 10px 10px rgba(0, 0, 0, 0.05), 0px 5px 5px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 1); /* Update the background color to your desired color */
  position: relative; /* Add position:relative */
  z-index: 9999; /* Set a high z-index to ensure the modal is on top */
}

.modal-content {
  background-color: #fefefe;
  padding: 20px;
  border: 1px solid #888;
  width: 100%;
  height: 100%;
  border-radius: 6px;
}

.h1-succes-modal {
  font-weight: 500;
  font-weight: bold;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
}

.closeModalButton {
  margin: auto;
  display: block;
  margin-top: 10px;
}

.textSuccesInvitation {
  width: 284px;
  height: 54px;
  font-size: 15px;
  color: #030303;
  text-decoration: none solid rgb(3, 3, 3);
  margin-left: 10px;
}

.email-modal-invitation {
  font-weight: 500;
  font-weight: bold;
}
</style>
