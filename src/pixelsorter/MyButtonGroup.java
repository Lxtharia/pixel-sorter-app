package pixelsorter;

import javax.swing.*;
import java.util.ArrayList;

public class MyButtonGroup {

    ArrayList<JButton> buttons;

    public MyButtonGroup(JButton... bs) {
        buttons = new ArrayList<>();
        for(JButton b: bs)
            buttons.add(b);
    }
    //TODO: change the auto-"tab"-selected button to be always a specific component cause else it's annoying H
    void setSelected(JButton button) {
        for (JButton b : buttons) {
            if (b == button) {
                b.setEnabled(false);
            }
            else{
                b.setEnabled(true);
            }
        }
    }


}
