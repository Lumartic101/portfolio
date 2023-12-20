import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import ManageSmallCard from '@/components/Organisms/ManageSmallCard.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';

describe('ManageSmallCard', () => {
    it('renders the component with correct props', async () => {
        const iconName = 'ticket';
        const heading = 'John Doe';
        const subheading = 'Weekender';

        const wrapper = mount(ManageSmallCard, {
            props: {
                iconName,
                heading,
                subheading,
            },
        });

        const iconComponent = wrapper.findComponent(IconComponent);
        expect(iconComponent.props('name')).toBe(iconName);
        expect(iconComponent.props('size')).toBe('35');

        const h1 = wrapper.find('h1');
        expect(h1.text()).toBe(heading);

        const h2 = wrapper.find('h2');
        expect(h2.text()).toBe(subheading);

        const basicButton = wrapper.findComponent(BasicButton);
        expect(basicButton.props('label')).toBe('Reset');
        expect(basicButton.props('size')).toBe('medium');
    });
});
