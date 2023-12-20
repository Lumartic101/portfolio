import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import TicketOverview from '@/views/TicketOverview.vue';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import OverallTicketCard from '@/components/Molecules/OverallTicketCard.vue';

const mockTickets = [
    {
        id: 1,
        holderName: 'John Doe',
        type: 'Regular',
        orderId: 'ABC123',
        price: '10.99',
        event: {
            id: 1,
            eventName: 'Lowlands',
            country: 'England',
            location: 'Location',
            startDate: '10:00 May 31, 2023',
            endDate: '22:00 May 31, 2023'
        },
    },
];

describe('TicketOverview', () => {
    it('renders the correct number of cards', () => {
        const wrapper = mount(TicketOverview);

        (wrapper.vm as any).tickets = mockTickets;

        return wrapper.vm.$nextTick().then(() => {
            expect(wrapper.findAll('li')).toHaveLength(5);
        });
    });

    it('displays the correct name', () => {
        const wrapper = mount(TicketOverview);

        (wrapper.vm as any).tickets = mockTickets;

        return wrapper.vm.$nextTick().then(() => {
            expect(wrapper.find('h1').text()).toContain('Hi John Doe');
        });
    });

    it('renders the BasicCard component with correct props', async () => {
        const wrapper = mount(TicketOverview);

        (wrapper.vm as any).tickets = mockTickets;

        await wrapper.vm.$nextTick(); // Wait for reactive updates

        const basicCardWrapper = wrapper.findComponent(BasicCard);
        expect(basicCardWrapper.props('label')).toBe('Lowlands');
        expect(basicCardWrapper.props('secLabel')).toBe('May 31, 2023 at 10:00 AM');
        expect(basicCardWrapper.props('tekstInIcon')).toBe(true);
        expect(basicCardWrapper.props('tekstNextToIcon')).toBe(true);
        expect(basicCardWrapper.props('showPrice')).toBe(false);
        expect(basicCardWrapper.props('buttonClass')).toBe(false);
        expect(basicCardWrapper.props('showPriceBig')).toBe(true);
        expect(basicCardWrapper.props('ticketIcon')).toBe(true);
        expect(basicCardWrapper.props('iconSize')).toBe('41');
        });
    });

    it('renders the OverallTicketCard components with correct props', () => {
        const wrapper = mount(TicketOverview);

        return wrapper.vm.$nextTick().then(() => {
            const overallTicketCards = wrapper.findAllComponents(OverallTicketCard);
            expect(overallTicketCards).toHaveLength(5);

            expect(overallTicketCards[0].props('heading')).toBe('Show tickets(s)');
            expect(overallTicketCards[0].props('subheading')).toBe('Display, download or add to wallet');
            expect(overallTicketCards[0].props('iconName')).toBe('ticket');

            expect(overallTicketCards[1].props('heading')).toBe('Manage personalization');
            expect(overallTicketCards[1].props('subheading')).toBe('Name, adress, phone you name it');
            expect(overallTicketCards[1].props('iconName')).toBe('manage');
        });
});
