package pixelsorter;

import selectors.DefaultSelector;

public abstract class ThresholdSelector extends DefaultSelector {
    //If i really dont have need for a range (f.E. the RandomSelector (hooh)

    public ThresholdSelector(int end, int min, int max){
        super(-1, end, min, max);
    }

    ThresholdSelector(int end){
        this(end, -1, 255);
    }


    @Override
    public void shiftRange(int val) {
        if(end+val >= 0)
        super.shiftRange(val);
        else
            setEnd(1);
    }

    @Override
    public void setStart(int start) {
        super.setStart(0);
    }
}
