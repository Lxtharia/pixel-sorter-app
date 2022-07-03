package selectors;

public class SaturationSelector extends DefaultSelector {
    public SaturationSelector() {
        super(0, 127);
    }

    public boolean isValid(int pixel) {
        //println(pixel, this.value);
        return sketch.saturation(pixel) > this.end;
    }
}