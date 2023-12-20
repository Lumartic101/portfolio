import type { Meta, StoryObj } from '@storybook/vue3';

import MyOrderLinkAccess from '@/components/Molecules/MyOrderLinkAccess.vue';

const meta: Meta<typeof MyOrderLinkAccess> = {
  title: 'Molecules/MyOrderLinkAccess',
  component: MyOrderLinkAccess,
  render: (args: any) => ({
    components: { MyHeader: MyOrderLinkAccess },
    setup() {
      return { args };
    },
    template: '<my-header  />',
  }),
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof MyOrderLinkAccess>;

export const OrderLinkAccess: Story = {
  args: {
  },
};
