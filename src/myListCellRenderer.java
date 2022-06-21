import javax.swing.*;
import java.awt.*;

public class myListCellRenderer extends DefaultListCellRenderer.UIResource {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        setText(value.getClass().getSimpleName());
        return this;
    }
}
