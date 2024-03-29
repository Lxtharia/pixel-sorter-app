package pixelsorter;

import processing.core.*;
import processing.event.*;
import selectors.DefaultSelector;
import javax.swing.*;
import java.nio.file.Paths;

public class MyPixelSortApp extends PApplet {
    /********

     App by Latharia :>

     core functionality by:
     ASDF Pixel Sort
     Kim Asendorf | 2010 | kimasendorf.com
     ********/
    // image path is relative to sketch directory

    //WorkingImg is for freezing an image, so it can also be unfrozen and set back to the originalImg
    //Yes, I implement very unnecessary features
    private PImage originalImg;
    private PImage baseImg;
    private PImage sortedImg;
    private String lastSavedImagePath = null;
    private String currentFilename;
    PixelSorter sorter;
    OptionPanel optionPanel;
    private int shouldSurfaceHeight = 600;
    private boolean surfaceSizeIsOneToOne = false;
    private boolean doDraw = true;
    private boolean defaultImgIsSet = true;
    private boolean showMask = false;
    private boolean drawBackground = false;

    //TODO:
    // If this is minimized, minimize the other shit (idk how)
    // If the optionPanel is minimized/maximized, min max this (which is on top anyway)

    public void setup() {
        surface.setLocation(500, 10);
        background(0x2b2b2b);
        text("Loading...", 20, height-20);
        // allow resize and update surface to image dimensions
        surface.setResizable(false);
        surface.setIcon(loadImage("icon.png"));

        //pass this sketch to the Selectors, so they can use Processing functions etc. (it needs to be an instance for colorMode etc)
        DefaultSelector.sketch = this;

        //Load and set originalSizedImg
        loadAndSetImg("default.png");
        defaultImgIsSet = true;
        //sorter and OptionPanel
        sorter = new PixelSorter(this, new selectors.HueSelector(125, 200), baseImg);
        //This takes a while to start H
        optionPanel = new OptionPanel(this, sorter);

        //Processing options
        colorMode(HSB, 360, 100, 100);
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

    //======IMAGES AND FREEZING======

    public boolean loadAndSetImg(String filepath) {
        //because the shitty FileDialog can't hide files, so we need to check manually which file type was selected
        if (filepath.endsWith(".png") || filepath.endsWith(".jpg") || filepath.endsWith(".tga") || filepath.endsWith(".gif") || filepath.endsWith(".jpeg")) {
            PImage originalSizedImg = loadImage(filepath);
            //get filename
            String filename = Paths.get(filepath).getFileName().toString();
            //strip extension
            currentFilename = filename.substring(0, filename.lastIndexOf('.'));
            //if something went wrong we choose again
            return setImg(originalSizedImg);
        } else return false;
    }

    public boolean setImg(PImage newImg) {
        //set a new originalImage
        if (newImg == null) return false;
        originalImg = newImg.copy();
        baseImg = newImg.copy();
        updateSurfaceSize();
        defaultImgIsSet = false;
        return true;
    }

    public void freezeImg() {
        //Set the base image to the currently displayed (sorted) image
        baseImg = sortedImg.copy();
        drawAgain();
    }

    public void unfreezeImg() {
        //Set the base image to the originally loaded image
        baseImg = originalImg.copy();
        drawAgain();
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

    public void setSurfaceSizeOneToOne(boolean fullSize) {
        surfaceSizeIsOneToOne = fullSize;
        updateSurfaceSize();
    }

    public int getSurfaceHeight() {
        return shouldSurfaceHeight;
    }

    public boolean surfaceSizeIsOneToOne() {
        return surfaceSizeIsOneToOne;
    }


    public boolean saveImg() {
        //TODO: full path for the path-field bar
        //String savePathName = "export" + "_" + hour() + minute() + second() + "_" + sorter.getXDirection().toString() + sorter.getYDirection().toString() + "_" + sortedImg.hashCode() + ".png";
        String savePathName = "exp_" + currentFilename + "_" + hour() + minute() + second() + millis() % 1000 + ".png";
        savePathName = sketchPath(savePathName);
        lastSavedImagePath = savePathName;
        return sortedImg.save(savePathName);
    }

    //CONTROLLED

    public void mouseWheel(MouseEvent e) {
        int val = -e.getCount();
        println(key, keyCode);
        if (keyPressed && keyCode == SHIFT)
            sorter.getSelector().shiftRange(val * 10);
        else
            sorter.getSelector().shiftRange(val);
        optionPanel.updateValueUI();
        drawAgain();
    }


    public void mouseReleased() {
        drawAgain();
    }

    public void keyPressed() {
        if (key == ' ') {
            showMask = !showMask;
            optionPanel.updateUI();
            drawAgain();
        }
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

    public boolean isMaskShown() {
        return showMask;
    }

    //==============MAIN AND SETTING===============

    public void settings() {
        // use only numbers (not variables) for the size() command, Processing 3
        size(757, 600);
//        size(1,1);
        noSmooth();
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
