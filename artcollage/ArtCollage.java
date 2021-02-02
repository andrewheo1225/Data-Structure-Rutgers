/*************************************************************************
 *  Compilation:  javac ArtCollage.java
 *  Execution:    java ArtCollage Flo2.jpeg
 *
 *  @author:
 *
 *************************************************************************/

import java.awt.Color;

public class ArtCollage {

    // The orginal picture
    private Picture original;

    // The collage picture
    private Picture collage;

    // The collage Picture consists of collageDimension X collageDimension tiles
    private int collageDimension;

    // A tile consists of tileDimension X tileDimension pixels
    private int tileDimension;

    /*
     * One-argument Constructor 1. set default values of collageDimension to 4 and
     * tileDimension to 100 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename) {
      //edit
        this.collageDimension = 4;
        this.tileDimension = 100;
        original = new Picture(filename);
        int h = collageDimension * tileDimension;
        collage = new Picture(h, h);
        for (int col = 0; col < h;col++) {
            for (int row = 0; row < h; row++)
            {
                int col2 = col * original.width() / h;
                int row2 = row * original.height() / h;
                Color C = original.get(col2, row2);
                this.collage.set(col, row, C);
            }
        }
    }

    /*
     * Three-arguments Constructor 1. set default values of collageDimension to cd
     * and tileDimension to td 2. initializes original with the filename image 3.
     * initializes collage as a Picture of tileDimension*collageDimension x
     * tileDimension*collageDimension, where each pixel is black (see all
     * constructors for the Picture class). 4. update collage to be a scaled version
     * of original (see scaling filter on Week 9 slides)
     *
     * @param filename the image filename
     */
    public ArtCollage(String filename, int td, int cd) {
        this.collageDimension = cd;
        this.tileDimension = td;
        original = new Picture(filename);
        
        int h = tileDimension * collageDimension;
        collage = new Picture(h, h);

        for (int col = 0; col < h; col++) {
            for (int row = 0; row < h; row++)
            {
                int col2 = col * original.width() / h;
                int row2 = row * original.height() / h;
                Color C = original.get(col2, row2);
                this.collage.set(col, row, C);
            }
        }

    }

    /*
     * Returns the collageDimension instance variable
     *
     * @return collageDimension
     */
    public int getCollageDimension() {
        return this.collageDimension;
    }

    /*
     * Returns the tileDimension instance variable
     *
     * @return tileDimension
     */
    public int getTileDimension() {
        return this.tileDimension;
    }

    /*
     * Returns original instance variable
     *
     * @return original
     */
    public Picture getOriginalPicture() {
        return this.original;
    }

    /*
     * Returns collage instance variable
     *
     * @return collage
     */
    public Picture getCollagePicture() {
        return this.collage;
    }

    /*
     * Display the original image Assumes that original has been initialized
     */
    public void showOriginalPicture() {
        this.original.show();
    }

    /*
     * Display the collage image Assumes that collage has been initialized
     */
    public void showCollagePicture() {
        this.collage.show();

    }

    /*
     * Replaces the tile at collageCol,collageRow with the image from filename Tile
     * (0,0) is the upper leftmost tile
     *
     * @param filename image to replace tile
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void replaceTile(String filename, int collageCol, int collageRow) {
        int height = this.tileDimension;
        int collageDimension = getCollageDimension();
        int tileDimension = getTileDimension();
        int width = this.tileDimension;
        Picture originalPicture = new Picture(filename);
        for (int col = 0,
                colLocation = 0; col < collageDimension * tileDimension; col += tileDimension, colLocation++) {
            for (int row = 0,
                    rowLocation = 0; row < collageDimension * tileDimension; row += tileDimension, rowLocation++) {
                for (int col2 = 0; col2 < width; col2++) {
                    for (int row2 = 0; row2 < height; row2++) {
                        if (collageCol == colLocation && collageRow == rowLocation) {
                            int colTemp = col2 * originalPicture.width() / width;
                            int rowTemp = row2 * originalPicture.height() / height;
                            Color color = originalPicture.get(colTemp, rowTemp);
                            collage.set(col2 + col, row2 + row, color);
                        }
                    }
                }
            }
        }
      

    }

    /*
     * Makes a collage of tiles from original Picture original will have
     * collageDimension x collageDimension tiles, each tile has tileDimension X
     * tileDimension pixels
     */
    public void makeCollage() {
        Picture scaled = new Picture(tileDimension, tileDimension);
        for (int col = 0; col < tileDimension; col++) {
            for (int row = 0; row < tileDimension; row++)
            {
                int col2 = col * original.width() / tileDimension;
                int row2 = row * original.height() / tileDimension;
                Color color = original.get(col2, row2);
                scaled.set(col, row, color);
            }
        }
        
        for (int row1 = 0; row1 < collage.height()/tileDimension; row1++) {
            for (int col1 = 0; col1 < collage.height()/tileDimension; col1++) {
                for (int row2 = 0; row2 < scaled.height(); row2++) {
                    for (int col2 = 0; col2 < scaled.height(); col2++) {
                        Color color = scaled.get(row2, col2);
                        collage.set(row2+(row1*tileDimension), col2+col1*(tileDimension), color);
                    }
                }
            }
        }
    }

    /*
     * Colorizes the tile at (collageCol, collageRow) with component (see CS111 Week
     * 9 slides, the code for color separation is at the book's website)
     *
     * @param component is either red, blue or green
     * 
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */
    public void colorizeTile(String component, int collageCol, int collageRow) {
            for(int row = 0; row < collage.height()/tileDimension; row++) {
                for (int col = 0; col < collage.height()/tileDimension; col++) {
                    if (col== collageCol && row == collageRow ) {
                        for (int pRow = 0; pRow < tileDimension ; pRow++) {
                            for (int pCol = 0; pCol < tileDimension; pCol++) {
                                Color color = collage.get(pCol + (tileDimension * col), pRow + (tileDimension * row));
                                int red = color.getRed();
                                int green = color.getGreen();
                                int blue = color.getBlue();
                                    if(component.equals("red")) {
                                        collage.set(pCol + (tileDimension * col), pRow + (tileDimension * row), new Color(red, 0, 0));
                                    }
                                    else if(component.equals("green")) {
                                        collage.set(pCol + (tileDimension * col), pRow + (tileDimension * row), new Color(0, green, 0));
                                    }
                                    else if(component.equals("blue")){
                                        collage.set(pCol + (tileDimension * col), pRow + (tileDimension * row), new Color(0, 0, blue));
                                    }
                            }
                        }
                    }
                }
            }



    }

    /*
     * Grayscale tile at (collageCol, collageRow) (see CS111 Week 9 slides, the code
     * for luminance is at the book's website)
     *
     * @param collageCol tile column
     * 
     * @param collageRow tile row
     */

    public void grayscaleTile(int collageCol, int collageRow) {
        int tileDimension = getTileDimension();
        for( int col = 0; col< collage.height()/tileDimension; col++) {
            for (int row= 0; row < collage.height()/tileDimension; row++) {
                if (row == collageCol && col == collageRow ) { 
                    for (int pixelRow = 0; pixelRow < tileDimension ; pixelRow++) {
                        for (int pixelCol = 0; pixelCol < tileDimension; pixelCol++) {                          
                            Color color = collage.get(pixelCol + (tileDimension * row), pixelRow + (tileDimension * col));                           
                            this.collage.set(pixelCol + (tileDimension * row), pixelRow + (tileDimension * col), 
                            Luminance.toGray(color));
                        }
                    }
                }
            }
        }

    }

    /*
     *
     * Test client: use the examples given on the assignment description to test
     * your ArtCollage
     */
    public static void main(String[] args) {

    }
}