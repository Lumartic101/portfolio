import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import Barcode from '@/components/Atoms/Barcode.vue';

describe('Barcode', () => {
    it('renders the component with the correct props', () => {
        const barcodeSrc = 'path/to/barcode.png';

        const wrapper = mount(Barcode, {
            props: {
                barcodeSrc,
            },
        });

        const imgElement = wrapper.find('img');

        expect(imgElement.exists()).toBe(true);
        expect(imgElement.attributes('src')).toBe(barcodeSrc);
        expect(imgElement.attributes('alt')).toBe('Barcode');
    });
});