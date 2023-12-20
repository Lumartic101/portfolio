import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import ManageOverview from '@/views/ManageOverview.vue';
import axios from 'axios';
import MockAdapter from 'axios-mock-adapter';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import BasicButton from '@/components/Atoms/BasicButton.vue';
import ManageSmallCard from '@/components/Organisms/ManageSmallCard.vue';
import ManagePersonalizeCard from '@/components/Organisms/ManagePersonalizeCard.vue';


const mockTickets = [
    {
        id: 1,
        holderName: 'John Doe',
        type: 'VIP',
        orderId: 'ORD123',
        price: 100,
        event: {
            id: 1,
            eventName: 'Concert',
            country: 'USA',
            location: 'New York',
            startDate: '10:00 May 31, 2023',
            endDate: '22:00 May 31, 2023'
        }
    },
    {
        id: 2,
        holderName: 'Jane Smith',
        type: 'General',
        orderId: 'ORD456',
        price: 50,
        event: {
            id: 2,
            eventName: 'Festival',
            country: 'Canada',
            location: 'Toronto',
            startDate: '10:00 May 31, 2023',
            endDate: '22:00 May 31, 2023'
        }
    },
    {
        id: 3,
        holderName: '',
        type: 'General',
        orderId: 'ORD789',
        price: 30,
        event: {
            id: 3,
            eventName: 'Conference',
            country: 'UK',
            location: 'London',
            startDate: '10:00 May 31, 2023',
            endDate: '22:00 May 31, 2023'
        }
    }
];

describe('ManageOverview', () => {
    it('renders the BasicCard component with correct props', async () => {
        const wrapper = mount(ManageOverview);

        (wrapper.vm as any).tickets = mockTickets;

        await wrapper.vm.$nextTick(); // Wait for reactive updates

        const basicCardWrapper = wrapper.findComponent(BasicCard);
        expect(basicCardWrapper.props('label')).toBe('Concert');
        expect(basicCardWrapper.props('secLabel')).toBe('May 31, 2023 at 10:00 AM');
        expect(basicCardWrapper.props('totalTickets')).toBe(3);
        expect(basicCardWrapper.props('tekstInIcon')).toBe(true);
        expect(basicCardWrapper.props('tekstNextToIcon')).toBe(true);
        expect(basicCardWrapper.props('showPrice')).toBe(false);
        expect(basicCardWrapper.props('buttonClass')).toBe(false);
        expect(basicCardWrapper.props('showPriceBig')).toBe(true);
        expect(basicCardWrapper.props('ticketIcon')).toBe(true);
        expect(basicCardWrapper.props('iconSize')).toBe('41');
    });

    it('BasicButton should exist', async () => {
        const wrapper = mount(ManageOverview);

        (wrapper.vm as any).tickets = mockTickets;

        await wrapper.vm.$nextTick(); // Wait for reactive updates

        const basicButtonWrapper = wrapper.findComponent(BasicButton);
        expect(basicButtonWrapper.exists()).toBe(true);
    });

    it('closes the BasicModal component when calling the closeModal method', async () => {
        const wrapper = mount(ManageOverview);

        (wrapper.vm as any).tickets = mockTickets;

        await wrapper.vm.$nextTick(); // Wait for reactive updates

        await wrapper.setData({ isModalOpen: true });
        expect(wrapper.vm.isModalOpen).toBe(true);

        await wrapper.vm.closeModal();
        expect(wrapper.vm.isModalOpen).toBe(false);
    });

    it('opens the BasicModal component when calling the openModal method', async () => {
        const wrapper = mount(ManageOverview);

        // Mock the Axios request
        const mockAdapter = new MockAdapter(axios);
        mockAdapter.onPost('http://127.0.0.1:8000/api/send-invitation-email/test@example.com').reply(200);

        (wrapper.vm as any).tickets = mockTickets;
        await wrapper.vm.$nextTick(); // Wait for reactive updates

        expect(wrapper.vm.isModalOpen).toBe(false);

        await wrapper.setData({ email: 'test@example.com' });
        await wrapper.vm.openModal();
        await wrapper.vm.$nextTick();

        expect(wrapper.vm.isModalOpen).toBe(true);
    });

    it('renders the ManageSmallCard component with personalized tickets', async () => {
        const wrapper = mount(ManageOverview);

        (wrapper.vm as any).tickets = mockTickets;
        (wrapper.vm as any).personalizedTickets = mockTickets.filter(ticket => ticket.holderName !== '');

        await wrapper.vm.$nextTick();

        const personalizedCards = wrapper.findAllComponents(ManageSmallCard);
        expect(personalizedCards.length).toBe(2);

        const firstPersonalizedCard = personalizedCards[0];
        expect(firstPersonalizedCard.props('iconName')).toBe('ticket');
        expect(firstPersonalizedCard.props('heading')).toBe('John Doe');
        expect(firstPersonalizedCard.props('subheading')).toBe('Weekender');

        const secondPersonalizedCard = personalizedCards[1];
        expect(secondPersonalizedCard.props('iconName')).toBe('ticket');
        expect(secondPersonalizedCard.props('heading')).toBe('Jane Smith');
        expect(secondPersonalizedCard.props('subheading')).toBe('Weekender');
    });

    it('renders the ManagePersonalizeCard component with not personalized tickets', async () => {
        const wrapper = mount(ManageOverview);

        (wrapper.vm as any).tickets = mockTickets;
        (wrapper.vm as any).personalizedTickets = mockTickets.filter(ticket => ticket.holderName !== '');
        (wrapper.vm as any).notPersonalizedTickets = mockTickets.filter(ticket => ticket.holderName === '');

        await wrapper.vm.$nextTick();

        const notPersonalizedCards = wrapper.findAllComponents(ManagePersonalizeCard);
        expect(notPersonalizedCards.length).toBe(1);

        const notPersonalizedCard = notPersonalizedCards[0];
        expect(notPersonalizedCard.props('header')).toBe('ORD789');
        expect(notPersonalizedCard.props('price')).toBe(30);
        expect(notPersonalizedCard.props('subheader')).toBe('Weekender');
    });
});