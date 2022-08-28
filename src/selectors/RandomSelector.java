package selectors;

public class RandomSelector extends ThresholdSelector {
    public RandomSelector() {
        super(20, 0, 100);
    }

    public boolean isValid(int pixel) {
        return 1 < sketch.random(end);
    }
}
