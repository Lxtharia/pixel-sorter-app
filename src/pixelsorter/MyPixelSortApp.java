package pixelsorter;

import processing.core.*;
import processing.event.*;
import selectors.DefaultSelector;

import javax.swing.*;

public class MyPixelSortApp extends PApplet {
    /********

     ASDF Pixel Sort
     Kim Asendorf | 2010 | kimasendorf.com

     modified by Latharia :>
     ********/
    // image path is relative to sketch directory

    //WorkingImg is for freezing an image, so it can also be unfrozen and set back to the originalImg
    //Yes, I implement very unnecessary features
    private PImage originalImg;
    private PImage baseImg;
    private PImage sortedImg;
    private String lastSavedImagePath = null;
    PixelSorter sorter;
    OptionPanel optionPanel;
    private int shouldSurfaceHeight;
    private boolean surfaceSizeIsOneToOne;
    private boolean doDraw = true;
    private boolean defaultImgIsSet = true;
    private boolean showMask = false;
    private boolean drawBackground = false;

    public void setup() {
        //give all the Selectors this sketch, so they have use Processing functions etc. (it needs to be an instance for colorMode etc)
        DefaultSelector.sketch = this;

        //Load and set originalSizedImg and set surface size
        loadAndSetImg("default.png");
        defaultImgIsSet = true;
        shouldSurfaceHeight = 800;
        surfaceSizeIsOneToOne = false;

        //sorter and OptionPanel
        sorter = new PixelSorter(this, new selectors.HueSelector(125, 200), baseImg);
        optionPanel = new OptionPanel(this, sorter);

        // allow resize and update surface to image dimensions
        //frame.setIconImage(new ImageIcon("icon.png").getImage());
        surface.setIcon(loadImage("icon.png"));
        surface.setResizable(false);
        surface.setLocation(500, 10);
        updateSurfaceSize();
        colorMode(HSB, 360, 100, 100);
        //TODO:
        // If this is minimized, minimize the other shit (idk how)
        // If the optionPanel is minimized/maximized, min max this (which is on top anyway)
    }


    public boolean loadAndSetImg(String filepath) {
        //TODO: store filename in a variable to use in exported filename
        //because the shitty FileDialog can't hide these and using JFileChooser is even worse //what
        if (filepath.endsWith(".png") || filepath.endsWith(".jpg") || filepath.endsWith(".tga") || filepath.endsWith(".gif") || filepath.endsWith(".jpeg")) {
            PImage originalSizedImg = loadImage(filepath);
            return setImg(originalSizedImg);
        } else return false;
    }

    //IMAGE TOSSING
    public boolean setImg(PImage newImg) {
        if (newImg == null) return false;
        originalImg = newImg.copy();
        baseImg = newImg.copy();
        updateSurfaceSize();
        defaultImgIsSet = false;
        return true;
    }

    public void freezeImg() {
        //Save the image before setting it to the currently sorted image
        baseImg = sortedImg.copy();
        drawAgain();
    }

    public void unfreezeImg() {
        baseImg = originalImg.copy();
        drawAgain();
    }


    //========DRAW========
    public void drawAgain() {
        doDraw = true;
    }

    public void draw() {
        if (doDraw) {
            doDraw = false;
            if (showMask) {
                image(sorter.getMaskedImage(baseImg), 0, 0, width, height);
            } else {
                if (drawBackground) background(0);
                sortedImg = sorter.sortImg(baseImg);
                image(sortedImg, 0, 0, width, height);
            }
        }
    }

    //=======SURFACE CONFUSION=======
    //cause this takes current surfaceHeight and calculates depending on setImg ratio (yea)
    //and takes current surfaceSizeIsOneToOne to decide if to show 1:1 or not
    public void updateSurfaceSize() {
        if (surfaceSizeIsOneToOne) {
            surface.setSize(baseImg.width, baseImg.height);
        } else {
            surface.setSize((shouldSurfaceHeight * baseImg.width) / baseImg.height, shouldSurfaceHeight);
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


    public boolean saveImg() {
        //TODO: full path for the path-field bar
        String savePathName = "export" + "_" + hour() + minute() + second() + "_" + sorter.getXDirection().toString() + sorter.getYDirection().toString() + "_" + sortedImg.hashCode() + ".png";
        savePathName = sketchPath(savePathName);
        lastSavedImagePath = savePathName;
        return sortedImg.save(savePathName);
    }

    //CONTROLLED

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
            mousePressed = false;
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
        String[] appletArgs = new String[]{"pixelsorter.MyPixelSortApp"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    public void drawBackgroundForTransparentImages(boolean selected) {
        drawBackground = selected;
        drawAgain();
    }

    public boolean isDrawingBackground() {
        return drawBackground;
    }

    public String getLastSavedImagePath() {
        return lastSavedImagePath;
    }
}
