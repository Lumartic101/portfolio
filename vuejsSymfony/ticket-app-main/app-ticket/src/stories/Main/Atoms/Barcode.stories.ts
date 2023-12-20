import type { Meta, StoryObj } from '@storybook/vue3';
import Barcode from '@/components/Atoms/Barcode.vue';

const meta: Meta<typeof Barcode> = {
  title: 'Atoms/Barcode',
  component: Barcode,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof Barcode>;

export const Default: Story = {
  args: {
    barcodeSrc: 'https://cdn-dfhjh.nitrocdn.com/BzQnABYFnLkAUVnIDRwDtFjmHEaLtdtL/assets/images/optimized/rev-c133d21/wp-content/uploads/2015/02/barcode-13.png',
  },
};