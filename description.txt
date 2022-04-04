/* *****************************************************************************
 *  Name: Daniel Friedman
 *  NetID: df19
 *  Precept: P01
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Hours to complete assignment (optional): 8
 *
 **************************************************************************** */

Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
Finding a vertical seam: I used a dynamic programming approach. Assuming we
have the solution to the shortest path to a pixel in the row above, the only
way to get to a pixel in the row below is using the path from the upper-left,
upper-right, or upper-middle pixel. Therefore, the shortest path to the pixel
in the current (lower) row is the minimum of the shortest path between these
three pixels plus the energy value of the pixel below (which corresponds to the
edge weight of the path to this lower pixel). I stored all of the shortest
distances in a 2D double array corresponding to the pixels of the image. In
order to get the first row's shortest distances, this is just their respective
energy values. I also stored the pixel used in the upper row to get to this
current lower row's pixel in an edgeTo double 2D boolean array in order to
eventually track the path of the past of smallest energy and return these
pixels.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
An image that would work well: one that that has a foreground that contrasts
starkly with the background, and has several key elements that are the main
part of the image, where if parts of them were shown in a smaller version of
the image, the image wouldn't lose too much of its value. An image with a more
monochrome background and a colorful foreground would also work well, since the
seams here would be part of the background which is less crucial to the image.

An image that wouldn't work well: a random image, since the pixels to be
removed will be relatively arbitrary, since nothing is background and nothing
is foreground. Alternatively, an image where there are many items very close
together with little background, such as a family picture with the whole family
standing right up against each other, so that few of the pixels in between
family members can be removed without causing significant difference to the
image.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250          0.306
500          0.634               2.072
1000         0.986               1.555
2000         1.99                2.018        1.013
2000         1.714               1.738        0.797
Average 2000 1.852               1.87         0.905
4000         3.543               1.91
4000         4.143               2.237
Average 4000 3.843               2.075        1.053
8000         8.683               2.259
8000         7.49                1.949
Average 8000 8.087               2.104        1.073
16000        18.489              2.286
16000        17.442              2.157
Average 16000 17.966             2.221        1.15
32000        43.504
32000        43.436
Average 32000 43.470             2.420        1.275


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250          0.362
250          0.366
Average 250  0.364
500          0.574
500          0.534
Average 500  0.554               1.522        0.606
1000         1.015
1000         1.088
Average 1000 1.051               1.9          0.926
2000         2.151
2000         1.733
Average 2000 1.942               1.848        0.886
4000         3.868
4000         3.499
Average 4000 3.6835              1.9          0.926
8000         8.837
8000         7.091
Average 8000 7.964               2.162        1.112
16000        19.838
16000        16.423
Average 16000 18.131             2.28         1.19
32000        44.347
32000        41.476
Average 32000 42.912             2.367        1.243

/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 6.7*10^-9 * W^1.2 * H^1.3
       _______________________________________
       For the exponents of both W and H, I used the average log ratio for
       their respective inputs of 32000, since the ratios were increasing and
       thus converging towards that value. I used the input for constant height
       (2000) and width of 32000 and used the formula
       T(n, m) = a * H^b(h) * W^b(w) to compute the formula as above. Plugging
       in, I got 42.912 = a * (2000)^1.275 * (32000)^1.243. Solving for a
       gives a = 6.666 * 10^-9. Rounding everything to two significant digits
       gives the result as above.

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */
Danqi Liao (office hours)

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */


/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */
n/a

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
I really enjoyed this assignment, especially because debugging (while it did
take a few hours) wasn't terrible and extremely tedious like some of the
previous assignments, while at the same time giving me a conceptual
understanding of shortest paths and the fact that by traversing the array as I
did in this assignment allows me to relax all the edges just once. It also
produces an output that can be easily explained to friends about the usefulness
of computer science and how useful this could be in practice, which is always
a plus. Thanks!
