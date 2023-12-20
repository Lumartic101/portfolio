import {shallowMount} from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import ManagePersonalizeCard from '@/components/Organisms/ManagePersonalizeCard.vue';

describe('ManagePersonalizeCard', () => {
    it('renders the header and subheader correctly', () => {
        const header = 'Test Header';
        const subheader = 'Test Subheader';

        const wrapper = shallowMount(ManagePersonalizeCard, {
            props: {
                header,
                subheader,
                price: '10',
                inputValue: '',
            },
        });

        expect(wrapper.find('h1').text()).toBe(header);
        expect(wrapper.find('h2').text()).toBe(subheader);
    });

    it('renders the price correctly', () => {
        const price = '10';

        const wrapper = shallowMount(ManagePersonalizeCard, {
            props: {
                header: 'Test Header',
                subheader: 'Test Subheader',
                price,
                inputValue: '',
            },
        });

        expect(wrapper.find('.price').text()).toBe(`€${price}`);
    });

    it('renders the input field with the correct placeholder', () => {
        const placeholder = 'Fill in e-mail address';

        const wrapper = shallowMount(ManagePersonalizeCard, {
            props: {
                header: 'Test Header',
                subheader: 'Test Subheader',
                price: '10',
                inputValue: '',
            },
        });

        const inputField = wrapper.findComponent({ name: 'InputField' });
        expect(inputField.props('placeholder')).toBe(placeholder);
    });
});
