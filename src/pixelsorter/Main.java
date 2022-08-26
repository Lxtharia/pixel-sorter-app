package pixelsorter;

import processing.core.PApplet;

import javax.swing.*;

import static processing.core.PApplet.concat;

public class Main {
    public static void main(String[] passedArgs) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] appletArgs = new String[]{"pixelsorter.MyPixelSortApp"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
