# <p align="center"> Seam Carving Algorithm </p>

## Description
Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. A vertical seam in an image is a path of pixels connected from the top to the bottom with one pixel in each row; a horizontal seam is a path of pixels connected from the left to the right with one pixel in each column. Below left is the original 505-by-287 pixel image; below right is the result after removing 150 vertical seams, resulting in a 30% narrower image. Unlike standard content-agnostic resizing techniques (such as cropping and scaling), seam carving preserves the most interest features (aspect ratio, set of objects present, etc.) of the image.

Not-resized | Resized
:-------------------------:|:-------------------------:
![not_resized](https://www.cs.princeton.edu/courses/archive/spring21/cos226/assignments/seam/images/HJoceanSmall.png) |![not_resized](https://www.cs.princeton.edu/courses/archive/spring21/cos226/assignments/seam/images/HJoceanSmall357x285.png)

---

### An Image That Would Work Well
One that that has a foreground that contrasts starkly with the background, and has several key elements that are the main part of the image, where if parts of them were shown in a smaller version of the image, the image wouldn't lose too much of its value. An image with a more monochrome background and a colorful foreground would also work well, since the seams here would be part of the background which is less crucial to the image. An image that wouldn't work well: a random image, since the pixels to be removed will be relatively arbitrary, since nothing is background and nothing is foreground. Alternatively, an image where there are many items very close together with little background, such as a family picture with the whole family standing right up against each other, so that few of the pixels in between family members can be removed without causing significant difference to the image.
