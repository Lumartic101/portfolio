import { describe,test, it, expect } from 'vitest'
import { shallowMount } from '@vue/test-utils';
import MyForm from '@/components/Molecules/MyOrderLinkAccess.vue';

describe('MyForm', () => {
    test('Valid email should be submitted', () => {
        const wrapper = shallowMount(MyForm);
        const input = wrapper.find('input');
        input.setValue('test@example.com');
        wrapper.find('.button').trigger('click');
        expect(wrapper.vm.$data.value).toBe('test@example.com');
    });

    test('Invalid email should not be submitted', () => {
        const wrapper = shallowMount(MyForm);
        const input = wrapper.find('input');
        input.setValue('invalid-email');
        wrapper.find('.button').trigger('click');
        expect(wrapper.vm.$data.value).toBe('invalid-email');
    });
});
