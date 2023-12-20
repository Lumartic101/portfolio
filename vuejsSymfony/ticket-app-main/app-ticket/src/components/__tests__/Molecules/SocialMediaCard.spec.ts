import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import SocialMediaCard from '@/components/Molecules/SocialMediaCard.vue';

describe('SocialMediaCard', () => {
    it('renders the card with correct props', () => {
        const name = 'Facebook';
        const path = 'facebook-icon.png';
        const heading = 'Follow us on Facebook';

        const wrapper = mount(SocialMediaCard, {
            props: {
                name,
                path,
                heading,
            },
        });

        const card = wrapper.find('.card');
        expect(card.exists()).toBe(true);

        const icon = wrapper.find('.icon');
        expect(icon.exists()).toBe(true);

        const image = wrapper.find('img');
        expect(image.exists()).toBe(true);
        expect(image.attributes('src')).toBe(path);
        expect(image.attributes('alt')).toBe(name);

        const text = wrapper.find('.text');
        expect(text.exists()).toBe(true);

        const h1 = text.find('h1');
        expect(h1.exists()).toBe(true);
        expect(h1.text()).toBe(heading);
    });
});
