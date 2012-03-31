package skia.javacpp.gm;

import static skia.javacpp.core.*;
import static skia.javacpp.effects.*;

public class GradientsGM extends GM {
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
                shader.unref();
                canvas.translate(0, SkIntToScalar(120));
            }
            canvas.restore();
            canvas.translate(SkIntToScalar(120), 0);
        }
    }

    public static GMRegistry.Factory factory = new GMRegistry.Factory() {
        public GM apply() {
            return new GradientsGM();
        }
    };
}
