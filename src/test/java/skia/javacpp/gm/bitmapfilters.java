package skia.javacpp.gm;

import static skia.javacpp.core.*;

public class bitmapfilters {
    static void make_bm(SkBitmap bm) {
        final int[] colors = {
                SK_ColorRED, SK_ColorGREEN,
                SK_ColorBLUE, SK_ColorWHITE
        };
        int[] colorsPM = new int[4];
        for (int i = 0; i < colors.length; ++i) {
            colorsPM[i] = SkPreMultiplyColor(colors[i]);
        }
        SkColorTable ctable = new SkColorTable(colorsPM);

        bm.setConfig(SkBitmap.kIndex8_Config, 2, 2);
        bm.allocPixels(ctable);
        ctable.unref();

        bm.getAddr8(0, 0).put((byte) 0);
        bm.getAddr8(1, 0).put((byte) 1);
        bm.getAddr8(0, 1).put((byte) 2);
        bm.getAddr8(1, 1).put((byte) 3);
    }

    static float draw_bm(SkCanvas canvas, SkBitmap bm,
                         float x, float y, SkPaint paint) {
        canvas.drawBitmap(bm, x, y, paint);
        return SkIntToScalar(bm.width()) * 5/4;
    }

    static float draw_set(SkCanvas c, SkBitmap bm, float x,
                          SkPaint p) {
        x += draw_bm(c, bm, x, 0, p);
        p.setFilterBitmap(true);
        x += draw_bm(c, bm, x, 0, p);
        p.setDither(true);
        return x + draw_bm(c, bm, x, 0, p);
    }

    static final String[] gConfigNames = {
            "unknown config",
            "A1",
            "A8",
            "Index8",
            "565",
            "4444",
            "8888"
    };

    static float draw_row(SkCanvas canvas, SkBitmap bm) {
        int saveCount = canvas.getSaveCount();
        canvas.save();
        try {
            SkPaint paint = new SkPaint();
            float x = 0;
            final int scale = 32;

            paint.setAntiAlias(true);
            final String name = gConfigNames[bm.config()];
            canvas.drawText(name, x, SkIntToScalar(bm.height())*scale*5/8,
                    paint);
            canvas.translate(SkIntToScalar(48), 0);

            canvas.scale(SkIntToScalar(scale), SkIntToScalar(scale));

            x += draw_set(canvas, bm, 0, paint);
            paint.reset();
            paint.setAlpha(0x80);
            draw_set(canvas, bm, x, paint);
            return x * scale / 3;
        } finally {
            canvas.restoreToCount(saveCount);
        }
    }

    public static class FilterGM extends GM {
        private boolean fOnce;
        void init() {
            if (fOnce) {
                return;
            }
            fOnce = true;
            make_bm(fBM8);
            fBM8.copyTo(fBM4444, SkBitmap.kARGB_4444_Config);
            fBM8.copyTo(fBM16, SkBitmap.kRGB_565_Config);
            fBM8.copyTo(fBM32, SkBitmap.kARGB_8888_Config);
        }
        public SkBitmap fBM8 = new SkBitmap(), fBM4444 = new SkBitmap(), fBM16 = new SkBitmap(), fBM32 = new SkBitmap();

        FilterGM() {
            fOnce = false;
            this.setBGColor(0xFFDDDDDD);
        }

        @Override
        protected String onShortName() {
            return "bitmapfilters";
        }

        @Override
        protected SkISize onISize() {
            return make_isize(540, 330);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            this.init();

            float x = SkIntToScalar(10);
            float y = SkIntToScalar(10);

            canvas.translate(x, y);
            y = draw_row(canvas, fBM8);
            canvas.translate(0, y);
            y = draw_row(canvas, fBM4444);
            canvas.translate(0, y);
            y = draw_row(canvas, fBM16);
            canvas.translate(0, y);
            draw_row(canvas, fBM32);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new FilterGM();
        }
    };
}
