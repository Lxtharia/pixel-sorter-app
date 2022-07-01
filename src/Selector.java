

public abstract class Selector {
    static MyPixelSortApp sketch;
    //TODO: make these private and create a file for each Selector (cause idfk)
    //TODO: add getName() and getInvertedName()
    protected int start;
    protected int end;
    //Loose min and max values, they are just to adjust the sliders :>
    protected int min;
    protected int max;
    protected boolean inverted;

    public Selector(int start, int end, int min, int max) {
        this.start = start;
        this.end = end;
        this.min = min;
        this.max = max;
        this.inverted = false;
    }

    public Selector(int start, int end) {
        this(start, end, 0, 255);
    }

    //=========================

    public abstract boolean isValid(int pixel);


    public boolean isValidConsideringInverted(int pixel){
        return isValid(pixel) == !inverted;
    }

    public void shiftRange(int val) {
        if(start+val < end) { //to prevent overflow :>
            setStart(start + val);
            setEnd(end + val);
        }
    }

    //======GETTER/SETTER=======
    //if i set start and end should automatically adjust? Leaving this here cause maybe maybe i might need it again H
    //this.end = val + (end-start);
    //this.start = val;

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
        sketch.drawAgain();
    }

    public int getEnd() {
        return this.end;
    }

    public void setEnd(int end) {
        this.end = end;
        sketch.drawAgain();
    }


    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (min < max) this.min = min;
        sketch.drawAgain();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (min < max) this.max = max;
        sketch.drawAgain();
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
        sketch.drawAgain();
    }
}
