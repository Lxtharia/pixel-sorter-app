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
        selectorArray.add(new Selectors.InvertHueSelector(50, 90));
        selectorArray.add(new Selectors.WhiteSelector());
        selectorArray.add(new Selectors.BlackSelector());
    }

    @Override
    public Selector getElementAt(int index) {
        return selectorArray.get(index);
    }

    @Override
    public int getSize() {
        return selectorArray.size();
    }


}
