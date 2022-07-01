import processing.core.PApplet;

public abstract class MySelectors {
    //=========SELECTORCLASSES==============

    public static PApplet sketch;


    static class HueSelector extends DefaultSelector {
        // sort all pixels whiter than the threshold
        HueSelector(int s, int e) {
            super(s, e, 0, 360);
        }

        HueSelector() {
            this(80, 130);
        }

        public boolean isValid(int pixel) {
            if( start%360 < end%360 )
                return (sketch.hue(pixel) >= start % 360 && sketch.hue(pixel) <= end % 360);
            else
                return (sketch.hue(pixel) >= start % 360 || sketch.hue(pixel) <= end % 360);
        }
    }

    static class RandomSelector extends ThresholdSelector {
        RandomSelector() {
            super(100, 0, 500);
        }

        public boolean isValid(int pixel) {
            return 5 < sketch.random(end);
        }
    }


    static class SaturationSelector extends DefaultSelector {
        SaturationSelector() {
            super(0, 127);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return sketch.saturation(pixel) > this.end;
        }
    }


    static class BrightnessSelector extends DefaultSelector {
        BrightnessSelector() {
            super(80, 120);
        }

        BrightnessSelector(int s, int e) {
            super(s, e);
        }

        public boolean isValid(int pixel) {
            return (sketch.brightness(pixel) >= start && sketch.brightness(pixel) <= end);
        }
    }


    static class BlackWhiteSelector extends DefaultSelector {
        // sort all pixels whiter than the bottom range but blacker than the top range
        BlackWhiteSelector() {
            super(0x439EB2, 0xCB40EB, 0, 0xFFFFFF);
        }

        public boolean isValid(int pixel) {
            int p = pixel & 0XFFFFFF; //ignore alpha 0xFF______
            return (p>=start && p<end);
        }

        @Override
        public void shiftRange(int val) {
            super.shiftRange(val*0x00F000);
        }
    }


}
