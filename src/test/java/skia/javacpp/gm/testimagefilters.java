package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class testimagefilters {
    static final float FILTER_WIDTH = SkIntToScalar(150);
    static final float FILTER_HEIGHT = SkIntToScalar(200);

    static SkImageFilter make0() { return new SkDownSampleImageFilter(SK_Scalar1 / 5); }
    static SkImageFilter make1() { return new SkOffsetImageFilter(SkIntToScalar(16), SkIntToScalar(16)); }
    static SkImageFilter make2() {
        SkColorFilter cf = SkColorFilter.CreateModeFilter(SK_ColorBLUE, SkXfermode.kSrcIn_Mode);
        return new SkColorFilterImageFilter(cf);
    }
    static SkImageFilter make3() {
        return new SkBlurImageFilter(8, 0);
    }

    static SkImageFilter make4() {
        SkImageFilter outer = new SkOffsetImageFilter(SkIntToScalar(16), SkIntToScalar(16));
        SkImageFilter inner = new SkDownSampleImageFilter(SK_Scalar1 / 5);
        return new SkComposeImageFilter(outer, inner);
    }
    static SkImageFilter make5() {
        SkImageFilter first = new SkOffsetImageFilter(SkIntToScalar(16), SkIntToScalar(16));
        SkImageFilter second = new SkDownSampleImageFilter(SK_Scalar1 / 5);
        return new SkMergeImageFilter(first, second);
    }

    static SkImageFilter make6() {
        SkImageFilter outer = new SkOffsetImageFilter(SkIntToScalar(16), SkIntToScalar(16));
        SkImageFilter inner = new SkDownSampleImageFilter(SK_Scalar1 / 5);
        SkImageFilter compose = new SkComposeImageFilter(outer, inner);

        SkColorFilter cf = SkColorFilter.CreateModeFilter(0x880000FF, SkXfermode.kSrcIn_Mode);

        SkImageFilter blue = new SkColorFilterImageFilter(cf);

        return new SkMergeImageFilter(compose, blue);
    }

    static SkImageFilter make7() {
        SkImageFilter outer = new SkOffsetImageFilter(SkIntToScalar(16), SkIntToScalar(16));
        SkImageFilter inner = make3();
        SkImageFilter compose = new SkComposeImageFilter(outer, inner);

        SkColorFilter cf = SkColorFilter.CreateModeFilter(0x880000FF, SkXfermode.kSrcIn_Mode);

        SkImageFilter blue = new SkColorFilterImageFilter(cf);

        return new SkMergeImageFilter(compose, blue);
    }

    static void draw0(SkCanvas canvas) {
        SkPaint p = new SkPaint();
        p.setAntiAlias(true);
        SkRect r = SkRect.MakeWH(FILTER_WIDTH, FILTER_HEIGHT);
        r.inset(SK_Scalar1 * 12, SK_Scalar1 * 12);
        p.setColor(SK_ColorRED);
        canvas.drawOval(r, p);
    }

    public static class TestImageFiltersGM extends GM {
        @Override
        protected String onShortName() {
            return "testimagefilters";
        }

        @Override
        protected SkISize onISize() { return SkISize.Make(700, 460); }

        @Override
        protected void onDraw(SkCanvas canvas) {
    //        this->drawSizeBounds(canvas, 0xFFCCCCCC);

            SkImageFilter[] gFilterProc = {
                    make0(), make1(), make2(), make3(), make4(), make5(), make6(), make7()
            };

            final SkRect bounds = SkRect.MakeWH(FILTER_WIDTH, FILTER_HEIGHT);

            final float dx = bounds.width() * 8 / 7;
            final float dy = bounds.height() * 8 / 7;

            canvas.translate(SkIntToScalar(8), SkIntToScalar(8));

            for (int i = 0; i < gFilterProc.length; ++i) {
                int ix = i % 4;
                int iy = i / 4;

                int saveCount = canvas.getSaveCount();
                canvas.save();
                try {
                    canvas.translate(ix * dx, iy * dy);

                    SkPaint p = new SkPaint();
                    p.setStyle(SkPaint.kStroke_Style);
                    canvas.drawRect(bounds, p);

                    SkPaint paint = new SkPaint();
                    paint.setImageFilter(gFilterProc[i]);
                    canvas.saveLayer(bounds, paint);
                    draw0(canvas);
                } finally {
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new TestImageFiltersGM();
        }
    };
}
