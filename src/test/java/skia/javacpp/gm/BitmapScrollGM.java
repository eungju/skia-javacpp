package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class BitmapScrollGM extends GM {
    /** Create a bitmap image suitable for testing SkBitmap::scrollRect().
     *
     *  @param quarterWidth bitmap will be 4x this many pixels wide
     *  @param quarterHeight bitmap will be 4x this many pixels tall
     *  @param bitmap the bitmap data is written into this object
     */
    static void make_bitmap(int quarterWidth, int quarterHeight, SkBitmap bitmap) {
        SkPaint pRed = new SkPaint(), pWhite = new SkPaint(), pGreen = new SkPaint(), pBlue = new SkPaint(), pLine = new SkPaint(), pAlphaGray = new SkPaint();
        pRed.setColor(0xFFFF9999);
        pWhite.setColor(0xFFFFFFFF);
        pGreen.setColor(0xFF99FF99);
        pBlue.setColor(0xFF9999FF);
        pLine.setColor(0xFF000000);
        pLine.setStyle(SkPaint.kStroke_Style);
        pAlphaGray.setColor(0x66888888);

        // Prepare bitmap, and a canvas that draws into it.
        bitmap.reset();
        bitmap.setConfig(SkBitmap.kARGB_8888_Config,
                quarterWidth*4, quarterHeight*4);
        bitmap.allocPixels();
        SkCanvas canvas = new SkCanvas(bitmap);
        canvas.autoUnref();

        float w = SkIntToScalar(quarterWidth);
        float h = SkIntToScalar(quarterHeight);
        canvas.drawRectCoords(  0,   0, w*2, h*2, pRed);
        canvas.drawRectCoords(w*2,   0, w*4, h*2, pGreen);
        canvas.drawRectCoords(  0, h*2, w*2, h*4, pBlue);
        canvas.drawRectCoords(w*2, h*2, w*4, h*4, pWhite);
        canvas.drawRectCoords(w, h, w*3, h*3, pAlphaGray);
        canvas.drawLine(w*2,   0, w*2, h*4, pLine);
        canvas.drawLine(  0, h*2, w*4, h*2, pLine);
        canvas.drawRectCoords(w, h, w*3, h*3, pLine);
    }

    private boolean fInited;
    private void init() {
        if (fInited) {
            return;
        }
        fInited = true;
        // Create the original bitmap.
        make_bitmap(quarterWidth, quarterHeight, origBitmap);
    }

    public BitmapScrollGM() {
        fInited = false;
        this.setBGColor(0xFFDDDDDD);
    }

    @Override
    protected String onShortName() {
        return "bitmapscroll";
    }

    @Override
    protected SkISize onISize() {
        return make_isize(800, 600);
    }

    @Override
    protected void onDraw(SkCanvas canvas) {
        this.init();
        SkIRect scrollCenterRegion = SkIRect.MakeXYWH(
                quarterWidth, quarterHeight, quarterWidth*2+1, quarterHeight*2+1);
        int x = quarterWidth;
        int y = quarterHeight;
        int xSpacing = quarterWidth * 20;
        int ySpacing = quarterHeight * 16;

        // Draw left-hand text labels.
        drawLabel(canvas, "scroll entire bitmap",
                x, y, x, y + ySpacing);
        drawLabel(canvas, "scroll part of bitmap",
                x, y + ySpacing, x, y + ySpacing*2);
        x += 30;

        // Draw various permutations of scrolled bitmaps, scrolling a bit
        // further each time.
        draw9(canvas, x, y, null, quarterWidth*1/2, quarterHeight*1/2);
        draw9(canvas, x, y+ySpacing, scrollCenterRegion,
                quarterWidth*1/2, quarterHeight*1/2);
        x += xSpacing;
        draw9(canvas, x, y, null, quarterWidth*3/2, quarterHeight*3/2);
        draw9(canvas, x, y+ySpacing, scrollCenterRegion,
                quarterWidth*3/2, quarterHeight*3/2);
        x += xSpacing;
        draw9(canvas, x, y, null, quarterWidth*5/2, quarterHeight*5/2);
        draw9(canvas, x, y+ySpacing, scrollCenterRegion,
                quarterWidth*5/2, quarterHeight*5/2);
        x += xSpacing;
        draw9(canvas, x, y, null, quarterWidth*9/2, quarterHeight*9/2);
        draw9(canvas, x, y+ySpacing, scrollCenterRegion,
                quarterWidth*9/2, quarterHeight*9/2);
    }

    void drawLabel(SkCanvas canvas, String text, int startX, int startY,
                   int endX, int endY) {
        SkPaint paint = new SkPaint();
        paint.setColor(0xFF000000);
        SkPath path = new SkPath();
        path.moveTo(SkIntToScalar(startX), SkIntToScalar(startY));
        path.lineTo(SkIntToScalar(endX), SkIntToScalar(endY));
        canvas.drawTextOnPath(text, path, null, paint);
    }

    /** Stamp out 9 copies of origBitmap, scrolled in each direction (and
     *  not scrolled at all).
     */
    void draw9(SkCanvas canvas, int x, int y, SkIRect subset,
               int scrollX, int scrollY) {
        for (int yMult=-1; yMult<=1; yMult++) {
            for (int xMult=-1; xMult<=1; xMult++) {
                // Figure out the (x,y) to draw this copy at
                float bitmapX = SkIntToScalar(
                        x + quarterWidth * 5 * (xMult+1));
                float bitmapY = SkIntToScalar(
                        y + quarterHeight * 5 * (yMult+1));

                // Scroll a new copy of the bitmap, and then draw it.
                // scrollRect() should always return true, even if it's a no-op
                SkBitmap scrolledBitmap = new SkBitmap();
                boolean copyToReturnValue = origBitmap.copyTo(
                scrolledBitmap, origBitmap.config());
                SkASSERT(copyToReturnValue);
                boolean scrollRectReturnValue = scrolledBitmap.scrollRect(
                        subset, scrollX * xMult, scrollY * yMult);
                SkASSERT(scrollRectReturnValue);
                canvas.drawBitmap(scrolledBitmap, bitmapX, bitmapY);
            }
        }
    }

    private static final int quarterWidth = 10;
    private static final int quarterHeight = 14;
    private SkBitmap origBitmap = new SkBitmap();

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new BitmapScrollGM();
        }
    };
}
