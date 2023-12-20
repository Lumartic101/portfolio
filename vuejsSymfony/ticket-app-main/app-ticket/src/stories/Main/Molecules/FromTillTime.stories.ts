import type { Meta, StoryObj } from '@storybook/vue3';
import FromTillTime from '@/components/Molecules/FromTillTime.vue';

const meta: Meta<typeof FromTillTime> = {
  title: 'Molecules/FromTillTime',
  component: FromTillTime,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof FromTillTime>;

export const Default: Story = {
  args: {
    startDate: new Date('10:00 May 31, 2023'),
    endDate: new Date('18:00 May 31, 2023'),
  },
};