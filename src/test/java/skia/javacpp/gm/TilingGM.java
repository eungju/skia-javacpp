package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;
import static skia.javacpp.utils.*;

public class TilingGM extends GM {
    static void makebm(SkBitmap bm, int config, int w, int h) {
        bm.setConfig(config, w, h);
        bm.allocPixels();
        bm.eraseColor(0);

        SkCanvas    canvas = new SkCanvas(bm);
        SkPoint[]     pts = { SkPoint.Make(0, 0), SkPoint.Make(SkIntToScalar(w), SkIntToScalar(h)) };
        int[]     colors = { SK_ColorRED, SK_ColorGREEN, SK_ColorBLUE };
        float[]    pos = { 0, SK_Scalar1/2, SK_Scalar1 };
        SkPaint     paint = new SkPaint();

        SkUnitMapper   um = null;

        um = new SkCosineMapper();
    //    um = new SkDiscreteMapper(12);

        try {
            paint.setDither(true);
            paint.setShader(SkGradientShader.CreateLinear(pts, colors, pos, SkShader.kClamp_TileMode, um)).unref();
            canvas.drawPaint(paint);
        } finally {
            SkSafeUnref(um);
        }
        canvas.unref();
    }

    static void setup(SkPaint paint, SkBitmap bm, boolean filter,
                      int tmx, int tmy) {
        SkShader shader = SkShader.CreateBitmapShader(bm, tmx, tmy);
        paint.setShader(shader).unref();
        paint.setFilterBitmap(filter);
    }

    static final int[] gConfigs = {
            SkBitmap.kARGB_8888_Config,
            SkBitmap.kRGB_565_Config,
            SkBitmap.kARGB_4444_Config
            };
    static final int gWidth = 32;
    static final int gHeight = 32;

    private SkBlurDrawLooper    fLooper;

    public TilingGM() {
        fLooper = new SkBlurDrawLooper(SkIntToScalar(1), SkIntToScalar(2), SkIntToScalar(2),
                0x88000000);
        for (int i = 0; i < gConfigs.length; i++) {
            fTexture[i] = new SkBitmap();
            makebm(fTexture[i], gConfigs[i], gWidth, gHeight);
        }
    }

    public SkBitmap[]    fTexture = new SkBitmap[gConfigs.length];

    @Override
    protected String onShortName() {
        return "tilemodes";
    }

    @Override
    protected SkISize onISize() { return make_isize(880, 560); }

    @Override
    protected void onDraw(SkCanvas canvas) {

        SkRect r = SkRect.MakeLTRB(0, 0, SkIntToScalar(gWidth*2), SkIntToScalar(gHeight*2));

        final String[] gConfigNames = { "8888", "565", "4444" };

        final boolean[]           gFilters = { false, true };
        final String[]          gFilterNames = {     "point",                     "bilinear" };

        final int[] gModes = { SkShader.kClamp_TileMode, SkShader.kRepeat_TileMode, SkShader.kMirror_TileMode };
        final String[] gModeNames = {    "C",                    "R",                   "M" };

        float y = SkIntToScalar(24);
        float x = SkIntToScalar(10);

        for (int kx = 0; kx < gModes.length; kx++) {
            for (int ky = 0; ky < gModes.length; ky++) {
                SkPaint p = new SkPaint();
                String str;
                p.setAntiAlias(true);
                p.setDither(true);
                p.setLooper(fLooper);
                str = String.format("[%s,%s]", gModeNames[kx], gModeNames[ky]);

                p.setTextAlign(SkPaint.kCenter_Align);
                canvas.drawText(str, x + r.width()/2, y, p);

                x += r.width() * 4 / 3;
            }
        }

        y += SkIntToScalar(16);

        for (int i = 0; i < gConfigs.length; i++) {
            for (int j = 0; j < gFilters.length; j++) {
                x = SkIntToScalar(10);
                for (int kx = 0; kx < gModes.length; kx++) {
                    for (int ky = 0; ky < gModes.length; ky++) {
                        SkPaint paint = new SkPaint();
                        setup(paint, fTexture[i], gFilters[j], gModes[kx], gModes[ky]);
                        paint.setDither(true);

                        canvas.save();
                        canvas.translate(x, y);
                        canvas.drawRect(r, paint);
                        canvas.restore();

                        x += r.width() * 4 / 3;
                    }
                }
                {
                    SkPaint p = new SkPaint();
                    String str;
                    p.setAntiAlias(true);
                    p.setLooper(fLooper);
                    str = String.format("%s, %s", gConfigNames[i], gFilterNames[j]);
                    canvas.drawText(str, x, y + r.height() * 2 / 3, p);
                }

                y += r.height() * 4 / 3;
            }
        }
    }
}
