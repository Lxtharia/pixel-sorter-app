import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;

public class myListModel extends DefaultListModel<Selector> {

    ArrayList<Selector> selectorArray;

    myListModel() {
        selectorArray = new ArrayList<>();
        selectorArray.add(new Selectors.RandomSelector());
        selectorArray.add(new Selectors.BrightnessSelector(50, 90));
        selectorArray.add(new Selectors.HueSelector(125, 200));
        selectorArray.add(new Selectors.BlackWhiteSelector());
    }

    @Override
    public Selector getElementAt(int index) {
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
