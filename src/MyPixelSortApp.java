import processing.core.*;
import processing.event.*;

public class MyPixelSortApp extends PApplet {
    /********

     ASDF Pixel Sort
     Kim Asendorf | 2010 | kimasendorf.com

     modified by Latharia :>
     ********/

    // image path is relative to sketch directory
    private PImage originalImg;
    private PImage originalSizedImg;
    private PImage img;
    PixelSorter sorter;
    OptionPanel optionPanel;
    private int shouldSurfaceHeight;
    private boolean surfaceSizeIsOneToOne;
    private boolean doDraw = true;
    //int loops = 1;

    public void setup() {

        //give all the Selectors this sketch so they have use Processing functions etc (it needs to be an instance for colorMode etc)
        Selector.sketch = this;

        //Load and set originalSizedImg and set surface size
        loadAndSetImg("default", "png");
        shouldSurfaceHeight = 600;
        surfaceSizeIsOneToOne = false;

        //sorter and OptionPanel
        sorter = new PixelSorter(this, new Selectors.HueSelector(125, 200), originalImg);
        optionPanel = new OptionPanel(this, sorter);

        // allow resize and update surface to image dimensions
        surface.setResizable(false);
        surface.setLocation(500, 10);
        updateSurfaceSize();
//        noLoop();
    }

    //=======CONFUSION=======

    public void loadAndSetImg(String filename, String extention) {
        originalSizedImg = loadImage(filename + "." + extention);
        originalImg = originalSizedImg.copy();
        updateSurfaceSize();
    }

    //cause this takes current surfaceHeight and calculates depending on setImg ratio (yea)
    //and takes current surfaceSizeIsOneToOne to decide if to show 1:1 or not
    public void updateSurfaceSize() {
        if (surfaceSizeIsOneToOne) {
            surface.setSize(originalImg.width, originalImg.height);
        } else {
            surface.setSize((shouldSurfaceHeight * originalImg.width) / originalImg.height, shouldSurfaceHeight);
        }
        drawAgain();
    }

    public void setSurfaceSizeOneToOne(boolean fullSize) {
        surfaceSizeIsOneToOne = fullSize;
        updateSurfaceSize();
    }

    public boolean surfaceSizeIsOneToOne() {
        return surfaceSizeIsOneToOne;
    }

    public boolean resizeSurfaceToHeight(int newHeight) {
        if (newHeight >= 128 && newHeight <= 3000) {
            //Resized
            shouldSurfaceHeight = newHeight;
            updateSurfaceSize();
            return true;
        } else {
            System.out.println("Invalid new height :(");
            return false;
        }
    }

    public int getSurfaceHeight() {
        return shouldSurfaceHeight;
    }

//    private void resizeImg(int newHeight) {
//        //for low res stuff idk
//        originalImg = originalSizedImg.copy();
//        originalImg.resize((newHeight * originalSizedImg.width) / originalSizedImg.height, newHeight);
//        //the ratio stays the same hOh
//    }

    //========DRAW========
    public void drawAgain() {
        doDraw = true;
    }

    public void draw() {
        if (doDraw || mousePressed) {
            doDraw = false;
//            println("draw", frameRate, sorter.getSelector().getEnd());
            if (mousePressed) {
                image(sorter.visualizeSelection(originalImg), 0, 0, width, height);
            } else {
                img = sorter.sortImg(originalImg);
                image(img, 0, 0, width, height);
            }
        }
    }

    public void saveImg() {
        img.save("export" + "_" + sorter.getXDirection() + sorter.getYDirection() + "_" + sorter.hashCode() + hour() + minute() + second() + ".png");
    }

    public void mouseWheel(MouseEvent e) {
        int val = -e.getCount();
        sorter.getSelector().shiftRange(val * 3);
        println((sorter.getSelector()).getStart() + " : " + (sorter.getSelector()).getEnd());
        optionPanel.updateValueUI();
        drawAgain();
    }


    public void mouseReleased() {
        drawAgain();
    }

    //==============MAIN AND SETTING===============

    public void settings() {
        // use only numbers (not variables) for the size() command, Processing 3
        size(1, 1);
    }

    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"MyPixelSortApp"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
