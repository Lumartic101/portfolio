import type { Meta, StoryObj } from '@storybook/vue3';
import Card from '@/components/Organisms/BasicCard.vue';

const meta: Meta<typeof Card> = {
  title: 'Atoms/Card',
  component: Card,
  argTypes: {
    size: { control: 'select', options: ['normal', 'big'] },
    backgroundColor: { control: 'color' },
    onClick: { action: 'clicked' },
  },
  args: { primary: false }, // default value
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof Card>;

export const normal: Story = {
  args: {
    primary: true,
    label: 'Lorem ipsulum',
    icon: true,
    tekstInIcon: true,
    buttonClass: true,
    showPrice: false
  },
};

export const big: Story = {
  args: {
    primary: false,
    label: 'Lorem ipsulum',
    icon: false,
    tekstInIcon: false,
    buttonClass: false,
    showPrice: true
  },
};