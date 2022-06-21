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
    private int shouldSurfaceHeight = 500;
    private boolean doDraw = true;
    //int loops = 1;

    public void setup() {

        //give all the Selectors this sketch so they have use Processing functions etc (it needs to be an instance for colorMode etc)
        Selector.sketch = this;

        //Load and set originalSizedImg
        loadAndSetImg("mousepad", "jpg");

        //sorter and OptionPanel
        sorter = new PixelSorter(this, new Selectors.HueSelector(), originalImg);
        optionPanel = new OptionPanel(this, sorter);

        // allow resize and update surface to image dimensions
        surface.setResizable(false);
        surface.setLocation(500, 10);

        resizeSurface(800);
//        noLoop();
    }

    //=======CONFUSION=======

    public void loadAndSetImg(String filename, String extention) {
        originalSizedImg = loadImage(filename + "." + extention);
        originalImg = originalSizedImg.copy();
        resizeSurface();
    }

    private void resizeImg(int newHeight) {
        //for low res stuff idk
        originalImg = originalSizedImg.copy();
        originalImg.resize((newHeight * originalSizedImg.width) / originalSizedImg.height, newHeight);
        //the ratio stays the same hOh
    }

    public void resizeSurface() {
        resizeSurface(shouldSurfaceHeight);
    }

    public void resizeSurface(int newHeight) {
        if (newHeight >= 128) {
            //Resized
            surface.setSize((newHeight * originalImg.width) / originalImg.height, newHeight);
        } else if (newHeight <= 0) {
            //100%
            surface.setSize(originalImg.width, originalImg.height);
        } else
            System.out.println("Doing nothing. Set to someting bigger than 128px or <= 0 to show image 1:1");

    }


    private void setShouldSurfaceHeight(int newHeight) {
        if (newHeight >= 128 || newHeight <= 0) {
            shouldSurfaceHeight = newHeight;
        } else System.out.println("Set to someting bigger than 128px or <= 0 to show image 1:1");
    }


    //========DRAW========

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

    public void drawAgain(){
        doDraw = true;
    }

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
