import processing.core.PApplet;

public abstract class Selectors {
    //=========SELECTORCLASSES==============

    public static PApplet sketch;

    static class HueSelector extends Selector {
        // sort all pixels whiter than the threshold
        HueSelector() {
            super(80, 130);
        }

        HueSelector(int s, int e) {
            super(s, e);
        }

        public boolean isValid(int pixel) {
            if( start%255 < end%255 )
                return (sketch.hue(pixel) >= start % 255 && sketch.hue(pixel) <= end % 255);
            else
                return (sketch.hue(pixel) >= start % 255 || sketch.hue(pixel) <= end % 255);
        }
    }

    static class InvertHueSelector extends Selector {
        // sort all pixels whiter than the threshold
        InvertHueSelector() {
            super(80, 130);
        }

        InvertHueSelector(int s, int e) {
            super(s, e);
        }

        public boolean isValid(int pixel) {
            if( start%255 < end%255 )
                return (sketch.hue(pixel) <= start % 255 || sketch.hue(pixel) >= end % 255);
            else
                return (sketch.hue(pixel) >= start % 255 && sketch.hue(pixel) <= end % 255);
        }
    }


    static class RandomSelector extends Selector {
        RandomSelector() {
            super(0, 100);
        }

        public boolean isValid(int pixel) {
            return 5 < sketch.random(end);
        }
    }


    static class SaturationSelector extends Selector {
        // sort all pixels whiter than the threshold
        SaturationSelector() {
            super(0, 127);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return sketch.saturation(pixel) > this.end;
        }
    }


    static class BrightnessSelector extends Selector {
        // sort all pixels whiter than the threshold
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


    static class WhiteSelector extends Selector {
        // sort all pixels whiter than the threshold
        WhiteSelector() {
            super(Integer.MIN_VALUE, -12345678);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return pixel > this.end;
        }

    }


    static class BlackSelector extends Selector {
        // sort all pixels whiter than the threshold
        BlackSelector() {
            super(Integer.MIN_VALUE,-3456789);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return pixel < this.end;
        }

        @Override
        public void shiftRange(int val) {
            super.shiftRange(val*30000);
        }
    }


    static class BrightSelector extends Selector {
        // sort all pixels whiter than the threshold
        BrightSelector() {
            super(0, 127);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return sketch.brightness(pixel) > this.end;
        }
    }

    static class DarkSelector extends Selector {
        // sort all pixels whiter than the threshold
        DarkSelector() {
            super(0, 223);
        }

        public boolean isValid(int pixel) {
            //println(pixel, this.value);
            return sketch.brightness(pixel) < this.end;
        }
    }
}
