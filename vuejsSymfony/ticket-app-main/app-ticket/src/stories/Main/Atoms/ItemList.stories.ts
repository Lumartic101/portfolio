import type { Meta, StoryObj } from '@storybook/vue3';
import ItemList from '@/components/Atoms/ItemList.vue';

const meta: Meta<typeof ItemList> = {
  title: 'Atoms/ItemList',
  component: ItemList,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof ItemList>;

export const Default: Story = {
  args: {
  },
};