package selectors;

public class BrightnessSelector extends DefaultSelector {

    public boolean isValid(int pixel) {
        return (sketch.brightness(pixel) >= start && sketch.brightness(pixel) <= end);
    }
}