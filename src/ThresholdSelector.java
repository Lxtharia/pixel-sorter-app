public abstract class ThresholdSelector extends Selector{
    //If i really dont have need for a range (f.E. the RandomSelector (hooh)

    ThresholdSelector(int end, int min, int max){
        super(-1, end, min, max);
    }

    ThresholdSelector(int end){
        this(end, -1, 255);
    }

    @Override
    public void setStart(int start) {
        //TODO: this ofc breaks stuff again (with the stackoverflow error u know?)
        super.setStart(0);
    }
}
