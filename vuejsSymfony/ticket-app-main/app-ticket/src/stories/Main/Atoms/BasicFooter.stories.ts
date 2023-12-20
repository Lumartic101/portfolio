import type { Meta, StoryObj } from '@storybook/vue3';
import BasicFooter from '@/components/Atoms/BasicFooter.vue';

const meta: Meta<typeof BasicFooter> = {
  title: 'Atoms/BasicFooter',
  component: BasicFooter,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof BasicFooter>;

export const Default: Story = {
  args: {
    tearLine: false,
  },
};

export const TearLine: Story = {
  args: {
    tearLine: true,
  },
};