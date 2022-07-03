import processing.core.*;
import processing.event.*;

import javax.swing.*;

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
    private boolean defaultImgIsSet = true;
    private boolean showMask = false;
    //int loops = 1;

    public void setup() {

        //give all the Selectors this sketch so they have use Processing functions etc (it needs to be an instance for colorMode etc)
        DefaultSelector.sketch = this;

        //Load and set originalSizedImg and set surface size
        loadAndSetImg("default.png");
        defaultImgIsSet = true;
        shouldSurfaceHeight = 600;
        surfaceSizeIsOneToOne = false;

        //sorter and OptionPanel
        sorter = new PixelSorter(this, new MySelectors.HueSelector(125, 200), originalImg);
        optionPanel = new OptionPanel(this, sorter);

        // allow resize and update surface to image dimensions
        surface.setResizable(false);
        surface.setLocation(500, 10);
        updateSurfaceSize();
//        noLoop();
        colorMode(HSB, 360, 100, 100);
        //TODO:
        // If this is minimized, minimize the other shit (idk how)
        // If the optionPanel is minimized/maximized, min max this (which is on top anyway)
    }

    //=======CONFUSION=======

    public void loadAndSetImg(String filepath) {
        originalSizedImg = loadImage(filepath);
        originalImg = originalSizedImg.copy();
        updateSurfaceSize();
        defaultImgIsSet = false;
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
        if (doDraw) {
            doDraw = false;
            if (showMask) {

                image(sorter.getMaskedImage(originalImg), 0, 0, width, height);
            } else {
                img = sorter.sortImg(originalImg);
                image(img, 0, 0, width, height);
            }
        }
        if (defaultImgIsSet && mousePressed) {
            fill(0, 80);
            rect(0, 0, width, height);
        }
    }

    public boolean saveImg() {
        return img.save("export" + "_" + sorter.getXDirection() + sorter.getYDirection() + "_" + sorter.hashCode() + hour() + minute() + second() + ".png");
    }

    public void mouseWheel(MouseEvent e) {
        int val = -e.getCount();
        sorter.getSelector().shiftRange(val * 3);
//        println((sorter.getSelector()).getStart() + " : " + (sorter.getSelector()).getEnd());
        optionPanel.updateValueUI();
        drawAgain();
    }


    public void mouseReleased() {
        drawAgain();
    }

    public void keyPressed() {
        showMask = !showMask;
    }

    public void mousePressed() {
        if (defaultImgIsSet) {
            println("yes yes, we are selecting an img now lolll... ");
            optionPanel.chooseFile();
        }
    }

    public void setShowMask(boolean showMask) {
        this.showMask = showMask;
        drawAgain();
    }

    public boolean isShowMask() {
        return showMask;
    }

    //==============MAIN AND SETTING===============

    public void settings() {
        // use only numbers (not variables) for the size() command, Processing 3
        size(1, 1);
        noSmooth();
    }

    static public void main(String[] passedArgs) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] appletArgs = new String[]{"MyPixelSortApp"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }
}
