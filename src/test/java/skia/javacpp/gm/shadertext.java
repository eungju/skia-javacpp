package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;
import static skia.javacpp.utils.*;

public class shadertext {
    static void makebm(SkBitmap bm, int config, int w, int h) {
        bm.setConfig(config, w, h);
        bm.allocPixels();
        bm.eraseColor(0);

        SkCanvas canvas = new SkCanvas(bm);
        canvas.autoUnref();
        float    s = SkIntToScalar(SkMin32(w, h));
        SkPoint[]     pts = { SkPoint.Make(0, 0), SkPoint.Make(s, s) };
        int[]     colors = { SK_ColorRED, SK_ColorGREEN, SK_ColorBLUE };
        float[]    pos = { 0, SK_Scalar1/2, SK_Scalar1 };
        SkPaint     paint = new SkPaint();

        SkUnitMapper   um = null;

        um = new SkCosineMapper();

        new SkAutoUnref(um);

        paint.setDither(true);
        paint.setShader(SkGradientShader.CreateLinear(pts, colors, pos, SkShader.kClamp_TileMode, um)).unref();
        canvas.drawPaint(paint);
    }

    static SkShader MakeBitmapShader(int tx, int ty, int w, int h) {
        SkBitmap bmp = new SkBitmap();
        if (bmp.isNull()) {
            makebm(bmp, SkBitmap.kARGB_8888_Config, w/2, h/4);
        }
        return SkShader.CreateBitmapShader(bmp, tx, ty);
    }

    static class GradData {
        int             fCount;
        final int[]  fColors;
        final float[] fPos;
        public GradData(int count, int[] colors, float[] pos) {
            fCount = count;
            fColors = new int[count];
            System.arraycopy(colors, 0, fColors, 0, count);
            fPos = pos;
        }
    };

    static final int[] gColors = {
            SK_ColorRED, SK_ColorGREEN, SK_ColorBLUE, SK_ColorWHITE, SK_ColorBLACK
    };

    static final GradData[] gGradData = {
            new GradData(2, gColors, null),
            new GradData(5, gColors, null),
    };

    static final GradMaker MakeLinear = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data, int tm, SkUnitMapper mapper) {
            return SkGradientShader.CreateLinear(pts, data.fColors, data.fPos, tm, mapper);
        }
    };

    static final GradMaker MakeRadial = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data, int tm, SkUnitMapper mapper) {
            SkPoint center = new SkPoint();
            center.set(SkScalarAve(pts[0].fX(), pts[1].fX()),
                    SkScalarAve(pts[0].fY(), pts[1].fY()));
            return SkGradientShader.CreateRadial(center, center.fX(), data.fColors,
                    data.fPos, tm, mapper);
        }
    };

    static final GradMaker MakeSweep = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data, int tm, SkUnitMapper mapper) {
            SkPoint center = new SkPoint();
            center.set(SkScalarAve(pts[0].fX(), pts[1].fX()),
                    SkScalarAve(pts[0].fY(), pts[1].fY()));
            return SkGradientShader.CreateSweep(center.fX(), center.fY(), data.fColors,
                    data.fPos, mapper);
        }
    };

    static final GradMaker Make2Radial = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data, int tm, SkUnitMapper mapper) {
            SkPoint center0 = new SkPoint(), center1 = new SkPoint();
            center0.set(SkScalarAve(pts[0].fX(), pts[1].fX()),
                    SkScalarAve(pts[0].fY(), pts[1].fY()));
            center1.set(SkScalarInterp(pts[0].fX(), pts[1].fX(), SkIntToScalar(3)/5),
                    SkScalarInterp(pts[0].fY(), pts[1].fY(), SkIntToScalar(1)/4));
            return SkGradientShader.CreateTwoPointRadial(
                    center1, (pts[1].fX() - pts[0].fX()) / 7,
                    center0, (pts[1].fX() - pts[0].fX()) / 2,
                    data.fColors, data.fPos, tm, mapper);
        }
    };

    static interface GradMaker {
        public SkShader apply(SkPoint[] pts, GradData data, int tm, SkUnitMapper mapper);
    }
    static final GradMaker[] gGradMakers = {
            MakeLinear, MakeRadial, MakeSweep, Make2Radial
    };

    public static class ShaderTextGM extends GM {
        public ShaderTextGM() {
            this.setBGColor(0xFFDDDDDD);
        }

        @Override
        protected String onShortName() {
            return "shadertext";
        }

        @Override
        protected SkISize onISize() { return make_isize(1450, 500); }

        @Override
        protected void onDraw(SkCanvas canvas) {
            String text = "Shaded Text";
            final int textLen = text.length();
            final int pointSize = 36;

            int w = pointSize * textLen;
            int h = pointSize;

            SkPoint[] pts = {
                SkPoint.Make(0, 0),
                SkPoint.Make(SkIntToScalar(w), SkIntToScalar(h))
            };
            float textBase = SkIntToScalar(h/2);

            int tileModes[] = {
                    SkShader.kClamp_TileMode,
                    SkShader.kRepeat_TileMode,
                    SkShader.kMirror_TileMode
            };

            final int gradCount = gGradData.length * gGradMakers.length;
            final int bmpCount = tileModes.length * tileModes.length;
            SkShader[] shaders = new SkShader[gradCount + bmpCount];

            int shdIdx = 0;
            for (int d = 0; d < gGradData.length; ++d) {
                for (int m = 0; m < gGradMakers.length; ++m) {
                    shaders[shdIdx++] = gGradMakers[m].apply(pts,
                            gGradData[d],
                            SkShader.kClamp_TileMode,
                            null);
                }
            }
            for (int tx = 0; tx < tileModes.length; ++tx) {
                for (int ty = 0; ty < tileModes.length; ++ty) {
                    shaders[shdIdx++] = MakeBitmapShader(tileModes[tx],
                            tileModes[ty],
                            w/8, h);
                }
            }

            SkPaint paint = new SkPaint();
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setTextSize(SkIntToScalar(pointSize));

            canvas.save();
            canvas.translate(SkIntToScalar(20), SkIntToScalar(10));

            SkPath path = new SkPath();
            path.arcTo(SkRect.MakeXYWH(SkIntToScalar(-40), SkIntToScalar(15),
                    SkIntToScalar(300), SkIntToScalar(90)),
                    SkIntToScalar(225), SkIntToScalar(90),
                    false);
            path.close();

            final int testsPerCol = 8;
            final int rowHeight = 60;
            final int colWidth = 300;
            canvas.save();
            for (int s = 0; s < shaders.length; s++) {
                canvas.save();
                int i = 2*s;
                canvas.translate(SkIntToScalar((i / testsPerCol) * colWidth),
                        SkIntToScalar((i % testsPerCol) * rowHeight));
                paint.setShader(shaders[s]).unref();
                canvas.drawText(text, 0, textBase, paint);
                canvas.restore();
                canvas.save();
                ++i;
                canvas.translate(SkIntToScalar((i / testsPerCol) * colWidth),
                        SkIntToScalar((i % testsPerCol) * rowHeight));
                canvas.drawTextOnPath(text, path, null, paint);
                canvas.restore();
            }
            canvas.restore();
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new ShaderTextGM();
        }
    };
}
