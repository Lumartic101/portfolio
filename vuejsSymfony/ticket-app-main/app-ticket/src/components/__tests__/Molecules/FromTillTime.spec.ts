import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import FromTillTime from '@/components/Molecules/FromTillTime.vue';

describe('EventTicket', () => {
  const wrapper = mount(FromTillTime, {
    propsData: {
      startDate: new Date('10:00 May 31, 2023'),
      endDate: new Date('18:00 May 31, 2023'),
    },
  });
  it('renders correctly', () => {
    // Assert that the component renders without errors
    expect(wrapper.exists()).toBe(true);
  });
  it('correct time is displayed', () => {
    // Assert that specific elements or classes are present
    expect(wrapper.find('.time-container').text()).toContain('10:00-18:00');
  });
  it('more than 24 hours difference', () => {
    const wrapper = mount(FromTillTime, {
      propsData: {
        startDate: new Date('08:00 May 24, 2023'),
        endDate: new Date('20:00 May 31, 2023'),
      },
    });
    // Assert that correct time is displayed with different dates
    expect(wrapper.find('.time-container').text()).toContain('08:00-20:00');
  });
});
