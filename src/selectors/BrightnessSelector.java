package selectors;

public class BrightnessSelector extends DefaultSelector {
    BrightnessSelector() {
        super(80, 120);
    }

    public BrightnessSelector(int s, int e) {
        super(s, e);
    }

    public boolean isValid(int pixel) {
        return (sketch.brightness(pixel) >= start && sketch.brightness(pixel) <= end);
    }
}