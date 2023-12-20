import type { Meta, StoryObj } from '@storybook/vue3';
import DateTime from '@/components/Atoms/DateTime.vue';

const meta: Meta<typeof DateTime> = {
  title: 'Atoms/DateTime',
  component: DateTime,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof DateTime>;

export const Default: Story = {
  args: {
    date: new Date('10:00 May 31, 2023'),
  },
};

export const DateOnly: Story = {
  args: {
    date: new Date('10:00 May 31, 2023'),
    dateOnly: true,
  },
};

export const TimeOnly: Story = {
  args: {
    date: new Date('10:00 May 31, 2023'),
    timeOnly: true,
  },
};