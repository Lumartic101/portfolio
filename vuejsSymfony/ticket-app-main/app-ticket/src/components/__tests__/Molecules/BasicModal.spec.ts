import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import BasicModal from '@/components/Molecules/BasicModal.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';
import IconComponent from '@/components/Atoms/IconComponent.vue';

describe('BasicModal', () => {
    it('closes the modal when clicking the close button', async () => {
        let closeModalCalled = false;

        const wrapper = mount(BasicModal, {
            props: {
                isModalOpen: true,
                closeModal: () => {
                    closeModalCalled = true;
                },
            },
        });

        await new Promise(resolve => setTimeout(resolve)); // Wait for next tick

        const closeButton = wrapper.findComponent(BasicButton);
        expect(closeButton.exists()).toBe(true);

        await closeButton.trigger('click');

        expect(closeModalCalled).toBe(true);
    });

    it('renders the success message and button', async () => {
        const wrapper = mount(BasicModal, {
            props: {
                isModalOpen: true,
                closeModal: () => {},
            },
        });

        await new Promise(resolve => setTimeout(resolve)); // Wait for next tick

        const successMessage = wrapper.find('h1.h1-succes-modal');
        expect(successMessage.exists()).toBe(true);
        expect(successMessage.text()).toBe('Success!');

        const successText = wrapper.find('.textSuccesInvitation');
        expect(successText.exists()).toBe(true);
        expect(successText.text()).toBe('They have been invited to claim this ticket.');

        const closeButton = wrapper.findComponent(BasicButton);
        expect(closeButton.exists()).toBe(true);
        expect(closeButton.props('label')).toBe('Close');
        expect(closeButton.props('primary')).toBe(true);
        expect(closeButton.props('size')).toBe('wide');
    });
});
