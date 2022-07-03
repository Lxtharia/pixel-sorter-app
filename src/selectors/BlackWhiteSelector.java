package selectors;

public class BlackWhiteSelector extends DefaultSelector {
    // sort all pixels whiter than the bottom range but blacker than the top range
    public BlackWhiteSelector() {
        super(0x439EB2, 0xCB40EB, 0, 0xFFFFFF);
    }

    public boolean isValid(int pixel) {
        int p = pixel & 0XFFFFFF; //ignore alpha 0xFF______
        return (p>=start && p<end);
    }

    @Override
    public void shiftRange(int val) {
        super.shiftRange(val*0x00F000);
    }
}