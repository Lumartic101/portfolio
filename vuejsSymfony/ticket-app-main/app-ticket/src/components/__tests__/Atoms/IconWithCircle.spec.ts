import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import IconWithCircle from '@/components/Atoms/IconWithCircle.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';


describe('IconWithCircle', () => {
    it('renders the component with the correct props', () => {
        const iconName = 'circle';

        const wrapper = mount(IconWithCircle, {
            props: {
                icon: iconName,
            },
            global: {
                components: {
                    IconComponent,
                },
            },
        });

        const iconComponent = wrapper.findComponent(IconComponent);
        const circleElement = wrapper.find('.circle');

        expect(iconComponent.exists()).toBe(true);
        expect(iconComponent.props('name')).toBe(iconName);
        expect(circleElement.exists()).toBe(true);
        expect(circleElement.classes()).toContain('circle');
    });
});