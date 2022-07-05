package selectors;

public class SaturationSelector extends DefaultSelector {
    public boolean isValid(int pixel) {
        return (sketch.saturation(pixel) >= start && sketch.saturation(pixel) <= end);
    }
}