package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class arithmode {
    static final int WW = 100;
    static final int HH = 32;

    static SkBitmap make_bm() {
        SkBitmap bm = new SkBitmap();
        bm.setConfig(SkBitmap.kARGB_8888_Config, WW, HH);
        bm.allocPixels();
        bm.eraseColor(0);
        return bm;
    }

    static SkBitmap make_src() {
        SkBitmap bm = make_bm();
        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();
        SkPaint paint = new SkPaint();
        SkPoint pts[] = { SkPoint.Make(0, 0), SkPoint.Make(SkIntToScalar(WW), SkIntToScalar(HH)) };
        int[] colors = {
                SK_ColorBLACK, SK_ColorGREEN, SK_ColorCYAN,
                SK_ColorRED, SK_ColorMAGENTA, SK_ColorWHITE
        };
        SkShader s = SkGradientShader.CreateLinear(pts, colors, null, SkShader.kClamp_TileMode);
        paint.setShader(s).unref();
        canvas.drawPaint(paint);
        return bm;
    }

    static SkBitmap make_dst() {
        SkBitmap bm = make_bm();
        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();
        SkPaint paint = new SkPaint();
        SkPoint pts[] = { SkPoint.Make(0, SkIntToScalar(HH)), SkPoint.Make(SkIntToScalar(WW), 0) };
        int[] colors = {
                SK_ColorBLUE, SK_ColorYELLOW, SK_ColorBLACK, SK_ColorGREEN, SK_ColorGRAY
        };
        SkShader s = SkGradientShader.CreateLinear(pts, colors, null, SkShader.kClamp_TileMode);
        paint.setShader(s).unref();
        canvas.drawPaint(paint);
        return bm;
    }

    static SkBitmap make_arith(SkBitmap src, SkBitmap dst,
                               float[] k) {
        SkBitmap bm = make_bm();
        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();
        SkPaint paint = new SkPaint();
        canvas.drawBitmap(dst, 0, 0, null);
        SkXfermode xfer = SkArithmeticMode.Create(k[0], k[1], k[2], k[3]);
        paint.setXfermode(xfer).unref();
        canvas.drawBitmap(src, 0, 0, paint);
        return bm;
    }

    static void show_k_text(SkCanvas canvas, float x, float y, float[] k) {
        SkPaint paint = new SkPaint();
        paint.setTextSize(SkIntToScalar(24));
        paint.setAntiAlias(true);
        for (int i = 0; i < 4; ++i) {
            String str = String.valueOf(k[i]).replaceFirst("\\.?0+$", "");
            float width = paint.measureText(str);
            canvas.drawText(str, x, y + paint.getTextSize(), paint);
            x += width + SkIntToScalar(10);
        }
    }

    public static class ArithmodeGM extends GM {
        @Override
        protected String onShortName() {
            return "arithmode";
        }

        @Override
        protected SkISize onISize() { return SkISize.Make(640, 480); }

        @Override
        protected void onDraw(SkCanvas canvas) {
            SkBitmap src = make_src();
            SkBitmap dst = make_dst();

            final float one = SK_Scalar1;
            final float[] K = {
                    0, 0, 0, 0,
                    0, 0, 0, one,
                    0, one, 0, 0,
                    0, 0, one, 0,
                    0, one, one, 0,
                    0, one, -one, 0,
                    0, one/2, one/2, 0,
                    0, one/2, one/2, one/4,
                    0, one/2, one/2, -one/4,
                    one/4, one/2, one/2, 0,
                    -one/4, one/2, one/2, 0,
            };

            int i = 0;
            float y = 0;
            float gap = SkIntToScalar(src.width() + 20);
            while (i < K.length) {
                float[] k = new float[4];
                System.arraycopy(K, i, k, 0, 4);
                float x = 0;
                SkBitmap res = make_arith(src, dst, k);
                canvas.drawBitmap(src, x, y, null);
                x += gap;
                canvas.drawBitmap(dst, x, y, null);
                x += gap;
                canvas.drawBitmap(res, x, y, null);
                x += gap;
                show_k_text(canvas, x, y, k);
                i += 4;
                y += SkIntToScalar(src.height() + 12);
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ArithmodeGM();
        }
    };
}
