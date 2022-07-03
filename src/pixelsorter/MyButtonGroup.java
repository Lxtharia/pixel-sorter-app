package pixelsorter;

import javax.swing.*;
import java.awt.*;
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
//                b.setEnabled(false);
//                b.setBorder(BorderFactory.createLineBorder(new Color(0x3376A5), 2, true));
                b.setBackground(new Color(0xB6B6B6));
                b.setBorderPainted(false);
            }
            else{
//                b.setEnabled(true);
                b.setBackground(new Color(0x9F9F9F));
                b.setBorderPainted(true);
            }
        }
    }


}
