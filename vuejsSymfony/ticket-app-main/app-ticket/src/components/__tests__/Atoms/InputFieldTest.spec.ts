import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils';
import InputField from '@/components/Atoms/InputField.vue';
describe('InputField', () => {
  it('emits inputValue event with entered text', async () => {
    const wrapper = mount(InputField);
    const input = wrapper.find('input');

    await input.setValue('example text');

    expect(input.element.value).toBe('example text');

    expect(wrapper.emitted?.('inputValue')).toBeTruthy();
    expect(wrapper.emitted?.('inputValue')?.[0]?.[0]).toBe('example text');
  });
});