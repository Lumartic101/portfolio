import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';import BasicCard from '@/components/Organisms/BasicCard.vue';
import Button from '@/components/Atoms/BasicButton.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';

describe('BasicCard', () => {
    it('renders the component with correct props', async () => {
        const label = 'Concert';
        const secLabel = '2023-06-30 19:00';
        const totalTickets = 3;
        const ticketIcon = true;
        const showPrice = false;
        const buttonClass = false;
        const showPriceBig = true;
        const iconSize = '41';
        const price = 100;

        const wrapper = mount(BasicCard, {
            props: {
                label,
                secLabel,
                totalTickets,
                ticketIcon,
                showPrice,
                buttonClass,
                showPriceBig,
                iconSize,
                price,
            },
            components: {
                Button,
                IconComponent,
            },
        });

        const p = wrapper.find('p');
        expect(p.text()).toBe(String(totalTickets));

        const iconComponents = wrapper.findAllComponents(IconComponent);
        expect(iconComponents.length).toBe(1);
        expect(iconComponents[0].props('name')).toBe('ticket');
        expect(iconComponents[0].props('size')).toBe(iconSize);

        const b = wrapper.find('b');
        expect(b.text()).toBe(label);

        const buttonComponent = wrapper.findComponent(Button);
        expect(buttonComponent.props('label')).toBe('Button');
        expect(buttonComponent.props('primary')).toBe(true);
        expect(buttonComponent.props('size')).toBe('small');
    });
});
