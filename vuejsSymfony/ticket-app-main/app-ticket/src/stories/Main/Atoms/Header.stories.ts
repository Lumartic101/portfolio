import type { Meta, StoryObj } from '@storybook/vue3';

import MyHeader from '@/components/Atoms/BackgroundHeaderComponent.vue';

const meta: Meta<typeof MyHeader> = {
  title: 'Atoms/Header',
  component: MyHeader,
  render: (args: any) => ({
    components: { MyHeader },
    setup() {
      return { args };
    },
    template: '<my-header  />',
  }),
  parameters: {
    layout: 'fullscreen',
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof MyHeader>;

export const baseHeader: Story = {
  args: {
  },
};
