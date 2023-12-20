import { shallowMount } from '@vue/test-utils';
import Button from '@/components/Atoms/BasicButton.vue';
import { describe, it, expect } from 'vitest'

describe('Button.vue', () => {
  it('emits click event when button is clicked', async () => {
    const wrapper = shallowMount(Button, {
      props: {
        label: 'Send access link',
      },
    });

    await wrapper.find('button').trigger('click');
    expect(wrapper.emitted('click')).toBeTruthy();
  });
});