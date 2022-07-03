package pixelsorter;

import selectors.DefaultSelector;

import javax.swing.*;
import java.awt.*;

public class myListCellRenderer extends DefaultListCellRenderer.UIResource {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        String text =  value.getClass().getSimpleName();
        if (((DefaultSelector)value).isInverted()){
            text += " ⭮ ";
        }
        setText(text);
        return this;
    }
}
