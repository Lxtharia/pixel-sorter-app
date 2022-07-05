package pixelsorter;

import javax.swing.*;
import java.util.ArrayList;
import selectors.*;

public class myListModel extends DefaultListModel<DefaultSelector> {

    ArrayList<DefaultSelector> selectorArray;

    myListModel() {
        selectorArray = new ArrayList<>();
        selectorArray.add(new selectors.RandomSelector());
        selectorArray.add(new selectors.HueSelector(130, 270));
        selectorArray.add(new selectors.BrightnessSelector());
        selectorArray.add(new selectors.BlackWhiteSelector());
        selectorArray.add(new selectors.SaturationSelector());
//        selectorArray.add(new RedSelector());
//        selectorArray.add(new BlueSelector());
//        selectorArray.add(new GreenSelector());
    }

    @Override
    public DefaultSelector getElementAt(int index) {
        return selectorArray.get(index);
    }

    @Override
    public int getSize() {
        return selectorArray.size();
    }

    protected void fireContentsChanged(int index0, int index1) {
        super.fireContentsChanged(this, index0, index1);
    }
}
