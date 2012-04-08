package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class gradients {
    static class GradData {
        int             fCount;
        int[]  fColors;
        float[] fPos;
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
    static final float[] gPos0 = { 0, SK_Scalar1 };
    static final float[] gPos1 = { SK_Scalar1/4, SK_Scalar1*3/4 };
    static final float[] gPos2 = {
            0, SK_Scalar1/8, SK_Scalar1/2, SK_Scalar1*7/8, SK_Scalar1
    };

    static final GradData[] gGradData = {
            new GradData(2, gColors, null),
            new GradData(2, gColors, gPos0),
            new GradData(2, gColors, gPos1),
            new GradData(5, gColors, null),
            new GradData(5, gColors, gPos2)
    };

    static GradMaker MakeLinear = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data,
                              int tm, SkUnitMapper mapper) {
            return SkGradientShader.CreateLinear(pts, data.fColors, data.fPos, tm, mapper);
        }
    };

    static GradMaker MakeRadial = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data,
                              int tm, SkUnitMapper mapper) {
            SkPoint center = new SkPoint();
            center.set(SkScalarAve(pts[0].fX(), pts[1].fX()),
                    SkScalarAve(pts[0].fY(), pts[1].fY()));
            return SkGradientShader.CreateRadial(center, center.fX(), data.fColors,
                    data.fPos, tm, mapper);
        }
    };

    static GradMaker MakeSweep = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data,
                              int tm, SkUnitMapper mapper) {
            SkPoint center = new SkPoint();
            center.set(SkScalarAve(pts[0].fX(), pts[1].fX()),
                    SkScalarAve(pts[0].fY(), pts[1].fY()));
            return SkGradientShader.CreateSweep(center.fX(), center.fY(), data.fColors,
                    data.fPos, mapper);
        }
    };

    static GradMaker Make2Radial = new GradMaker() {
        public SkShader apply(SkPoint[] pts, GradData data,
                              int tm, SkUnitMapper mapper) {
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
        public SkShader apply(SkPoint[] pts, GradData data,
                              int tm, SkUnitMapper mapper);
    }
    static final GradMaker gGradMakers[] = {
            MakeLinear, MakeRadial, MakeSweep, Make2Radial
    };

    public static class GradientsGM extends GM {
        public GradientsGM() {
            this.setBGColor(0xFFDDDDDD);
        }

        @Override
        protected String onShortName() {
            return "gradients";
        }

        @Override
        protected SkISize onISize() { return make_isize(640, 510); }

        @Override
        protected void onDraw(SkCanvas canvas) {

            SkPoint[] pts = {
                SkPoint.Make(0, 0),
                SkPoint.Make(SkIntToScalar(100), SkIntToScalar(100))
            };
            int tm = SkShader.kClamp_TileMode;
            SkRect r = SkRect.MakeLTRB(0, 0, SkIntToScalar(100), SkIntToScalar(100));
            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);

            canvas.translate(SkIntToScalar(20), SkIntToScalar(20));
            for (int i = 0; i < gGradData.length; i++) {
                canvas.save();
                for (int j = 0; j < gGradMakers.length; j++) {
                    SkShader shader = gGradMakers[j].apply(pts, gGradData[i], tm, null);
                    paint.setShader(shader);
                    canvas.drawRect(r, paint);
                    canvas.translate(0, SkIntToScalar(120));
                }
                canvas.restore();
                canvas.translate(SkIntToScalar(120), 0);
            }
        }
    }

    /*
     Inspired by this <canvas> javascript, where we need to detect that we are not
     solving a quadratic equation, but must instead solve a linear (since our X^2
     coefficient is 0)

     ctx.fillStyle = '#f00';
     ctx.fillRect(0, 0, 100, 50);

     var g = ctx.createRadialGradient(-80, 25, 70, 0, 25, 150);
     g.addColorStop(0, '#f00');
     g.addColorStop(0.01, '#0f0');
     g.addColorStop(0.99, '#0f0');
     g.addColorStop(1, '#f00');
     ctx.fillStyle = g;
     ctx.fillRect(0, 0, 100, 50);
     */
    public static class GradientsDegenrate2PointGM extends GM {
        public GradientsDegenrate2PointGM() {}

        @Override
        protected String onShortName() {
            return "gradients_degenerate_2pt";
        }

        @Override
        protected SkISize onISize() { return make_isize(320, 320); }

        void drawBG(SkCanvas canvas) {
            canvas.drawColor(SK_ColorBLUE);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            this.drawBG(canvas);

            int[] colors = { SK_ColorRED, SK_ColorGREEN, SK_ColorGREEN, SK_ColorRED };
            float[] pos = { 0, SkFloatToScalar(0.01f), SkFloatToScalar(0.99f), SK_Scalar1 };
            SkPoint c0 = new SkPoint();
            c0.iset(-80, 25);
            float r0 = SkIntToScalar(70);
            SkPoint c1 = new SkPoint();
            c1.iset(0, 25);
            float r1 = SkIntToScalar(150);
            SkShader s = SkGradientShader.CreateTwoPointRadial(c0, r0, c1, r1, colors,
                    pos,
                    SkShader.kClamp_TileMode);
            SkPaint paint = new SkPaint();
            paint.setShader(s);
            canvas.drawPaint(paint);
        }
    }

    /// Tests correctness of *optimized* codepaths in gradients.

    public static class ClampedGradientsGM extends GM {
        public ClampedGradientsGM() {}

        @Override
        protected String onShortName() { return "clamped_gradients"; }

        @Override
        protected SkISize onISize() { return make_isize(640, 510); }

        void drawBG(SkCanvas canvas) {
            canvas.drawColor(0xFFDDDDDD);
        }

        @Override
        protected void onDraw(SkCanvas canvas) {
            this.drawBG(canvas);

            SkRect r = SkRect.MakeLTRB(0, 0, SkIntToScalar(100), SkIntToScalar(300));
            SkPaint paint = new SkPaint();
            paint.setAntiAlias(true);

            SkPoint center = new SkPoint();
            center.iset(0, 300);
            canvas.translate(SkIntToScalar(20), SkIntToScalar(20));
            SkShader shader = SkGradientShader.CreateRadial(
                    new SkPoint().copy(center),
                    SkIntToScalar(200), gColors, null,
                    SkShader.kClamp_TileMode, null);
            paint.setShader(shader);
            canvas.drawRect(r, paint);
        }
    }

    /// Checks quality of large radial gradients, which may display
    /// some banding.

    public static class RadialGradientGM extends GM {
        public RadialGradientGM() {}

        @Override
        protected String onShortName() { return "radial_gradient"; }
        @Override
        protected SkISize onISize() { return make_isize(1280, 1280); }
        void drawBG(SkCanvas canvas) {
            canvas.drawColor(0xFF000000);
        }
        @Override
        protected void onDraw(SkCanvas canvas) {
            final SkISize dim = this.getISize();

            this.drawBG(canvas);

            SkPaint paint = new SkPaint();
            paint.setDither(true);
            SkPoint center = new SkPoint();
            center.set(SkIntToScalar(dim.width())/2, SkIntToScalar(dim.height())/2);
            float radius = SkIntToScalar(dim.width())/2;
            final int[] colors = { 0x7f7f7f7f, 0x7f7f7f7f, 0xb2000000 };
            final float[] pos = { SkFloatToScalar(0.0f),
                    SkFloatToScalar(0.35f),
                    SkFloatToScalar(1.0f) };
            SkShader shader =
                    SkGradientShader.CreateRadial(center, radius, colors,
                            pos,
                            SkShader.kClamp_TileMode);
            paint.setShader(shader);
            SkRect r = SkRect.MakeLTRB(
                    0, 0, SkIntToScalar(dim.width()), SkIntToScalar(dim.height())
                    );
            canvas.drawRect(r, paint);
        }
    }

    public static GMRegistry.Factory MyFactory = new GMRegistry.Factory() {
        public GM apply() {
            return new GradientsGM();
        }
    };

    public static GMRegistry.Factory MyFactory2 = new GMRegistry.Factory() {
        public GM apply() {
            return new GradientsDegenrate2PointGM();
        }
    };

    public static GMRegistry.Factory MyFactory3 = new GMRegistry.Factory() {
        public GM apply() {
            return new ClampedGradientsGM();
        }
    };

    public static GMRegistry.Factory MyFactory4 = new GMRegistry.Factory() {
        public GM apply() {
            return new RadialGradientGM();
        }
    };
}
