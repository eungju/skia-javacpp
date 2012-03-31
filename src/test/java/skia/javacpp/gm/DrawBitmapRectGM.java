package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class DrawBitmapRectGM extends GM {
    static void makebm(SkBitmap bm, int config, int w, int h) {
        bm.setConfig(config, w, h);
        bm.allocPixels();
        bm.eraseColor(0);

        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();

        float wScalar = SkIntToScalar(w);
        float hScalar = SkIntToScalar(h);

        SkPoint     pt = SkPoint.Make(wScalar / 2, hScalar / 2);

        float    radius = 4 * SkMaxScalar(wScalar, hScalar);

        int[]     colors = { SK_ColorRED, SK_ColorYELLOW,
                SK_ColorGREEN, SK_ColorMAGENTA,
                SK_ColorBLUE, SK_ColorCYAN,
                SK_ColorRED};

        float[]    pos = {0,
                SK_Scalar1 / 6,
                2 * SK_Scalar1 / 6,
                3 * SK_Scalar1 / 6,
                4 * SK_Scalar1 / 6,
                5 * SK_Scalar1 / 6,
                SK_Scalar1};

        SkPaint     paint = new SkPaint();
        paint.setShader(SkGradientShader.CreateRadial(
                pt, radius,
                colors, pos,
        SkShader.kRepeat_TileMode)).unref();
        SkRect rect = SkRect.MakeWH(wScalar, hScalar);
        SkMatrix mat = SkMatrix.I();
        for (int i = 0; i < 4; ++i) {
            paint.getShader().setLocalMatrix(mat);
            canvas.drawRect(rect, paint);
            rect.inset(wScalar / 8, hScalar / 8);
            mat.postScale(SK_Scalar1 / 4, SK_Scalar1 / 4);
        }
    }

    static final int gSize = 1024;

    public SkBitmap    fLargeBitmap = new SkBitmap();

    @Override
    protected String onShortName() {
        return "drawbitmaprect";
    }

    @Override
    protected SkISize onISize() { return make_isize(gSize, gSize); }

    @Override
    protected void onDraw(SkCanvas canvas) {
        final int kBmpSize = 2048;
        if (fLargeBitmap.isNullPixels() ) {
            makebm(fLargeBitmap,
                    SkBitmap.kARGB_8888_Config,
                    kBmpSize, kBmpSize);
        }
        SkRect dstRect = SkRect.MakeLTRB(0, 0, SkIntToScalar(64), SkIntToScalar(64));
        final int kMaxSrcRectSize = 1 << (SkNextLog2(kBmpSize) + 2);

        final int kPadX = 30;
        final int kPadY = 40;
        SkPaint paint = new SkPaint();
        paint.setAlpha(0x20);
        canvas.drawBitmapRect(fLargeBitmap, null,
                SkRect.MakeWH(gSize * SK_Scalar1,
                gSize * SK_Scalar1),
        paint);
        canvas.translate(SK_Scalar1 * kPadX / 2,
                SK_Scalar1 * kPadY / 2);
        SkPaint blackPaint = new SkPaint();
        float titleHeight = SK_Scalar1 * 24;
        blackPaint.setColor(SK_ColorBLACK);
        blackPaint.setTextSize(titleHeight);
        blackPaint.setAntiAlias(true);
        String title;
        title = String.format("Bitmap size: %d x %d", kBmpSize, kBmpSize);
        canvas.drawText(title, 0,
                titleHeight, blackPaint);

        canvas.translate(0, SK_Scalar1 * kPadY / 2  + titleHeight);
        int rowCount = 0;
        canvas.save();
        for (int w = 1; w <= kMaxSrcRectSize; w *= 4) {
            for (int h = 1; h <= kMaxSrcRectSize; h *= 4) {

                SkIRect srcRect = SkIRect.MakeXYWH((kBmpSize - w) / 2,
                        (kBmpSize - h) / 2,
                        w, h);
                canvas.drawBitmapRect(fLargeBitmap, srcRect, dstRect);

                String label;
                label = String.format("%d x %d", w, h);
                blackPaint.setAntiAlias(true);
                blackPaint.setStyle(SkPaint.kFill_Style);
                blackPaint.setTextSize(SK_Scalar1 * 10);
                float baseline = dstRect.height() +
                        blackPaint.getTextSize() + SK_Scalar1 * 3;
                canvas.drawText(label,
                        0, baseline,
                        blackPaint);
                blackPaint.setStyle(SkPaint.kStroke_Style);
                blackPaint.setStrokeWidth(SK_Scalar1);
                blackPaint.setAntiAlias(false);
                canvas.drawRect(dstRect, blackPaint);

                canvas.translate(dstRect.width() + SK_Scalar1 * kPadX, 0);
                ++rowCount;
                if ((dstRect.width() + kPadX) * rowCount > gSize) {
                    canvas.restore();
                    canvas.translate(0, dstRect.height() + SK_Scalar1 * kPadY);
                    canvas.save();
                    rowCount = 0;
                }
            }
        }
    }

}
