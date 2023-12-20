import Button from '@/components/Atoms/BasicButton.vue';

import type { Meta, StoryObj } from '@storybook/vue3';

// More on how to set up stories at: https://storybook.js.org/docs/vue/writing-stories/introduction
const meta: Meta<typeof Button> = {
  title: 'Atoms/Button',
  component: Button,
  tags: ['autodocs'],
  argTypes: {
    size: { control: 'select', options: ['small', 'medium', 'large', 'wide'] },
    backgroundColor: { control: 'color' },
    onClick: { action: 'clicked' },
  },
  args: { primary: false }, // default value
};

export default meta;
type Story = StoryObj<typeof Button>;

export const SendAccessLink: Story = {
  args: {
    primary: true,
    label: 'Send access link',
  },
};

export const downloadAll: Story = {
  args: {
    primary: true,
    label: 'Download All',
  },
};

export const addToWallet: Story = {
  args: {
    primary: true,
    label: 'Add to wallet',
  },
};

export const CloseScreen: Story = {
  args: {
    primary: false,
    label: 'Close screen',
  },
};

export const medium: Story = {
  args: {
    primary: true,
    label: 'button',
    size: 'medium'
  },
};

export const Large: Story = {
  args: {
    label: 'Button',
    size: 'large',
  },
};

export const Small: Story = {
  args: {
    label: 'Button',
    size: 'small',
  },
};

export const Wide: Story = {
  args: {
    label: 'Button',
    size: 'wide',
  },
};