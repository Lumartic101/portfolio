import type { Meta, StoryObj } from '@storybook/vue3';
import ImageHeader from '@/components/Atoms/ImageHeader.vue';

const meta: Meta<typeof ImageHeader> = {
  title: 'Atoms/ImageHeader',
  component: ImageHeader,
  tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<typeof ImageHeader>;

export const Default: Story = {
  args: {
    imageSrc: 'https://upload.wikimedia.org/wikipedia/commons/e/e6/Bvf_haarlem_11_hq.jpeg',
  },
};