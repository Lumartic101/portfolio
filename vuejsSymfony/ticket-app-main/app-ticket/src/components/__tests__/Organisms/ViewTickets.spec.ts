import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import ViewTickets from '@/components/Organisms/ViewTickets.vue';
import BasicCard from '@/components/Organisms/BasicCard.vue';
import Button from '@/components/Atoms/BasicButton.vue';

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
      date: '2023-06-30',
      beginTime: '19:00',
      endTime: '22:00'
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
      date: '2023-07-15',
      beginTime: '15:00',
      endTime: '23:00'
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
      date: '2023-08-05',
      beginTime: '09:00',
      endTime: '17:00'
    }
  }
];

describe('ViewTickets', () => {
  it('renders the BasicCard component with correct props', async () => {
    const wrapper = mount(ViewTickets, {
      props: {
        tickets: mockTickets
      }
    });

    await wrapper.vm.$nextTick();

    const basicCardWrapper = wrapper.findComponent(BasicCard);
    expect(basicCardWrapper.props('label')).toBe('Concert');
    expect(basicCardWrapper.props('price')).toBe('180,00');
    expect(basicCardWrapper.props('totalTickets')).toBe(3);
    expect(basicCardWrapper.props('tekstInIcon')).toBe(true);
    expect(basicCardWrapper.props('tekstNextToIcon')).toBe(true);
    expect(basicCardWrapper.props('showPrice')).toBe(false);
    expect(basicCardWrapper.props('buttonClass')).toBe(false);
    expect(basicCardWrapper.props('showPriceBig')).toBe(true);
    expect(basicCardWrapper.props('ticketIcon')).toBe(true);
    expect(basicCardWrapper.props('iconSize')).toBe('41');
  });

  it('renders the personalized tickets correctly', async () => {
    const wrapper = mount(ViewTickets, {
      props: {
        tickets: mockTickets
      }
    });

    (wrapper.vm as any).personalizedTickets = mockTickets.filter(ticket => ticket.holderName !== '');
    (wrapper.vm as any).notPersonalizedTickets = mockTickets.filter(ticket => ticket.holderName === '');

    await wrapper.vm.$nextTick();

    const personalizedCards = wrapper.findAll('.card-view-prosonalized');
    expect(personalizedCards.length).toBe(2);

    const firstPersonalizedCard = personalizedCards[0];
    expect(firstPersonalizedCard.findComponent(BasicCard).props('label')).toBe('John Doe');
    expect(firstPersonalizedCard.findComponent(BasicCard).props('secLabel')).toBe('Concert');
    expect(firstPersonalizedCard.findComponent(BasicCard).props('price')).toBe('100');

    const secondPersonalizedCard = personalizedCards[1];
    expect(secondPersonalizedCard.findComponent(BasicCard).props('label')).toBe('Jane Smith');
    expect(secondPersonalizedCard.findComponent(BasicCard).props('secLabel')).toBe('Festival');
    expect(secondPersonalizedCard.findComponent(BasicCard).props('price')).toBe('50');
  });

  it('renders the "Download All" button', async () => {
    const wrapper = mount(ViewTickets, {
      props: {
        tickets: mockTickets
      }
    });

    await wrapper.vm.$nextTick();

    const downloadButton = wrapper.findComponent(Button);
    expect(downloadButton.exists()).toBe(true);
  });

  it('renders the not personalized tickets correctly', async () => {
    const wrapper = mount(ViewTickets, {
      props: {
        tickets: mockTickets
      }
    });

    await wrapper.vm.$nextTick();

    const notPersonalizedCards = wrapper.findAll('.card-view-not-personalized');
    expect(notPersonalizedCards.length).toBe(1);

    const notPersonalizedCard = notPersonalizedCards[0];
    expect(notPersonalizedCard.findComponent(BasicCard).props('label')).toBe('ORD789');
    expect(notPersonalizedCard.findComponent(BasicCard).props('secLabel')).toBe('Conference');
    expect(notPersonalizedCard.findComponent(BasicCard).props('price')).toBe('30');
  });

});
