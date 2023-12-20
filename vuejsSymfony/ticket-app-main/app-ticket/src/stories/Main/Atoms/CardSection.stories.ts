import type { Meta, StoryObj } from '@storybook/vue3';
import CardSection from '@/components/Atoms/CardSection.vue';

const meta: Meta<typeof CardSection> = {
  title: 'Atoms/CardSection',
  component: CardSection,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof CardSection>;

export const Default: Story = {
  args: {
  },
};