

public abstract class Selector {
    static MyPixelSortApp sketch;
    protected int start;
    protected int end;
    protected int min;
    protected int max;

    public Selector(int start, int end, int min, int max) {
        this.start = start;
        this.end = end;
        this.min = min;
        this.max = max;
    }

    public Selector(int start, int end) {
        this(start, end, 0, 255);
    }

    public Selector() {
        this(0, 90);
    }

    //=========================

    public boolean isValid(int pixel) {
        return pixel >= start && pixel <= end;
    }

    public void shiftRange(int val) {
        this.start += val;
        this.end += val;
        sketch.drawAgain();
    }

    //======GETTER/SETTER=======

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

    //if i set start and end should automatically adjust? //Leaving this here cause maybe maybe i might need it again H
    //this.end = val + (end-start);
    //this.start = val;

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        sketch.drawAgain();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        sketch.drawAgain();
    }


}
