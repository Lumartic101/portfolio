import { shallowMount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import DateTime from '@/components/Atoms/DateTime.vue';

describe('DateTime', () => {
  it('renders the component', () => {
    const wrapper = shallowMount(DateTime);

    expect(wrapper.exists()).toBe(true);
  });

  it('renders the full date and time by default', () => {
    const date = new Date('2023-05-31T10:00:00');

    const wrapper = shallowMount(DateTime, {
      props: {
        date,
      },
    });

    expect(wrapper.text()).toBe('31 May 2023 at 10:00');
  });

  it('renders date only when dateOnly prop is true', () => {
    const date = new Date('2023-05-31T10:00:00');

    const wrapper = shallowMount(DateTime, {
      props: {
        date,
        dateOnly: true,
      },
    });

    expect(wrapper.text()).toBe('31 May 2023');
  });

  it('renders time only when timeOnly prop is true', () => {
    const date = new Date('2023-05-31T10:00:00');

    const wrapper = shallowMount(DateTime, {
      props: {
        date,
        timeOnly: true,
      },
    });

    expect(wrapper.text()).toBe('10:00');
  });
});
