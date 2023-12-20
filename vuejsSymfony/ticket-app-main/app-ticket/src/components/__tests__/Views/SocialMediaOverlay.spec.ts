import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';import SocialMediaOverlay from '@/views/SocialMediaOverlay.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';
import SocialMediaCard from '@/components/Molecules/SocialMediaCard.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';

describe('SocialMediaOverlay', () => {
    it('renders the component with the correct structure and props', () => {
        const wrapper = mount(SocialMediaOverlay);

        // Assert the structure
        expect(wrapper.find('.background').exists()).toBe(true);
        expect(wrapper.find('.main').exists()).toBe(true);
        expect(wrapper.find('.container').exists()).toBe(true);
        expect(wrapper.find('.header').exists()).toBe(true);
        expect(wrapper.findComponent(IconComponent).exists()).toBe(true);
        expect(wrapper.find('h1').exists()).toBe(true);
        expect(wrapper.find('.social_list').exists()).toBe(true);
        expect(wrapper.findAllComponents(SocialMediaCard).length).toBe(4);
        expect(wrapper.find('.button').exists()).toBe(true);
        expect(wrapper.findComponent(BasicButton).exists()).toBe(true);

        // Assert the props
        const socialMediaCards = wrapper.findAllComponents(SocialMediaCard);
        expect(socialMediaCards[0].props('heading')).toBe('Share by whatsapp');
        expect(socialMediaCards[0].props('name')).toBe('whatsapp');
        expect(socialMediaCards[0].props('path')).toBe('/src/assets/whatsapp.png');

        expect(socialMediaCards[1].props('heading')).toBe('Share by facebook');
        expect(socialMediaCards[1].props('name')).toBe('facebook');
        expect(socialMediaCards[1].props('path')).toBe('/src/assets/facebook.png');

        expect(socialMediaCards[2].props('heading')).toBe('Share by instagram');
        expect(socialMediaCards[2].props('name')).toBe('facebook');
        expect(socialMediaCards[2].props('path')).toBe('/src/assets/insta.png');

        expect(socialMediaCards[3].props('heading')).toBe('Share by telegram');
        expect(socialMediaCards[3].props('name')).toBe('facebook');
        expect(socialMediaCards[3].props('path')).toBe('/src/assets/telegram.png');

        expect(wrapper.findComponent(BasicButton).props('size')).toBe('wide');
        expect(wrapper.findComponent(BasicButton).props('label')).toBe('Close');
    });
});
