import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import SuccessView from '@/views/SuccessView.vue';
import BackgroundHeaderComponent from '@/components/Atoms/BackgroundHeaderComponent.vue';
import SuccessIcon from '@/components/Atoms/SuccessIconComponent.vue';

describe('SuccessView', () => {
    const wrapper = mount(SuccessView);
    it('renders the component correctly', () => {
        const backgroundHeader = wrapper.findComponent(BackgroundHeaderComponent);
        const successIcon = wrapper.findComponent(SuccessIcon);
        const approvedText = wrapper.find('.approved-text');
        const approvedDot = wrapper.find('.approved-dot');
        const closingText = wrapper.find('.closing-text');

        expect(backgroundHeader.exists()).toBeTruthy();
        expect(successIcon.exists()).toBeTruthy();
        expect(approvedText.text()).toBe('Approved');
        expect(approvedDot.text()).toBe('.');
        expect(closingText.text()).toBe('You may now close this screen and continue on the other one.');
    });
});
