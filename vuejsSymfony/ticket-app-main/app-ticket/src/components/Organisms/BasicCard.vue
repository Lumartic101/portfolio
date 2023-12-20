<template>
  <div :class="classes" @click="onClick" :style="style">
    <div :class="iconConst">
      <div class="iconTekst">
        <p>{{totalTickets}}</p>
        <IconComponent
          v-if="ticketIcon"
          name="ticket"
          :size="iconSize"
        />
        <IconComponent
          v-if="circleIcon"
          name="ticket"
          :size="iconSize"
        />
      </div>
    </div>
    <div v-if="showPriceBig" :class="showPriceConst">€{{ price }} </div>
    <div :class="tekstInIconConst">
      <b :class="tekstNextToIconConst">
        {{ label }}
      </b>
      <p :class="tekstNextToIconConst">
        {{ secLabel }}
      </p>
    </div>
    <div v-if="!showPriceBig" :class="showPriceConst">€ {{ price }} </div>
    <div :class="buttonClassConst">
      <Button
        :primary="true"
        :size="'small'"
        :background-color="'#2196F3'"
        :label="'Button'"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import {computed} from 'vue';
import Button from '../Atoms/BasicButton.vue';
import IconComponent from '../Atoms/IconComponent.vue';

const props = withDefaults(defineProps<{
  /**
   * The label of the button
   */
  label: string,
  /**
   * primary or secondary button
   */
  primary?: boolean,

  icon?: boolean,

  secLabel: string,

  price: number,

  iconSize: string,

  totalTickets?: number,

  tekstInIcon?: boolean,

  ticketIcon?: boolean,

  tekstNextToIcon?: boolean,

  circleIcon?: boolean,

  showPrice?: boolean,

  showPriceBig?: boolean,

  buttonClass?: boolean,
  /**
   * size of the button
   */
  size?: 'normal' | 'big',
  /**
   * background color of the button
   */
  backgroundColor?: string,

}>(), {primary: false});

const emit = defineEmits<{
  (e: 'click', id: number): void;
}>();

const showPriceConst = computed(() => ({
  'show-price': props.showPrice,
  'show-price-second': !props.showPrice
}));


const buttonClassConst = computed(() => ({
  'button-class': props.buttonClass,
  'button-class--big': !props.buttonClass,
}));

const iconConst = computed(() => ({
  'icon-primary-card': props.icon,
  'icon-secondary-card': !props.icon,
}));

const tekstInIconConst = computed(() => ({
  'tekst-in-icon': props.tekstInIcon,
  'tekst-in-icon-second': !props.tekstInIcon
}));


const tekstNextToIconConst = computed(() => ({
  'tekst-next-to-icon': props.tekstNextToIcon,
  'tekst-next-to-icon-second': !props.tekstNextToIcon
}));

const classes = computed(() => ({
  'storybook-card': true,
  'storybook-card--primary': props.primary,
  'storybook-card--secondary': !props.primary,
  [`storybook-card--${props.size || 'normal'}`]: true,
}));

const style = computed(() => ({
  backgroundColor: props.backgroundColor
}));

const onClick = () => {
  emit('click', 1);
};

</script>

<style scoped>
.iconTekst{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 45px;
}
.iconTekst p{
  color: #007fff;
  font-weight: bold;
  font-size: 28px;
}
</style>