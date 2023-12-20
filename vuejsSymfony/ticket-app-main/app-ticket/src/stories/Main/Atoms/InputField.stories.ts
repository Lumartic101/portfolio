import type { Meta, StoryObj } from '@storybook/vue3';
import InputField from '@/components/Atoms/InputField.vue';

const meta: Meta<typeof InputField> = {
  title: 'Atoms/InputField',
  component: InputField,
  render: (args: any) => ({
    components: { InputField },
    setup() {
      return { args };
    },
    template: '<InputField :type="args.type" :placeholder="args.placeholder" :value="args.value" />',
  }),
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof InputField>;

export const WithPlaceholder: Story = {
  args: {
    placeholder: 'Enter your name'
  },
};

export const WithValue: Story = {
  args: {
    value: 'John Doe'
  },
};

export const PasswordInput: Story = {
  args: {
    type: 'password',
    placeholder: 'Enter your password'
  },
};
