import type { Meta, StoryObj } from '@storybook/vue3';
import BasicBody from '@/components/Atoms/BasicBody.vue';

const meta: Meta<typeof BasicBody> = {
  title: 'Atoms/BasicBody',
  component: BasicBody,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof BasicBody>;

export const Default: Story = {
  args: {
  },
};