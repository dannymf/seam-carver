/* *****************************************************************************
 *  Name:    Daniel Friedman
 *  NetID:   df19
 *  Precept: P01
 *
 *  Description:  Seam-carving is a content-aware image resizing technique
 *                where the image is reduced in size by one pixel of height
 *                (or width) at a time.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class SeamCarver {
    // image to be resized
    private Picture picture;
    // width and height of image
    private int width, height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Picture is null");
        width = picture.width();
        height = picture.height();
        // create a deep copy of client's picture
        this.picture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        // returns a deep copy so instance picture is immutable by client
        Picture toClient = new Picture(picture);
        return toClient;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        inBounds(x, y);
        // x-gradient
        int xRight;
        // if x is not a right-border pixel, calculate as usual
        if (x != width - 1) xRight = picture.getRGB(x + 1, y);
            // otherwise wrap-around
        else xRight = picture.getRGB(0, y);
        // convert RGB int into red, green, and blue colors
        /* @citation Adapted from: https://algs4.cs.princeton.edu/code/javadoc/
        edu/princeton/cs/algs4/Picture.html. Accessed 04/10/2020. */
        int r1 = (xRight >> 16) & 0xFF;
        int g1 = (xRight >> 8) & 0xFF;
        int b1 = (xRight >> 0) & 0xFF;

        int xLeft;
        // if x is not a left-border pixel, calculate as usual
        if (x != 0) xLeft = picture.getRGB(x - 1, y);
        else xLeft = picture.getRGB(width - 1, y);
        int r2 = (xLeft >> 16) & 0xFF;
        int g2 = (xLeft >> 8) & 0xFF;
        int b2 = (xLeft >> 0) & 0xFF;
        int gradX = ((r1 - r2) * (r1 - r2) + (g1 - g2) * (g1 - g2) +
                (b1 - b2) * (b1 - b2));

        // y-gradient
        int yUp;
        if (y != 0) yUp = picture.getRGB(x, y - 1);
            // if y is a top-border pixel wrap-around
        else yUp = picture.getRGB(x, height - 1);
        int r3 = (yUp >> 16) & 0xFF;
        int g3 = (yUp >> 8) & 0xFF;
        int b3 = (yUp >> 0) & 0xFF;

        int yDown;
        if (y != height - 1) yDown = picture.getRGB(x, y + 1);
        else yDown = picture.getRGB(x, 0);
        int r4 = (yDown >> 16) & 0xFF;
        int g4 = (yDown >> 8) & 0xFF;
        int b4 = (yDown >> 0) & 0xFF;
        int gradY = ((r3 - r4) * (r3 - r4) + (g3 - g4) * (g3 - g4) +
                (b3 - b4) * (b3 - b4));

        return Math.sqrt(gradX + gradY);
    }

    // private helper to take transpose of image
    private Picture transpose(Picture preTranspose) {
        // create a new image with dimensions switching width/height
        Picture transpose = new Picture(preTranspose.height(),
                                        preTranspose.width());
        for (int i = 0; i < transpose.height(); i++) {
            for (int j = 0; j < transpose.width(); j++) {
                // switch columns and rows
                int pictureRGB = preTranspose.getRGB(i, j);
                transpose.setRGB(j, i, pictureRGB);
            }
        }
        return transpose;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // store shortest distances to every pixel
        double[][] shortestDistTo = new double[height][width];
        // store edge that gives shortest distance to this pixel
        int[][] edgeTo = new int[height][width];
        // shortest distance to all pixels in top row of image are just their
        // respective energy values
        for (int v = 0; v < width; v++) {
            shortestDistTo[0][v] = energy(v, 0);
        }
        // relax the edges using dynamic programming. When traversing the array
        // column by column from top to bottom, this is a traversal in
        // topological order so you will only need to relax each edge once
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // path to any pixel can only be from one of these pixels
                double topLeft, topRight, topMiddle;
                // check whether all of these pixels exist without
                // wrapping-around
                if (j != 0) topLeft = shortestDistTo[i - 1][j - 1];
                    // if top-left pixel doesn't exist, cannot be shortest distance
                else topLeft = Double.POSITIVE_INFINITY;
                if (j != width - 1) topRight = shortestDistTo[i - 1][j + 1];
                else topRight = Double.POSITIVE_INFINITY;
                topMiddle = shortestDistTo[i - 1][j];
                // find minimum distance to pixel
                double shortestPath = Math.min(Math.min(topLeft, topRight),
                                               topMiddle);
                // shortest path is minimum distance of the three
                // above-adjacent pixels and the current pixel's energy value
                shortestDistTo[i][j] = shortestPath + energy(j, i);
                // set edgeTo[] to the pixel which is closest
                if (shortestPath == topLeft) edgeTo[i][j] = j - 1;
                else if ((shortestPath == topRight)) edgeTo[i][j] = j + 1;
                else edgeTo[i][j] = j;
            }
        }
        // since we have all the pixels with all of their minimum distances,
        // minimum energy seam is smallest value in bottom row of array
        double champ = Double.POSITIVE_INFINITY;
        // keep track of champ's index to eventually locate seam
        int champIndex = 0;
        for (int q = 0; q < width; q++) {
            if (shortestDistTo[height - 1][q] < champ) {
                champ = shortestDistTo[height - 1][q];
                champIndex = q;
            }
        }
        // seam to be returned
        int[] verticalSeam = new int[height];
        // trace path backwards (starting from bottom row) and moving upwards
        for (int q = height - 1; q >= 0; q--) {
            verticalSeam[q] = champIndex;
            // iterate to next champIndex
            champIndex = edgeTo[q][champIndex];
        }
        return verticalSeam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        // create a new image as the transpose of the original
        picture = transpose(picture);
        // set width and height to those of transpose
        height = picture.height();
        width = picture.width();
        // use findVerticalSeam() of transpose
        int[] horizontalSeam = findVerticalSeam();
        // transpose the image back and reset the width and height
        picture = transpose(picture);
        height = picture.height();
        width = picture.width();
        return horizontalSeam;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(
                "No seam to remove");
        if (seam.length != height) throw new IllegalArgumentException(
                "Illegal seam length");
        // create new picture which is one less pixel wide
        Picture carved = new Picture(width - 1, height);
        for (int i = 0; i < height; i++) {
            inBounds(seam[i], i);
            // verify that two adjacent seam entries differ by no more than 1
            if (i >= 1 && Math.abs(seam[i] - seam[i - 1]) > 1)
                throw new IllegalArgumentException("Input seam not valid");
            for (int j = 0; j < width; j++) {
                // if not yet reached pixel to be deleted, copy pixels to new
                // picture as normal
                if (j < seam[i]) {
                    int preCarvedRGB = picture.getRGB(j, i);
                    carved.setRGB(j, i, preCarvedRGB);
                }
                // if we've passed pixel to be deleted, copy pixel shifted
                // one place to left in new image
                else if (j > seam[i]) {
                    int preCarvedRGB = picture.getRGB(j, i);
                    carved.setRGB(j - 1, i, preCarvedRGB);
                }
                // if j == seam[i], delete this pixel, so continue
            }
        }
        // reset picture to carved image
        picture = carved;
        width--;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException(
                "No seam to remove");
        // transpose image and set height to that of transpose
        picture = transpose(picture);
        height = picture.height();
        width = picture.width();
        // remove vertical seem of transpose, corresponding to horizontal seam
        removeVerticalSeam(seam);
        // reverse transpose operations
        picture = transpose(picture);
        height = picture.height();
        width = picture.width();
    }


    // check whether coordinates are in bounds
    private void inBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("coordinate not in bounds");
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        SeamCarver test = new SeamCarver(picture);
        StdOut.println("Original dimensions (width x height): " + test.width()
                               + " x " + test.height());
        int randomWidth = StdRandom.uniform(0, test.width());
        int randomHeight = StdRandom.uniform(0, test.height());
        StdOut.println("Energy of random pixel: " + test.energy(
                randomWidth, randomHeight));
        int[] verticalSeam = test.findVerticalSeam();
        test.removeVerticalSeam(verticalSeam);
        int[] horizontalSeam = test.findHorizontalSeam();
        test.removeHorizontalSeam(horizontalSeam);
        StdOut.println("New dimensions: " + test.width()
                               + " x " + test.height());
        test.picture().show();
    }
}
