import processing.core.PApplet;
import processing.core.PImage;

enum xDirection {
    Right, Left, None
};

enum yDirection {
    Down, Up, None
};

public class PixelSorter {
    //PImage originalImg; //should be dealt with from outside
    MyPixelSortApp sketch;
    private PImage img;
    private DefaultSelector pixelSelector;
    private int row, column;
    //private Direction direction = Direction.Right;
    private xDirection xdir = xDirection.None;
    private yDirection ydir = yDirection.Down;

    PixelSorter(MyPixelSortApp sketch, DefaultSelector pixelSelector) {
        this.sketch = sketch;
        this.pixelSelector = pixelSelector;
    }

    PixelSorter(MyPixelSortApp sketch, DefaultSelector pixelSelector, PImage _img) {
        this(sketch, pixelSelector);
        setImg(_img);
    }


    //==================== FUNCTIONS =========

    public PImage getMaskedImage() {
        return getMaskedImage(this.img);
    }

    public PImage getMaskedImage(PImage _img) {
        PImage img = _img.copy();
        //visualizes which pixels would be sorted (by hue for example)
        img.loadPixels();
        for (int y = 0; y < img.height - 1; y++) {
            for (int x = 0; x < img.width - 1; x++) {
                if (pixelSelector.isValidConsideringInverted(img.pixels[x + y * img.width])) {
                    //IDK why but if this is commented out, it doesn't update correctly
                    img.pixels[x + y * img.width] = 0xFFFFFF; //white
                    //These also dont work well //TODO: fix this maybe
//                    img.pixels[x + y * img.width] = img.pixels[x + y * img.width];
//                    img.pixels[x + y * img.width] = PApplet.constrain(img.pixels[x + y * img.width] + 0x222222, 0, 0xFFFFFFFF); //whiter
                } else {
                    //TODO: Mask Color
                    img.pixels[x + y * img.width] = 0x000000; //black
                }
            }
        }
        img.updatePixels();
        return img;
    }

    //sets this.img, sorts it and returns it
    public PImage sortImg(PImage _img) {
        setImg(_img);
        return sortImg();
    }

    public PImage sortImg() {
        row = 0;
        column = 0;

        // loop through rows
        if (xdir != xDirection.None) {
            //println("Sorting Rows");
            while (row < img.height - 1) {
                img.loadPixels();
                sortRow();
                row++;
                img.updatePixels();
            }
        }

        // loop through columns
        if (ydir != yDirection.None) {
            //println("Sorting Columns");
            while (column < img.width - 1) {
                img.loadPixels();
                sortColumn();
                column++;
                img.updatePixels();
            }
        }

        return img.copy();
    }

    //==================== SORT FUNCTIONS

    private void sortRow() {
        // current row
        int y = row;

        // where to start sorting
        int x = 0;

        // where to stop sorting
        int xEnd = 0;

        while (xEnd < img.width - 1) {
            x = getSectionStartX(x, y);
            xEnd = getSectionEndX(x, y);

            if (x < 0) break;

            int sortingLength = xEnd - x;

            int[] unsorted = new int[sortingLength];
            int[] sorted = new int[sortingLength];

            for (int i = 0; i < sortingLength; i++) {
                unsorted[i] = img.pixels[x + i + y * img.width];
            }

            sorted = PApplet.sort(unsorted);
            if (xdir == xDirection.Left) sorted = PApplet.reverse(sorted);

            for (int i = 0; i < sortingLength; i++) {
                img.pixels[x + i + y * img.width] = sorted[i];
            }

            x = xEnd + 1;
        }
    }


    private void sortColumn() {
        // current column
        int x = column;

        // where to start sorting
        int y = 0;

        // where to stop sorting
        int yEnd = 0;

        while (yEnd < img.height - 1) {
            y = getSectionStartY(x, y);
            yEnd = getSectionEndY(x, y);

            if (y < 0) break;

            int sortingLength = yEnd - y;

            int[] unsorted = new int[sortingLength];
            int[] sorted = new int[sortingLength];

            for (int i = 0; i < sortingLength; i++) {
                unsorted[i] = img.pixels[x + (y + i) * img.width];
            }

            sorted = PApplet.sort(unsorted);
            if (ydir == yDirection.Up) sorted = PApplet.reverse(sorted);

            for (int i = 0; i < sortingLength; i++) {
                img.pixels[x + (y + i) * img.width] = sorted[i];
            }

            y = yEnd + 1;
        }
    }

    //==================== Section FUNCTIONS

    private int getSectionStartX(int x, int y) {
        while (pixelSelector.isValidConsideringInverted(img.pixels[x + y * img.width])) {
            x++;
            if (x >= img.width) return -1; //end of row
        }
        return x;
    }

    private int getSectionEndX(int x, int y) {
        x++;
        while (pixelSelector.isValidConsideringInverted(img.pixels[x + y * img.width])) {
            x++;
            if (x >= img.width) return img.width - 1; //end of section
        }
        return x - 1;
    }

    private int getSectionStartY(int x, int y) {
        if (y < img.height) {
            while (pixelSelector.isValidConsideringInverted(img.pixels[x + y * img.width])) {
                y++;
                if (y >= img.height) return -1; //end of column
            }
        }
        //found a valid one, this be the start
        return y;
    }

    private int getSectionEndY(int x, int y) {
        //int start = y; //add this to the while loop: && y<=start+70
        y++;
        if (y < img.height) {
            while (pixelSelector.isValidConsideringInverted(img.pixels[x + y * img.width])) {
                y++;
                if (y >= img.height) return img.height - 1; //end of section
            }
        }
        //found a not valid one, return the last valid one
        return y - 1;
    }

    //====================  GETTER / SETTER

    public void setImg(PImage _img) {
        this.img = _img.copy();
    }

    public void setSelector(DefaultSelector pixelSelector) {
        this.pixelSelector = pixelSelector;
        sketch.drawAgain();
    }

    public void setXDirection(xDirection direction) {
        this.xdir = direction;
        sketch.drawAgain();
    }

    public void setYDirection(yDirection direction) {
        this.ydir = direction;
        sketch.drawAgain();
    }


    //======GETTER=====

    public PImage getImg() {
        return this.img.copy();
    }

    public DefaultSelector getSelector() {
        return this.pixelSelector;
    }

    public xDirection getXDirection() {
        return this.xdir;
    }

    public yDirection getYDirection() {
        return this.ydir;
    }

}