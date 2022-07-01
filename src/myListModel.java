import javax.swing.*;
import java.util.ArrayList;

public class myListModel extends DefaultListModel<DefaultSelector> {

    ArrayList<DefaultSelector> selectorArray;

    myListModel() {
        selectorArray = new ArrayList<>();
        selectorArray.add(new MySelectors.RandomSelector());
        selectorArray.add(new MySelectors.BrightnessSelector(50, 90));
        selectorArray.add(new MySelectors.HueSelector(125, 200));
        selectorArray.add(new MySelectors.BlackWhiteSelector());
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
