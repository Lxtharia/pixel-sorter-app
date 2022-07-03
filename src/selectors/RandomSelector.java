package selectors;

import pixelsorter.ThresholdSelector;

public class RandomSelector extends ThresholdSelector {
    public RandomSelector() {
        super(100, 0, 500);
    }

    public boolean isValid(int pixel) {
        return 5 < sketch.random(end);
    }
}
