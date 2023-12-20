import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import EventTicket from '@/components/Organisms/EventTicket.vue';

describe('EventTicket', () => {
  const wrapper = mount(EventTicket, {
    propsData: {
      imageSrc: 'https://upload.wikimedia.org/wikipedia/commons/e/e6/Bvf_haarlem_11_hq.jpeg',
      title: 'Event Title',
      country: 'Netherlands',
      numPeople: '10',
      startDate: new Date('10:00 May 31, 2023'),
      endDate: new Date('18:00 May 31, 2023'),
      ticketHolder: 'John Doe',
      ticketType: 'General Admission',
      orderId: 123456789,
      barcodeSrc: 'https://cdn-dfhjh.nitrocdn.com/BzQnABYFnLkAUVnIDRwDtFjmHEaLtdtL/assets/images/optimized/rev-c133d21/wp-content/uploads/2015/02/barcode-13.png',
    },
  });
  it('renders correctly', () => {
    // Assert that the component renders without errors
    expect(wrapper.exists()).toBe(true);
  });
  it('separate parts render correctly', () => {
    // Assert that specific elements or classes are present
    expect(wrapper.find('.title').text()).toBe('Event Title');
    expect(wrapper.find('.country').text()).toBe('Netherlands');
    expect(wrapper.find('.amount-people').text()).toBe('10');
    expect(wrapper.find('.date').text()).toContain('31 May 2023');
    expect(wrapper.find('.time').text()).toContain('10:00-18:00');
    expect(wrapper.find('.detail').text()).toContain('John Doe');
    expect(wrapper.find('.detail').text()).toContain('General Admission');
    expect(wrapper.find('.detail').text()).toContain(123456789);
    expect(wrapper.find('.barcode').exists()).toBeTruthy();
    expect(wrapper.find('.header > img').exists()).toBeTruthy();
  });
});
