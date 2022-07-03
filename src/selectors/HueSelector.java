package selectors;

public class HueSelector extends DefaultSelector {
    // sort all pixels whiter than the threshold
    public HueSelector(int s, int e) {
        super(s, e, 0, 360);
    }

    public HueSelector() {
        this(80, 130);
    }

    public boolean isValid(int pixel) {
        if( start%360 < end%360 )
            return (sketch.hue(pixel) >= start % 360 && sketch.hue(pixel) <= end % 360);
        else
            return (sketch.hue(pixel) >= start % 360 || sketch.hue(pixel) <= end % 360);
    }
}