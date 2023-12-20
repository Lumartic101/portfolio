import { mount } from '@vue/test-utils';
import { describe, expect, it } from 'vitest';
import ImageHeader from '@/components/Atoms/ImageHeader.vue';

describe('ImageHeader', () => {
    it('renders the component with the correct props', () => {
        const imageSrc = 'path/to/image.jpg';

        const wrapper = mount(ImageHeader, {
            props: {
                imageSrc,
            },
        });

        const imageElement = wrapper.find('img');

        expect(imageElement.exists()).to.be.true;
        expect(imageElement.attributes('src')).to.equal(imageSrc);
        expect(imageElement.attributes('alt')).to.equal('Header Image');
    });
});
